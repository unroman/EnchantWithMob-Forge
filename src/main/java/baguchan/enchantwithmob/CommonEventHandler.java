package baguchan.enchantwithmob;

import baguchan.enchantwithmob.capability.ItemMobEnchantCapability;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.message.AncientMobMessage;
import baguchan.enchantwithmob.message.MobEnchantedMessage;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.registry.ModItems;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class CommonEventHandler {

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(MobEnchantCapability.class);
		event.register(ItemMobEnchantCapability.class);
	}

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(new ResourceLocation(EnchantWithMob.MODID, "mob_enchant"), new MobEnchantCapability());
		}

		if (event.getObject() instanceof Projectile) {
			event.addCapability(new ResourceLocation(EnchantWithMob.MODID, "item_mob_enchant"), new ItemMobEnchantCapability());
		}
	}


	@SubscribeEvent
	public static void onSpawnEntity(LivingSpawnEvent.SpecialSpawn event) {
		if (event.getEntity() instanceof LivingEntity) {
			LevelAccessor world = event.getLevel();

			LivingEntity livingEntity = event.getEntity();

			if (isSpawnAlwayEnchantableAncientEntity(livingEntity)) {
				if (!world.isClientSide()) {
					livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
					{
						int i = 0;
						float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
						switch (world.getDifficulty()) {
							case EASY:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 30);

								MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, true);
								break;
							case NORMAL:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(15)) * difficultScale, 1, 60);

								MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, true);
								break;
							case HARD:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(20)) * difficultScale, 1, 100);

								MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, true);
								break;
						}

						livingEntity.setHealth(livingEntity.getMaxHealth());
					});
				}
			}

			// On add MobEnchant Alway Enchantable Mob
			if (isSpawnAlwayEnchantableEntity(livingEntity)) {
				if (!world.isClientSide()) {
					livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
					{
						int i = 0;
						float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
						switch (world.getDifficulty()) {
							case EASY:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

								MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false);
								break;
							case NORMAL:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

								MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false);
								break;
							case HARD:
								i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

								MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false);
								break;
						}

						livingEntity.setHealth(livingEntity.getMaxHealth());
					});
				}
			}


			if (EnchantConfig.COMMON.naturalSpawnEnchantedMob.get() && isSpawnEnchantableEntity(event.getEntity())) {

				if (!(livingEntity instanceof Animal) && !(livingEntity instanceof WaterAnimal) || EnchantConfig.COMMON.spawnEnchantedAnimal.get()) {
					if (event.getSpawnReason() != MobSpawnType.BREEDING && event.getSpawnReason() != MobSpawnType.CONVERSION && event.getSpawnReason() != MobSpawnType.STRUCTURE && event.getSpawnReason() != MobSpawnType.MOB_SUMMONED) {
						if (world.getRandom().nextFloat() < (EnchantConfig.COMMON.difficultyBasePercent.get() * world.getDifficulty().getId()) + world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() * EnchantConfig.COMMON.effectiveBasePercent.get()) {
							if (!world.isClientSide()) {
								livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
								{
									int i = 0;
									float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
									switch (world.getDifficulty()) {
										case EASY:
											i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

											MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false);
											break;
										case NORMAL:
											i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

											MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false);
											break;
										case HARD:
											i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

											MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false);
											break;
									}

									livingEntity.setHealth(livingEntity.getMaxHealth());
								});
							}
						}
					}
				}
			}
		}
	}

	private static boolean isSpawnAlwayEnchantableEntity(Entity entity) {
		return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && EnchantConfig.COMMON.ALWAY_ENCHANTABLE_MOBS.get().contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
	}

	private static boolean isSpawnAlwayEnchantableAncientEntity(Entity entity) {
		return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && EnchantConfig.COMMON.ALWAY_ENCHANTABLE_ANCIENT_MOBS.get().contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
	}

	private static boolean isSpawnEnchantableEntity(Entity entity) {
		return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && !EnchantConfig.COMMON.ENCHANT_ON_SPAWN_EXCLUSION_MOBS.get().contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
	}

	@SubscribeEvent
	public static void onEntitySpawn(LivingSpawnEvent event) {
		LivingEntity livingEntity = (LivingEntity) event.getEntity();

		if (!livingEntity.level.isClientSide) {
			livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
			{
				//Sync Client Enchant
				if (cap.hasEnchant()) {
					for (int i = 0; i < cap.getMobEnchants().size(); i++) {
						MobEnchantedMessage message = new MobEnchantedMessage(livingEntity, cap.getMobEnchants().get(i));
						EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> livingEntity), message);
					}

					AncientMobMessage message = new AncientMobMessage(livingEntity, cap.getEnchantType().name());
					EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> livingEntity), message);
				}

				if (cap.isFromOwner() && (!cap.hasOwner() || cap.hasOwner() && livingEntity.distanceToSqr(cap.getEnchantOwner().get()) > 512)) {
					cap.removeMobEnchantFromOwner(livingEntity);
					cap.setEnchantType(livingEntity, MobEnchantCapability.EnchantType.NORMAL);
					livingEntity.playSound(SoundEvents.ITEM_BREAK, 1.5F, 1.6F);
				}
			});
		}

	}

	@SubscribeEvent
	public static void onUpdateEnchanted(LivingEvent.LivingTickEvent event) {
		LivingEntity livingEntity = event.getEntity();

		livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			for (MobEnchantHandler enchantHandler : cap.getMobEnchants()) {
				enchantHandler.getMobEnchant().tick(livingEntity, enchantHandler.getEnchantLevel());
			}

			if (cap.isFromOwner() && (!cap.hasOwner() || cap.hasOwner() && livingEntity.distanceToSqr(cap.getEnchantOwner().get()) > 512)) {
				cap.removeMobEnchantFromOwner(livingEntity);
				livingEntity.playSound(SoundEvents.ITEM_BREAK, 1.5F, 1.6F);
			}
		});
	}


	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		LivingEntity livingEntity = event.getEntity();

		if (event.getSource().getEntity() instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

			attacker.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
			{
				if (cap.hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getMobEnchants(), MobEnchants.STRONG.get())) {
					//make snowman stronger
					if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof SnowGolem && event.getAmount() == 0) {
						event.setAmount(getDamageAddition(1, cap));
					} else if (event.getAmount() > 0) {
						event.setAmount(getDamageAddition(event.getAmount(), cap));
					}
				}

				if (cap.hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getMobEnchants(), MobEnchants.POISON.get())) {
					int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.POISON.get());

					if (event.getAmount() > 0) {
						if (attacker.getRandom().nextFloat() < i * 0.125F) {
							livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), attacker);
						}
					}
				}
			});

			livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
			{
				if (cap.hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getMobEnchants(), MobEnchants.THORN.get())) {
					int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.THORN.get());

					if (event.getSource().getDirectEntity() == attacker && !event.getSource().isExplosion() && livingEntity.getRandom().nextFloat() < i * 0.1F) {
						attacker.hurt(DamageSource.thorns(livingEntity), getThornDamage(event.getAmount(), cap));
					}
				}
			});
		}

		livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (event.getSource() != DamageSource.STARVE && cap.hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getMobEnchants(), MobEnchants.PROTECTION.get())) {
				event.setAmount(getDamageReduction(event.getAmount(), cap));
			}
		});

		//new mob enchant protection system like Minecraft Dungeons
		livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant()) {
				event.setAmount(getBonusMobEnchantDamageReduction(event.getAmount(), cap));
			}
		});
	}

	public static float getDamageAddition(float damage, MobEnchantCapability cap) {
		int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.STRONG.get());
		if (level > 0) {
			damage += 1.0F + (float) Math.max(0, level - 1) * 1.0F;
		}
		return damage;
	}

	public static float getDamageReduction(float damage, MobEnchantCapability cap) {
		int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.PROTECTION.get());
		if (i > 0) {
			damage -= (double) Mth.floor(damage * (double) ((float) i * 0.15F));
		}
		return damage;
	}

	public static float getBonusMobEnchantDamageReduction(float damage, MobEnchantCapability cap) {
		int i = cap.getMobEnchants().size();
		if (i > 0) {
			damage -= (double) Mth.floor(damage * (double) ((float) i * 0.05F));
		}
		return damage;
	}

	public static float getThornDamage(float damage, MobEnchantCapability cap) {
		int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.THORN.get());
		if (i > 0) {
			damage = (float) Mth.floor(damage * (double) ((float) i * 0.15F));
		}
		return damage;
	}


	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.EntityInteract event) {
		ItemStack stack = event.getItemStack();
		Entity entityTarget = event.getTarget();

		if (stack.getItem() == ModItems.MOB_ENCHANT_BOOK.get() && !event.getEntity().getCooldowns().isOnCooldown(stack.getItem())) {
			if (entityTarget instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) entityTarget;
				if (MobEnchantUtils.hasMobEnchant(stack)) {

					target.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
					{
						final boolean[] flag = {false};
						flag[0] = MobEnchantUtils.addItemMobEnchantToEntity(stack, target, cap);

						if (flag[0]) {
							event.getEntity().playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

							stack.hurtAndBreak(1, event.getEntity(), (entity) -> entity.broadcastBreakEvent(event.getHand()));

							event.getEntity().getCooldowns().addCooldown(stack.getItem(), 60);

							event.setCancellationResult(InteractionResult.SUCCESS);
							event.setCanceled(true);
						} else {
							event.getEntity().displayClientMessage(Component.translatable("enchantwithmob.cannot.enchant"), true);
							event.getEntity().getCooldowns().addCooldown(stack.getItem(), 20);
							event.setCancellationResult(InteractionResult.FAIL);
							event.setCanceled(true);
						}
					});
				}
			}
		}

		if (stack.getItem() == ModItems.MOB_UNENCHANT_BOOK.get() && !event.getEntity().getCooldowns().isOnCooldown(stack.getItem())) {
			if (entityTarget instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) entityTarget;

				target.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
				{
					MobEnchantUtils.removeMobEnchantToEntity(target, cap);
					event.getEntity().playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

					stack.hurtAndBreak(1, event.getEntity(), (entity) -> entity.broadcastBreakEvent(event.getHand()));

					event.getEntity().getCooldowns().addCooldown(stack.getItem(), 80);

					event.setCancellationResult(InteractionResult.SUCCESS);
					event.setCanceled(true);
				});

			}
		}
	}

	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack stack1 = event.getLeft();
		ItemStack stack2 = event.getRight();

		if (stack1.getItem() == ModItems.MOB_ENCHANT_BOOK.get() && stack2.getItem() == ModItems.MOB_ENCHANT_BOOK.get()) {
			Map<MobEnchant, Integer> map = MobEnchantUtils.getEnchantments(stack1);

			Map<MobEnchant, Integer> map1 = MobEnchantUtils.getEnchantments(stack2);
			boolean flag2 = false;
			boolean flag3 = false;

			for (MobEnchant enchantment1 : map1.keySet()) {
				if (enchantment1 != null) {
					int i2 = map.getOrDefault(enchantment1, 0);
					int j2 = map1.get(enchantment1);
					j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
					boolean flag1 = true;

					for (MobEnchant enchantment : map.keySet()) {
						if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
							flag1 = false;
						}
					}

					if (!flag1) {
						flag3 = true;
					} else {
						flag2 = true;
						if (j2 > enchantment1.getMaxLevel()) {
							j2 = enchantment1.getMaxLevel();
						}

						map.put(enchantment1, j2);
						int k3 = 0;
						switch (enchantment1.getRarity()) {
							case COMMON:
								k3 = 1;
								break;
							case UNCOMMON:
								k3 = 2;
								break;
							case RARE:
								k3 = 4;
								break;
							case VERY_RARE:
								k3 = 8;
						}
					}
				}
			}
			if (!stack1.isEmpty()) {
				int k2 = stack1.getBaseRepairCost();
				if (!stack2.isEmpty() && k2 < stack2.getBaseRepairCost()) {
					k2 = stack2.getBaseRepairCost();
				}

				ItemStack stack3 = new ItemStack(stack1.getItem());

				MobEnchantUtils.setEnchantments(map, stack3);
				stack3.setRepairCost(4 + k2);
				event.setOutput(stack3);
				event.setCost(4 + k2);
				event.setMaterialCost(1);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		if (player instanceof ServerPlayer)
			player.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(handler -> {
				for (int i = 0; i < handler.getMobEnchants().size(); i++) {
					EnchantWithMob.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new MobEnchantedMessage(player, handler.getMobEnchants().get(i)));

				}

				AncientMobMessage message = new AncientMobMessage(player, handler.getEnchantType().name());
				EnchantWithMob.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
			});
	}

	@SubscribeEvent
	public static void onExpDropped(LivingExperienceDropEvent event) {
		LivingEntity entity = event.getEntity();
		entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
			if (cap.hasEnchant()) {
				if (cap.isAncient()) {
					event.setDroppedExperience(event.getDroppedExperience() + MobEnchantUtils.getExperienceFromMob(cap) * 5);
				} else {
					event.setDroppedExperience(event.getDroppedExperience() + MobEnchantUtils.getExperienceFromMob(cap));
				}
			}
		});
	}

	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		Player playerEntity = event.getEntity();
		playerEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(handler -> {
			for (int i = 0; i < handler.getMobEnchants().size(); i++) {
				EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new MobEnchantedMessage(playerEntity, handler.getMobEnchants().get(i)));
			}
			AncientMobMessage message = new AncientMobMessage(playerEntity, handler.getEnchantType().name());
			EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), message);
		});
	}
}
