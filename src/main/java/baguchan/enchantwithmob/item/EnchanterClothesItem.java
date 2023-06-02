package baguchan.enchantwithmob.item;

import baguchan.enchantwithmob.client.ModModelLayers;
import baguchan.enchantwithmob.client.model.EnchanterClothesModel;
import baguchan.enchantwithmob.registry.ModArmorMaterials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EnchanterClothesItem extends ArmorItem {
    public EnchanterClothesItem(ArmorItem.Type type, Properties p_40388_) {
        super(ModArmorMaterials.ENCHANTER_CLOTHES, type, p_40388_);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(ArmorRender.INSTANCE);
    }

    private static final class ArmorRender implements IClientItemExtensions {
        private static final ArmorRender INSTANCE = new ArmorRender();


        @Override
        public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            EntityModelSet models = Minecraft.getInstance().getEntityModels();
            ModelPart root = models.bakeLayer(ModModelLayers.ENCHANTER_CLOTHES);

            EnchanterClothesModel<?> model2 = new EnchanterClothesModel<>(root);
            ForgeHooksClient.copyModelProperties(original, model2);
            return model2;
        }
/*
        protected void setPartVisibility(HumanoidModel p_117126_, EquipmentSlot p_117127_) {
            p_117126_.setAllVisible(false);
            switch (p_117127_) {
                case HEAD:
                    p_117126_.head.visible = true;
                    p_117126_.hat.visible = true;
                    break;
                case CHEST:
                    p_117126_.body.visible = true;
                    p_117126_.rightArm.visible = true;
                    p_117126_.leftArm.visible = true;
                    break;
                case LEGS:
                    p_117126_.body.visible = true;
                    p_117126_.rightLeg.visible = true;
                    p_117126_.leftLeg.visible = true;
                    break;
                case FEET:
                    p_117126_.rightLeg.visible = true;
                    p_117126_.leftLeg.visible = true;
            }

        }*/

    }
}