package com.baguchan.enchantwithmob.capability;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.message.MobEnchantFromOwnerMessage;
import com.baguchan.enchantwithmob.message.MobEnchantedMessage;
import com.baguchan.enchantwithmob.message.RemoveAllMobEnchantMessage;
import com.baguchan.enchantwithmob.message.RemoveMobEnchantOwnerMessage;
import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MobEnchantCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
	private List<MobEnchantHandler> mobEnchants = Lists.newArrayList();
	private Optional<LivingEntity> enchantOwner = Optional.empty();
	private boolean fromOwner;


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

		this.addOwner(entity, owner);
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
    }


    /*
	 * Add Enchant Attribute
	 */
	public void onNewEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
		if (!entity.level.isClientSide) {
			enchant.applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel);
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

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return capability == EnchantWithMob.MOB_ENCHANT_CAP ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
	}

	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();

		ListNBT listnbt = new ListNBT();

		for (int i = 0; i < mobEnchants.size(); i++) {
			listnbt.add(mobEnchants.get(i).writeNBT());
		}

		nbt.put("StoredMobEnchants", listnbt);
		nbt.putBoolean("FromOwner", fromOwner);


		return nbt;
    }

    public void deserializeNBT(CompoundNBT nbt) {
		ListNBT list = MobEnchantUtils.getEnchantmentListForNBT(nbt);

		for (int i = 0; i < list.size(); ++i) {
			CompoundNBT compoundnbt = list.getCompound(i);

			mobEnchants.set(i, new MobEnchantHandler(MobEnchantUtils.getEnchantFromNBT(compoundnbt), MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt)));
		}

		fromOwner = nbt.getBoolean("FromOwner");
    }
}