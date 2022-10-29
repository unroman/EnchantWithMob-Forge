package baguchan.enchantwithmob.mixin;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IReward;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements IReward {
	private boolean patreonReward;


	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"), cancellable = true)
	public void addAdditionalSaveData(CompoundTag p_213281_1_, CallbackInfo callbackInfo) {
		if (hasReward()) {
			p_213281_1_.putBoolean("EnchantPatreonReward", hasVisibleReward());
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"), cancellable = true)
	public void readAdditionalSaveData(CompoundTag p_70037_1_, CallbackInfo callbackInfo) {
		if (p_70037_1_.contains("EnchantPatreonReward")) {
			this.setVisibleReward(p_70037_1_.getBoolean("EnchantPatreonReward"));
		}
	}

	@Override
	public boolean hasReward() {
		return EnchantWithMob.PATREONS.contains(getName().getString());
	}

	@Override
	public void setVisibleReward(boolean reward) {
		patreonReward = reward;
	}

	@Override
	public boolean hasVisibleReward() {
		return patreonReward;
	}

	@Shadow
	public Component getName() {
		return null;
	}
}
