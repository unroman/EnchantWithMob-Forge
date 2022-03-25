package com.baguchan.enchantwithmob;

import com.baguchan.enchantwithmob.capability.ItemMobEnchantCapability;
import com.baguchan.enchantwithmob.capability.MobEnchantCapability;
import com.baguchan.enchantwithmob.message.MobEnchantFromOwnerMessage;
import com.baguchan.enchantwithmob.message.MobEnchantedMessage;
import com.baguchan.enchantwithmob.message.RemoveAllMobEnchantMessage;
import com.baguchan.enchantwithmob.message.RemoveMobEnchantOwnerMessage;
import com.baguchan.enchantwithmob.registry.MobEnchants;
import com.baguchan.enchantwithmob.registry.ModEntities;
import com.baguchan.enchantwithmob.registry.ModItems;
import com.baguchan.enchantwithmob.registry.ModLootItemFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EnchantWithMob.MODID)
public class EnchantWithMob {

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "enchantwithmob";
	public static final String NETWORK_PROTOCOL = "2";

	public static Capability<MobEnchantCapability> MOB_ENCHANT_CAP = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static Capability<ItemMobEnchantCapability> ITEM_MOB_ENCHANT_CAP = CapabilityManager.get(new CapabilityToken<>() {
	});


	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "net"))
			.networkProtocolVersion(() -> NETWORK_PROTOCOL)
			.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
			.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
			.simpleChannel();

	public EnchantWithMob() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		this.setupMessages();
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MobEnchants.MOB_ENCHANT.register(bus);
		ModEntities.ENTITIES_REGISTRY.register(bus);
		ModItems.ITEM_REGISTRY.register(bus);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnchantConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EnchantConfig.CLIENT_SPEC);
	}

    private void setup(final FMLCommonSetupEvent event) {
		ModLootItemFunctions.init();
		Raid.RaiderType.create("enchanter", ModEntities.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});
		SpawnPlacements.register(ModEntities.ENCHANTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
	}

    private void setupMessages() {
		CHANNEL.messageBuilder(MobEnchantedMessage.class, 0)
				.encoder(MobEnchantedMessage::serialize).decoder(MobEnchantedMessage::deserialize)
				.consumer(MobEnchantedMessage::handle)
				.add();
		CHANNEL.messageBuilder(RemoveAllMobEnchantMessage.class, 1)
				.encoder(RemoveAllMobEnchantMessage::serialize).decoder(RemoveAllMobEnchantMessage::deserialize)
				.consumer(RemoveAllMobEnchantMessage::handle)
				.add();
		CHANNEL.messageBuilder(MobEnchantFromOwnerMessage.class, 2)
				.encoder(MobEnchantFromOwnerMessage::serialize).decoder(MobEnchantFromOwnerMessage::deserialize)
				.consumer(MobEnchantFromOwnerMessage::handle)
				.add();
		CHANNEL.messageBuilder(RemoveMobEnchantOwnerMessage.class, 3)
				.encoder(RemoveMobEnchantOwnerMessage::serialize).decoder(RemoveMobEnchantOwnerMessage::deserialize)
				.consumer(RemoveMobEnchantOwnerMessage::handle)
				.add();
	}

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event)
    {
    }
}
