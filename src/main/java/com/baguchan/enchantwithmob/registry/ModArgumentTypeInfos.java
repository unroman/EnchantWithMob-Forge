package com.baguchan.enchantwithmob.registry;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.command.MobEnchantArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModArgumentTypeInfos {
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, EnchantWithMob.MODID);

	public static final RegistryObject<SingletonArgumentInfo<MobEnchantArgument>> MOB_ENCHANT = COMMAND_ARGUMENT_TYPES.register("mob_enchant",
			() -> ArgumentTypeInfos.registerByClass(MobEnchantArgument.class, SingletonArgumentInfo.contextFree(MobEnchantArgument::mobEnchantment)));
}
