package com.baguchan.enchantwithmob.client.render.layer;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnchantEyeLayer<T extends LivingEntity, M extends EntityModel<T>> extends AbstractEyesLayer<T, M> {
	private final RenderType renderType;
	private final M model;

	public EnchantEyeLayer(IEntityRenderer<T, M> p_i50947_1_, M p_i232478_2_, RenderType renderType) {
		super(p_i50947_1_);
		this.model = p_i232478_2_;
		this.renderType = renderType;
	}

	public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
		p_225628_4_.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
			if (cap.hasEnchant()) {
				this.getParentModel().copyPropertiesTo(this.model);
				this.model.prepareMobModel(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_7_);
				this.model.setupAnim(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
				IVertexBuilder ivertexbuilder = p_225628_2_.getBuffer(this.renderType());
				this.model.renderToBuffer(p_225628_1_, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			}
		});
	}

	@Override
	public RenderType renderType() {
		return renderType;
	}
}