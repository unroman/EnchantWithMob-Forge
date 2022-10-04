package baguchan.enchantwithmob.event;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.registry.ModItems;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class TradeEvent {
    @SubscribeEvent
    public static void wanderTradeEvent(WandererTradesEvent event) {
		List<VillagerTrades.ItemListing> trades = event.getRareTrades();
        trades.add(new MobEnchantedBookForEmeraldsTrade(15));
    }

	static class MobEnchantedBookForEmeraldsTrade implements VillagerTrades.ItemListing {
		private final int xpValue;

		public MobEnchantedBookForEmeraldsTrade(int xpValueIn) {
			this.xpValue = xpValueIn;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			List<MobEnchant> list = MobEnchants.getRegistry().get().getValues().stream().filter(mobEnchant -> {
				return !mobEnchant.isOnlyChest();
			}).collect(Collectors.toList());
			MobEnchant enchantment = list.get(rand.nextInt(list.size()));
			int i = Mth.nextInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = new ItemStack(ModItems.MOB_ENCHANT_BOOK.get());
			MobEnchantUtils.addMobEnchantToItemStack(itemstack, enchantment, i);
			int j = 2 + rand.nextInt(5 + i * 10) + 3 * i;

			if (enchantment.isTresureEnchant()) {
				j += 4;
			}

			if (j > 64) {
				j = 64;
			}

			return new MerchantOffer(new ItemStack(Items.EMERALD, j), ItemStack.EMPTY, itemstack, 12, this.xpValue, 0.2F);
		}
    }
}
