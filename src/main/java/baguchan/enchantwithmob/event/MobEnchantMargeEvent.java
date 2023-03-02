package baguchan.enchantwithmob.event;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MobEnchantMargeEvent {

	@SubscribeEvent
	public static void onEntityConversion(LivingConversionEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();
        LivingEntity outcome = event.getOutcome();

        if (outcome instanceof IEnchantCap cap) {
            if (livingEntity instanceof IEnchantCap livingCap) {
                if (livingCap.getEnchantCap().hasEnchant()) {
                    for (MobEnchantHandler mobEnchantHandler : livingCap.getEnchantCap().getMobEnchants()) {
                        cap.getEnchantCap().addMobEnchant(outcome, mobEnchantHandler.getMobEnchant(), mobEnchantHandler.getEnchantLevel());
                    }
                }
            }
        }
    }
}
