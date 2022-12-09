package baguchan.enchantwithmob.client.render;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.client.ModModelLayers;
import baguchan.enchantwithmob.client.model.EnchanterModel;
import baguchan.enchantwithmob.entity.EnchanterEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnchanterRenderer<T extends EnchanterEntity> extends MobRenderer<T, EnchanterModel<T>> {
    private static final ResourceLocation ILLAGER = new ResourceLocation(EnchantWithMob.MODID, "textures/entity/enchanter.png");


    protected final BookModel bookModel;

    public EnchanterRenderer(EntityRendererProvider.Context p_173952_) {
        super(p_173952_, new EnchanterModel<>(p_173952_.bakeLayer(ModModelLayers.ENCHANTER)), 0.5F);
        //this.addLayer(new HeadLayer<>(this));
        bookModel = new BookModel(p_173952_.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        float bookAnimation = entityIn.getBookAnimationScale(partialTicks);

        float f = Mth.approach(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        float swingProgress = this.getAttackAnim(entityIn, partialTicks);

        if (entityIn.isAlive()) {
            matrixStackIn.pushPose();
			matrixStackIn.translate(0.0D, 1.1625D, 0.0F);
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f + 90F));
			matrixStackIn.translate(-0.575D, 0.0D, 0.0D);
			matrixStackIn.mulPose(Axis.ZP.rotationDegrees(60.0F * bookAnimation));

            //When spell casting, stop animation
            if (swingProgress > 0 && !entityIn.isCastingSpell()) {
				matrixStackIn.translate(-0.05F * (1.0F - swingProgress), -0.1F * (1.0F - swingProgress), 0.0D);
				matrixStackIn.mulPose(Axis.ZP.rotationDegrees(45.0F * (1.0F - swingProgress)));
            }

            this.bookModel.setupAnim(0.0F, Mth.clamp(bookAnimation, 0.0F, 0.1F), Mth.clamp(bookAnimation, 0.0F, 0.9F), bookAnimation);
            VertexConsumer ivertexbuilder = EnchantTableRenderer.BOOK_LOCATION.buffer(bufferIn, RenderType::entitySolid);
            this.bookModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return ILLAGER;
    }
}