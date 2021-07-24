package com.baguchan.enchantwithmob.registry;

import com.baguchan.enchantwithmob.EnchantWithMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSoundEvents {
	public static final SoundEvent ENCHANTER_AMBIENT = createEvent("entity.enchanter.ambient");
	public static final SoundEvent ENCHANTER_HURT = createEvent("entity.enchanter.hurt");
	public static final SoundEvent ENCHANTER_DEATH = createEvent("entity.enchanter.death");
	public static final SoundEvent ENCHANTER_SPELL = createEvent("entity.enchanter.spell");
	public static final SoundEvent ENCHANTER_ATTACK = createEvent("entity.enchanter.attack");

	private static SoundEvent createEvent(String sound) {
		ResourceLocation name = new ResourceLocation(EnchantWithMob.MODID, sound);
		return (new SoundEvent(name)).setRegistryName(name);
	}

	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> registry) {
		registry.getRegistry().register(ENCHANTER_AMBIENT);
		registry.getRegistry().register(ENCHANTER_HURT);
		registry.getRegistry().register(ENCHANTER_DEATH);
		registry.getRegistry().register(ENCHANTER_SPELL);
		registry.getRegistry().register(ENCHANTER_ATTACK);
	}
}
