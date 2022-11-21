package baguchan.enchantwithmob.message;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AncientMobMessage {
	private int entityId;
	private String enchantType;

	public AncientMobMessage(Entity entity, String enchantType) {
		this.entityId = entity.getId();
		this.enchantType = enchantType;
	}

	public AncientMobMessage(int id, String enchantType) {
		this.entityId = id;
		this.enchantType = enchantType;
	}

	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeUtf(this.enchantType);
	}

	public static AncientMobMessage deserialize(FriendlyByteBuf buffer) {
		int entityId = buffer.readInt();
		String enchantType = buffer.readUtf();

		return new AncientMobMessage(entityId, enchantType);
	}

	public static boolean handle(AncientMobMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Entity entity = Minecraft.getInstance().level.getEntity(message.entityId);
				if (entity != null && entity instanceof LivingEntity) {
					entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP, null).ifPresent(enchantCap ->
					{
						enchantCap.setEnchantType((LivingEntity) entity, MobEnchantCapability.EnchantType.get(message.enchantType));
					});
				}
			});
		}

		return true;
	}
}