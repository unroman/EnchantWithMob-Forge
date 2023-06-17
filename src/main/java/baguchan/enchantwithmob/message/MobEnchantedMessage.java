package baguchan.enchantwithmob.message;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MobEnchantedMessage {
    private int entityId;
    private MobEnchant enchantType;
    private int level;

    public MobEnchantedMessage(Entity entity, MobEnchantHandler enchantType) {
        this.entityId = entity.getId();
        this.enchantType = enchantType.getMobEnchant();
        this.level = enchantType.getEnchantLevel();
    }

    public MobEnchantedMessage(int id, MobEnchantHandler enchantType) {
        this.entityId = id;
        this.enchantType = enchantType.getMobEnchant();
        this.level = enchantType.getEnchantLevel();
    }

    public MobEnchantedMessage(Entity entity, MobEnchant enchantType, int level) {
        this.entityId = entity.getId();
        this.enchantType = enchantType;
        this.level = level;
    }

    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeRegistryId(MobEnchants.getRegistry().get(), this.enchantType);
        buffer.writeInt(this.level);
    }

    public static MobEnchantedMessage deserialize(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        MobEnchant enchantType = buffer.readRegistryId();
        int level = buffer.readInt();

        return new MobEnchantedMessage(entityId, new MobEnchantHandler(enchantType, level));
    }

    public static boolean handle(MobEnchantedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
                if (entity != null && entity instanceof LivingEntity livingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        if (!MobEnchantUtils.findMobEnchantHandler(cap.getEnchantCap().getMobEnchants(), message.enchantType)) {
                            cap.getEnchantCap().addMobEnchant((LivingEntity) entity, message.enchantType, message.level);
                        }
                    }
                }
            });
        }

        return true;
    }
}