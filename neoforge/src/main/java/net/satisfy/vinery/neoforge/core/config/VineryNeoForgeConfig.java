package net.satisfy.vinery.neoforge.core.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.satisfy.vinery.core.util.InfoOverlayMode;
import net.satisfy.vinery.core.wine.WineEffects;

import java.util.List;

public class VineryNeoForgeConfig {
    public static final ModConfigSpec COMMON_CONFIG;

    public static final ModConfigSpec.IntValue TOTAL_FERMENTATION_TIME;
    public static final ModConfigSpec.IntValue MAX_FLUID_LEVEL;
    public static final ModConfigSpec.IntValue MAX_FLUID_INCREASE;
    public static final ModConfigSpec.IntValue APPLE_PRESS_MASHING_TIME;
    public static final ModConfigSpec.IntValue APPLE_PRESS_FERMENTING_TIME;
    public static final ModConfigSpec.DoubleValue CHERRY_GROWTH_CHANCE;
    public static final ModConfigSpec.DoubleValue APPLE_GROWTH_CHANCE;
    public static final ModConfigSpec.DoubleValue GRAPE_GROWTH_CHANCE;
    public static final ModConfigSpec.DoubleValue GRAPE_GROWTH_MULTIPLIER;
    public static final ModConfigSpec.BooleanValue GRAPEVINE_LEAVES_ENABLED;
    public static final ModConfigSpec.IntValue GRAPEVINE_POT_STOMPS;
    public static final ModConfigSpec.IntValue GRAPEVINE_POT_HEAVY_ARMOR_BONUS;
    public static final ModConfigSpec.BooleanValue GRAPEVINE_POT_SPLASH;
    public static final ModConfigSpec.DoubleValue CELLAR_AGING_MULTIPLIER;
    public static final ModConfigSpec.IntValue MAX_LEVEL;
    public static final ModConfigSpec.BooleanValue AGING_ENABLED;
    public static final ModConfigSpec.BooleanValue AGE_ONLY_IN_STORAGE;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WINE_EFFECTS;
    public static final ModConfigSpec.EnumValue<InfoOverlayMode> INFO_OVERLAY_MODE;
    public static final ModConfigSpec.IntValue DURATION_PER_YEAR;
    public static final ModConfigSpec.IntValue DAYS_PER_YEAR;
    public static final ModConfigSpec.IntValue YEARS_PER_EFFECT_LEVEL;
    public static final ModConfigSpec.IntValue MAX_DURATION;
    public static final ModConfigSpec.BooleanValue GIVE_EFFECT;
    public static final ModConfigSpec.BooleanValue SHOW_TOOLTIP;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL1_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL2_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL3_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL4_TRADES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEVEL5_TRADES;
    public static final ModConfigSpec.DoubleValue TRADER_SPAWN_CHANCE;
    public static final ModConfigSpec.BooleanValue SPAWN_WITH_MULES;
    public static final ModConfigSpec.IntValue TRADER_SPAWN_DELAY;

    public static List<? extends String> level1TradesCache;
    public static List<? extends String> level2TradesCache;
    public static List<? extends String> level3TradesCache;
    public static List<? extends String> level4TradesCache;
    public static List<? extends String> level5TradesCache;

    public static int totalFermentationTimeCache;
    public static int maxFluidLevelCache;
    public static int maxFluidIncreaseCache;
    public static int applePressMashingTimeCache;
    public static int applePressFerentingTimeCache;
    public static double cherryGrowthChanceCache;
    public static double appleGrowthChanceCache;
    public static double grapeGrowthChanceCache;
    public static double grapeGrowthMultiplierCache;
    public static boolean grapevineLeavesEnabledCache;
    public static int grapevinePotStompsCache;
    public static int grapevinePotHeavyArmorBonusCache;
    public static boolean grapevinePotSplashCache;
    public static double cellarAgingMultiplierCache;
    public static int maxLevelCache;
    public static boolean agingEnabledCache;
    public static boolean ageOnlyInStorageCache;
    public static List<? extends String> wineEffectsCache;
    public static InfoOverlayMode infoOverlayModeCache;
    public static int durationPerYearCache;
    public static int daysPerYearCache;
    public static int yearsPerEffectLevelCache;
    public static int maxDurationCache;
    public static boolean giveEffectCache;
    public static boolean showTooltipCache;
    public static double traderSpawnChanceCache;
    public static boolean spawnWithMulesCache;
    public static int traderSpawnDelayCache;

    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
        commonBuilder.push("Blocks");

        TOTAL_FERMENTATION_TIME = commonBuilder
                .comment("Total fermentation time in ticks")
                .defineInRange("totalFermentationTime", 6000, 1, Integer.MAX_VALUE);

        MAX_FLUID_LEVEL = commonBuilder
                .comment("Maximum fluid level in the Fermentation Barrel")
                .defineInRange("maxFluidLevel", 100, 10, 1000);

        MAX_FLUID_INCREASE = commonBuilder
                .comment("How much Fluid a Grapejuice Bottle fills")
                .defineInRange("maxFluidPerJuice", 25, 1, 1000);

        APPLE_PRESS_MASHING_TIME = commonBuilder
                .comment("Apple Press mashing time in ticks")
                .defineInRange("applePressMaxMashingProgress", 600, 1, 1000);

        APPLE_PRESS_FERMENTING_TIME = commonBuilder
                .comment("Apple Press fermenting time in ticks")
                .defineInRange("applePressMaxFermentingProgress", 800, 1, 1000);

        CHERRY_GROWTH_CHANCE = commonBuilder
                .comment("Chance for cherries to grow")
                .defineInRange("cherryGrowthChance", 0.4, 0.0, 1.0);

        APPLE_GROWTH_CHANCE = commonBuilder
                .comment("Chance for apples to grow")
                .defineInRange("appleGrowthChance", 0.4, 0.0, 1.0);

        GRAPE_GROWTH_CHANCE = commonBuilder
                .comment("Chance for grapes to grow")
                .defineInRange("grapeGrowthChance", 0.5, 0.0, 1.0);

        GRAPE_GROWTH_MULTIPLIER = commonBuilder
                .comment("Multiplies the growth chance of grape bushes, grapevine stems and lattices. 0 = grapes never grow.")
                .defineInRange("grapeGrowthMultiplier", 1.0, 0.0, 10.0);

        GRAPEVINE_LEAVES_ENABLED = commonBuilder
                .comment("Whether Red/White Grapevine stems grow a canopy of leaves around them after some time")
                .define("grapevineLeavesEnabled", true);

        GRAPEVINE_POT_STOMPS = commonBuilder
                .comment("Jumps needed to stomp a full Grapevine Pot")
                .defineInRange("grapevinePotStomps", 12, 1, 100);

        GRAPEVINE_POT_HEAVY_ARMOR_BONUS = commonBuilder
                .comment("How much faster heavy armor (iron or better) stomps in the Grapevine Pot, in percent")
                .defineInRange("grapevinePotHeavyArmorBonus", 10, 0, 100);

        GRAPEVINE_POT_SPLASH = commonBuilder
                .comment("Whether juice splashes out of the Grapevine Pot when stomping")
                .define("grapevinePotSplashParticles", true);

        commonBuilder.pop();

        commonBuilder.push("Items");
        commonBuilder.push("Wine");

        AGING_ENABLED = commonBuilder
                .comment("Whether wine gets stronger with age. If false, every wine uses the effect, amplifier and duration from 'effects'.")
                .define("agingEnabled", true);

        AGE_ONLY_IN_STORAGE = commonBuilder
                .comment("If true, wine only ages while it sits in a storage block (wine racks, wine box, shelves, placed bottles).",
                        "If false, wine ages from the moment it is brewed, wherever it is.")
                .define("ageOnlyInStorage", false);

        CELLAR_AGING_MULTIPLIER = commonBuilder
                .comment("How much faster wine ages in storage without sky light (underground or fully roofed). 1.0 = no bonus.")
                .defineInRange("cellarAgingMultiplier", 1.5, 1.0, 5.0);

        DAYS_PER_YEAR = commonBuilder
                .comment("Minecraft days that make one wine year (one day = 24000 ticks = 20 minutes).")
                .defineInRange("daysPerYear", 24, 1, 100);

        YEARS_PER_EFFECT_LEVEL = commonBuilder
                .comment("Wine years needed for each additional effect level.")
                .defineInRange("yearsPerEffectLevel", 6, 1, 100);

        MAX_LEVEL = commonBuilder
                .comment("Maximum effect levels gained through aging, added to the wine's base amplifier.")
                .defineInRange("maxLevel", 5, 0, 10);

        DURATION_PER_YEAR = commonBuilder
                .comment("Effect duration in ticks added per wine year (20 ticks = 1 second).")
                .defineInRange("durationPerYear", 200, 0, 10000);

        MAX_DURATION = commonBuilder
                .comment("Maximum effect duration in ticks reachable through aging (20 ticks = 1 second).")
                .defineInRange("maxDuration", 15000, 1, 100000);

        WINE_EFFECTS = commonBuilder
                .comment("Effect of each wine. Format: wine|effect|amplifier|durationTicks",
                        "amplifier 0 = level I. durationTicks is the duration of a fresh bottle, aging adds to it.")
                .defineListAllowEmpty("effects", WineEffects.DEFAULTS, () -> "", obj -> obj instanceof String);

        commonBuilder.pop();
        commonBuilder.push("Banner");

        GIVE_EFFECT = commonBuilder
                .comment("Set to false to disable the banner's effect.")
                .define("giveEffect", true);

        SHOW_TOOLTIP = commonBuilder
                .comment("Set to false to hide the banner's tooltip. If giveEffect is false, showTooltip is automatically false.")
                .define("showTooltip", true);

        commonBuilder.pop();

        commonBuilder.push("WanderingTrader");

        TRADER_SPAWN_CHANCE = commonBuilder
                .comment("Chance for the custom trader to spawn. Range: 0.0 to 1.0")
                .defineInRange("spawnChance", 0.5, 0.0, 1.0);

        SPAWN_WITH_MULES = commonBuilder
                .comment("If true, the trader will spawn with mules.")
                .define("spawnWithMules", true);

        TRADER_SPAWN_DELAY = commonBuilder
                .comment("Spawn delay for the trader in ticks.")
                .defineInRange("spawnDelay", 48000, 1, Integer.MAX_VALUE);

        commonBuilder.pop();
        commonBuilder.push("InfoOverlay");

        INFO_OVERLAY_MODE = commonBuilder
                .comment("When the info overlay for grapes, grapevine pots and wine storage is shown: OFF, ON or STRAW_HAT (only while wearing the straw hat).")
                .defineEnum("mode", InfoOverlayMode.ON);

        commonBuilder.pop();
        commonBuilder.push("VillagerTrades");

        LEVEL1_TRADES = commonBuilder
                .comment("List of trades for Level 1. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level1Trades", List.of(
                        "vinery:red_grape|5|4|5|false",
                        "vinery:white_grape|5|4|5|false",
                        "vinery:red_grape_seeds|2|1|1|true",
                        "vinery:white_grape_seeds|2|1|1|true"
                ), obj -> obj instanceof String);

        LEVEL2_TRADES = commonBuilder
                .comment("List of trades for Level 2. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level2Trades", List.of(
                        "vinery:wine_bottle|1|1|4|true",
                        "vinery:cherry|12|1|4|false",
                        "vinery:apple_mash|1|1|4|true"
                ), obj -> obj instanceof String);

        LEVEL3_TRADES = commonBuilder
                .comment("List of trades for Level 3. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level3Trades", List.of(
                        "vinery:white_grape_bag|7|1|2|true",
                        "vinery:red_grape_bag|7|1|2|true",
                        "vinery:cherry_bag|7|1|2|true",
                        "vinery:apple_bag|7|1|2|true"
                ), obj -> obj instanceof String);

        LEVEL4_TRADES = commonBuilder
                .comment("List of trades for Level 4. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level4Trades", List.of(
                        "vinery:window|12|1|2|true",
                        "vinery:dark_cherry_beam|6|4|2|true",
                        "vinery:grapevine_pot|6|1|2|true",
                        "vinery:taiga_grape_seeds_red|2|1|2|true",
                        "vinery:taiga_grape_seeds_white|2|1|2|true"
                ), obj -> obj instanceof String);

        LEVEL5_TRADES = commonBuilder
                .comment("List of trades for Level 5. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level5Trades", List.of(
                        "vinery:wine_box|10|1|2|true",
                        "vinery:lilitu_wine|4|1|2|true",
                        "vinery:winemaker_apron|18|1|1|true",
                        "vinery:straw_hat|12|1|1|true"
                ), obj -> obj instanceof String);

        commonBuilder.pop();

        COMMON_CONFIG = commonBuilder.build();

        initializeCache();
    }

    private static void initializeCache() {
        totalFermentationTimeCache = 6000;
        maxFluidLevelCache = 100;
        maxFluidIncreaseCache = 25;
        applePressMashingTimeCache = 600;
        applePressFerentingTimeCache = 800;
        cherryGrowthChanceCache = 0.4;
        appleGrowthChanceCache = 0.4;
        grapeGrowthChanceCache = 0.5;
        grapeGrowthMultiplierCache = 1.0;
        grapevineLeavesEnabledCache = true;
        grapevinePotStompsCache = 12;
        grapevinePotHeavyArmorBonusCache = 10;
        grapevinePotSplashCache = true;
        cellarAgingMultiplierCache = 1.5;
        maxLevelCache = 5;
        agingEnabledCache = true;
        ageOnlyInStorageCache = false;
        wineEffectsCache = WineEffects.DEFAULTS;
        infoOverlayModeCache = InfoOverlayMode.ON;
        durationPerYearCache = 200;
        daysPerYearCache = 24;
        yearsPerEffectLevelCache = 6;
        maxDurationCache = 15000;
        giveEffectCache = true;
        showTooltipCache = true;
        traderSpawnChanceCache = 0.5;
        spawnWithMulesCache = true;
        traderSpawnDelayCache = 48000;

        level1TradesCache = List.of(
                "vinery:red_grape|5|4|5|false",
                "vinery:white_grape|5|4|5|false",
                "vinery:red_grape_seeds|2|1|1|true",
                "vinery:white_grape_seeds|2|1|1|true"
        );

        level2TradesCache = List.of(
                "vinery:wine_bottle|1|1|4|true",
                "vinery:cherry|12|1|4|false",
                "vinery:apple_mash|1|1|4|true"
        );

        level3TradesCache = List.of(
                "vinery:white_grape_bag|7|1|2|true",
                "vinery:red_grape_bag|7|1|2|true",
                "vinery:cherry_bag|7|1|2|true",
                "vinery:apple_bag|7|1|2|true"
        );

        level4TradesCache = List.of(
                "vinery:window|12|1|2|true",
                "vinery:dark_cherry_beam|6|4|2|true",
                "vinery:grapevine_pot|6|1|2|true",
                "vinery:taiga_grape_seeds_red|2|1|2|true",
                "vinery:taiga_grape_seeds_white|2|1|2|true"
        );

        level5TradesCache = List.of(
                "vinery:wine_box|10|1|2|true",
                "vinery:lilitu_wine|4|1|2|true",
                "vinery:winemaker_apron|18|1|1|true",
                "vinery:straw_hat|12|1|1|true"
        );
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        updateCache();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        updateCache();
    }

    private static void updateCache() {
        totalFermentationTimeCache = TOTAL_FERMENTATION_TIME.get();
        maxFluidLevelCache = MAX_FLUID_LEVEL.get();
        maxFluidIncreaseCache = MAX_FLUID_INCREASE.get();
        applePressMashingTimeCache = APPLE_PRESS_MASHING_TIME.get();
        applePressFerentingTimeCache = APPLE_PRESS_FERMENTING_TIME.get();
        cherryGrowthChanceCache = CHERRY_GROWTH_CHANCE.get();
        appleGrowthChanceCache = APPLE_GROWTH_CHANCE.get();
        grapeGrowthChanceCache = GRAPE_GROWTH_CHANCE.get();
        grapevineLeavesEnabledCache = GRAPEVINE_LEAVES_ENABLED.get();
        grapevinePotStompsCache = GRAPEVINE_POT_STOMPS.get();
        grapevinePotHeavyArmorBonusCache = GRAPEVINE_POT_HEAVY_ARMOR_BONUS.get();
        grapevinePotSplashCache = GRAPEVINE_POT_SPLASH.get();
        cellarAgingMultiplierCache = CELLAR_AGING_MULTIPLIER.get();
        grapeGrowthMultiplierCache = GRAPE_GROWTH_MULTIPLIER.get();
        maxLevelCache = MAX_LEVEL.get();
        agingEnabledCache = AGING_ENABLED.get();
        ageOnlyInStorageCache = AGE_ONLY_IN_STORAGE.get();
        wineEffectsCache = WINE_EFFECTS.get();
        infoOverlayModeCache = INFO_OVERLAY_MODE.get();
        durationPerYearCache = DURATION_PER_YEAR.get();
        daysPerYearCache = DAYS_PER_YEAR.get();
        yearsPerEffectLevelCache = YEARS_PER_EFFECT_LEVEL.get();
        maxDurationCache = MAX_DURATION.get();
        giveEffectCache = GIVE_EFFECT.get();
        showTooltipCache = SHOW_TOOLTIP.get();
        traderSpawnChanceCache = TRADER_SPAWN_CHANCE.get();
        spawnWithMulesCache = SPAWN_WITH_MULES.get();
        traderSpawnDelayCache = TRADER_SPAWN_DELAY.get();
        level1TradesCache = LEVEL1_TRADES.get();
        level2TradesCache = LEVEL2_TRADES.get();
        level3TradesCache = LEVEL3_TRADES.get();
        level4TradesCache = LEVEL4_TRADES.get();
        level5TradesCache = LEVEL5_TRADES.get();
    }
}
