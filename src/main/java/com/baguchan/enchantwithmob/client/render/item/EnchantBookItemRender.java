package com.baguchan.enchantwithmob.client.render.item;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.registry.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnchantBookItemRender extends ItemStackTileEntityRenderer {
	public static final ResourceLocation BOOK_LOCATION = new ResourceLocation("textures/entity/enchanting_table_book.png");
	public static final ResourceLocation UNENCHANTED_BOOK_LOCATION = new ResourceLocation(EnchantWithMob.MODID, "textures/entity/mob_unenchant_book.png");
	private final BookModel bookModel = new BookModel();


	public void renderByItem(ItemStack p_239207_1_, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack p_239207_3_, IRenderTypeBuffer p_239207_4_, int p_239207_5_, int p_239207_6_) {
		Item item = p_239207_1_.getItem();

		if (item == ModItems.MOB_ENCHANT_BOOK) {
			p_239207_3_.pushPose();
			p_239207_3_.scale(1.0F, -1.0F, -1.0F);
			this.bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.0F);
			IVertexBuilder ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(p_239207_4_, this.bookModel.renderType(BOOK_LOCATION), false, p_239207_1_.hasFoil());
			this.bookModel.renderToBuffer(p_239207_3_, ivertexbuilder1, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);
			p_239207_3_.popPose();
		} else if (item == ModItems.MOB_UNENCHANT_BOOK) {
			p_239207_3_.pushPose();
			p_239207_3_.scale(1.0F, -1.0F, -1.0F);
			this.bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.0F);
			IVertexBuilder ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(p_239207_4_, this.bookModel.renderType(UNENCHANTED_BOOK_LOCATION), false, p_239207_1_.hasFoil());
			this.bookModel.renderToBuffer(p_239207_3_, ivertexbuilder1, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);
			p_239207_3_.popPose();
		}
	}
}
