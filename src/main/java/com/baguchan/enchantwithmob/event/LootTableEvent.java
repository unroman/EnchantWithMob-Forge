package com.baguchan.enchantwithmob.event;

import com.baguchan.enchantwithmob.EnchantWithMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class LootTableEvent {
	private static final ResourceLocation DESERT_CHEST = BuiltInLootTables.DESERT_PYRAMID;
	private static final ResourceLocation STRONGHOLD_CHEST = BuiltInLootTables.STRONGHOLD_LIBRARY;
	private static final ResourceLocation WOODLAND_MANSION_CHEST = BuiltInLootTables.WOODLAND_MANSION;

	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) {
		if (event.getName().equals(DESERT_CHEST)) {
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(EnchantWithMob.MODID, "inject/mob_enchant_desert")).setWeight(10).setQuality(0)).name("mob_enchant_desert").build());
		}

		if (event.getName().equals(WOODLAND_MANSION_CHEST)) {
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(EnchantWithMob.MODID, "inject/mob_enchant_mansion")).setWeight(20).setQuality(0)).name("mob_enchant_mansion").build());
		}

		if (event.getName().equals(STRONGHOLD_CHEST)) {
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(EnchantWithMob.MODID, "inject/mob_enchant_stronghold")).setWeight(5).setQuality(0)).name("mob_enchant_stronghold").build());
		}
	}
}
