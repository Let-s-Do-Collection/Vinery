package net.satisfy.vinery.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.List;
import java.util.function.Supplier;

public class PlatformHelper {

    @ExpectPlatform
    public static int getTotalFermentationTime() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getMaxFluidLevel() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getMaxFluidIncrease() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getApplePressMashingTime() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getApplePressFermentationTime() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static double getCherryGrowthChance() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static double getAppleGrowthChance() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static double getGrapeGrowthChance() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean shouldGrapevineLeavesGrow() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWineMaxLevel() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWineStartDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWineDurationPerYear() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWineDaysPerYear() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWineYearsPerEffectLevel() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getWineMaxDuration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean shouldGiveEffect() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean shouldShowTooltip() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static double getTraderSpawnChance() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean shouldSpawnWithMules() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getTraderSpawnDelay() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> Supplier<EntityType<T>> registerBoatType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        throw new AssertionError();
    }
}