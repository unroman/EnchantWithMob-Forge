package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.message.SoulParticleMessage;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class SoulStealMobEnchant extends MobEnchant {
    public SoulStealMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }

    @SubscribeEvent
    public static void onLivingDeathAndStealEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (damageSource != null && damageSource.getDirectEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) damageSource.getDirectEntity();
            if (attacker instanceof IEnchantCap cap) {
                int enchantLevel = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SOUL_STEAL.get());
                if (cap.getEnchantCap().hasEnchant() && enchantLevel > 0 && !attacker.hasEffect(MobEffects.ABSORPTION)) {
                    if (attacker.getAbsorptionAmount() < 6) {
                        attacker.setAbsorptionAmount(Mth.clamp(attacker.getAbsorptionAmount() + enchantLevel, 0, 6));
                    }
                    if (!entity.level.isClientSide()) {
                        SoulParticleMessage message = new SoulParticleMessage(entity);
                        EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
                    }
                }
            }
            ;
        }
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }

    @Override
    public boolean isOnlyChest() {
        return true;
    }
}
