package baguchan.enchantwithmob.capability;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EmptyMobEnchantCapability extends MobEnchantCapability {
    /**
     * add MobEnchant on Entity
     *
     * @param entity       Entity given a MobEnchant
     * @param mobEnchant   Mob Enchant attached to mob
     * @param enchantLevel Mob Enchant Level
     */
    public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel) {
    }

    public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, boolean ancient) {
    }

    public void setEnchantType(LivingEntity entity, EnchantType enchantType) {
    }

    /**
     * add MobEnchant on Entity From Owner
     *
     * @param entity       Entity given a MobEnchant
     * @param mobEnchant   Mob Enchant attached to mob
     * @param enchantLevel Mob Enchant Level
     * @param owner        OwnerEntity with a mob Enchant attached to that mob
     */
    public void addMobEnchantFromOwner(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, LivingEntity owner) {

    }

    public void addOwner(LivingEntity entity, @Nullable LivingEntity owner) {
    }

    public void removeOwner(LivingEntity entity) {
    }

    /*
     * Remove MobEnchant on Entity
     */
    public void removeAllMobEnchant(LivingEntity entity) {
    }

    /*
     * Remove MobEnchant on Entity from owner
     */
    public void removeMobEnchantFromOwner(LivingEntity entity) {
    }

    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    public void deserializeNBT(CompoundTag nbt) {
    }
}
