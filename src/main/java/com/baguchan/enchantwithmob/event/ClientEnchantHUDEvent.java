package com.baguchan.enchantwithmob.event;

import com.baguchan.enchantwithmob.EnchantConfig;
import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.capability.MobEnchantHandler;
import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.baguchan.enchantwithmob.registry.MobEnchants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientEnchantHUDEvent {

	@SubscribeEvent
	public static void renderHudEvent(RenderGameOverlayEvent.Post event) {
		PoseStack stack = event.getPoseStack();

		Minecraft mc = Minecraft.getInstance();

		if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.crosshairPickEntity != null && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			stack.pushPose();
			mc.crosshairPickEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent((cap) -> {
				if (cap.hasEnchant()) {
					mc.font.draw(stack, mc.crosshairPickEntity.getDisplayName(), (int) 20, (int) 50, 0xe0e0e0);

					for (MobEnchantHandler mobEnchantHandler : cap.getMobEnchants()) {
						MobEnchant mobEnchant = mobEnchantHandler.getMobEnchant();
						int mobEnchantLevel = mobEnchantHandler.getEnchantLevel();

						ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

						MutableComponent s = Component.translatable("mobenchant." + MobEnchants.getRegistry().get().getKey(mobEnchant).getNamespace() + "." + MobEnchants.getRegistry().get().getKey(mobEnchant).getPath()).withStyle(textformatting).append(" ").append(Component.translatable("enchantment.level." + mobEnchantLevel)).withStyle(textformatting);

						int xOffset = 20;
						int yOffset = cap.getMobEnchants().indexOf(mobEnchantHandler) * 10 + 60;

						mc.font.draw(stack, s, (int) (xOffset), (int) yOffset, 0xe0e0e0);
					}
				}
			});
			stack.popPose();
		}
	}
}