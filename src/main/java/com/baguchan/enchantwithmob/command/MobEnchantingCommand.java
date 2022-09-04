package com.baguchan.enchantwithmob.command;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.baguchan.enchantwithmob.registry.MobEnchants;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class MobEnchantingCommand {

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_MOB_ENCHANT = (p_136344_, p_136345_) -> {
		Collection<MobEnchant> collection = MobEnchants.getRegistry().get().getValues();
		return SharedSuggestionProvider.suggestResource(collection.stream().map((mobenchant) -> MobEnchants.getRegistry().get().getKey(mobenchant)), p_136345_);
	};


	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		LiteralArgumentBuilder<CommandSourceStack> enchantCommand = Commands.literal("mob_enchanting")
				.requires(player -> player.hasPermission(2));

		enchantCommand.then(Commands.literal("clear").then(Commands.argument("target", EntityArgument.entity()).executes((ctx) -> {
			return setClear(ctx.getSource(), EntityArgument.getEntity(ctx, "target"));
		}))).then(Commands.literal("give").then(Commands.argument("target", EntityArgument.entity())
				.then(Commands.argument("MobEnchantment", MobEnchantArgument.mobEnchantment()).executes((p_198357_0_) -> setMobEnchants(p_198357_0_.getSource(), EntityArgument.getEntity(p_198357_0_, "target"), MobEnchantArgument.getMobEnchant(p_198357_0_, "MobEnchantment"), 1))
						.then(Commands.argument("level", IntegerArgumentType.integer()).executes((p_198357_0_) -> setMobEnchants(p_198357_0_.getSource(), EntityArgument.getEntity(p_198357_0_, "target"), MobEnchantArgument.getMobEnchant(p_198357_0_, "MobEnchantment"), IntegerArgumentType.getInteger(p_198357_0_, "level")))))));

		dispatcher.register(enchantCommand);
	}

	private static int setClear(CommandSourceStack commandStack, Entity entity) {

		if (entity != null) {
			if (entity instanceof LivingEntity) {
				entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
					cap.removeAllMobEnchant((LivingEntity) entity);
				});

				commandStack.sendSuccess(Component.translatable("commands.enchantwithmob.mob_enchanting.clear", entity.getDisplayName()), true);
				return 1;
			} else {
				commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.clear.fail.no_living_entity", entity.getDisplayName()));

				return 0;
			}
		} else {
			commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.clear.fail.no_entity"));

			return 0;
		}
	}

	private static int setMobEnchants(CommandSourceStack commandStack, Entity entity, MobEnchant mobEnchant, int level) {

		if (entity != null) {
			if (entity instanceof LivingEntity) {
				if (mobEnchant != null) {
					entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
						cap.addMobEnchant((LivingEntity) entity, mobEnchant, level);
					});

					commandStack.sendSuccess(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant", entity.getDisplayName(), MobEnchants.getRegistry().get().getKey(mobEnchant)), true);
					return 1;
				} else {
					commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_mobenchant"));

					return 0;
				}
			} else {
				commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_living_entity", entity.getDisplayName()));

				return 0;
			}
		} else {
			commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_entity"));

			return 0;
		}
	}
}