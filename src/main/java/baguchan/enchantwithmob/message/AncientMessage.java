package baguchan.enchantwithmob.message;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AncientMessage {
    private int entityId;
    private boolean isAncient;

    public AncientMessage(Entity entity, boolean ancient) {
        this.entityId = entity.getId();
        this.isAncient = ancient;
    }

    public AncientMessage(int id, boolean ancient) {
        this.entityId = id;
        this.isAncient = ancient;
    }

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.isAncient);
    }

    public static AncientMessage deserialize(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();

        return new AncientMessage(entityId, buffer.readBoolean());
    }

    public static boolean handle(AncientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
                if (entity != null && entity instanceof LivingEntity livingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        cap.getEnchantCap().setEnchantType((LivingEntity) entity, message.isAncient ? MobEnchantCapability.EnchantType.ANCIENT : MobEnchantCapability.EnchantType.NORMAL);
                    }
                    ;
                }
            });
        }

        return true;
    }
}