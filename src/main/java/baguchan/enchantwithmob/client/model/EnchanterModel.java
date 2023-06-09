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
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart cape;
	private final ModelPart illagerArms;
	private final ModelPart bipedArms;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart armorLeftLeg;
	private final ModelPart armorRightLeg;
	private final ModelPart rightArm;
	private final ModelPart leftArm;

	private final ModelPart rightHand;
	private final ModelPart leftHand;
	private final ModelPart illagerRightArm;
	private final ModelPart illagerLeftArm;
	private final ModelPart armorBipedLeftArm;
	private final ModelPart armorBipedRightArm;

	public EnchanterModel(ModelPart root) {
		this.realRoot = root;
		this.root = root.getChild("root");
		this.body = this.root.getChild("bipedBody");
		this.head = this.body.getChild("bipedHeadBaseRotator");
		this.cape = this.body.getChild("bipedCape");
		this.illagerArms = this.body.getChild("illagerArms");
		this.illagerRightArm = this.illagerArms.getChild("armorIllagerLeftArm");
		this.illagerLeftArm = this.illagerArms.getChild("armorIllagerRightArm");
		this.leftLeg = this.root.getChild("bipedLegs").getChild("bipedLegLeft");
		this.rightLeg = this.root.getChild("bipedLegs").getChild("bipedLegRight");
		this.armorRightLeg = this.rightLeg.getChild("armorBipedRightLeg");
		this.armorLeftLeg = this.leftLeg.getChild("armorBipedLeftLeg");
		this.bipedArms = this.body.getChild("bipedArms");
		this.leftArm = this.bipedArms.getChild("bipedArmLeft");
		this.rightArm = this.bipedArms.getChild("bipedArmRight");
		this.leftHand = this.leftArm.getChild("bipedHandLeft");
		this.rightHand = this.rightArm.getChild("bipedHandRight");
		this.armorBipedLeftArm = this.leftArm.getChild("armorBipedLeftArm");
		this.armorBipedRightArm = this.rightArm.getChild("armorBipedRightArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition bipedLegs = root.addOrReplaceChild("bipedLegs", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition bipedLegLeft = bipedLegs.addOrReplaceChild("bipedLegLeft", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition armorBipedLeftLeg = bipedLegLeft.addOrReplaceChild("armorBipedLeftLeg", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armorBipedLeftFoot = bipedLegLeft.addOrReplaceChild("armorBipedLeftFoot", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bipedLegRight = bipedLegs.addOrReplaceChild("bipedLegRight", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition armorBipedRightLeg = bipedLegRight.addOrReplaceChild("armorBipedRightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armorBipedRightFoot = bipedLegRight.addOrReplaceChild("armorBipedRightFoot", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bipedBody = root.addOrReplaceChild("bipedBody", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 38).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 19.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition bipedArms = bipedBody.addOrReplaceChild("bipedArms", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition bipedArmLeft = bipedArms.addOrReplaceChild("bipedArmLeft", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition bipedHandLeft = bipedArmLeft.addOrReplaceChild("bipedHandLeft", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 11.0F, 0.0F));

		PartDefinition armorBipedLeftArm = bipedArmLeft.addOrReplaceChild("armorBipedLeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bipedArmRight = bipedArms.addOrReplaceChild("bipedArmRight", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition bipedHandRight = bipedArmRight.addOrReplaceChild("bipedHandRight", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 11.0F, 0.0F));

		PartDefinition armorBipedRightArm = bipedArmRight.addOrReplaceChild("armorBipedRightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition illagerArms = bipedBody.addOrReplaceChild("illagerArms", CubeListBuilder.create().texOffs(44, 22).addBox(4.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 22).mirror().addBox(-8.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 38).addBox(-4.0F, 4.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.5F, 0.3F, -0.7505F, 0.0F, 0.0F));

		PartDefinition armorIllagerLeftArm = illagerArms.addOrReplaceChild("armorIllagerLeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -1.5F, -2.05F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition armorIllagerRightArm = illagerArms.addOrReplaceChild("armorIllagerRightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -1.5F, -2.05F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition bipedCape = bipedBody.addOrReplaceChild("bipedCape", CubeListBuilder.create().texOffs(0, 64).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 3.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition bipedPotionSlot = bipedBody.addOrReplaceChild("bipedPotionSlot", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 1.0F, 0.0F));

		PartDefinition armorBipedBody = bipedBody.addOrReplaceChild("armorBipedBody", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition bipedHeadBaseRotator = bipedBody.addOrReplaceChild("bipedHeadBaseRotator", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition bipedHead = bipedHeadBaseRotator.addOrReplaceChild("bipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(1, 91).addBox(-5.0F, -14.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armorBipedHead = bipedHead.addOrReplaceChild("armorBipedHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition book = root.addOrReplaceChild("book", CubeListBuilder.create(), PartPose.offset(0.0F, -16.5F, -9.0F));

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

		boolean flag = abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CROSSED;
		if (this.riding && abstractillager$illagerarmpose != AbstractIllager.IllagerArmPose.SPELLCASTING) {
			this.rightHand.xRot = (-(float) Math.PI / 5F);
			this.rightHand.yRot = 0.0F;
			this.rightHand.zRot = 0.0F;
			this.leftHand.xRot = (-(float) Math.PI / 5F);
			this.leftHand.yRot = 0.0F;
			this.leftHand.zRot = 0.0F;
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float) Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		}
		this.armorBipedRightArm.visible = false;
		this.armorBipedLeftArm.visible = false;
		this.illagerRightArm.visible = false;
		this.illagerLeftArm.visible = false;
		this.armorRightLeg.visible = false;
		this.armorLeftLeg.visible = false;
		this.rightHand.visible = false;
		this.leftHand.visible = false;

		this.animateWalk(EnchanterAnimation.ENCHANTER_MCD_WALK, limbSwing, limbSwingAmount, 3.0F, 4.5F);

		if (!entity.castingAnimationState.isStarted() && !entity.attackAnimationState.isStarted()) {
			this.animateWalk(EnchanterAnimation.ENCHANTER_IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);

		}

		this.animate(entity.idleAnimationState, EnchanterAnimation.ENCHANTER_MCD_IDLE, ageInTicks);
		this.animate(entity.attackAnimationState, EnchanterAnimation.ENCHANTER_MCD_ATTACK, ageInTicks, 3.0F);
		this.animate(entity.castingAnimationState, EnchanterAnimation.ENCHANTER_MCD_CAST_SPELL, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return this.realRoot;
	}

	@Override
	public void translateToHead(ModelPart modelPart, PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToChest(ModelPart modelPart, PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToLeg(ModelPart modelPart, PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		this.root.getChild("bipedLegs").translateAndRotate(poseStack);
		if (modelPart == armorRightLeg) {
			this.rightLeg.translateAndRotate(poseStack);
		} else {
			this.leftLeg.translateAndRotate(poseStack);
		}
		modelPart.translateAndRotate(poseStack);
		poseStack.translate(0, 0.75F, 0);
	}

	@Override
	public void translateToChestPat(ModelPart modelPart, PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		if ((this.illagerRightArm == modelPart || this.illagerLeftArm == modelPart)) {
			this.illagerArms.translateAndRotate(poseStack);
			modelPart.translateAndRotate(poseStack);
			if (modelPart == illagerRightArm) {
				poseStack.translate(0.195F, 0F, 0F);
			} else {
				poseStack.translate(-0.195F, 0F, 0F);
			}
		} else {
			this.bipedArms.translateAndRotate(poseStack);
			if (this.armorBipedRightArm == modelPart) {
				this.rightArm.translateAndRotate(poseStack);
			} else if (this.armorBipedLeftArm == modelPart) {
				this.leftArm.translateAndRotate(poseStack);
			}
			modelPart.translateAndRotate(poseStack);
		}

	}

	@Override
	public Iterable<ModelPart> rightHandArmors() {
		return ImmutableList.of(this.armorBipedRightArm, this.illagerRightArm);
	}

	@Override
	public Iterable<ModelPart> leftHandArmors() {
		return ImmutableList.of(this.armorBipedLeftArm, this.illagerLeftArm);
	}

	@Override
	public Iterable<ModelPart> rightLegPartArmors() {
		return ImmutableList.of(this.armorRightLeg);
	}

	@Override
	public Iterable<ModelPart> leftLegPartArmors() {
		return ImmutableList.of(this.armorLeftLeg);
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
