package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class SmallMobEnchant extends MobEnchant {
    public SmallMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

            if (attacker instanceof IEnchantCap cap) {
                if (cap.getEnchantCap().hasEnchant()) {
                    if (event.getAmount() > 0) {
                        event.setAmount(getDamageIncrease(event.getAmount(), cap.getEnchantCap()));
                    }
                }
            }
            ;
        }
    }

    public static float getDamageIncrease(float damage, MobEnchantCapability cap) {
        int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.SMALL.get());
        if (level > 0) {
            damage /= 1.0F + level * 0.15F;
        }
        return damage;
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }

    public boolean isCursedEnchant() {
        return true;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return !(livingEntity instanceof Player) || EnchantConfig.COMMON.bigYourSelf.get();
    }

    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != MobEnchants.HUGE.get();
    }
}