package com.baguchan.enchantwithmob.event;

import com.baguchan.enchantwithmob.EnchantWithMob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientEnchantHUDEvent {

	/*@SubscribeEvent
	public static void renderHudEvent(RenderGameOverlayEvent.Post event) {
		PoseStack stack = event.getPoseStack();

		Minecraft mc = Minecraft.getInstance();

		if (EnchantConfig.showEnchantedMobHud && mc.crosshairPickEntity != null && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			stack.pushPose();
			mc.crosshairPickEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent((cap) -> {
				if (cap.hasEnchant()) {
					mc.font.draw(stack, mc.crosshairPickEntity.getDisplayName(), (int) 20, (int) 50, 0xe0e0e0);

					for (MobEnchantHandler mobEnchantHandler : cap.getMobEnchants()) {
						MobEnchant mobEnchant = mobEnchantHandler.getMobEnchant();
						int mobEnchantLevel = mobEnchantHandler.getEnchantLevel();

						TextFormatting[] textformatting = new TextFormatting[]{TextFormatting.AQUA};

						ITextComponent s = new TranslationTextComponent("mobenchant.enchantwithmob.name." + mobEnchant.getRegistryName().getNamespace() + "." + mobEnchant.getRegistryName().getPath()).withStyle(textformatting).append(" ").append(new TranslationTextComponent("enchantment.level." + mobEnchantLevel)).withStyle(textformatting);

						int xOffset = 20;
						int yOffset = cap.getMobEnchants().indexOf(mobEnchantHandler) * 10 + 60;

						mc.font.draw(stack, s, (int) (xOffset), (int) yOffset, 0xe0e0e0);
					}
				}
			});
			stack.popPose();
		}
	}*/
}