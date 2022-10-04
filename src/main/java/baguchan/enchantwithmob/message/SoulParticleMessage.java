package baguchan.enchantwithmob.message;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SoulParticleMessage {
	private int entityId;

	public SoulParticleMessage(Entity entity) {
		this.entityId = entity.getId();
	}

	public SoulParticleMessage(int id) {
		this.entityId = id;
	}

	public void serialize(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}

	public static SoulParticleMessage deserialize(FriendlyByteBuf buffer) {
		int entityId = buffer.readInt();

		return new SoulParticleMessage(entityId);
	}

	public static boolean handle(SoulParticleMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();

		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Entity entity = Minecraft.getInstance().level.getEntity(message.entityId);
				if (entity != null) {
					for (int i = 0; i < 4; i++) {
						entity.level.addParticle(ParticleTypes.SCULK_SOUL, entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), 0.0F, 0.1F, 0.0F);
					}
				}
			});
		}

		return true;
	}
}