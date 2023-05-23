package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.api.IEnchantVisual;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EvokerFangs.class)
public abstract class EvokerFangsMixin extends Entity implements TraceableEntity, IEnchantVisual {
    private static final EntityDataAccessor<Boolean> MOB_ENCHANT = SynchedEntityData.defineId(EvokerFangs.class, EntityDataSerializers.BOOLEAN);

    public EvokerFangsMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    public void defineSynchedData(CallbackInfo callbackInfo) {
        this.entityData.define(MOB_ENCHANT, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("EnchantVisual", this.hasEnchantVisual());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        this.setEnchantVisual(nbt.getBoolean("EnchantVisual"));
    }

    @Override
    public boolean hasEnchantVisual() {
        return this.entityData.get(MOB_ENCHANT);
    }

    @Override
    public void setEnchantVisual(boolean enchantVisual) {
        this.entityData.set(MOB_ENCHANT, enchantVisual);
    }
}
