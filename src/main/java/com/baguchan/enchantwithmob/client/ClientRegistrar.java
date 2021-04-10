package com.baguchan.enchantwithmob.client;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.client.render.layer.EnchantEyeLayer;
import com.baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@OnlyIn(Dist.CLIENT)
public class ClientRegistrar {
    private static final RenderType ILLAGER_EYES = RenderType.eyes(new ResourceLocation(EnchantWithMob.MODID, "textures/entity/enchant_eye/illager_eye.png"));


    public static void loadComplete(FMLLoadCompleteEvent evt) {
        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(r -> {

            if (r instanceof IllagerRenderer) {
                ((IllagerRenderer) r).addLayer(new EnchantEyeLayer((IllagerRenderer) r, new IllagerModel(0, 0, 64, 64), ILLAGER_EYES));
            }

            if (r instanceof LivingRenderer) {
                ((LivingRenderer) r).addLayer(new EnchantLayer((LivingRenderer) r));

            }
        });

        Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().forEach(r -> {
            if (r instanceof LivingRenderer) {
                ((LivingRenderer) r).addLayer(new EnchantLayer((LivingRenderer) r));
            }
        });
    }
}
