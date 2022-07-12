package com.baguchan.enchantwithmob.registry;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.entity.EnchanterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EnchantWithMob.MODID);

    public static final RegistryObject<EntityType<EnchanterEntity>> ENCHANTER = ENTITIES_REGISTRY.register("enchanter", () -> EntityType.Builder.of(EnchanterEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build(prefix("enchanter")));

    private static String prefix(String path) {
        return EnchantWithMob.MODID + "." + path;
    }

    @SubscribeEvent
    public static void registerEntity(EntityAttributeCreationEvent event) {
        event.put(ENCHANTER.get(), EnchanterEntity.createAttributeMap().build());
    }
}
