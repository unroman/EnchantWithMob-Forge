package com.baguchan.enchantwithmob.capability;

import com.baguchan.enchantwithmob.EnchantWithMob;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemMobEnchantCapability<T extends ItemMobEnchantCapability> implements ICapabilityProvider, Capability.IStorage<T> {
	private boolean hasEnchant;

	public boolean hasEnchant() {
		return hasEnchant;
	}

	public void setHasEnchant(boolean hasEnchant) {
		this.hasEnchant = hasEnchant;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return capability == EnchantWithMob.ITEM_MOB_ENCHANT_CAP ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
	}

	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();

		nbt.putBoolean("HasEnchant", hasEnchant);


		return nbt;
	}

	public void deserializeNBT(CompoundNBT nbt) {
		hasEnchant = nbt.getBoolean("HasEnchant");
	}

	@Nullable
	@Override
	public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();

		nbt.putBoolean("HasEnchant", hasEnchant);


		return nbt;
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
		hasEnchant = ((CompoundNBT) nbt).getBoolean("HasEnchant");
	}
}