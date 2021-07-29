package com.baguchan.enchantwithmob.mobenchant;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;

public class PoisonMobEnchant extends MobEnchant {
	public PoisonMobEnchant(Properties properties) {
		super(properties);
	}

	public int getMinEnchantability(int enchantmentLevel) {
		return 5 + (enchantmentLevel - 1) * 10;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 30;
	}

	@Override
	public void tick(LivingEntity entity, int level) {
		super.tick(entity, level);

		entity.level.addParticle(ParticleTypes.ENTITY_EFFECT, entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), 0.4F, 0.8F, 0.4F);
	}
}
