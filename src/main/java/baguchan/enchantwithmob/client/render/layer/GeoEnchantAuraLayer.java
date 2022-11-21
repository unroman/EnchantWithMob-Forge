package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.client.render.layer.geo.EnchantedAuraGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GeoEnchantAuraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> implements IGeoRenderer {
	private final EnchantedAuraGeoModel modelProvider;
	protected float widthScale = 1;
	protected float heightScale = 1;

	public GeoEnchantAuraLayer(RenderLayerParent<T, M> p_i50947_1_) {
		super(p_i50947_1_);
		this.modelProvider = new EnchantedAuraGeoModel();
	}

	public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float tick = (float) entitylivingbaseIn.tickCount + partialTicks;
		entitylivingbaseIn.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant() && !EnchantConfig.CLIENT.disableAuraRender.get()) {
				float f = (float) entitylivingbaseIn.tickCount + partialTicks;
				poseStackIn.pushPose();
				poseStackIn.mulPose(Vector3f.YP.rotationDegrees(f * 15F));
				poseStackIn.scale(-1.0F, -1.0F, 1.0F);
				poseStackIn.translate(0.0D, (double) -1.501F, 0.0D);
				GeoModel model = modelProvider.getModel(modelProvider.getModelResource(entitylivingbaseIn));
				RenderType renderType = RenderType.entityTranslucentEmissive(this.modelProvider.getTextureResource(entitylivingbaseIn));
				if (!entitylivingbaseIn.isInvisibleTo(Minecraft.getInstance().player))
					render(model, entitylivingbaseIn, partialTicks, renderType, poseStackIn, bufferIn, null, packedLightIn,
							getPackedOverlay(entitylivingbaseIn, 0), 1.0f,
							(float) 1.0f, (float) 1.0f,
							(float) 1.0f);
				poseStackIn.popPose();
			}
		});
	}


	public static int getPackedOverlay(Entity livingEntityIn, float uIn) {
		return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(false));
	}

	protected MultiBufferSource rtb = null;

	@Override
	public MultiBufferSource getCurrentRTB() {
		return rtb;
	}

	@Override
	public void setCurrentRTB(MultiBufferSource rtb) {
		this.rtb = rtb;
	}

	@Override
	public GeoModelProvider<T> getGeoModelProvider() {
		return this.modelProvider;
	}

	@Override
	public ResourceLocation getTextureLocation(Object instance) {
		return this.modelProvider.getTextureResource(instance);
	}

	@Override
	public float getWidthScale(Object animatable2) {
		return this.widthScale;
	}

	@Override
	public float getHeightScale(Object entity) {
		return this.heightScale;
	}
}