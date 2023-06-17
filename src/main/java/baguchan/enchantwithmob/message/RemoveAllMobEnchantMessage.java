package baguchan.enchantwithmob.message;

import baguchan.enchantwithmob.api.IEnchantCap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveAllMobEnchantMessage {
    private int entityId;

    public RemoveAllMobEnchantMessage(Entity entity) {
        this.entityId = entity.getId();
    }

    public RemoveAllMobEnchantMessage(int id) {
        this.entityId = id;
    }

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public static RemoveAllMobEnchantMessage deserialize(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();

        return new RemoveAllMobEnchantMessage(entityId);
    }

    public static boolean handle(RemoveAllMobEnchantMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
                if (entity != null && entity instanceof LivingEntity livingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        cap.getEnchantCap().removeAllMobEnchant((LivingEntity) entity);
                    }
                    ;
                }
            });
        }

        return true;
    }
}