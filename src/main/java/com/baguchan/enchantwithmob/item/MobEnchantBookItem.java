package com.baguchan.enchantwithmob.item;

import com.baguchan.enchantwithmob.EnchantConfig;
import com.baguchan.enchantwithmob.EnchantWithMob;
import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.baguchan.enchantwithmob.registry.MobEnchants;
import com.baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class MobEnchantBookItem extends Item {
	public MobEnchantBookItem(Item.Properties group) {
		super(group);
	}


    /*
     * Implemented onRightClick (method) inside CommonEventHandler instead of this method
     */
    /*@Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (MobEnchantUtils.hasMobEnchant(stack)) {
            target.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
            {
                MobEnchantUtils.addMobEnchantToEntityFromItem(stack, target, cap);
            });
            playerIn.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

            stack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(hand));

            return ActionResultType.SUCCESS;
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }*/

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (EnchantConfig.COMMON.enchantYourSelf.get() && MobEnchantUtils.hasMobEnchant(stack)) {
			final boolean[] flag = {false};
			playerIn.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
			{
				flag[0] = MobEnchantUtils.addItemMobEnchantToEntity(stack, playerIn, cap);
			});

			//When flag is true, enchanting is success.
			if (flag[0]) {
				playerIn.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

				stack.hurtAndBreak(1, playerIn, (entity) -> entity.broadcastBreakEvent(handIn));

				playerIn.getCooldowns().addCooldown(stack.getItem(), 40);

				return InteractionResultHolder.success(stack);
			} else {
				playerIn.displayClientMessage(new TranslatableComponent("enchantwithmob.cannot.enchant_yourself"), true);

				playerIn.getCooldowns().addCooldown(stack.getItem(), 20);

				return InteractionResultHolder.fail(stack);
			}
        }
		return super.use(level, playerIn, handIn);
    }

	@Override
	public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
		if (this.allowdedIn(p_41391_)) {
			for (MobEnchant enchant : MobEnchants.getRegistry()) {
				ItemStack stack = new ItemStack(this);
				MobEnchantUtils.addMobEnchantToItemStack(stack, enchant, enchant.getMaxLevel());
				p_41392_.add(stack);
			}
		}
	}

	public static ListTag getEnchantmentList(ItemStack stack) {
		CompoundTag compoundnbt = stack.getTag();
		return compoundnbt != null ? compoundnbt.getList(MobEnchantUtils.TAG_STORED_MOBENCHANTS, 10) : new ListTag();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag p_41424_) {
		super.appendHoverText(stack, level, tooltip, p_41424_);
		ChatFormatting[] textformatting2 = new ChatFormatting[]{ChatFormatting.DARK_PURPLE};

		tooltip.add(new TranslatableComponent("mobenchant.enchantwithmob.mob_enchant_book.tooltip").withStyle(textformatting2));
		if (MobEnchantUtils.hasMobEnchant(stack)) {
			ListTag listnbt = MobEnchantUtils.getEnchantmentListForNBT(stack.getTag());

			for (int i = 0; i < listnbt.size(); ++i) {
				CompoundTag compoundnbt = listnbt.getCompound(i);

				MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
				int enchantmentLevel = MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt);

				if (mobEnchant != null) {
					ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

					tooltip.add(new TranslatableComponent("mobenchant." + mobEnchant.getRegistryName().getNamespace() + "." + mobEnchant.getRegistryName().getPath()).withStyle(textformatting).append(" ").append(new TranslatableComponent("enchantment.level." + enchantmentLevel).withStyle(textformatting)));
				}
            }

            List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();

            for (int i = 0; i < listnbt.size(); ++i) {
				CompoundTag compoundnbt = listnbt.getCompound(i);

                MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
                int mobEnchantLevel = MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt);

                if (mobEnchant != null) {
                    Map<Attribute, AttributeModifier> map = mobEnchant.getAttributeModifierMap();
                    if (!map.isEmpty()) {
                        for (Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                            AttributeModifier attributemodifier = entry.getValue();
                            AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), mobEnchant.getAttributeModifierAmount(mobEnchantLevel, attributemodifier), attributemodifier.getOperation());
                            list1.add(new Pair<>(entry.getKey(), attributemodifier1));
                        }
                    }
                }
            }


            if (!list1.isEmpty()) {
				//tooltip.add(StringTextComponent.EMPTY);
				tooltip.add((new TranslatableComponent("mobenchant.enchantwithmob.when_ehcnanted")).withStyle(ChatFormatting.DARK_PURPLE));

				for (Pair<Attribute, AttributeModifier> pair : list1) {
					AttributeModifier attributemodifier2 = pair.getSecond();
					double d0 = attributemodifier2.getAmount();
					double d1;
					if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
						d1 = attributemodifier2.getAmount();
					} else {
						d1 = attributemodifier2.getAmount() * 100.0D;
					}

                    if (d0 > 0.0D) {
						tooltip.add((new TranslatableComponent("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
                    } else if (d0 < 0.0D) {
						d1 = d1 * -1.0D;
						tooltip.add((new TranslatableComponent("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }
}
