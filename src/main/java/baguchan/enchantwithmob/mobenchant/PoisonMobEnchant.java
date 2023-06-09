package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.registry.MobEnchants;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.CaveSpider;

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

        if (entity.level().isClientSide() && !EnchantConfig.CLIENT.disablePoisonParticle.get()) {
            entity.level().addParticle(ParticleTypes.ENTITY_EFFECT, entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), 0.4F, 0.8F, 0.4F);
        }
    }

	@Override
	public boolean isCompatibleMob(LivingEntity livingEntity) {
		return !(livingEntity instanceof Bee) && !(livingEntity instanceof CaveSpider);
	}

	@Override
	protected boolean canApplyTogether(MobEnchant ench) {
		return ench != MobEnchants.POISON_CLOUD.get() && super.canApplyTogether(ench);
	}
}
