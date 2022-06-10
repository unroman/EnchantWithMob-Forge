package com.baguchan.enchantwithmob.registry;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.loot.MobEnchantRandomlyFunction;
import com.baguchan.enchantwithmob.loot.MobEnchantWithLevelsFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ModLootItemFunctions {
	public static final LootItemFunctionType MOB_ENCHANT_WITH_LEVELS = register("mob_enchant_with_levels", new MobEnchantWithLevelsFunction.Serializer());
	public static final LootItemFunctionType MOB_ENCHANT_RANDOMLY_FUNCTION = register("mob_enchant_randomly_function", new MobEnchantRandomlyFunction.Serializer());


	private static LootItemFunctionType register(String p_80763_, Serializer<? extends LootItemFunction> p_80764_) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(EnchantWithMob.MODID, p_80763_), new LootItemFunctionType(p_80764_));
	}

	public static void init() {

	}
}
