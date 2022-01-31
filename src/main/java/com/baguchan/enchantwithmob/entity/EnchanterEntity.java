package com.baguchan.enchantwithmob.entity;

import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.registry.ModItems;
import com.baguchan.enchantwithmob.registry.ModSoundEvents;
import com.baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class EnchanterEntity extends SpellcasterIllager {
    private LivingEntity enchantTarget;

    public float prevCapeX, prevCapeY, prevCapeZ;
    public float capeX, capeY, capeZ;
    private float clientSideBookAnimation0;
    private float clientSideBookAnimation;

    public EnchanterEntity(EntityType<? extends EnchanterEntity> type, Level p_i48551_2_) {
        super(type, p_i48551_2_);
        this.xpReward = 12;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EnchanterEntity.CastingSpellGoal());
        this.goalSelector.addGoal(1, new AttackGoal(this));
        this.goalSelector.addGoal(3, new AvoidTargetEntityGoal<>(this, Mob.class, 6.5F, 0.8D, 1.05D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.8D, 1.15D));
        this.goalSelector.addGoal(4, new EnchanterEntity.SpellGoal());
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public static AttributeSupplier.Builder createAttributeMap() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 24.0D).add(Attributes.FOLLOW_RANGE, 24.0D).add(Attributes.ATTACK_DAMAGE, 2.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide) {
            this.clientSideBookAnimation0 = this.clientSideBookAnimation;
            if (this.isCastingSpell()) {
                this.clientSideBookAnimation = Mth.clamp(this.clientSideBookAnimation + 0.1F, 0.0F, 1.0F);
            } else {
                this.clientSideBookAnimation = Mth.clamp(this.clientSideBookAnimation - 0.15F, 0.0F, 1.0F);
            }
        }

        this.updateCape();
    }

    @OnlyIn(Dist.CLIENT)
    public float getBookAnimationScale(float tick) {
        return Mth.lerp(tick, this.clientSideBookAnimation0, this.clientSideBookAnimation) / 1.0F;
    }

    private void updateCape() {
        double elasticity = 0.25;
        double gravity = -0.1;
        this.prevCapeX = this.capeX;
        this.prevCapeY = this.capeY;
        this.prevCapeZ = this.capeZ;
        this.capeY += gravity;
        this.capeX += (this.getX() - this.capeX) * elasticity;
        this.capeY += (this.getY() - this.capeY) * elasticity;
        this.capeZ += (this.getZ() - this.capeZ) * elasticity;
    }

    public boolean isAlliedTo(Entity p_184191_1_) {
        if (super.isAlliedTo(p_184191_1_)) {
            return true;
        } else if (p_184191_1_ instanceof LivingEntity && ((LivingEntity) p_184191_1_).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && p_184191_1_.getTeam() == null;
        } else {
            return false;
        }
    }

    private void setEnchantTarget(@Nullable LivingEntity enchantTargetIn) {
        this.enchantTarget = enchantTargetIn;
    }

    @Nullable
    public LivingEntity getEnchantTarget() {
        return enchantTarget;
    }


    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);

        //when raid is active, reward is more bigger
        if (this.random.nextFloat() < 0.25F + 0.025F * looting) {
            if (this.raid != null && this.hasActiveRaid() && this.getWave() > 0) {
                ItemStack itemStack = new ItemStack(ModItems.ENCHANTERS_BOOK);

                if (this.random.nextFloat() < 0.5F) {
                    itemStack = new ItemStack(ModItems.MOB_ENCHANT_BOOK);
                }

                this.spawnAtLocation(MobEnchantUtils.addRandomEnchantmentToItemStack(random, itemStack, 20 + this.getWave() * 4, true));
            } else {
                ItemStack itemStack = new ItemStack(ModItems.ENCHANTERS_BOOK);

                if (this.random.nextFloat() < 0.5F) {
                    itemStack = new ItemStack(ModItems.MOB_ENCHANT_BOOK);
                }

                this.spawnAtLocation(MobEnchantUtils.addRandomEnchantmentToItemStack(random, itemStack, 20, true));
            }
        }
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity p_70652_1_) {
        this.playSound(ModSoundEvents.ENCHANTER_ATTACK, this.getSoundVolume(), this.getVoicePitch());

        return super.doHurtTarget(p_70652_1_);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.ENCHANTER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ENCHANTER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.ENCHANTER_HURT;
    }


    protected SoundEvent getCastingSoundEvent() {
        return ModSoundEvents.ENCHANTER_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.ENCHANTER_AMBIENT;
    }

    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return AbstractIllager.IllagerArmPose.SPELLCASTING;
        } else {
            return AbstractIllager.IllagerArmPose.CROSSED;
        }
    }

    class CastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {
        private CastingSpellGoal() {
            super();
        }

        @Override
        public void tick() {
            if (EnchanterEntity.this.isCastingSpell() && EnchanterEntity.this.getEnchantTarget() != null) {
                EnchanterEntity.this.getLookControl().setLookAt(EnchanterEntity.this.getEnchantTarget(), (float) EnchanterEntity.this.getMaxHeadYRot(), (float) EnchanterEntity.this.getMaxHeadXRot());
            } else if (EnchanterEntity.this.isCastingSpell() && EnchanterEntity.this.getTarget() != null) {
                EnchanterEntity.this.getLookControl().setLookAt(EnchanterEntity.this.getTarget(), (float) EnchanterEntity.this.getMaxHeadYRot(), (float) EnchanterEntity.this.getMaxHeadXRot());
            }
        }
    }


    public class SpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        private final Predicate<LivingEntity> fillter = (entity) -> {
            return !(entity instanceof EnchanterEntity) && entity instanceof Raider && entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).map(mob -> !mob.hasEnchant()).orElse(false);
        };

        private final Predicate<LivingEntity> enchanted_fillter = (entity) -> {
            return !(entity instanceof EnchanterEntity) && entity instanceof Raider && entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).map(mob -> mob.hasEnchant()).orElse(false);
        };

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (EnchanterEntity.this.getTarget() == null) {
                return false;
            } else if (EnchanterEntity.this.isCastingSpell()) {
                return false;
            } else if (EnchanterEntity.this.tickCount < this.nextAttackTickCount) {
                return false;
            } else {
                List<LivingEntity> list = EnchanterEntity.this.level.getEntitiesOfClass(LivingEntity.class, EnchanterEntity.this.getBoundingBox().expandTowards(16.0D, 8.0D, 16.0D), this.fillter);
                if (list.isEmpty()) {
                    return false;
                } else {
                    List<LivingEntity> enchanted_list = EnchanterEntity.this.level.getEntitiesOfClass(LivingEntity.class, EnchanterEntity.this.getBoundingBox().expandTowards(16.0D, 8.0D, 16.0D), this.enchanted_fillter);

                    //set enchant limit
                    if (enchanted_list.size() < 5) {
                        LivingEntity target = list.get(EnchanterEntity.this.random.nextInt(list.size()));
                        if (target != EnchanterEntity.this.getTarget()) {
                            EnchanterEntity.this.setEnchantTarget(list.get(EnchanterEntity.this.random.nextInt(list.size())));
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return EnchanterEntity.this.getEnchantTarget() != null && EnchanterEntity.this.getEnchantTarget() != EnchanterEntity.this.getTarget() && this.attackWarmupDelay > 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
            EnchanterEntity.this.setEnchantTarget(null);
        }

        protected void performSpellCasting() {
            LivingEntity entity = EnchanterEntity.this.getEnchantTarget();
            if (entity != null && entity.isAlive()) {
                entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
                {
                    MobEnchantUtils.addUnstableRandomEnchantmentToEntity(entity, EnchanterEntity.this, cap, entity.getRandom(), 12, false);
                });
            }
        }

        protected int getCastWarmupTime() {
            return 40;
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 200;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BOOK_PAGE_TURN;
        }

        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.WOLOLO;
        }
    }

    class AvoidTargetEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final EnchanterEntity enchanter;

        public AvoidTargetEntityGoal(EnchanterEntity enchanterIn, Class<T> entityClassToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
            super(enchanterIn, entityClassToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
            this.enchanter = enchanterIn;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (super.canUse() && this.toAvoid == this.enchanter.getTarget()) {
                return this.enchanter.getTarget() != null;
            } else {
                return false;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            EnchanterEntity.this.setTarget((LivingEntity) null);
            super.start();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            EnchanterEntity.this.setTarget((LivingEntity) null);
            super.tick();
        }
    }

    static class AttackGoal extends Goal {
        private final EnchanterEntity enchanter;
        private int cooldown;

        AttackGoal(EnchanterEntity enchanter) {
            this.enchanter = enchanter;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.enchanter.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                return this.getAttackReachSqr(livingentity) >= this.enchanter.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.cooldown = 5;
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity livingentity = this.enchanter.getTarget();

            if (livingentity != null && livingentity.isAlive()) {
                this.enchanter.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                if (this.getAttackReachSqr(livingentity) >= this.enchanter.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ())) {
                    if (this.cooldown <= 0) {
                        this.enchanter.swing(InteractionHand.MAIN_HAND);
                        this.enchanter.doHurtTarget(livingentity);

                        this.cooldown = 30;
                    }
                    this.enchanter.getNavigation().stop();
                }
            }
            this.cooldown = Math.max(this.cooldown - 1, 0);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double) (this.enchanter.getBbWidth() * 1.5F * this.enchanter.getBbWidth() * 1.5F + attackTarget.getBbWidth());
        }
    }


}
