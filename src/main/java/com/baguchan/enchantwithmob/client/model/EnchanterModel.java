package com.baguchan.enchantwithmob.client.model;

import com.baguchan.enchantwithmob.entity.EnchanterEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * EnchanterModel - bagu
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class EnchanterModel<T extends EnchanterEntity> extends SegmentedModel<T> implements IHasArm, IHasHead {
    public ModelRenderer legR;
    public ModelRenderer head;
    public ModelRenderer handR;
    public ModelRenderer legL;
    public ModelRenderer handL;
    public ModelRenderer body;
    public ModelRenderer hat;
    public ModelRenderer nose;
    public ModelRenderer cape;
    private final ModelRenderer arms;
    private final ModelRenderer crossArmR;
    private final ModelRenderer crossArmL;

    public EnchanterModel() {
		this.texWidth = 128;
		this.texHeight = 64;
		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.setPos(0.0F, 0.0F, 0.0F);
		this.hat.addBox(-4.0F, -14.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.45F, 0.45F, 0.45F);
		this.body = new ModelRenderer(this, 0, 0);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, 0.0F, 0.0F);
		this.body.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, 0.5F, 0.5F);
		this.nose = new ModelRenderer(this, 24, 0);
		this.nose.setPos(0.0F, -2.0F, 0.0F);
		this.nose.addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F);
		this.legL = new ModelRenderer(this, 0, 22);
		this.legL.mirror = true;
		this.legL.setPos(2.0F, 12.0F, 0.0F);
		this.legL.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
		this.handR = new ModelRenderer(this, 40, 46);
		this.handR.setPos(-5.0F, 2.0F, 0.0F);
		this.handR.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
		this.arms = new ModelRenderer(this, 44, 22);
		this.arms.setPos(0.0F, 2.0F, 0.0F);
		this.arms.addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F);
		ModelRenderer modelrenderer1 = (new ModelRenderer(this, 44, 22));
		modelrenderer1.mirror = true;
		modelrenderer1.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F);
		this.arms.addChild(modelrenderer1);
		this.arms.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F);
		this.crossArmR = (new ModelRenderer(this, 0, 22));
		this.crossArmR.setPos(-2.0F, 12.0F, 0.0F);
		this.crossArmR.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F);
		this.crossArmL = (new ModelRenderer(this, 0, 22));
		this.crossArmL.mirror = true;
		this.crossArmL.setPos(2.0F, 12.0F, 0.0F);
		this.crossArmL.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, 0.0F, 0.0F);
		this.legR = new ModelRenderer(this, 0, 22);
		this.legR.setPos(-2.0F, 12.0F, 0.0F);
		this.legR.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.handL = new ModelRenderer(this, 40, 46);
		this.handL.mirror = true;
		this.handL.setPos(5.0F, 2.0F, 0.0F);
		this.handL.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
		this.cape = new ModelRenderer(this, 64, 0);
		this.cape.setPos(0.0F, 1.0F, 3.4F);
		this.cape.addBox(-4.5F, 0.0F, 0.0F, 9.0F, 15.0F, 1.0F, 0.6F, 0.7F, 0.0F);
        this.setRotateAngle(cape, 0.1563815016444822F, 0.0F, 0.0F);
        this.head.addChild(this.hat);
        this.head.addChild(this.nose);
        this.body.addChild(this.cape);
    }

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.handL, this.legL, this.arms, this.legR, this.handR, this.head, this.body);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		this.arms.y = 3.0F;
		this.arms.z = -1.0F;
		this.arms.xRot = -0.75F;

		this.legL.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.legR.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;

		this.cape.xRot = 0.1F + limbSwingAmount * 0.6F;

		if (this.riding) {
			this.handR.xRot = (-(float) Math.PI / 5F);
			this.handR.yRot = 0.0F;
			this.handR.zRot = 0.0F;
			this.handL.xRot = (-(float) Math.PI / 5F);
			this.handL.yRot = 0.0F;
			this.handL.zRot = 0.0F;
			this.crossArmR.xRot = -1.4137167F;
			this.crossArmR.yRot = ((float) Math.PI / 10F);
			this.crossArmR.zRot = 0.07853982F;
			this.crossArmL.xRot = -1.4137167F;
			this.crossArmL.yRot = (-(float) Math.PI / 10F);
			this.crossArmL.zRot = -0.07853982F;
		} else {
			this.handR.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.handR.yRot = 0.0F;
			this.handR.zRot = 0.0F;
			this.handL.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.handL.yRot = 0.0F;
			this.handL.zRot = 0.0F;
			this.crossArmR.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.crossArmR.yRot = 0.0F;
			this.crossArmR.zRot = 0.0F;
			this.crossArmL.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.crossArmL.yRot = 0.0F;
			this.crossArmL.zRot = 0.0F;
		}

        AbstractIllagerEntity.ArmPose abstractillagerentity$armpose = entityIn.getArmPose();
        if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (entityIn.getMainHandItem().isEmpty()) {
				ModelHelper.animateZombieArms(this.handL, this.handR, true, this.attackTime, ageInTicks);
			} else {
				ModelHelper.swingWeaponDown(this.handR, this.handL, entityIn, this.attackTime, ageInTicks);
			}
		} else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
			this.handR.z = 0.0F;
			this.handR.x = -5.0F;
			this.handL.z = 0.0F;
			this.handL.x = 5.0F;
			this.handR.xRot = -0.95F + MathHelper.cos(ageInTicks * 0.265F) * 0.075F;
			this.handL.xRot = -0.95F + MathHelper.cos(ageInTicks * 0.265F) * 0.075F;
			this.handR.zRot = -MathHelper.cos(ageInTicks * 0.265F) * 0.075F;
			this.handL.zRot = MathHelper.cos(ageInTicks * 0.265F) * 0.075F;
			this.handR.yRot = 0.0F;
			this.handL.yRot = 0.0F;
		} else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.handR.z = 0.0F;
			this.handR.x = -5.0F;
			this.handR.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.handR.zRot = 2.670354F;
			this.handR.yRot = 0.0F;
			this.handL.z = 0.0F;
			this.handL.x = 5.0F;
			this.handL.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.handL.zRot = -2.3561945F;
			this.handL.yRot = 0.0F;
			this.cape.xRot = 0.1F + limbSwingAmount * 0.6F + 0.5F;
		} else {
			if (this.attackTime > 0) {
				this.arms.xRot = -0.75F - 0.6F * (1.0F - this.attackTime);
			}
		}

		boolean flag = abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.visible = flag;
		this.handL.visible = !flag;
		this.handR.visible = !flag;
	}

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void translateToHand(HandSide p_225599_1_, MatrixStack p_225599_2_) {

	}

	@Override
	public ModelRenderer getHead() {
		return this.head;
	}
}
