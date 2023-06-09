package baguchan.enchantwithmob.event;

import baguchan.enchantwithmob.EnchantWithMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class LootTableEvent {
	private static final ResourceLocation DESERT_CHEST = BuiltInLootTables.DESERT_PYRAMID;
	private static final ResourceLocation STRONGHOLD_CHEST = BuiltInLootTables.STRONGHOLD_LIBRARY;
	private static final ResourceLocation WOODLAND_MANSION_CHEST = BuiltInLootTables.WOODLAND_MANSION;

	private static final ResourceLocation ANCIENT_CITY = BuiltInLootTables.ANCIENT_CITY;
}
