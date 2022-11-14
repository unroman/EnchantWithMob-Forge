package baguchan.enchantwithmob.client.render.layer;

import baguchan.enchantwithmob.EnchantWithMob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GeoEnchantAuraLayer<T extends Entity & IAnimatable> extends GeoLayerRenderer<T> {
	public static final ResourceLocation LOCATION = new ResourceLocation(EnchantWithMob.MODID, "textures/entity/enchanted_mob_aureole.png");

	private final RenderType renderType = RenderType.entityTranslucent(LOCATION);


	public GeoEnchantAuraLayer(IGeoRenderer<T> p_i50947_1_) {
		super(p_i50947_1_);
	}

	public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float tick = (float) entitylivingbaseIn.tickCount + partialTicks;
		entitylivingbaseIn.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant()) {
				float f = (float) entitylivingbaseIn.tickCount + partialTicks;
				RenderType glint = renderType;
				this.getRenderer().render(this.getEntityModel().getModel(this.getAuraModelResource()), entitylivingbaseIn, partialTicks, glint, poseStackIn, bufferIn,
						bufferIn.getBuffer(glint), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
			}
		});
	}

	public ResourceLocation getAuraModelResource() {
		return new ResourceLocation(EnchantWithMob.MODID, "geo/enchanted_aura.geo.json");
	}
}