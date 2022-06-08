package com.baguchan.enchantwithmob.registry;

import com.baguchan.enchantwithmob.EnchantWithMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSoundEvents {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EnchantWithMob.MODID);

	public static final RegistryObject<SoundEvent> ENCHANTER_AMBIENT = createEvent("entity.enchanter.ambient");
	public static final RegistryObject<SoundEvent> ENCHANTER_HURT = createEvent("entity.enchanter.hurt");
	public static final RegistryObject<SoundEvent> ENCHANTER_DEATH = createEvent("entity.enchanter.death");
	public static final RegistryObject<SoundEvent> ENCHANTER_SPELL = createEvent("entity.enchanter.spell");
	public static final RegistryObject<SoundEvent> ENCHANTER_ATTACK = createEvent("entity.enchanter.attack");

	private static RegistryObject<SoundEvent> createEvent(String sound) {
		ResourceLocation name = new ResourceLocation(EnchantWithMob.MODID, sound);
		return SOUND_EVENTS.register(sound, () -> new SoundEvent(name));
	}

}
