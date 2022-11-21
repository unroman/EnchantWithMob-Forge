package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.EnchantWithMob;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static baguchan.enchantwithmob.client.render.layer.EnchantLayer.ANCIENT_GLINT;
import static baguchan.enchantwithmob.client.render.layer.EnchantLayer.enchantSwirl;

public class GeoEnchantLayer<T extends Entity & IAnimatable> extends GeoLayerRenderer<T> {

	public GeoEnchantLayer(IGeoRenderer<T> p_i50947_1_) {
		super(p_i50947_1_);
	}

	public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float tick = (float) entitylivingbaseIn.tickCount + partialTicks;
		entitylivingbaseIn.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant() && !entitylivingbaseIn.isInvisible()) {
				RenderType glint = enchantSwirl(cap.isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANT_GLINT_LOCATION);
				this.getRenderer().render(this.getEntityModel().getModel(this.getEntityModel().getModelResource(entitylivingbaseIn)), entitylivingbaseIn, partialTicks, glint, poseStackIn, bufferIn,
						bufferIn.getBuffer(glint), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
			}
		});
	}

	private static void setupGlintTexturing(float p_110187_) {
		long var1 = Util.getMillis() * 8L;
		float var3 = (float) (var1 % 110000L) / 110000.0F;
		float var4 = (float) (var1 % 30000L) / 30000.0F;
		Matrix4f var5 = Matrix4f.createTranslateMatrix(-var3, var4, 0.0F);
		var5.multiply(Vector3f.ZP.rotationDegrees(10.0F));
		var5.multiply(Matrix4f.createScaleMatrix(p_110187_, p_110187_, p_110187_));
		RenderSystem.setTextureMatrix(var5);
	}
}