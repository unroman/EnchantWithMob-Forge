package baguchan.enchantwithmob.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

public class BaguLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	protected final BookModel bookModel;

	public BaguLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_i50947_1_, EntityModelSet context) {
		super(p_i50947_1_);
		bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
	}

	public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		String username = Minecraft.getInstance().player.getName().getString();
		if (true) {
			float bookAnimation = 1.0F;
			poseStackIn.pushPose();
			poseStackIn.translate(0.0D, 1.1625D, 0.0F);
			poseStackIn.mulPose(Vector3f.YP.rotationDegrees(-partialTicks * 0.15F));
			poseStackIn.translate(-0.575D, 0.0D, 0.0D);
			poseStackIn.mulPose(Vector3f.ZP.rotationDegrees(60.0F * bookAnimation));

			this.bookModel.setupAnim(0.0F, Mth.clamp(bookAnimation, 0.0F, 0.1F), Mth.clamp(bookAnimation, 0.0F, 0.9F), bookAnimation);
			VertexConsumer ivertexbuilder = EnchantTableRenderer.BOOK_LOCATION.buffer(bufferIn, RenderType::entitySolid);
			this.bookModel.render(poseStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			poseStackIn.popPose();
		}
	}
}