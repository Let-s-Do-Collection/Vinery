package net.satisfy.vinery.neoforge.core.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.File;
import java.util.List;

public class VineryForgeConfig {
    public static final ModConfigSpec COMMON_CONFIG;

    public static final ModConfigSpec.IntValue TOTAL_FERMENTATION_TIME;
    public static final ModConfigSpec.IntValue MAX_FLUID_LEVEL;
    public static final ModConfigSpec.IntValue MAX_FLUID_INCREASE;
    public static final ModConfigSpec.IntValue APPLE_PRESS_MASHING_TIME;
    public static final ModConfigSpec.IntValue APPLE_PRESS_FERMENTING_TIME;
    public static final ModConfigSpec.DoubleValue CHERRY_GROWTH_CHANCE;
    public static final ModConfigSpec.DoubleValue APPLE_GROWTH_CHANCE;
    public static final ModConfigSpec.DoubleValue GRAPE_GROWTH_CHANCE;
    public static final ModConfigSpec.IntValue MAX_LEVEL;
    public static final ModConfigSpec.IntValue START_DURATION;
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
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BASKET_BLACKLIST;
    public static final ModConfigSpec.DoubleValue TRADER_SPAWN_CHANCE;
    public static final ModConfigSpec.BooleanValue SPAWN_WITH_MULES;
    public static final ModConfigSpec.IntValue TRADER_SPAWN_DELAY;

    // Cached config values - these are safe to access at any time
    public static List<? extends String> level1TradesCache;
    public static List<? extends String> level2TradesCache;
    public static List<? extends String> level3TradesCache;
    public static List<? extends String> level4TradesCache;
    public static List<? extends String> level5TradesCache;
    public static List<? extends String> basketBlacklistCache;

    // Cached numeric/boolean values
    public static int totalFermentationTimeCache;
    public static int maxFluidLevelCache;
    public static int maxFluidIncreaseCache;
    public static int applePressMashingTimeCache;
    public static int applePressFerentingTimeCache;
    public static double cherryGrowthChanceCache;
    public static double appleGrowthChanceCache;
    public static double grapeGrowthChanceCache;
    public static int maxLevelCache;
    public static int startDurationCache;
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
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.push("Blocks");

        TOTAL_FERMENTATION_TIME = COMMON_BUILDER
                .comment("Total fermentation time in ticks")
                .defineInRange("totalFermentationTime", 6000, 1, Integer.MAX_VALUE);

        MAX_FLUID_LEVEL = COMMON_BUILDER
                .comment("Maximum fluid level in the Fermentation Barrel")
                .defineInRange("maxFluidLevel", 100, 10, 1000);

        MAX_FLUID_INCREASE = COMMON_BUILDER
                .comment("How much Fluid a Grapejuice Bottle fills")
                .defineInRange("maxFluidPerJuice", 25, 1, 1000);

        APPLE_PRESS_MASHING_TIME = COMMON_BUILDER
                .comment("Apple Press mashing time in ticks")
                .defineInRange("applePressMaxMashingProgress", 600, 1, 1000);

        APPLE_PRESS_FERMENTING_TIME = COMMON_BUILDER
                .comment("Apple Press fermenting time in ticks")
                .defineInRange("applePressMaxFermentingProgress", 800, 1, 1000);

        CHERRY_GROWTH_CHANCE = COMMON_BUILDER
                .comment("Chance for cherries to grow")
                .defineInRange("cherryGrowthChance", 0.4, 0.0, 1.0);

        APPLE_GROWTH_CHANCE = COMMON_BUILDER
                .comment("Chance for apples to grow")
                .defineInRange("appleGrowthChance", 0.4, 0.0, 1.0);

        GRAPE_GROWTH_CHANCE = COMMON_BUILDER
                .comment("Chance for grapes to grow")
                .defineInRange("grapeGrowthChance", 0.5, 0.0, 1.0);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("Items");
        COMMON_BUILDER.push("Wine");

        MAX_LEVEL = COMMON_BUILDER
                .comment("Maximum level for wine")
                .defineInRange("maxLevel", 5, 1, 10);

        START_DURATION = COMMON_BUILDER
                .comment("Start duration for wine in seconds")
                .defineInRange("startDuration", 1800, 1, 100000);

        DURATION_PER_YEAR = COMMON_BUILDER
                .comment("Duration per year in seconds")
                .defineInRange("durationPerYear", 200, 1, 10000);

        DAYS_PER_YEAR = COMMON_BUILDER
                .comment("Days per year")
                .defineInRange("daysPerYear", 24, 1, 100);

        YEARS_PER_EFFECT_LEVEL = COMMON_BUILDER
                .comment("Years per effect level")
                .defineInRange("yearsPerEffectLevel", 6, 1, 100);

        MAX_DURATION = COMMON_BUILDER
                .comment("Maximum duration in seconds")
                .defineInRange("maxDuration", 15000, 1, 100000);

        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Banner");

        GIVE_EFFECT = COMMON_BUILDER
                .comment("Set to false to disable the banner's effect.")
                .define("giveEffect", true);

        SHOW_TOOLTIP = COMMON_BUILDER
                .comment("Set to false to hide the banner's tooltip. If giveEffect is false, showTooltip is automatically false.")
                .define("showTooltip", true);

        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Basket");

        BASKET_BLACKLIST = COMMON_BUILDER
                .comment("List of item IDs that are blacklisted from being placed in the picnic basket. Format: 'modid:itemname'")
                .defineList("basketBlacklist", List.of(
                        "vinery:basket"
                ), obj -> obj instanceof String);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("WanderingTrader");
        TRADER_SPAWN_CHANCE = COMMON_BUILDER
                .comment("Chance for the custom trader to spawn. Range: 0.0 to 1.0")
                .defineInRange("spawnChance", 0.5, 0.0, 1.0);

        SPAWN_WITH_MULES = COMMON_BUILDER
                .comment("If true, the trader will spawn with mules.")
                .define("spawnWithMules", true);

        TRADER_SPAWN_DELAY = COMMON_BUILDER
                .comment("Spawn delay for the trader in ticks.")
                .defineInRange("spawnDelay", 48000, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("VillagerTrades");

        LEVEL1_TRADES = COMMON_BUILDER
                .comment("List of trades for Level 1. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level1Trades", List.of(
                        "vinery:red_grape|5|4|5|false",
                        "vinery:white_grape|5|4|5|false",
                        "vinery:red_grape_seeds|2|1|1|true",
                        "vinery:white_grape_seeds|2|1|1|true"
                ), obj -> obj instanceof String);

        LEVEL2_TRADES = COMMON_BUILDER
                .comment("List of trades for Level 2. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level2Trades", List.of(
                        "vinery:wine_bottle|1|2|7|true"
                ), obj -> obj instanceof String);

        LEVEL3_TRADES = COMMON_BUILDER
                .comment("List of trades for Level 3. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level3Trades", List.of(
                        "vinery:flower_box|3|1|10|true",
                        "vinery:white_grape_bag|7|1|10|true",
                        "vinery:red_grape_bag|7|1|10|true"
                ), obj -> obj instanceof String);

        LEVEL4_TRADES = COMMON_BUILDER
                .comment("List of trades for Level 4. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level4Trades", List.of(
                        "vinery:basket|4|1|10|true",
                        "vinery:flower_pot_big|5|1|10|true",
                        "vinery:window|12|1|10|true",
                        "vinery:dark_cherry_beam|6|1|10|true",
                        "vinery:taiga_red_grape_seeds|2|1|5|true",
                        "vinery:taiga_white_grape_seeds|2|1|5|true"
                ), obj -> obj instanceof String);

        LEVEL5_TRADES = COMMON_BUILDER
                .comment("List of trades for Level 5. Format: item|price|quantity|maxUses|isSelling")
                .defineList("level5Trades", List.of(
                        "vinery:wine_box|10|1|10|true",
                        "vinery:lilitu_wine|4|1|10|true",
                        "vinery:calendar|12|1|15|true"
                ), obj -> obj instanceof String);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();

        // Initialize cache with default values
        initializeCache();
    }

    private static void initializeCache() {
        // Initialize with default values from config definitions
        totalFermentationTimeCache = 6000;
        maxFluidLevelCache = 100;
        maxFluidIncreaseCache = 25;
        applePressMashingTimeCache = 600;
        applePressFerentingTimeCache = 800;
        cherryGrowthChanceCache = 0.4;
        appleGrowthChanceCache = 0.4;
        grapeGrowthChanceCache = 0.5;
        maxLevelCache = 5;
        startDurationCache = 1800;
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
        level2TradesCache = List.of("vinery:wine_bottle|1|2|7|true");
        level3TradesCache = List.of(
                "vinery:flower_box|3|1|10|true",
                "vinery:white_grape_bag|7|1|10|true",
                "vinery:red_grape_bag|7|1|10|true"
        );
        level4TradesCache = List.of(
                "vinery:basket|4|1|10|true",
                "vinery:flower_pot_big|5|1|10|true",
                "vinery:window|12|1|10|true",
                "vinery:dark_cherry_beam|6|1|10|true",
                "vinery:taiga_red_grape_seeds|2|1|5|true",
                "vinery:taiga_white_grape_seeds|2|1|5|true"
        );
        level5TradesCache = List.of(
                "vinery:wine_box|10|1|10|true",
                "vinery:lilitu_wine|4|1|10|true",
                "vinery:calendar|12|1|15|true"
        );
        basketBlacklistCache = List.of("vinery:basket");
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading e) {
        if (e.getConfig().getSpec() instanceof ModConfigSpec s) {
            loadConfig(s, e.getConfig().getFileName());
            updateCache();
        }
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading e) {
        if (e.getConfig().getSpec() instanceof ModConfigSpec s) {
            loadConfig(s, e.getConfig().getFileName());
            updateCache();
        }
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
        maxLevelCache = MAX_LEVEL.get();
        startDurationCache = START_DURATION.get();
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
        basketBlacklistCache = BASKET_BLACKLIST.get();
    }

    public static void loadConfig(ModConfigSpec spec, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync()
                .preserveInsertionOrder()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        file.load();
        spec.correct(file);
        file.save();
    }
}