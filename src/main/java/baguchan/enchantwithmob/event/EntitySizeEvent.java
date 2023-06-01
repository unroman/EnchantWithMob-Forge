package baguchan.enchantwithmob.event;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class EntitySizeEvent {
    @SubscribeEvent
    public static void onSetSize(EntityEvent.Size event) {
        Entity entity = event.getEntity();


        if (entity instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get());

                    float totalWidth = entity.getDimensions(entity.getPose()).width * (1.0F - level * 0.015F);
                    float totalHeight = entity.getDimensions(entity.getPose()).height * (1.0F - level * 0.015F);

                    event.setNewEyeHeight(entity.getEyeHeight(entity.getPose()) * (1.0F - level * 0.015F));
                    event.setNewSize(EntityDimensions.fixed(totalWidth, totalHeight));
                } else if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get());

                    float totalWidth = entity.getDimensions(entity.getPose()).width * (1.0F + level * 0.015F);
                    float totalHeight = entity.getDimensions(entity.getPose()).height * (1.0F + level * 0.015F);

                    event.setNewEyeHeight(entity.getEyeHeight(entity.getPose()) * (1.0F + level * 0.015F));
                    event.setNewSize(EntityDimensions.fixed(totalWidth, totalHeight));
                } else if (EnchantConfig.COMMON.changeSizeWhenEnchant.get()) {
                    float totalWidth = entity.getDimensions(entity.getPose()).width * 1.0025F;
                    float totalHeight = entity.getDimensions(entity.getPose()).height * 1.0025F;

                    event.setNewEyeHeight(entity.getEyeHeight(entity.getPose()) * 1.025F);
                    event.setNewSize(EntityDimensions.fixed(totalWidth, totalHeight));
                }
            }
        }
        ;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            Player player = Minecraft.getInstance().player;
            float scale = player.getBbHeight() / 1.8F;

            switch (Minecraft.getInstance().options.getCameraType()) {
                case THIRD_PERSON_BACK:
                    if (player.getBbHeight() > 1.8F || player.getBbHeight() < 1.8F)
                        event.getPoseStack().translate(0, 0, -scale * 2);
                    break;
                case THIRD_PERSON_FRONT:
                    if (player.getBbHeight() > 1.8F || player.getBbHeight() < 1.8F)
                        event.getPoseStack().translate(0, 0, scale * 2);
                    break;
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onEntityRenderPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();

        if (entity instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get());
                    event.getPoseStack().scale(1.0F - 0.15F * level, 1.0F - 0.15F * level, 1.0F - 0.15F * level);
                } else if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get());
                    event.getPoseStack().scale(1.0F + 0.15F * level, 1.0F + 0.15F * level, 1.0F + 0.15F * level);
                } else if (EnchantConfig.COMMON.changeSizeWhenEnchant.get()) {
                    event.getPoseStack().scale(1.05F, 1.05F, 1.05F);
                }


            }
        }
        ;

    }
}