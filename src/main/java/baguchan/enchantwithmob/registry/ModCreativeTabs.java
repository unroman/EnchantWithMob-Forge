package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.item.MobEnchantBookItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = EnchantWithMob.MODID)
public class ModCreativeTabs {
	@SubscribeEvent
	public static void registerCreativeTab(CreativeModeTabEvent.BuildContents event) {
		event.registerSimple(CreativeModeTabs.SPAWN_EGGS, ModItems.ENCHANTER_SPAWNEGG.get());
		event.registerSimple(CreativeModeTabs.COMBAT, ModItems.MOB_UNENCHANT_BOOK.get());
		event.register(CreativeModeTabs.COMBAT, (featureFlagSet, output, operatorEnabled) -> {
			MobEnchantBookItem.generateMobEnchantmentBookTypesOnlyMaxLevel(output, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
		});
	}
}
