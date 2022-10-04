package baguchan.enchantwithmob;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
        Pair<Client, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue showEnchantedMobHud;
        public final ForgeConfigSpec.BooleanValue disablePoisonParticle;

        public Client(ForgeConfigSpec.Builder builder) {
            showEnchantedMobHud = builder
                    .translation(EnchantWithMob.MODID + ".config.showEnchantedMobHud")
                    .define("Show Enchanted Mob Hud", true);
            disablePoisonParticle = builder
                    .comment("Disable Poison Mob Enchant Particle. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.disablePoisonParticle")
                    .define("Disable Poison Particle", true);
        }
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue naturalSpawnEnchantedMob;
        public final ForgeConfigSpec.BooleanValue spawnEnchantedAnimal;
        public final ForgeConfigSpec.BooleanValue enchantYourSelf;
        public final ForgeConfigSpec.BooleanValue changeSizeWhenEnchant;
        public final ForgeConfigSpec.BooleanValue dungeonsLikeHealth;
        public final ForgeConfigSpec.BooleanValue bigYourSelf;


        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANT_ON_SPAWN_EXCLUSION_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAY_ENCHANTABLE_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLE_ENCHANTS;

        public Common(ForgeConfigSpec.Builder builder) {
            naturalSpawnEnchantedMob = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.naturalSpawnEnchantedMob")
                    .define("Enchanted Mob can Spawn Natural", true);
            ENCHANT_ON_SPAWN_EXCLUSION_MOBS = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: minecraft:ender_dragon.")
                    .define("enchantOnSpawnExclusionMobs", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"));
            ALWAY_ENCHANTABLE_MOBS = builder
                    .comment("Allow the specific mob from alway receiveing enchantments on spawn. Use the full name, eg: minecraft:zombie.")
                    .define("alwayEnchantableMobs", Lists.newArrayList());
            DISABLE_ENCHANTS = builder
                    .comment("Disables the specific mob enchant. Use the full name(This config only disabled mob enchant when mob spawn. not mean delete complete, eg: enchantwithmob:thorn.")
                    .define("disableMobEnchants", Lists.newArrayList());

            spawnEnchantedAnimal = builder
                    .comment("Enable the the spawning of enchanted animal mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.spawnEnchantedAnimal")
                    .define("Enchanted Animal can Spawn Natural", false);
            enchantYourSelf = builder
                    .comment("Enable enchanting yourself. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.enchantYourSelf")
                    .define("Enchant yourself", true);
            changeSizeWhenEnchant = builder
                    .comment("Enable Change Size When Enchanted. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.changeSizeWhenEnchant")
                    .define("Change Size", true);
            dungeonsLikeHealth = builder
                    .comment("Enable Increase Health like Dungeons When Enchanted. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.dungeonsLikeHealth")
                    .define("Increase Health like Dungeons", false);
            bigYourSelf = builder
                    .comment("Enable Player More Bigger When You have Huge Enchant. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.bigYourSelf")
                    .define("Big Your Self", false);
        }
    }

}
