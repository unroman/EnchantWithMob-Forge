package com.baguchan.enchantwithmob.message;

import com.baguchan.enchantwithmob.EnchantWithMob;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MobEnchantFromOwnerMessage {
	private int entityId;
	private int ownerID;

	public MobEnchantFromOwnerMessage(Entity entity, Entity ownerEntity) {
		this.entityId = entity.getId();
		this.ownerID = ownerEntity.getId();
	}

	public MobEnchantFromOwnerMessage(int id, int ownerID) {
		this.entityId = id;
		this.ownerID = ownerID;
	}


	public void serialize(PacketBuffer buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeInt(this.ownerID);
	}

	public static MobEnchantFromOwnerMessage deserialize(PacketBuffer buffer) {
		int entityId = buffer.readInt();
		int ownerId = buffer.readInt();

		return new MobEnchantFromOwnerMessage(entityId, ownerId);
	}

	public static boolean handle(MobEnchantFromOwnerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
				Entity ownerEntity = Minecraft.getInstance().player.level.getEntity(message.ownerID);
				if (entity != null && entity instanceof LivingEntity && ownerEntity != null && ownerEntity instanceof LivingEntity) {
					entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP, null).ifPresent(enchantCap ->
					{
						enchantCap.addOwner((LivingEntity) entity, (LivingEntity) ownerEntity);
					});
				}
			});
		}

		return true;
	}
}