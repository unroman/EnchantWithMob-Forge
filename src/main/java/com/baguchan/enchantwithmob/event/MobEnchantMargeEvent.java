package com.baguchan.enchantwithmob.event;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.capability.MobEnchantHandler;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MobEnchantMargeEvent {
	public static Map<LivingEntity, List<MobEnchantHandler>> maps = Maps.newHashMap();

	@SubscribeEvent
	public static void onPreEntityConversion(LivingConversionEvent.Pre event) {
		LivingEntity livingEntity = event.getEntityLiving();
		livingEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(mobEnchantCapability -> {
			if (mobEnchantCapability.hasEnchant()) {
				maps.put(livingEntity, mobEnchantCapability.getMobEnchants());
			}
		});
	}

	@SubscribeEvent
	public static void onEntityConversion(LivingConversionEvent.Post event) {
		LivingEntity livingEntity = event.getEntityLiving();
		LivingEntity outcome = event.getOutcome();

		if (maps.containsKey(livingEntity)) {
			outcome.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
				for (MobEnchantHandler enchantHandler : maps.get(livingEntity)) {
					cap.addMobEnchant(outcome, enchantHandler.getMobEnchant(), enchantHandler.getEnchantLevel());
				}
			});
			maps.remove(livingEntity);
		}
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event) {
		maps.clear();
	}
}
