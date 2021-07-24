package com.baguchan.enchantwithmob.item;

import com.baguchan.enchantwithmob.EnchantConfig;
import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MobUnEnchantBookItem extends Item {
    public MobUnEnchantBookItem(Properties group) {
        super(group);
    }

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (EnchantConfig.COMMON.enchantYourSelf.get() && MobEnchantUtils.hasMobEnchant(stack)) {
			playerIn.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
			{
				MobEnchantUtils.removeMobEnchantToEntity(playerIn, cap);
			});
			playerIn.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

			stack.hurtAndBreak(1, playerIn, (entity) -> entity.broadcastBreakEvent(handIn));

			playerIn.getCooldowns().addCooldown(stack.getItem(), 80);

			return InteractionResultHolder.success(stack);
		}
		return super.use(level, playerIn, handIn);
	}

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }
}
