package net.satisfy.vinery.core.wine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.satisfy.vinery.core.component.WineYearComponent;
import net.satisfy.vinery.core.registry.DataComponentRegistry;
import net.satisfy.vinery.platform.PlatformHelper;

public class WineYears {
    public static final int YEARS_START = 0;

    public static int getDays(Level level) {
        return level != null ? (int) (level.getGameTime() / 24000L) : 0;
    }

    public static int getYear(Level level, int daysPerYear) {
        if (level == null) {
            return YEARS_START;
        }
        int safeDaysPerYear = Math.max(1, daysPerYear);
        return YEARS_START + (int) ((level.getGameTime() / 24000L) / safeDaysPerYear);
    }

    public static boolean isAgingEnabled() {
        return PlatformHelper.isWineAgingEnabled();
    }

    public static int getWineAgeYears(ItemStack wine, Level level) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null) {
            return 0;
        }
        return getWineAgeDays(wine, level) / Math.max(1, component.daysPerYear());
    }

    public static int getWineAgeDays(ItemStack wine, Level level) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null || level == null || !isAgingEnabled()) {
            return 0;
        }
        int currentDay = getDays(level);
        int running = component.storedSince() >= 0 ? Math.max(0, currentDay - component.storedSince()) : 0;
        int bonus = component.bonusDays() + bonusDays(running, component.storageRate());
        if (PlatformHelper.shouldWineAgeOnlyInStorage()) {
            return component.storedDays() + running + bonus;
        }
        return Math.max(0, currentDay - component.brewedDay()) + bonus;
    }

    private static int bonusDays(int days, int ratePercent) {
        return Math.max(0, days * (ratePercent - 100) / 100);
    }

    public static int storageRate(Level level, BlockPos pos) {
        if (level.getBrightness(LightLayer.SKY, pos) > 0) {
            return 100;
        }
        return (int) Math.round(PlatformHelper.getCellarAgingMultiplier() * 100);
    }

    public static int getEffectLevel(ItemStack wine, Level level) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null) {
            return 0;
        }
        int ageYears = getWineAgeYears(wine, level);
        int safeYearsPerLevel = Math.max(1, component.yearsPerEffectLevel());
        int computed = ageYears / safeYearsPerLevel;
        return Math.max(0, Math.min(component.maxLevel(), computed));
    }

    public static int getEffectDuration(ItemStack wine, Level level, int baseDuration) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null) {
            return baseDuration;
        }
        int ageYears = getWineAgeYears(wine, level);
        long duration = (long) baseDuration + (long) component.durationPerYear() * (long) ageYears;
        int clamped = (int) Math.min(Integer.MAX_VALUE, Math.max(0L, duration));
        return Math.max(baseDuration, Math.min(component.maxDuration(), clamped));
    }

    public static void setWineYear(ItemStack wine, Level level) {
        int brewedDay = getDays(level);
        int daysPerYear = Math.max(1, PlatformHelper.getWineDaysPerYear());
        int yearsPerEffectLevel = Math.max(1, PlatformHelper.getWineYearsPerEffectLevel());
        WineEffects.WineEffect effect = WineEffects.get(wine.getItem());
        int startDuration = effect != null ? effect.duration() : 0;
        int durationPerYear = Math.max(0, PlatformHelper.getWineDurationPerYear());
        int maxDuration = Math.max(0, PlatformHelper.getWineMaxDuration());
        int maxLevel = Math.max(0, PlatformHelper.getWineMaxLevel());

        wine.set(DataComponentRegistry.WINE_YEAR.get(), new WineYearComponent(
                brewedDay,
                daysPerYear,
                yearsPerEffectLevel,
                startDuration,
                durationPerYear,
                maxDuration,
                maxLevel,
                0,
                -1,
                0,
                100
        ));
    }

    public static void startStorage(ItemStack wine, Level level, BlockPos pos) {
        if (!hasWineYear(wine)) {
            setWineYear(wine, level);
        }
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component != null && component.storedSince() < 0) {
            wine.set(DataComponentRegistry.WINE_YEAR.get(), component.withStorage(component.storedDays(), component.bonusDays(), getDays(level), storageRate(level, pos)));
        }
    }

    public static void stopStorage(ItemStack wine, Level level) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component != null && component.storedSince() >= 0) {
            int stored = Math.max(0, getDays(level) - component.storedSince());
            int bonus = component.bonusDays() + bonusDays(stored, component.storageRate());
            wine.set(DataComponentRegistry.WINE_YEAR.get(), component.withStorage(component.storedDays() + stored, bonus, -1, 100));
        }
    }

    public static void setWineAgeDays(ItemStack wine, Level level, int days) {
        if (!hasWineYear(wine)) {
            setWineYear(wine, level);
        }
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        int delta = Math.max(0, days) - getWineAgeDays(wine, level);
        wine.set(DataComponentRegistry.WINE_YEAR.get(), component.withStorage(component.storedDays(), component.bonusDays() + delta, component.storedSince(), component.storageRate()));
    }

    public static int getDaysUntilNextLevel(ItemStack wine, Level level) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null || getEffectLevel(wine, level) >= component.maxLevel()) {
            return -1;
        }
        int daysPerLevel = Math.max(1, component.yearsPerEffectLevel()) * Math.max(1, component.daysPerYear());
        int remaining = (getEffectLevel(wine, level) + 1) * daysPerLevel - getWineAgeDays(wine, level);
        int rate = component.storedSince() >= 0 ? Math.max(100, component.storageRate()) : 100;
        return Math.max(1, (remaining * 100 + rate - 1) / rate);
    }

    public static boolean hasWineYear(ItemStack wine) {
        return wine.get(DataComponentRegistry.WINE_YEAR.get()) != null;
    }
}
