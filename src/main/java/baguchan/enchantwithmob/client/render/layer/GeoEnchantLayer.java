package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.EnchantWithMob;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GeoEnchantLayer<T extends Entity & IAnimatable> extends GeoLayerRenderer<T> {
	protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);
	protected static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENERGY_SWIRL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEnergySwirlShader);
	protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);
	protected static final RenderStateShard.TexturingStateShard ENTITY_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> {
		setupGlintTexturing(0.16F);
	}, () -> {
		RenderSystem.resetTextureMatrix();
	});
	protected static final RenderStateShard.DepthTestStateShard EQUAL_DEPTH_TEST = new RenderStateShard.DepthTestStateShard("==", 514);


	public GeoEnchantLayer(IGeoRenderer<T> p_i50947_1_) {
		super(p_i50947_1_);
	}

	public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float tick = (float) entitylivingbaseIn.tickCount + partialTicks;
		entitylivingbaseIn.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant()) {
				float f = (float) entitylivingbaseIn.tickCount + partialTicks;
				RenderType glint = enchantSwirl();
				this.getRenderer().render(this.getEntityModel().getModel(this.getEntityModel().getModelResource(entitylivingbaseIn)), entitylivingbaseIn, partialTicks, glint, poseStackIn, bufferIn,
						bufferIn.getBuffer(glint), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
			}
		});
	}

	public static RenderType enchantSwirl() {
		return RenderType.create("entity_enchant_glint", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, false, false)).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(ADDITIVE_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
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