package baguchan.enchantwithmob.capability;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.message.AncientMobMessage;
import baguchan.enchantwithmob.message.MobEnchantFromOwnerMessage;
import baguchan.enchantwithmob.message.MobEnchantedMessage;
import baguchan.enchantwithmob.message.RemoveAllMobEnchantMessage;
import baguchan.enchantwithmob.message.RemoveMobEnchantOwnerMessage;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MobEnchantCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
	private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("6699a403-e2cc-31e6-195e-4757200e0935");

	private static final AttributeModifier HEALTH_MODIFIER = new AttributeModifier(HEALTH_MODIFIER_UUID, "Health boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);


	private List<MobEnchantHandler> mobEnchants = Lists.newArrayList();
	private Optional<LivingEntity> enchantOwner = Optional.empty();
	private boolean fromOwner;

	private EnchantType enchantType = EnchantType.NORMAL;


	/**
	 * add MobEnchant on Entity
	 *
	 * @param entity       Entity given a MobEnchant
	 * @param mobEnchant   Mob Enchant attached to mob
	 * @param enchantLevel Mob Enchant Level
	 */
	public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel) {

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		//Sync Client Enchant
		if (!entity.level.isClientSide) {
			MobEnchantedMessage message = new MobEnchantedMessage(entity, mobEnchant, enchantLevel);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}

	public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, boolean ancient) {
		this.addMobEnchant(entity, mobEnchant, enchantLevel);
		this.setEnchantType(entity, ancient ? EnchantType.ANCIENT : EnchantType.NORMAL);
	}

	public void setEnchantType(LivingEntity entity, EnchantType enchantType) {
		this.enchantType = enchantType;
		if (!entity.level.isClientSide) {
			AncientMobMessage message = new AncientMobMessage(entity, enchantType.name());
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}
	}

	/**
	 * add MobEnchant on Entity From Owner
	 *
	 * @param entity       Entity given a MobEnchant
	 * @param mobEnchant   Mob Enchant attached to mob
	 * @param enchantLevel Mob Enchant Level
	 * @param owner        OwnerEntity with a mob Enchant attached to that mob
	 */
	public void addMobEnchantFromOwner(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, LivingEntity owner) {

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		//Sync Client Enchant
		if (!entity.level.isClientSide) {
			MobEnchantedMessage message = new MobEnchantedMessage(entity, mobEnchant, enchantLevel);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}

	public void addOwner(LivingEntity entity, @Nullable LivingEntity owner) {
		this.fromOwner = true;
		this.enchantOwner = Optional.ofNullable(owner);

		if (!entity.level.isClientSide) {
			MobEnchantFromOwnerMessage message = new MobEnchantFromOwnerMessage(entity, owner);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}
	}

	public void removeOwner(LivingEntity entity) {
		this.fromOwner = false;
		this.enchantOwner = Optional.empty();
		if (!entity.level.isClientSide) {
			RemoveMobEnchantOwnerMessage message = new RemoveMobEnchantOwnerMessage(entity);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}
	}

	/*
	 * Remove MobEnchant on Entity
	 */
	public void removeAllMobEnchant(LivingEntity entity) {

		for (int i = 0; i < mobEnchants.size(); ++i) {
			this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant());
		}
		//Sync Client Enchant
		if (!entity.level.isClientSide) {
			RemoveAllMobEnchantMessage message = new RemoveAllMobEnchantMessage(entity);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}
		this.mobEnchants.removeAll(mobEnchants);
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}

	/*
	 * Remove MobEnchant on Entity from owner
	 */
	public void removeMobEnchantFromOwner(LivingEntity entity) {
		for (int i = 0; i < mobEnchants.size(); ++i) {
			this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant());
		}
		//Sync Client Enchant
		if (!entity.level.isClientSide) {
			RemoveAllMobEnchantMessage message = new RemoveAllMobEnchantMessage(entity);
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
		}
		this.mobEnchants.removeAll(mobEnchants);
		this.removeOwner(entity);
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}


    /*
	 * Add Enchant Attribute
	 */
	public void onNewEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
		if (!entity.level.isClientSide) {
			enchant.applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel);

			if (EnchantConfig.COMMON.dungeonsLikeHealth.get()) {
				AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
				if (modifiableattributeinstance != null && !modifiableattributeinstance.hasModifier(HEALTH_MODIFIER)) {
					modifiableattributeinstance.removeModifier(HEALTH_MODIFIER);
					modifiableattributeinstance.addPermanentModifier(HEALTH_MODIFIER);
					entity.setHealth(entity.getHealth() * 1.25F);
				}
			}
		}
	}

    /*
     * Changed Enchant Attribute When Enchant is Changed
     */
    protected void onChangedEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
        if (!entity.level.isClientSide) {
            enchant.applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel);
        }
    }

    /*
     * Remove Enchant Attribute effect
     */
    protected void onRemoveEnchantEffect(LivingEntity entity, MobEnchant enchant) {
        if (!entity.level.isClientSide()) {
			enchant.removeAttributesModifiersFromEntity(entity, entity.getAttributes());

			AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
			if (modifiableattributeinstance != null) {
				if (modifiableattributeinstance.hasModifier(HEALTH_MODIFIER)) {
					entity.setHealth(entity.getHealth() / 1.25F);
					modifiableattributeinstance.removeModifier(HEALTH_MODIFIER);
				}
			}
		}
	}

	public List<MobEnchantHandler> getMobEnchants() {
		return mobEnchants;
	}

	public boolean hasEnchant() {
		return !this.mobEnchants.isEmpty();
	}

	public Optional<LivingEntity> getEnchantOwner() {
		return enchantOwner;
	}

	public boolean hasOwner() {
		return this.enchantOwner.isPresent() && this.enchantOwner.get().isAlive();
	}

	//check this enchant from owner
	public boolean isFromOwner() {
		return this.fromOwner;
	}

	public EnchantType getEnchantType() {
		return enchantType;
	}

	public boolean isAncient() {
		return enchantType == EnchantType.ANCIENT;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return capability == EnchantWithMob.MOB_ENCHANT_CAP ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		ListTag listnbt = new ListTag();

		for (int i = 0; i < mobEnchants.size(); i++) {
			listnbt.add(mobEnchants.get(i).writeNBT());
		}

		nbt.put("StoredMobEnchants", listnbt);
		nbt.putBoolean("FromOwner", fromOwner);

		nbt.putString("EnchantType", enchantType.name());

		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		ListTag list = MobEnchantUtils.getEnchantmentListForNBT(nbt);

		mobEnchants.clear();

		for (int i = 0; i < list.size(); ++i) {
			CompoundTag compoundnbt = list.getCompound(i);

			MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
			//check mob enchant is not null
			if (mobEnchant != null) {
				mobEnchants.add(new MobEnchantHandler(mobEnchant, MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt)));
			}
		}

		fromOwner = nbt.getBoolean("FromOwner");

		enchantType = EnchantType.get(nbt.getString("EnchantType"));
	}

	public enum EnchantType {
		NORMAL,
		ANCIENT;

		public static EnchantType get(String nameIn) {
			for (EnchantType enchantType : values()) {
				if (enchantType.name().equals(nameIn))
					return enchantType;
			}
			return NORMAL;
		}
	}
}