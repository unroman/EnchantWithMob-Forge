package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class DeflectMobEnchant extends MobEnchant {
    public DeflectMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }

    @SubscribeEvent
    public static void onHit(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();

        if (event.getRayTraceResult() instanceof EntityHitResult) {
            EntityHitResult entityHitResult = (EntityHitResult) event.getRayTraceResult();
            MobEnchantUtils.executeIfPresent(entityHitResult.getEntity(), MobEnchants.DEFLECT.get(), () -> {
                event.setCanceled(true);
                Vec3 vec3 = projectile.getDeltaMovement();

                projectile.setDeltaMovement(-vec3.x * 0.6F, -vec3.y * 0.6F, -vec3.z * 0.6F);
                projectile.setOwner(entityHitResult.getEntity());
                if (projectile instanceof AbstractArrow arrow) {
                    arrow.setPierceLevel((byte) 0);
                }
            });
        }
    }

    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != MobEnchants.THORN.get();
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }
}
