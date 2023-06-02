package baguchan.enchantwithmob.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class EnchanterClothesModel<T extends LivingEntity> extends HumanoidModel<T> {

    public EnchanterClothesModel(ModelPart root) {
        super(root);
    }


    public static MeshDefinition createMesh(CubeDeformation p_170682_, float p_170683_) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0), PartPose.offset(0.0F, 0.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0), PartPose.offset(0.0F, 0.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16), PartPose.offset(0.0F, 0.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16), PartPose.offset(-5.0F, 2.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror(), PartPose.offset(5.0F, 2.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16), PartPose.offset(-1.9F, 12.0F + p_170683_, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror(), PartPose.offset(1.9F, 12.0F + p_170683_, 0.0F));
        return meshdefinition;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();


        PartDefinition armorHead = partdefinition.getChild("head").addOrReplaceChild("armorHead", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                .texOffs(56, 32).addBox(-5.0F, -14.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorBody = partdefinition.getChild("body").addOrReplaceChild("armorBody", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.4F))
                .texOffs(68, 0).addBox(-4.0F, 10.75F, -3.0F, 8.0F, 9.0F, 6.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorRightArm = partdefinition.getChild("right_arm").addOrReplaceChild("armorRightArm", CubeListBuilder.create().texOffs(44, 16).mirror().addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorLeftArm = partdefinition.getChild("left_arm").addOrReplaceChild("armorLeftArm", CubeListBuilder.create().texOffs(44, 16).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorLeftLeg = partdefinition.getChild("left_leg").addOrReplaceChild("armorLeftLeg", CubeListBuilder.create().texOffs(60, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorLeftBoot = partdefinition.getChild("left_leg").addOrReplaceChild("armorLeftBoot", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorRightLeg = partdefinition.getChild("right_leg").addOrReplaceChild("armorRightLeg", CubeListBuilder.create().texOffs(60, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorRightBoot = partdefinition.getChild("right_leg").addOrReplaceChild("armorRightBoot", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));


        return LayerDefinition.create(meshdefinition, 96, 64);
    }
}