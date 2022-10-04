package baguchan.enchantwithmob.event;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.loot.MobEnchantRandomlyFunction;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class LootTableEvent {
	private static final ResourceLocation DESERT_CHEST = BuiltInLootTables.DESERT_PYRAMID;
	private static final ResourceLocation STRONGHOLD_CHEST = BuiltInLootTables.STRONGHOLD_LIBRARY;
	private static final ResourceLocation WOODLAND_MANSION_CHEST = BuiltInLootTables.WOODLAND_MANSION;

	private static final ResourceLocation ANCIENT_CITY = BuiltInLootTables.ANCIENT_CITY;

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

		if (event.getName().equals(ANCIENT_CITY)) {
			event.getTable().addPool(LootPool.lootPool().apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).add(LootItem.lootTableItem(ModItems.MOB_ENCHANT_BOOK.get()).setWeight(1).apply(new MobEnchantRandomlyFunction.Builder().withMobEnchant(MobEnchants.SOUL_STEAL.get()))).add(LootItem.lootTableItem(Items.AIR).setWeight(4)).build());
		}
	}
}
