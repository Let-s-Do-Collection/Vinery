package net.satisfy.vinery.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.satisfy.vinery.api.VineryPlugin;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.fabric.config.VineryFabricConfig;
import net.satisfy.vinery.platform.PlatformHelper;

import java.util.List;
import java.util.function.Supplier;

public class PlatformHelperImpl extends PlatformHelper {
    public static int getTotalFermentationTime() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.totalFermentationTime;
    }

    public static int getMaxFluidLevel() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.maxFluidLevel;
    }

    public static int getMaxFluidIncrease() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.maxFluidIncrease;
    }

    public static int getApplePressMashingTime() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.applePressMashingTime;
    }

    public static int getApplePressFermentationTime() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.applePressFermentationTime;
    }

    public static double getCherryGrowthChance() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.cherryGrowthChance / 100.0;
    }

    public static double getAppleGrowthChance() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.appleGrowthChance / 100.0;
    }

    public static double getGrapeGrowthChance() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.grapeGrowthChance / 100.0;
    }

    public static double getGrapeGrowthMultiplier() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.grapeGrowthMultiplier;
    }

    public static double getCellarAgingMultiplier() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.cellarAgingMultiplier;
    }

    public static int getGrapevinePotStomps() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.grapevinePotStomps;
    }

    public static int getGrapevinePotHeavyArmorBonus() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.grapevinePotHeavyArmorBonus;
    }

    public static boolean shouldShowGrapevinePotSplash() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.enableGrapevinePotSplash;
    }

    public static boolean shouldGrapevineLeavesGrow() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.blocks.enableGrapevineLeaves;
    }

    public static int getWineMaxLevel() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.maxLevel;
    }

    public static List<? extends String> getWineEffects() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.effects;
    }

    public static boolean isWineAgingEnabled() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.enableWineAging;
    }

    public static boolean shouldWineAgeOnlyInStorage() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.ageOnlyInStorage;
    }

    public static boolean infoTooltipsNeedStrawHat() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.infoTooltips.needStrawHat;
    }

    public static boolean showGrapevinePotInfo() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.infoTooltips.showGrapevinePotInfo;
    }

    public static boolean showStorageInfo() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.infoTooltips.showStorageInfo;
    }

    public static List<? extends String> getVillagerTrades(int level) {
        VineryFabricConfig.VillagerTradesSettings trades = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig().villagerTrades;
        return switch (level) {
            case 1 -> trades.level1Trades;
            case 2 -> trades.level2Trades;
            case 3 -> trades.level3Trades;
            case 4 -> trades.level4Trades;
            default -> trades.level5Trades;
        };
    }

    public static int getWineDurationPerYear() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.durationPerYear;
    }

    public static int getWineDaysPerYear() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.daysPerYear;
    }

    public static int getWineYearsPerEffectLevel() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.yearsPerEffectLevel;
    }

    public static int getWineMaxDuration() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wine.maxDuration;
    }

    public static boolean shouldGiveEffect() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.effects.completionistBannerEffect.bannerGiveEffect;
    }

    public static boolean shouldShowTooltip() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.effects.completionistBannerEffect.bannerGiveEffect && config.effects.completionistBannerEffect.bannerShowTooltip;
    }

    public static double getTraderSpawnChance() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wanderingTrader.spawnChance / 100.0;
    }

    public static boolean shouldSpawnWithMules() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wanderingTrader.spawnWithMules;
    }

    public static int getTraderSpawnDelay() {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        return config.wanderingTrader.spawnDelay;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        EntityType<T> registry = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Vinery.MOD_ID, name),
                FabricEntityTypeBuilder.create(category, factory).dimensions(EntityDimensions.scalable(width, height)).trackRangeChunks(clientTrackingRange).build()
        );
        return () -> registry;
    }

    public static boolean reloadConfig() {
        return AutoConfig.getConfigHolder(VineryFabricConfig.class).load();
    }

    public static List<VineryPlugin> getPlugins() {
        return FabricLoader.getInstance().getEntrypoints("vinery", VineryPlugin.class);
    }
}
