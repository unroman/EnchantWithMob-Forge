package baguchan.enchantwithmob.event;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class EntitySizeEvent {

    public static boolean settingSize;

    @SubscribeEvent
    public static void onSetSize(EntityEvent.Size event) {
        Entity entity = event.getEntity();

        if (entity instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get());

                    float totalWidth = entity.getDimensions(entity.getPose()).width * (1.0F - level * 0.15F);
                    float totalHeight = entity.getDimensions(entity.getPose()).height * (1.0F - level * 0.15F);
                    event.setNewEyeHeight(entity.getEyeHeight(entity.getPose()) * (1.0F - level * 0.15F));

                    event.setNewSize(EntityDimensions.fixed(totalWidth, totalHeight));
                } else if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get());

                    float totalWidth = entity.getDimensions(entity.getPose()).width * (1.0F + level * 0.15F);
                    float totalHeight = entity.getDimensions(entity.getPose()).height * (1.0F + level * 0.15F);
                    event.setNewEyeHeight(entity.getEyeHeight(entity.getPose()) * (1.0F + level * 0.15F));
                    event.setNewSize(EntityDimensions.fixed(totalWidth, totalHeight));
                } else if (EnchantConfig.COMMON.changeSizeWhenEnchant.get()) {
                    float totalWidth = entity.getDimensions(entity.getPose()).width * 1.025F;
                    float totalHeight = entity.getDimensions(entity.getPose()).height * 1.025F;
                    event.setNewEyeHeight(entity.getEyeHeight(entity.getPose()) * (1.025F));
                    event.setNewSize(EntityDimensions.fixed(totalWidth, totalHeight));

                }
            }
        }
    }
}