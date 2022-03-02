package com.baguchan.enchantwithmob.client;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.client.model.EnchanterModel;
import com.baguchan.enchantwithmob.client.render.EnchanterRenderer;
import com.baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import com.baguchan.enchantwithmob.client.render.layer.SlimeEnchantLayer;
import com.baguchan.enchantwithmob.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {
    private static final RenderType ILLAGER_EYES = RenderType.eyes(new ResourceLocation(EnchantWithMob.MODID, "textures/entity/enchant_eye/illager_eye.png"));

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ENCHANTER.get(), EnchanterRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ENCHANTER, EnchanterModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.AddLayers event) {
        event.getSkins().stream().forEach(skins ->
        {
            event.getSkin(skins).addLayer(new EnchantLayer(event.getSkin(skins)));
        });
        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(r -> {
            if (r instanceof SlimeRenderer) {
                ((SlimeRenderer) r).addLayer(new SlimeEnchantLayer<>((SlimeRenderer) r, event.getEntityModels()));
            }
            if (r instanceof LivingEntityRenderer) {
                ((LivingEntityRenderer) r).addLayer(new EnchantLayer((LivingEntityRenderer) r));
            }
        });
    }
}
