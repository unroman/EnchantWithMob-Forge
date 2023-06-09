package baguchan.enchantwithmob.client.overlay;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.MobEnchants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class MobEnchantOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();

        if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.crosshairPickEntity != null) {
            if (mc.crosshairPickEntity instanceof IEnchantCap cap) {
                if (cap.getEnchantCap().hasEnchant()) {
                    guiGraphics.drawString(mc.font, mc.crosshairPickEntity.getDisplayName(), (int) 20, (int) 50, 0xe0e0e0);

                    for (MobEnchantHandler mobEnchantHandler : cap.getEnchantCap().getMobEnchants()) {
                        MobEnchant mobEnchant = mobEnchantHandler.getMobEnchant();
                        int mobEnchantLevel = mobEnchantHandler.getEnchantLevel();

                        ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

                        MutableComponent s = Component.translatable("mobenchant." + MobEnchants.getRegistry().get().getKey(mobEnchant).getNamespace() + "." + MobEnchants.getRegistry().get().getKey(mobEnchant).getPath()).withStyle(textformatting).append(" ").append(Component.translatable("enchantment.level." + mobEnchantLevel)).withStyle(textformatting);

                        int xOffset = 20;
                        int yOffset = cap.getEnchantCap().getMobEnchants().indexOf(mobEnchantHandler) * 10 + 60;

                        guiGraphics.drawString(mc.font, s, (int) (xOffset), (int) yOffset, 0xe0e0e0);
                    }
                }
            }
        }
	}
}
