package net.satisfy.vinery.neoforge.core.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.satisfy.vinery.core.util.VineryTrades;
import net.satisfy.vinery.core.wine.WineEffects;

import java.util.List;

public class VineryNeoForgeConfig {
    public static final ModConfigSpec COMMON_CONFIG;

    public static final ModConfigSpec.IntValue TOTAL_FERMENTATION_TIME;
    public static final ModConfigSpec.IntValue MAX_FLUID_LEVEL;
    public static final ModConfigSpec.IntValue MAX_FLUID_INCREASE;
    public static final ModConfigSpec.IntValue APPLE_PRESS_MASHING_TIME;
    public static final ModConfigSpec.IntValue APPLE_PRESS_FERMENTATION_TIME;
    public static final ModConfigSpec.IntValue CHERRY_GROWTH_CHANCE;
    public static final ModConfigSpec.IntValue APPLE_GROWTH_CHANCE;
    public static final ModConfigSpec.IntValue GRAPE_GROWTH_CHANCE;
    public static final ModConfigSpec.DoubleValue GRAPE_GROWTH_MULTIPLIER;
    public static final ModConfigSpec.BooleanValue ENABLE_GRAPEVINE_LEAVES;
    public static final ModConfigSpec.IntValue GRAPEVINE_POT_STOMPS;
    public static final ModConfigSpec.IntValue GRAPEVINE_POT_HEAVY_ARMOR_BONUS;
    public static final ModConfigSpec.BooleanValue ENABLE_GRAPEVINE_POT_SPLASH;
    public static final ModConfigSpec.BooleanValue ENABLE_WINE_AGING;
    public static final ModConfigSpec.BooleanValue AGE_ONLY_IN_STORAGE;
    public static final ModConfigSpec.DoubleValue CELLAR_AGING_MULTIPLIER;
    public static final ModConfigSpec.IntValue DAYS_PER_YEAR;
    public static final ModConfigSpec.IntValue YEARS_PER_EFFECT_LEVEL;
    public static final ModConfigSpec.IntValue MAX_LEVEL;
    public static final ModConfigSpec.IntValue DURATION_PER_YEAR;
    public static final ModConfigSpec.IntValue MAX_DURATION;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WINE_EFFECTS;
    public static final ModConfigSpec.IntValue TRADER_SPAWN_CHANCE;
    public static final ModConfigSpec.BooleanValue SPAWN_WITH_MULES;
    public static final ModConfigSpec.IntValue TRADER_SPAWN_DELAY;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL1_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL2_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL3_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL4_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL5_TRADES;
    public static final ModConfigSpec.BooleanValue NEED_STRAW_HAT;
    public static final ModConfigSpec.BooleanValue SHOW_GRAPEVINE_POT_INFO;
    public static final ModConfigSpec.BooleanValue SHOW_STORAGE_INFO;
    public static final ModConfigSpec.BooleanValue BANNER_GIVE_EFFECT;
    public static final ModConfigSpec.BooleanValue BANNER_SHOW_TOOLTIP;

    public static int totalFermentationTime = 6000;
    public static int maxFluidLevel = 100;
    public static int maxFluidIncrease = 25;
    public static int applePressMashingTime = 600;
    public static int applePressFermentationTime = 800;
    public static int cherryGrowthChance = 40;
    public static int appleGrowthChance = 40;
    public static int grapeGrowthChance = 50;
    public static double grapeGrowthMultiplier = 1.0;
    public static boolean enableGrapevineLeaves = true;
    public static int grapevinePotStomps = 12;
    public static int grapevinePotHeavyArmorBonus = 10;
    public static boolean enableGrapevinePotSplash = true;
    public static boolean enableWineAging = true;
    public static boolean ageOnlyInStorage = false;
    public static double cellarAgingMultiplier = 1.5;
    public static int daysPerYear = 24;
    public static int yearsPerEffectLevel = 6;
    public static int maxLevel = 5;
    public static int durationPerYear = 200;
    public static int maxDuration = 15000;
    public static List<? extends String> wineEffects = WineEffects.DEFAULTS;
    public static int traderSpawnChance = 50;
    public static boolean spawnWithMules = true;
    public static int traderSpawnDelay = 48000;
    public static List<? extends String> level1Trades = VineryTrades.LEVEL_1;
    public static List<? extends String> level2Trades = VineryTrades.LEVEL_2;
    public static List<? extends String> level3Trades = VineryTrades.LEVEL_3;
    public static List<? extends String> level4Trades = VineryTrades.LEVEL_4;
    public static List<? extends String> level5Trades = VineryTrades.LEVEL_5;
    public static boolean needStrawHat = false;
    public static boolean showGrapevinePotInfo = true;
    public static boolean showStorageInfo = true;
    public static boolean bannerGiveEffect = true;
    public static boolean bannerShowTooltip = true;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Fermentation Barrel, Apple Press, fruit growth and the Grapevine Pot.").push("Blocks");
        TOTAL_FERMENTATION_TIME = builder.comment("How long the Fermentation Barrel needs for one wine, in ticks. 20 ticks = 1 second.").defineInRange("totalFermentationTime", 6000, 1, 72000);
        MAX_FLUID_LEVEL = builder.comment("How much juice fits into the Fermentation Barrel.").defineInRange("maxFluidLevel", 100, 10, 1000);
        MAX_FLUID_INCREASE = builder.comment("How much juice one bottle of grape juice adds to the Fermentation Barrel.").defineInRange("maxFluidIncrease", 25, 1, 1000);
        APPLE_PRESS_MASHING_TIME = builder.comment("How long the Apple Press needs to mash apples, in ticks. 20 ticks = 1 second.").defineInRange("applePressMashingTime", 600, 1, 72000);
        APPLE_PRESS_FERMENTATION_TIME = builder.comment("How long the Apple Press needs to ferment the mash, in ticks. 20 ticks = 1 second.").defineInRange("applePressFermentationTime", 800, 1, 72000);
        CHERRY_GROWTH_CHANCE = builder.comment("Chance in percent per random tick that cherries grow on cherry leaves.").defineInRange("cherryGrowthChance", 40, 0, 100);
        APPLE_GROWTH_CHANCE = builder.comment("Chance in percent per random tick that apples grow on apple leaves.").defineInRange("appleGrowthChance", 40, 0, 100);
        GRAPE_GROWTH_CHANCE = builder.comment("Chance in percent per random tick that grapes grow.").defineInRange("grapeGrowthChance", 50, 0, 100);
        GRAPE_GROWTH_MULTIPLIER = builder.comment("Multiplies the growth chance of grape bushes, grapevine stems and lattices. 1.0 = normal, 0 = grapes never grow.").defineInRange("grapeGrowthMultiplier", 1.0, 0.0, 10.0);
        ENABLE_GRAPEVINE_LEAVES = builder.comment("Grapevine stems grow a canopy of leaves around them after some time.").define("enableGrapevineLeaves", true);
        GRAPEVINE_POT_STOMPS = builder.comment("How many jumps it takes to stomp a full Grapevine Pot.").defineInRange("grapevinePotStomps", 12, 1, 100);
        GRAPEVINE_POT_HEAVY_ARMOR_BONUS = builder.comment("How much faster you stomp in heavy armor (iron or better), in percent.").defineInRange("grapevinePotHeavyArmorBonus", 10, 0, 100);
        ENABLE_GRAPEVINE_POT_SPLASH = builder.comment("Juice splashes out of the Grapevine Pot while stomping.").define("enableGrapevinePotSplash", true);
        builder.pop();

        builder.comment("How wine ages and gets stronger over time.").push("Wine");
        ENABLE_WINE_AGING = builder.comment("Wine gets stronger with age. If off, every wine uses the values from Effects.").define("enableWineAging", true);
        AGE_ONLY_IN_STORAGE = builder.comment("Wine only ages while it sits in a storage block like wine racks, the wine box, shelves or as a placed bottle.").define("ageOnlyInStorage", false);
        CELLAR_AGING_MULTIPLIER = builder.comment("How much faster wine ages in storage without sky light, underground or fully roofed. 1.0 = no bonus.").defineInRange("cellarAgingMultiplier", 1.5, 1.0, 5.0);
        DAYS_PER_YEAR = builder.comment("Minecraft days that make one wine year. 1 day = 20 minutes.").defineInRange("daysPerYear", 24, 1, 100);
        YEARS_PER_EFFECT_LEVEL = builder.comment("Wine years needed for each additional effect level.").defineInRange("yearsPerEffectLevel", 6, 1, 100);
        MAX_LEVEL = builder.comment("Maximum effect levels gained through aging, added to the base level of the wine.").defineInRange("maxLevel", 5, 0, 10);
        DURATION_PER_YEAR = builder.comment("Effect duration in ticks added per wine year. 20 ticks = 1 second.").defineInRange("durationPerYear", 200, 0, 10000);
        MAX_DURATION = builder.comment("Maximum effect duration in ticks reachable through aging. 20 ticks = 1 second.").defineInRange("maxDuration", 15000, 1, 100000);
        WINE_EFFECTS = builder.comment("Effect of each wine, one entry per wine as wine|effect|amplifier|durationTicks. Amplifier 0 = level I, the duration is for a fresh bottle.").defineListAllowEmpty("effects", WineEffects.DEFAULTS, () -> "", entry -> entry instanceof String);
        builder.pop();

        builder.comment("The wandering winemaker.").push("Wandering Trader");
        TRADER_SPAWN_CHANCE = builder.comment("Chance in percent that the wandering winemaker spawns when a spawn is attempted.").defineInRange("spawnChance", 50, 0, 100);
        SPAWN_WITH_MULES = builder.comment("The wandering winemaker arrives with mules.").define("spawnWithMules", true);
        TRADER_SPAWN_DELAY = builder.comment("Time between spawn attempts in ticks. 24000 ticks = 1 day.").defineInRange("spawnDelay", 48000, 1, 168000);
        builder.pop();

        builder.comment("Trades of the winemaker villager. Changes need a game restart.").push("Villager Trades");
        LEVEL1_TRADES = builder.comment("Novice trades, one entry per trade as item|buy or sell|price|count|maxUses|experience.").defineListAllowEmpty("level1Trades", VineryTrades.LEVEL_1, () -> "", VineryTrades::isValid);
        LEVEL2_TRADES = builder.comment("Apprentice trades, one entry per trade as item|buy or sell|price|count|maxUses|experience.").defineListAllowEmpty("level2Trades", VineryTrades.LEVEL_2, () -> "", VineryTrades::isValid);
        LEVEL3_TRADES = builder.comment("Journeyman trades, one entry per trade as item|buy or sell|price|count|maxUses|experience.").defineListAllowEmpty("level3Trades", VineryTrades.LEVEL_3, () -> "", VineryTrades::isValid);
        LEVEL4_TRADES = builder.comment("Expert trades, one entry per trade as item|buy or sell|price|count|maxUses|experience.").defineListAllowEmpty("level4Trades", VineryTrades.LEVEL_4, () -> "", VineryTrades::isValid);
        LEVEL5_TRADES = builder.comment("Master trades, one entry per trade as item|buy or sell|price|count|maxUses|experience.").defineListAllowEmpty("level5Trades", VineryTrades.LEVEL_5, () -> "", VineryTrades::isValid);
        builder.pop();

        builder.comment("Info tooltips that appear when looking at a block. Only affects your own screen.").push("Info Tooltips");
        NEED_STRAW_HAT = builder.comment("Info tooltips only show while wearing the Straw Hat.").define("needStrawHat", false);
        SHOW_GRAPEVINE_POT_INFO = builder.comment("Shows an info tooltip when looking at the Grapevine Pot.").define("showGrapevinePotInfo", true);
        SHOW_STORAGE_INFO = builder.comment("Shows an info tooltip when looking at wine racks, the wine box and other wine storage.").define("showStorageInfo", true);
        builder.pop();

        builder.comment("Fine-tuning of the effects.").push("Effects");
        builder.push("Completionist Banner Effect");
        BANNER_GIVE_EFFECT = builder.comment("The Completionist Banner gives its effect to players nearby.").define("bannerGiveEffect", true);
        BANNER_SHOW_TOOLTIP = builder.comment("The Completionist Banner shows its effect in the item tooltip. Hidden while the banner effect is off.").define("bannerShowTooltip", true);
        builder.pop();
        builder.pop();

        COMMON_CONFIG = builder.build();
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        sync();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        sync();
    }

    public static void sync() {
        totalFermentationTime = TOTAL_FERMENTATION_TIME.get();
        maxFluidLevel = MAX_FLUID_LEVEL.get();
        maxFluidIncrease = MAX_FLUID_INCREASE.get();
        applePressMashingTime = APPLE_PRESS_MASHING_TIME.get();
        applePressFermentationTime = APPLE_PRESS_FERMENTATION_TIME.get();
        cherryGrowthChance = CHERRY_GROWTH_CHANCE.get();
        appleGrowthChance = APPLE_GROWTH_CHANCE.get();
        grapeGrowthChance = GRAPE_GROWTH_CHANCE.get();
        grapeGrowthMultiplier = GRAPE_GROWTH_MULTIPLIER.get();
        enableGrapevineLeaves = ENABLE_GRAPEVINE_LEAVES.get();
        grapevinePotStomps = GRAPEVINE_POT_STOMPS.get();
        grapevinePotHeavyArmorBonus = GRAPEVINE_POT_HEAVY_ARMOR_BONUS.get();
        enableGrapevinePotSplash = ENABLE_GRAPEVINE_POT_SPLASH.get();
        enableWineAging = ENABLE_WINE_AGING.get();
        ageOnlyInStorage = AGE_ONLY_IN_STORAGE.get();
        cellarAgingMultiplier = CELLAR_AGING_MULTIPLIER.get();
        daysPerYear = DAYS_PER_YEAR.get();
        yearsPerEffectLevel = YEARS_PER_EFFECT_LEVEL.get();
        maxLevel = MAX_LEVEL.get();
        durationPerYear = DURATION_PER_YEAR.get();
        maxDuration = MAX_DURATION.get();
        wineEffects = WINE_EFFECTS.get();
        traderSpawnChance = TRADER_SPAWN_CHANCE.get();
        spawnWithMules = SPAWN_WITH_MULES.get();
        traderSpawnDelay = TRADER_SPAWN_DELAY.get();
        level1Trades = LEVEL1_TRADES.get();
        level2Trades = LEVEL2_TRADES.get();
        level3Trades = LEVEL3_TRADES.get();
        level4Trades = LEVEL4_TRADES.get();
        level5Trades = LEVEL5_TRADES.get();
        needStrawHat = NEED_STRAW_HAT.get();
        showGrapevinePotInfo = SHOW_GRAPEVINE_POT_INFO.get();
        showStorageInfo = SHOW_STORAGE_INFO.get();
        bannerGiveEffect = BANNER_GIVE_EFFECT.get();
        bannerShowTooltip = BANNER_SHOW_TOOLTIP.get();
    }
}
