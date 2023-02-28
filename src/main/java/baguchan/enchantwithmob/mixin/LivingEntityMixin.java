package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.registry.ModTrackedDatas;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IEnchantCap {
	private static final EntityDataAccessor<MobEnchantCapability> MOB_ENCHANT_CAP;

	public LivingEntityMixin(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo callbackInfo) {
		if ((this.getEnchantCap().getEnchantOwner().isPresent() || this.getEnchantCap().isFromOwner()) && this.getEnchantCap().hasEnchant()) {
			if (this.getEnchantCap().getEnchantOwner().isEmpty()) {
				LivingEntity livingEntity = (LivingEntity) ((Object) this);
				this.getEnchantCap().removeMobEnchantFromOwner(livingEntity);
				this.playSound(SoundEvents.ITEM_BREAK, 1.5F, 1.6F);
			} else if (this.distanceToSqr(this.getEnchantCap().getEnchantOwner().get()) > 512) {
				LivingEntity livingEntity = (LivingEntity) ((Object) this);
				this.getEnchantCap().removeMobEnchantFromOwner(livingEntity);
				this.playSound(SoundEvents.ITEM_BREAK, 1.5F, 1.6F);
			}
		}
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	public void defineSynchedData(CallbackInfo callbackInfo) {
		this.entityData.define(MOB_ENCHANT_CAP, new MobEnchantCapability());
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		nbt.put("MobEnchantData", this.getEnchantCap().serializeNBT());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();
		mobEnchantCapability.deserializeNBT(nbt.getCompound("MobEnchantData"));
		this.setEnchantCap(mobEnchantCapability);
	}

	@Override
	public MobEnchantCapability getEnchantCap() {
		return this.entityData.get(MOB_ENCHANT_CAP);
	}

	@Override
	public void setEnchantCap(MobEnchantCapability cap) {
		this.entityData.set(MOB_ENCHANT_CAP, cap);
	}

	static {
		MOB_ENCHANT_CAP = SynchedEntityData.defineId(LivingEntity.class, ModTrackedDatas.MOB_ENCHANT_CAPABILITY);
	}
}
