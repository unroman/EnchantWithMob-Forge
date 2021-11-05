package com.baguchan.enchantwithmob;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static boolean naturalSpawnEnchantedMob;
    public static boolean spawnEnchantedAnimal;
    public static boolean enchantYourSelf;

    public static boolean showEnchantedMobHud;

    public static List<? extends String> ENCHANT_ON_SPAWN_EXCLUSION_MOBS;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
        Pair<Client, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }

    public static void bakeConfig() {
        naturalSpawnEnchantedMob = COMMON.naturalSpawnEnchantedMob.get();
        spawnEnchantedAnimal = COMMON.spawnEnchantedAnimal.get();
        enchantYourSelf = COMMON.enchantYourSelf.get();
        ENCHANT_ON_SPAWN_EXCLUSION_MOBS = COMMON.ENCHANT_ON_SPAWN_EXCLUSION_MOBS.get();
    }

    public static void bakeConfigClient() {
        showEnchantedMobHud = CLIENT.showEnchantedMobHud.get();
    }


    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent.Loading configEvent) {
        if (configEvent.getConfig().getSpec() == EnchantConfig.COMMON_SPEC) {
            bakeConfig();
        }

        if (configEvent.getConfig().getSpec() == EnchantConfig.CLIENT_SPEC) {
            bakeConfigClient();
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue showEnchantedMobHud;

        public Client(ForgeConfigSpec.Builder builder) {
            showEnchantedMobHud = builder
                    .translation(EnchantWithMob.MODID + ".config.showEnchantedMobHud")
                    .define("Show Enchanted Mob Hud", true);
        }
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue naturalSpawnEnchantedMob;
        public final ForgeConfigSpec.BooleanValue spawnEnchantedAnimal;
        public final ForgeConfigSpec.BooleanValue enchantYourSelf;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANT_ON_SPAWN_EXCLUSION_MOBS;

        public Common(ForgeConfigSpec.Builder builder) {
            naturalSpawnEnchantedMob = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.naturalSpawnEnchantedMob")
                    .define("Enchanted Mob can Spawn Natural", true);
            ENCHANT_ON_SPAWN_EXCLUSION_MOBS = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: dungeons_mobs:protection.")
                    .define("enchantOnSpawnExclusionMobs", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"));
            spawnEnchantedAnimal = builder
                    .comment("Enable the the spawning of enchanted animal mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.spawnEnchantedAnimal")
                    .define("Enchanted Animal can Spawn Natural", false);
            enchantYourSelf = builder
                    .comment("Enable enchanting yourself. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.enchantYourSelf")
                    .define("Enchant yourself", true);
        }
    }

}
