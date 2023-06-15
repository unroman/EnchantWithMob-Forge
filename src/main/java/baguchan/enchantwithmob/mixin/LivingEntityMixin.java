package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IEnchantCap {

	public LivingEntityMixin(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	public MobEnchantCapability getEnchantCap() {
        return this.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).orElse(MobEnchantCapability.EMPTY_CAP);
	}
}
