package baguchan.enchantwithmob.event;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MobEnchantMargeEvent {
	public static Map<LivingEntity, List<MobEnchantHandler>> maps = Maps.newHashMap();

	@SubscribeEvent
	public static void onPreEntityConversion(LivingConversionEvent.Pre event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                maps.put(livingEntity, cap.getEnchantCap().getMobEnchants());
            }
        }
        ;
    }

	@SubscribeEvent
	public static void onEntityConversion(LivingConversionEvent.Post event) {
		LivingEntity livingEntity = event.getEntity();
		LivingEntity outcome = event.getOutcome();

		if (maps.containsKey(livingEntity)) {
            if (livingEntity instanceof IEnchantCap cap) {
                for (MobEnchantHandler enchantHandler : maps.get(livingEntity)) {
                    cap.getEnchantCap().addMobEnchant(outcome, enchantHandler.getMobEnchant(), enchantHandler.getEnchantLevel());
                }
            }
            ;
            maps.remove(livingEntity);
        }
	}

	@SubscribeEvent
	public static void onWorldUnload(LevelEvent.Unload event) {
		maps.clear();
	}
}
