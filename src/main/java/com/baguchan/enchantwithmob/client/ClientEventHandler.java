package com.baguchan.enchantwithmob.client;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
	private static final ResourceLocation ENCHANT_TEXTURE = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	protected static final RenderState.LightmapState LIGHTMAP = new RenderState.LightmapState(true);
	protected static final RenderState.TransparencyState ADDITIVE_TRANSPARENCY = new RenderState.TransparencyState("additive_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	protected static final RenderState.DiffuseLightingState DIFFUSE_LIGHTING_ENABLED = new RenderState.DiffuseLightingState(true);
	protected static final RenderState.FogState BLACK_FOG = new RenderState.FogState("black_fog", () -> {
		RenderSystem.fog(2918, 0.0F, 0.0F, 0.0F, 1.0F);
		RenderSystem.enableFog();
	}, () -> {
		FogRenderer.levelFogColor();
		RenderSystem.disableFog();
	});
	protected static final RenderState.AlphaState DEFAULT_ALPHA = new RenderState.AlphaState(0.003921569F);
	protected static final RenderState.OverlayState OVERLAY_ENABLED = new RenderState.OverlayState(true);
	protected static final RenderState.CullState CULL_DISABLED = new RenderState.CullState(false);

	@SubscribeEvent
	public static void renderEnchantBeam(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
		MatrixStack matrixStack = event.getMatrixStack();
		IRenderTypeBuffer bufferBuilder = event.getBuffers();
		float particalTick = event.getPartialRenderTick();
		event.getEntity().getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap -> {
			if (cap.hasOwner()) {
				LivingEntity entity = cap.getEnchantOwner().get();
				if (entity != null) {
					renderBeam(event.getEntity(), particalTick, matrixStack, bufferBuilder, entity, event.getRenderer());
				}
			}
		});

	}

	public static RenderType enchantBeamSwirl(ResourceLocation p_228636_0_, float p_228636_1_, float p_228636_2_) {
		return RenderType.create("enchant_beam", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, true, RenderType.State.builder().setTextureState(new RenderState.TextureState(p_228636_0_, false, false)).setTexturingState(new RenderState.OffsetTexturingState(p_228636_1_, p_228636_2_)).setFogState(BLACK_FOG).setTransparencyState(ADDITIVE_TRANSPARENCY).setDiffuseLightingState(DIFFUSE_LIGHTING_ENABLED).setAlphaState(DEFAULT_ALPHA).setCullState(CULL_DISABLED).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY_ENABLED).createCompositeState(false));
	}

	private static void renderBeam(LivingEntity p_229118_1_, float p_229118_2_, MatrixStack p_229118_3_, IRenderTypeBuffer p_229118_4_, Entity p_229118_5_, LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {

		float tick = (float) p_229118_1_.tickCount + p_229118_2_;
		p_229118_3_.pushPose();
		Vector3d vector3d = p_229118_5_.getRopeHoldPosition(p_229118_2_);
		double d0 = (double) (MathHelper.lerp(p_229118_2_, p_229118_1_.yBodyRot, p_229118_1_.yBodyRotO) * ((float) Math.PI / 180F)) + (Math.PI / 2D);
		Vector3d vector3d1 = new Vector3d(0.0D, (double) p_229118_1_.getEyeHeight() / 2, 0.0F);
		double d1 = Math.cos(d0) * vector3d1.z + Math.sin(d0) * vector3d1.x;
		double d2 = Math.sin(d0) * vector3d1.z - Math.cos(d0) * vector3d1.x;
		double d3 = MathHelper.lerp((double) p_229118_2_, p_229118_1_.xo, p_229118_1_.getX()) + d1;
		double d4 = MathHelper.lerp((double) p_229118_2_, p_229118_1_.yo, p_229118_1_.getY()) + vector3d1.y;
		double d5 = MathHelper.lerp((double) p_229118_2_, p_229118_1_.zo, p_229118_1_.getZ()) + d2;
		p_229118_3_.translate(d1, vector3d1.y, d2);
		float f = (float) (vector3d.x - d3);
		float f1 = (float) (vector3d.y - d4);
		float f2 = (float) (vector3d.z - d5);
		float f3 = 0.1F;
		IVertexBuilder ivertexbuilder = p_229118_4_.getBuffer(enchantBeamSwirl(ENCHANT_TEXTURE, tick * 0.01F, tick * 0.01F));
		Matrix4f matrix4f = p_229118_3_.last().pose();
		float f4 = MathHelper.fastInvSqrt(f * f + f2 * f2) * 0.1F / 2.0F;
		float f5 = f2 * f4;
		float f6 = f * f4;
		BlockPos blockpos = new BlockPos(p_229118_1_.getEyePosition(p_229118_2_));
		BlockPos blockpos1 = new BlockPos(p_229118_5_.getEyePosition(p_229118_2_));
		int i = getBlockLightLevel(p_229118_1_, blockpos);
		int j = getBlockLightLevel(p_229118_5_, blockpos1);
		int k = p_229118_1_.level.getBrightness(LightType.SKY, blockpos);
		int l = p_229118_1_.level.getBrightness(LightType.SKY, blockpos1);
		renderSide(ivertexbuilder, matrix4f, f, f1, f2, i, j, k, l, 0.05F, 0.1F, f5, f6);
		renderSide(ivertexbuilder, matrix4f, f, f1, f2, i, j, k, l, 0.1F, 0.0F, f5, f6);
		p_229118_3_.popPose();
	}

	public static void renderSide(IVertexBuilder p_229119_0_, Matrix4f p_229119_1_, float p_229119_2_, float p_229119_3_, float p_229119_4_, int p_229119_5_, int p_229119_6_, int p_229119_7_, int p_229119_8_, float p_229119_9_, float p_229119_10_, float p_229119_11_, float p_229119_12_) {
		int i = 24;

		for (int j = 0; j < 24; ++j) {
			float f = (float) j / 23.0F;
			int k = (int) MathHelper.lerp(f, (float) p_229119_5_, (float) p_229119_6_);
			int l = (int) MathHelper.lerp(f, (float) p_229119_7_, (float) p_229119_8_);
			int i1 = LightTexture.pack(k, l);
			addVertexPair(p_229119_0_, p_229119_1_, i1, p_229119_2_, p_229119_3_, p_229119_4_, p_229119_9_, p_229119_10_, 24, j, false, p_229119_11_, p_229119_12_);
			addVertexPair(p_229119_0_, p_229119_1_, i1, p_229119_2_, p_229119_3_, p_229119_4_, p_229119_9_, p_229119_10_, 24, j + 1, true, p_229119_11_, p_229119_12_);
		}

	}

	public static void addVertexPair(IVertexBuilder p_229120_0_, Matrix4f p_229120_1_, int p_229120_2_, float p_229120_3_, float p_229120_4_, float p_229120_5_, float p_229120_6_, float p_229120_7_, int p_229120_8_, int p_229120_9_, boolean p_229120_10_, float p_229120_11_, float p_229120_12_) {
		float f = 0.5F;
		float f1 = 0.4F;
		float f2 = 0.3F;
		if (p_229120_9_ % 2 == 0) {
			f *= 0.7F;
			f1 *= 0.7F;
			f2 *= 0.7F;
		}

		float f3 = (float) p_229120_9_ / (float) p_229120_8_;
		float f4 = p_229120_3_ * f3;
		float f5 = p_229120_4_ > 0.0F ? p_229120_4_ * f3 * f3 : p_229120_4_ - p_229120_4_ * (1.0F - f3) * (1.0F - f3);
		float f6 = p_229120_5_ * f3;
		if (!p_229120_10_) {
			p_229120_0_.vertex(p_229120_1_, f4 + p_229120_11_, f5 + p_229120_6_ - p_229120_7_, f6 - p_229120_12_).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229120_2_).endVertex();
		}

		p_229120_0_.vertex(p_229120_1_, f4 - p_229120_11_, f5 + p_229120_7_, f6 + p_229120_12_).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229120_2_).endVertex();
		if (p_229120_10_) {
			p_229120_0_.vertex(p_229120_1_, f4 + p_229120_11_, f5 + p_229120_6_ - p_229120_7_, f6 - p_229120_12_).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229120_2_).endVertex();
		}

	}

	protected static int getSkyLightLevel(Entity p_239381_1_, BlockPos p_239381_2_) {
		return p_239381_1_.level.getBrightness(LightType.SKY, p_239381_2_);
	}

	protected static int getBlockLightLevel(Entity p_225624_1_, BlockPos p_225624_2_) {
		return p_225624_1_.isOnFire() ? 15 : p_225624_1_.level.getBrightness(LightType.BLOCK, p_225624_2_);
	}
}
