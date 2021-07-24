package com.baguchan.enchantwithmob.registry;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.client.model.EnchanterModel;
import com.baguchan.enchantwithmob.client.render.EnchanterRenderer;
import com.baguchan.enchantwithmob.entity.EnchanterEntity;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlclient.registry.RenderingRegistry;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final EntityType<EnchanterEntity> ENCHANTER = EntityType.Builder.of(EnchanterEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build(prefix("enchanter"));

    private static String prefix(String path) {
        return EnchantWithMob.MODID + "." + path;
    }

    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(ENCHANTER.setRegistryName("enchanter"));
        Raid.RaiderType.create("enchanter", ENCHANTER, new int[]{0, 0, 1, 0, 1, 1, 2, 1});
    }

    @SubscribeEvent
    public static void registerEntity(EntityAttributeCreationEvent event) {
        event.put(ENCHANTER, EnchanterEntity.createAttributeMap().build());
    }


    @OnlyIn(Dist.CLIENT)
    public static void setupEntitiesClient() {
        EntityRenderers.register(ENCHANTER, EnchanterRenderer::new);
        RenderingRegistry.registerLayerDefinition(ModModelLayers.ENCHANTER, EnchanterModel::createBodyLayer);
    }
}
