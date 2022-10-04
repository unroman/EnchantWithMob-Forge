package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.EnchantWithMob;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlimeEnchantLayer<T extends LivingEntity> extends RenderLayer<T, SlimeModel<T>> {
	protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);
	protected static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_GLINT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEntityGlintShader);
	protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENERGY_SWIRL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEnergySwirlShader);
	protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);
	protected static final RenderStateShard.TexturingStateShard ENTITY_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> {
		setupGlintTexturing(0.16F);
	}, () -> {
		RenderSystem.resetTextureMatrix();
	});
	private final EntityModel<T> model;

	public SlimeEnchantLayer(RenderLayerParent<T, SlimeModel<T>> p_174536_, EntityModelSet p_174537_) {
		super(p_174536_);
		this.model = new SlimeModel<>(p_174537_.bakeLayer(ModelLayers.SLIME_OUTER));
	}

	public void render(PoseStack p_117470_, MultiBufferSource p_117471_, int p_117472_, T p_117473_, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_) {
		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = minecraft.shouldEntityAppearGlowing(p_117473_) && p_117473_.isInvisible();

		p_117473_.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant()) {
				if (!p_117473_.isInvisible() || flag) {
					float intensity = cap.getMobEnchants().size() < 3 ? ((float) cap.getMobEnchants().size() / 3) : 3;

					VertexConsumer vertexconsumer = p_117471_.getBuffer(enchantBeamSwirl());

					this.getParentModel().copyPropertiesTo(this.model);
					this.model.prepareMobModel(p_117473_, p_117474_, p_117475_, p_117476_);
					this.model.setupAnim(p_117473_, p_117474_, p_117475_, p_117477_, p_117478_, p_117479_);
					this.model.renderToBuffer(p_117470_, vertexconsumer, p_117472_, LivingEntityRenderer.getOverlayCoords(p_117473_, 0.0F), intensity, intensity, intensity, 1.0F);
				}
			}
		});
	}

	public RenderType enchantBeamSwirl() {
		return RenderType.create("entity_enchant_glint", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, false, false)).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setTransparencyState(ADDITIVE_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
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