package com.baguchan.enchantwithmob.mobenchant;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.registry.MobEnchants;
import com.baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
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

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			if (!shooterIsLiving(projectile)) return;
			LivingEntity owner = (LivingEntity) projectile.getOwner();
			MobEnchantUtils.executeIfPresent(owner, MobEnchants.MULTISHOT, () -> {
				if (!projectile.level.isClientSide && projectile.tickCount == 0 && !isAdding) {
					isAdding = true;
					CompoundTag compoundNBT = new CompoundTag();
					compoundNBT = projectile.saveWithoutId(compoundNBT);
					addProjectile(projectile, compoundNBT, 15.0F);
					addProjectile(projectile, compoundNBT, -15.0F);
					isAdding = false;
				}
			});
		}
	}

	private static void addProjectile(Projectile projectile, CompoundTag compoundNBT, float rotation) {
		Projectile newProjectile = (Projectile) projectile.getType().create(projectile.level);
		UUID uuid = newProjectile.getUUID();
		newProjectile.load(compoundNBT);
		newProjectile.setUUID(uuid);
		Vec3 vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));

		newProjectile.setDeltaMovement(vector3d);
		float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
		newProjectile.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI)));
		newProjectile.setXRot((float) (Mth.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI)));
		newProjectile.yRotO = newProjectile.getYRot();
		newProjectile.xRotO = newProjectile.getXRot();
		if (newProjectile instanceof Projectile) {
			Projectile newDamagingProjectile = (Projectile) newProjectile;
			Vec3 newPower = new Vec3(newDamagingProjectile.getDeltaMovement().x, newDamagingProjectile.getDeltaMovement().y, newDamagingProjectile.getDeltaMovement().z).yRot((float) (Math.PI / rotation));

			newDamagingProjectile.setDeltaMovement(newPower);
		}

		newProjectile.getCapability(EnchantWithMob.ITEM_MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			cap.setHasEnchant(true);
		});

		projectile.level.addFreshEntity(newProjectile);
	}

	@SubscribeEvent
	public static void onHit(ProjectileImpactEvent event) {
		Projectile projectile = event.getProjectile();

		projectile.getCapability(EnchantWithMob.ITEM_MOB_ENCHANT_CAP).ifPresent(cap -> {
			if (cap.hasEnchant()) {
				projectile.discard();
			}
		});
	}

	public static boolean shooterIsLiving(Projectile projectile) {
		return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
	}

	public int getMinEnchantability(int enchantmentLevel) {
		return 10;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 40;
	}
}
