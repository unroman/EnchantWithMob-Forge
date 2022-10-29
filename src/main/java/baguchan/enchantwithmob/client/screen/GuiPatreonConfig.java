package baguchan.enchantwithmob.client.screen;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IReward;
import baguchan.enchantwithmob.message.PatreonMessage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiPatreonConfig extends OptionsSubScreen {

	private Button changeButton;
	private boolean patreonHunterIllager;

	public GuiPatreonConfig(Screen parentScreenIn, Options gameSettingsIn) {
		super(parentScreenIn, gameSettingsIn, Component.translatable("enchantwithmob.gui.patreon_rewards_option"));
		if (Minecraft.getInstance().player instanceof IReward) {
			patreonHunterIllager = ((IReward) Minecraft.getInstance().player).hasVisibleReward();
		}
	}

	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 20, 16777215);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	protected void init() {
		super.init();
		int i = this.width / 2;
		int j = this.height / 6;
		this.addRenderableWidget(new Button(i - 100, j + 120, 200, 20, CommonComponents.GUI_DONE, (p_213079_1_) -> {
			this.minecraft.setScreen(this.lastScreen);
		}));
		this.addRenderableWidget(changeButton = new Button(i - 100, j, 200, 20, this.patreonHunterIllager ? Component.literal("Disable") : Component.literal("Enable"), (p_213079_1_) -> {
			this.patreonHunterIllager = !this.patreonHunterIllager;
			if (Minecraft.getInstance().player instanceof IReward) {
				((IReward) Minecraft.getInstance().player).setVisibleReward(this.patreonHunterIllager);
			}
			EnchantWithMob.CHANNEL.sendToServer(new PatreonMessage(Minecraft.getInstance().player.getId()));
		}));

	}
}