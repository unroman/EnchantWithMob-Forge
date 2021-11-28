package com.baguchan.enchantwithmob.mobenchant;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.registry.MobEnchants;
import com.baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MultiShotMobEnchant extends MobEnchant {
    private static boolean isAdding = false;

    public MultiShotMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 40;
    }


    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        World world = event.getWorld();
        if (entity instanceof ProjectileEntity) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            if (!shooterIsLiving(projectile)) return;
            LivingEntity owner = (LivingEntity) projectile.getOwner();
            MobEnchantUtils.executeIfPresent(owner, MobEnchants.MULTISHOT, () -> {
                if (!world.isClientSide && projectile.tickCount == 0 && !isAdding) {
                    isAdding = true;
                    CompoundNBT compoundNBT = new CompoundNBT();
                    compoundNBT = projectile.saveWithoutId(compoundNBT);
                    addProjectile(projectile, compoundNBT, world, 15.0F);
                    addProjectile(projectile, compoundNBT, world, -15.0F);
                    isAdding = false;
                }
            });
        }
    }

    private static void addProjectile(ProjectileEntity projectile, CompoundNBT compoundNBT, World world, float rotation) {
        ProjectileEntity newProjectile = (ProjectileEntity) projectile.getType().create(world);
        UUID uuid = newProjectile.getUUID();
        newProjectile.load(compoundNBT);
        newProjectile.setUUID(uuid);
        Vector3d vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));
        newProjectile.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        newProjectile.yRot = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
        newProjectile.xRot = (float) (MathHelper.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
        newProjectile.yRotO = newProjectile.yRot;
        newProjectile.xRotO = newProjectile.xRot;
        if (newProjectile instanceof DamagingProjectileEntity) {
            DamagingProjectileEntity newDamagingProjectile = (DamagingProjectileEntity) newProjectile;
            Vector3d newPower = new Vector3d(newDamagingProjectile.xPower, newDamagingProjectile.yPower, newDamagingProjectile.zPower).yRot((float) (Math.PI / rotation));
            newDamagingProjectile.xPower = newPower.x;
            newDamagingProjectile.yPower = newPower.y;
            newDamagingProjectile.zPower = newPower.z;
        }
        newProjectile.getCapability(EnchantWithMob.ITEM_MOB_ENCHANT_CAP).ifPresent(cap -> {
            cap.setHasEnchant(true);
        });

        world.addFreshEntity(newProjectile);
    }

    @SubscribeEvent
    public static void onHit(ProjectileImpactEvent.Arrow event) {
        ProjectileEntity projectile = event.getArrow();

        projectile.getCapability(EnchantWithMob.ITEM_MOB_ENCHANT_CAP).ifPresent(cap -> {
            if (cap.hasEnchant()) {
                projectile.remove();
            }
        });
    }

    public static boolean shooterIsLiving(ProjectileEntity projectile) {
        return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
    }
}
