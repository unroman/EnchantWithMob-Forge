package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.command.MobEnchantArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModArgumentTypeInfos {
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, EnchantWithMob.MODID);

	public static final RegistryObject<SingletonArgumentInfo<MobEnchantArgument>> MOB_ENCHANT = COMMAND_ARGUMENT_TYPES.register("mob_enchant",
			() -> ArgumentTypeInfos.registerByClass(MobEnchantArgument.class, SingletonArgumentInfo.contextFree(MobEnchantArgument::mobEnchantment)));
}
