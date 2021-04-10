package com.baguchan.enchantwithmob.message;

import com.baguchan.enchantwithmob.EnchantWithMob;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveMobEnchantOwnerMessage {
	private int entityId;

	public RemoveMobEnchantOwnerMessage(Entity entity) {
		this.entityId = entity.getId();
	}

	public RemoveMobEnchantOwnerMessage(int id) {
		this.entityId = id;
	}


	public void serialize(PacketBuffer buffer) {
		buffer.writeInt(this.entityId);
	}

	public static RemoveMobEnchantOwnerMessage deserialize(PacketBuffer buffer) {
		int entityId = buffer.readInt();

		return new RemoveMobEnchantOwnerMessage(entityId);
	}

	public static boolean handle(RemoveMobEnchantOwnerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
				if (entity != null && entity instanceof LivingEntity) {
					entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP, null).ifPresent(enchantCap ->
					{
						enchantCap.removeOwner((LivingEntity) entity);
					});
				}
			});
		}

		return true;
	}
}