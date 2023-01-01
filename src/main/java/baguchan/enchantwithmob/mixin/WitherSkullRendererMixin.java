package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.WitherSkullRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.WitherSkull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkullRenderer.class)
public class WitherSkullRendererMixin {

	@Shadow
	@Final
	private SkullModel model;

	@Inject(method = "render", at = @At("TAIL"))
	public void render(WitherSkull p_116484_, float p_116485_, float p_116486_, PoseStack p_116487_, MultiBufferSource p_116488_, int p_116489_, CallbackInfo callbackInfo) {
		if (p_116484_.getOwner() != null) {
			p_116484_.getOwner().getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
			{
				if (cap.hasEnchant()) {
					p_116487_.pushPose();
					p_116487_.scale(-1.0F, -1.0F, 1.0F);
					float f = Mth.rotlerp(p_116484_.yRotO, p_116484_.getYRot(), p_116486_);
					float f1 = Mth.lerp(p_116486_, p_116484_.xRotO, p_116484_.getXRot());
					VertexConsumer vertexconsumer = p_116488_.getBuffer(EnchantLayer.enchantSwirl(cap.isAncient() ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANT_GLINT_LOCATION));
					this.model.setupAnim(0.0F, f, f1);
					this.model.renderToBuffer(p_116487_, vertexconsumer, p_116489_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
					p_116487_.popPose();
				}
			});
		}
	}
}
