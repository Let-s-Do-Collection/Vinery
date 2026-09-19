package net.satisfy.vinery.core.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.components.WineYearComponent;
import net.satisfy.vinery.core.registry.DataComponentRegistry;
import net.satisfy.vinery.platform.PlatformHelper;

public class WineYears {
    public static final int YEARS_START = 0;

    public static int getDays(Level world) {
        return world != null ? (int) (world.getGameTime() / 24000L) : 0;
    }

    public static int getYear(Level world, int daysPerYear) {
        if (world == null) {
            return YEARS_START;
        }
        int safeDaysPerYear = Math.max(1, daysPerYear);
        return YEARS_START + (int) ((world.getGameTime() / 24000L) / safeDaysPerYear);
    }

    public static int getWineAgeYears(ItemStack wine, Level world) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null || world == null) {
            return 0;
        }
        int currentDay = getDays(world);
        int ageDays = Math.max(0, currentDay - component.brewedDay());
        int safeDaysPerYear = Math.max(1, component.daysPerYear());
        return ageDays / safeDaysPerYear;
    }

    public static int getWineAgeDays(ItemStack wine, Level world) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null || world == null) {
            return 0;
        }
        int currentDay = getDays(world);
        return Math.max(0, currentDay - component.brewedDay());
    }

    public static int getEffectLevel(ItemStack wine, Level world) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null) {
            return 0;
        }
        int ageYears = getWineAgeYears(wine, world);
        int safeYearsPerLevel = Math.max(1, component.yearsPerEffectLevel());
        int computed = ageYears / safeYearsPerLevel;
        return Math.max(0, Math.min(component.maxLevel(), computed));
    }

    public static int getEffectDuration(ItemStack wine, Level world) {
        WineYearComponent component = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (component == null) {
            return getDefaultEffectDuration();
        }
        int ageYears = getWineAgeYears(wine, world);
        long duration = (long) component.startDuration() + (long) component.durationPerYear() * (long) ageYears;
        int clamped = (int) Math.min(Integer.MAX_VALUE, Math.max(0L, duration));
        return Math.min(component.maxDuration(), clamped);
    }

    /**
     * A freshly bottled wine (age 0) would get this duration once {@link #setWineYear} runs -
     * used as the tooltip/preview value before that has happened yet (e.g. right after crafting,
     * looting or trading, before the bottle has ticked once in an inventory).
     */
    public static int getDefaultEffectDuration() {
        int startDuration = Math.max(0, PlatformHelper.getWineStartDuration());
        int maxDuration = Math.max(0, PlatformHelper.getWineMaxDuration());
        return Math.min(maxDuration, startDuration);
    }

    public static void setWineYear(ItemStack wine, Level world) {
        int brewedDay = getDays(world);
        int daysPerYear = Math.max(1, PlatformHelper.getWineDaysPerYear());
        int yearsPerEffectLevel = Math.max(1, PlatformHelper.getWineYearsPerEffectLevel());
        int startDuration = Math.max(0, PlatformHelper.getWineStartDuration());
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
                maxLevel
        ));
    }

    public static boolean hasWineYear(ItemStack wine) {
        return wine.get(DataComponentRegistry.WINE_YEAR.get()) != null;
    }
}