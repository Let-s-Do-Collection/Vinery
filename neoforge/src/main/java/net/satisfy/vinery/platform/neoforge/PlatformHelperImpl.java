package net.satisfy.vinery.platform.neoforge;

import dev.architectury.registry.registries.DeferredRegister;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.satisfy.vinery.api.VineryPlugin;
import net.satisfy.vinery.core.util.InfoOverlayMode;
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
        return VineryNeoForgeConfig.totalFermentationTimeCache;
    }

    public static int getMaxFluidLevel() {
        return VineryNeoForgeConfig.maxFluidLevelCache;
    }

    public static int getMaxFluidIncrease() {
        return VineryNeoForgeConfig.maxFluidIncreaseCache;
    }

    public static int getApplePressMashingTime() {
        return VineryNeoForgeConfig.applePressMashingTimeCache;
    }

    public static int getApplePressFermentationTime() {
        return VineryNeoForgeConfig.applePressFerentingTimeCache;
    }

    public static double getCherryGrowthChance() {
        return VineryNeoForgeConfig.cherryGrowthChanceCache;
    }

    public static double getAppleGrowthChance() {
        return VineryNeoForgeConfig.appleGrowthChanceCache;
    }

    public static double getGrapeGrowthChance() {
        return VineryNeoForgeConfig.grapeGrowthChanceCache;
    }

    public static double getGrapeGrowthMultiplier() {
        return VineryNeoForgeConfig.grapeGrowthMultiplierCache;
    }

    public static double getCellarAgingMultiplier() {
        return VineryNeoForgeConfig.cellarAgingMultiplierCache;
    }

    public static int getGrapevinePotStomps() {
        return VineryNeoForgeConfig.grapevinePotStompsCache;
    }

    public static int getGrapevinePotHeavyArmorBonus() {
        return VineryNeoForgeConfig.grapevinePotHeavyArmorBonusCache;
    }

    public static boolean shouldShowGrapevinePotSplash() {
        return VineryNeoForgeConfig.grapevinePotSplashCache;
    }

    public static boolean shouldGrapevineLeavesGrow() {
        return VineryNeoForgeConfig.grapevineLeavesEnabledCache;
    }

    public static int getWineMaxLevel() {
        return VineryNeoForgeConfig.maxLevelCache;
    }

    public static List<? extends String> getWineEffects() {
        return VineryNeoForgeConfig.wineEffectsCache;
    }

    public static boolean isWineAgingEnabled() {
        return VineryNeoForgeConfig.agingEnabledCache;
    }

    public static boolean shouldWineAgeOnlyInStorage() {
        return VineryNeoForgeConfig.ageOnlyInStorageCache;
    }

    public static InfoOverlayMode getInfoOverlayMode() {
        return VineryNeoForgeConfig.infoOverlayModeCache;
    }

    public static int getWineDurationPerYear() {
        return VineryNeoForgeConfig.durationPerYearCache;
    }

    public static int getWineDaysPerYear() {
        return VineryNeoForgeConfig.daysPerYearCache;
    }

    public static int getWineYearsPerEffectLevel() {
        return VineryNeoForgeConfig.yearsPerEffectLevelCache;
    }

    public static int getWineMaxDuration() {
        return VineryNeoForgeConfig.maxDurationCache;
    }

    public static boolean shouldGiveEffect() {
        return VineryNeoForgeConfig.giveEffectCache;
    }

    public static boolean shouldShowTooltip() {
        return VineryNeoForgeConfig.giveEffectCache && VineryNeoForgeConfig.showTooltipCache;
    }

    public static double getTraderSpawnChance() {
        return VineryNeoForgeConfig.traderSpawnChanceCache;
    }

    public static boolean shouldSpawnWithMules() {
        return VineryNeoForgeConfig.spawnWithMulesCache;
    }

    public static int getTraderSpawnDelay() {
        return VineryNeoForgeConfig.traderSpawnDelayCache;
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
