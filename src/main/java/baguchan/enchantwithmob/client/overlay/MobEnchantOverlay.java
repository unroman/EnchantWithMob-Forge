package baguchan.enchantwithmob.client.overlay;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.MobEnchants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class MobEnchantOverlay implements IGuiOverlay {
	@Override
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		Minecraft mc = Minecraft.getInstance();

		if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.crosshairPickEntity != null) {
			poseStack.pushPose();
			mc.crosshairPickEntity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent((cap) -> {
				if (cap.hasEnchant()) {
					mc.font.draw(poseStack, mc.crosshairPickEntity.getDisplayName(), (int) 20, (int) 50, 0xe0e0e0);

					for (MobEnchantHandler mobEnchantHandler : cap.getMobEnchants()) {
						MobEnchant mobEnchant = mobEnchantHandler.getMobEnchant();
						int mobEnchantLevel = mobEnchantHandler.getEnchantLevel();

						ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

						MutableComponent s = Component.translatable("mobenchant." + MobEnchants.getRegistry().get().getKey(mobEnchant).getNamespace() + "." + MobEnchants.getRegistry().get().getKey(mobEnchant).getPath()).withStyle(textformatting).append(" ").append(Component.translatable("enchantment.level." + mobEnchantLevel)).withStyle(textformatting);

						int xOffset = 20;
						int yOffset = cap.getMobEnchants().indexOf(mobEnchantHandler) * 10 + 60;

						mc.font.draw(poseStack, s, (int) (xOffset), (int) yOffset, 0xe0e0e0);
					}
				}
			});
			poseStack.popPose();
		}
	}
}
