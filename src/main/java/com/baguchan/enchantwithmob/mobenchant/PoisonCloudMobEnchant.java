package com.baguchan.enchantwithmob.mobenchant;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.registry.MobEnchants;
import com.baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class PoisonCloudMobEnchant extends MobEnchant {
	public PoisonCloudMobEnchant(Properties properties) {
		super(properties);
	}

	public int getMinEnchantability(int enchantmentLevel) {
		return 20;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return 50;
	}

	@Override
	public void tick(LivingEntity entity, int level) {
		super.tick(entity, level);
	}

	@Override
	public boolean isCompatibleMob(LivingEntity livingEntity) {
		return !(livingEntity instanceof Witch);
	}

	@Override
	protected boolean canApplyTogether(MobEnchant ench) {
		return ench != MobEnchants.POISON.get() && super.canApplyTogether(ench);
	}

	@Override
	public boolean isTresureEnchant() {
		return true;
	}

	@SubscribeEvent
	public static void onImpact(ProjectileImpactEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			if (!shooterIsLiving(projectile)) return;
			LivingEntity owner = (LivingEntity) projectile.getOwner();
			MobEnchantUtils.executeIfPresent(owner, MobEnchants.POISON_CLOUD.get(), () -> {
				if (!(projectile instanceof AbstractArrow) || !projectile.isOnGround()) {
					AreaEffectCloud areaeffectcloud = new AreaEffectCloud(owner.level, event.getRayTraceResult().getLocation().x, event.getRayTraceResult().getLocation().y, event.getRayTraceResult().getLocation().z);
					areaeffectcloud.setRadius(1.0F);
					areaeffectcloud.setRadiusOnUse(-0.05F);
					areaeffectcloud.setWaitTime(10);
					areaeffectcloud.setDuration(110);
					areaeffectcloud.setOwner(owner);
					areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

					areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
					owner.level.addFreshEntity(areaeffectcloud);
				}
			});
		}
	}

	public static boolean shooterIsLiving(Projectile projectile) {
		return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
	}
}
