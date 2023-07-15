package baguchan.enchantwithmob.client.model;

import bagu_chan.bagus_lib.client.layer.IArmor;
import baguchan.enchantwithmob.client.animation.EnchanterAnimation;
import baguchan.enchantwithmob.entity.EnchanterEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnchanterModel<T extends EnchanterEntity> extends HierarchicalModel<T> implements IArmor {
	private final ModelPart realRoot;
	private final ModelPart everything;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart cape;

	public EnchanterModel(ModelPart everything) {
		this.realRoot = everything;
		this.everything = everything.getChild("everything");
		this.body = this.everything.getChild("body");
		this.cape = this.body.getChild("Cape");
		this.head = this.body.getChild("head");
		this.leftLeg = this.everything.getChild("left_leg");
		this.rightLeg = this.everything.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_leg = everything.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition right_leg = everything.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

		PartDefinition body = everything.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 38).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition Cape = body.addOrReplaceChild("Cape", CubeListBuilder.create().texOffs(0, 64).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 3.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(1, 91).addBox(-5.0F, -14.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leftEye = head.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(6, 5).addBox(0.0F, -1.4604F, 0.74F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.5F, -4.75F));

		PartDefinition rightEye = head.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(6, 5).addBox(-1.0F, -1.4604F, 0.74F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -2.5F, -4.75F));

		PartDefinition righteyebrows = head.addOrReplaceChild("righteyebrows", CubeListBuilder.create(), PartPose.offset(-2.5F, -4.9604F, -3.5196F));

		PartDefinition righteyebrows_r1 = righteyebrows.addOrReplaceChild("righteyebrows_r1", CubeListBuilder.create().texOffs(0, 5).addBox(-1.5F, -1.0F, -0.5902F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.0902F, 0.0F, 3.1416F, 0.0F));

		PartDefinition lefteyebrows = head.addOrReplaceChild("lefteyebrows", CubeListBuilder.create(), PartPose.offset(2.5F, -4.9604F, -3.5196F));

		PartDefinition lefteyebrows_r1 = lefteyebrows.addOrReplaceChild("lefteyebrows_r1", CubeListBuilder.create().texOffs(0, 5).mirror().addBox(-1.5F, -1.0F, -0.5902F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.0902F, 0.0F, 3.1416F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -10.0F, 0.0F));

		PartDefinition leftHand = left_arm.addOrReplaceChild("leftHand", CubeListBuilder.create(), PartPose.offset(1.0F, 11.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -10.0F, 0.0F));

		PartDefinition rightHand = right_arm.addOrReplaceChild("rightHand", CubeListBuilder.create(), PartPose.offset(-1.0F, 11.0F, 0.0F));

		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -0.95F, -0.7854F, 0.0F, 0.0F));

		PartDefinition book = everything.addOrReplaceChild("book", CubeListBuilder.create(), PartPose.offset(0.0F, -18.5F, -9.0F));

		PartDefinition leftBookCover = book.addOrReplaceChild("leftBookCover", CubeListBuilder.create().texOffs(26, 75).addBox(-8.0F, -5.0F, -1.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(44, 63).addBox(-6.5F, -4.0F, -0.25F, 6.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition leftPage = leftBookCover.addOrReplaceChild("leftPage", CubeListBuilder.create().texOffs(44, 63).addBox(-6.5F, -4.0F, -0.24F, 6.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightBookCover = book.addOrReplaceChild("rightBookCover", CubeListBuilder.create().texOffs(26, 63).addBox(0.0F, -5.0F, -1.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(44, 63).addBox(0.5F, -4.0F, -0.25F, 6.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition rightPage = rightBookCover.addOrReplaceChild("rightPage", CubeListBuilder.create().texOffs(44, 63).addBox(0.5F, -4.0F, -0.24F, 6.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		this.cape.xRot = 0.1F + limbSwingAmount * 0.6F;

		AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = entity.getArmPose();
		if (this.riding) {
			this.applyStatic(EnchanterAnimation.ENCHANTER_SIT);
		}

		if (entity.castingAnimationState.isStarted()) {
			this.animate(entity.castingAnimationState, EnchanterAnimation.ENCHANTER_ENCHANCE, ageInTicks);
		}

		if (entity.attackAnimationState.isStarted()) {
			this.animate(entity.attackAnimationState, EnchanterAnimation.ENCHANTER_ATTACK, ageInTicks);
		}

		if (!entity.castingAnimationState.isStarted() && !entity.attackAnimationState.isStarted()) {
			this.animate(entity.idleAnimationState, EnchanterAnimation.ENCHANTER_IDLE, ageInTicks);
			this.applyStatic(EnchanterAnimation.ENCHANTER_NO_ARM);
		}
		this.animateWalk(EnchanterAnimation.ENCHANTER_WALK, limbSwing, limbSwingAmount, 3.0F, 4.5F);
	}

	@Override
	public ModelPart root() {
		return this.realRoot;
	}

	@Override
	public void translateToHead(ModelPart modelPart, PoseStack poseStack) {
		this.everything.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToChest(ModelPart modelPart, PoseStack poseStack) {
		this.everything.translateAndRotate(poseStack);
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToLeg(ModelPart modelPart, PoseStack poseStack) {
		this.everything.translateAndRotate(poseStack);
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToChestPat(ModelPart modelPart, PoseStack poseStack) {

	}

	@Override
	public Iterable<ModelPart> rightLegPartArmors() {
		return ImmutableList.of(this.rightLeg);
	}

	@Override
	public Iterable<ModelPart> leftLegPartArmors() {
		return ImmutableList.of(this.leftLeg);
	}

	@Override
	public Iterable<ModelPart> bodyPartArmors() {
		return ImmutableList.of(this.body);
	}

	@Override
	public Iterable<ModelPart> headPartArmors() {
		return ImmutableList.of(this.head);
	}
}
