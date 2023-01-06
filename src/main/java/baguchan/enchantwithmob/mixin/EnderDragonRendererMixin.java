package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonRenderer.class)
public class EnderDragonRendererMixin {

	@Shadow
	@Final
	private EnderDragonRenderer.DragonModel model;

	@Inject(method = "render", at = @At("TAIL"))
	public void render(EnderDragon p_114208_, float p_114209_, float p_114210_, PoseStack p_114211_, MultiBufferSource p_114212_, int p_114213_, CallbackInfo callbackInfo) {
		p_114208_.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant()) {
				p_114211_.pushPose();
				float f = (float) p_114208_.getLatencyPos(7, p_114210_)[0];
				float f1 = (float) (p_114208_.getLatencyPos(5, p_114210_)[1] - p_114208_.getLatencyPos(10, p_114210_)[1]);
				p_114211_.mulPose(Vector3f.YP.rotationDegrees(-f));
				p_114211_.mulPose(Vector3f.XP.rotationDegrees(f1 * 10.0F));
				p_114211_.translate(0.0F, 0.0F, 1.0F);
				p_114211_.scale(-1.0F, -1.0F, 1.0F);
				p_114211_.translate(0.0F, -1.501F, 0.0F);
				boolean flag = p_114208_.hurtTime > 0;
				this.model.prepareMobModel(p_114208_, 0.0F, 0.0F, p_114210_);
				if (p_114208_.dragonDeathTime <= 0) {
					VertexConsumer vertexconsumer3 = p_114212_.getBuffer(EnchantLayer.enchantSwirl(cap.isAncient() ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANT_GLINT_LOCATION));
					this.model.renderToBuffer(p_114211_, vertexconsumer3, p_114213_, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);
				}

				p_114211_.popPose();
			}
		});
	}
}