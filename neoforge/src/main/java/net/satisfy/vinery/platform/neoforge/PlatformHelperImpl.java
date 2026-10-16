package net.satisfy.vinery.platform.neoforge;

import dev.architectury.registry.registries.DeferredRegister;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.satisfy.vinery.api.VineryPlugin;
import org.objectweb.asm.Type;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.neoforge.core.config.VineryNeoForgeConfig;
import net.satisfy.vinery.platform.PlatformHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PlatformHelperImpl extends PlatformHelper {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Vinery.MOD_ID, Registries.ENTITY_TYPE);

    public static int getTotalFermentationTime() {
        return VineryNeoForgeConfig.totalFermentationTime;
    }

    public static int getMaxFluidLevel() {
        return VineryNeoForgeConfig.maxFluidLevel;
    }

    public static int getMaxFluidIncrease() {
        return VineryNeoForgeConfig.maxFluidIncrease;
    }

    public static int getApplePressMashingTime() {
        return VineryNeoForgeConfig.applePressMashingTime;
    }

    public static int getApplePressFermentationTime() {
        return VineryNeoForgeConfig.applePressFermentationTime;
    }

    public static double getCherryGrowthChance() {
        return VineryNeoForgeConfig.cherryGrowthChance / 100.0;
    }

    public static double getAppleGrowthChance() {
        return VineryNeoForgeConfig.appleGrowthChance / 100.0;
    }

    public static double getGrapeGrowthChance() {
        return VineryNeoForgeConfig.grapeGrowthChance / 100.0;
    }

    public static double getGrapeGrowthMultiplier() {
        return VineryNeoForgeConfig.grapeGrowthMultiplier;
    }

    public static double getCellarAgingMultiplier() {
        return VineryNeoForgeConfig.cellarAgingMultiplier;
    }

    public static int getGrapevinePotStomps() {
        return VineryNeoForgeConfig.grapevinePotStomps;
    }

    public static int getGrapevinePotHeavyArmorBonus() {
        return VineryNeoForgeConfig.grapevinePotHeavyArmorBonus;
    }

    public static boolean shouldShowGrapevinePotSplash() {
        return VineryNeoForgeConfig.enableGrapevinePotSplash;
    }

    public static boolean shouldGrapevineLeavesGrow() {
        return VineryNeoForgeConfig.enableGrapevineLeaves;
    }

    public static int getWineMaxLevel() {
        return VineryNeoForgeConfig.maxLevel;
    }

    public static List<? extends String> getWineEffects() {
        return VineryNeoForgeConfig.wineEffects;
    }

    public static boolean isWineAgingEnabled() {
        return VineryNeoForgeConfig.enableWineAging;
    }

    public static boolean shouldWineAgeOnlyInStorage() {
        return VineryNeoForgeConfig.ageOnlyInStorage;
    }

    public static boolean infoTooltipsNeedStrawHat() {
        return VineryNeoForgeConfig.needStrawHat;
    }

    public static boolean showGrapevinePotInfo() {
        return VineryNeoForgeConfig.showGrapevinePotInfo;
    }

    public static boolean showStorageInfo() {
        return VineryNeoForgeConfig.showStorageInfo;
    }

    public static List<? extends String> getVillagerTrades(int level) {
        return switch (level) {
            case 1 -> VineryNeoForgeConfig.level1Trades;
            case 2 -> VineryNeoForgeConfig.level2Trades;
            case 3 -> VineryNeoForgeConfig.level3Trades;
            case 4 -> VineryNeoForgeConfig.level4Trades;
            default -> VineryNeoForgeConfig.level5Trades;
        };
    }

    public static int getWineDurationPerYear() {
        return VineryNeoForgeConfig.durationPerYear;
    }

    public static int getWineDaysPerYear() {
        return VineryNeoForgeConfig.daysPerYear;
    }

    public static int getWineYearsPerEffectLevel() {
        return VineryNeoForgeConfig.yearsPerEffectLevel;
    }

    public static int getWineMaxDuration() {
        return VineryNeoForgeConfig.maxDuration;
    }

    public static boolean shouldGiveEffect() {
        return VineryNeoForgeConfig.bannerGiveEffect;
    }

    public static boolean shouldShowTooltip() {
        return VineryNeoForgeConfig.bannerGiveEffect && VineryNeoForgeConfig.bannerShowTooltip;
    }

    public static double getTraderSpawnChance() {
        return VineryNeoForgeConfig.traderSpawnChance / 100.0;
    }

    public static boolean shouldSpawnWithMules() {
        return VineryNeoForgeConfig.spawnWithMules;
    }

    public static int getTraderSpawnDelay() {
        return VineryNeoForgeConfig.traderSpawnDelay;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, category).sized(width, height).build(name));
    }

    public static List<VineryPlugin> getPlugins() {
        Type entry = Type.getType(VineryPlugin.Entry.class);
        List<VineryPlugin> plugins = new ArrayList<>();
        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData annotation : scanData.getAnnotations()) {
                if (entry.equals(annotation.annotationType())) {
                    try {
                        plugins.add(Class.forName(annotation.clazz().getClassName()).asSubclass(VineryPlugin.class).getDeclaredConstructor().newInstance());
                    } catch (ReflectiveOperationException | ClassCastException e) {
                        throw new IllegalStateException("Could not load Vinery plugin " + annotation.clazz().getClassName(), e);
                    }
                }
            }
        }
        return plugins;
    }

    public static boolean reloadConfig() {
        return false;
    }
}
