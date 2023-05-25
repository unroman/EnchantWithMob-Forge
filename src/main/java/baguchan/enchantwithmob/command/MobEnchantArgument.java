package baguchan.enchantwithmob.command;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.MobEnchants;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MobEnchantArgument implements ArgumentType<MobEnchant> {
	private static final Collection<String> EXAMPLES = Arrays.asList("enchantwithmob:strong", "enchantwithmob:protection");
	public static final DynamicCommandExceptionType ERROR_UNKNOWN_EFFECT = new DynamicCommandExceptionType((p_208663_0_) -> {
		return Component.translatable("mobEnchantment.mobEnchantmentNotFound", p_208663_0_);
	});

	public static MobEnchantArgument mobEnchantment() {
		return new MobEnchantArgument();
	}

	public static MobEnchant getMobEnchant(CommandContext<CommandSourceStack> source, String name) {
		return source.getArgument(name, MobEnchant.class);
	}

	public MobEnchant parse(StringReader p_parse_1_) throws CommandSyntaxException {
		ResourceLocation resourcelocation = ResourceLocation.read(p_parse_1_);
		if (MobEnchants.getRegistry().get().containsKey(resourcelocation)) {
			return MobEnchants.getRegistry().get().getValue(resourcelocation);
		} else {
			throw ERROR_UNKNOWN_EFFECT.create(resourcelocation);
		}
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_listSuggestions_1_, SuggestionsBuilder p_listSuggestions_2_) {
		return SharedSuggestionProvider.suggestResource(MobEnchants.getRegistry().get().getKeys().stream().filter(resource -> !EnchantConfig.COMMON.DISABLE_ENCHANTS.get().contains(resource.toString())), p_listSuggestions_2_);
	}

	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}