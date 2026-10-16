package net.satisfy.vinery.fabric.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.satisfy.vinery.core.util.VineryTrades;
import net.satisfy.vinery.core.wine.WineEffects;

import java.util.ArrayList;
import java.util.List;

@Config(name = "vinery")
@Config.Gui.Background("vinery:textures/block/dark_cherry_beam_side.png")
public class VineryFabricConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public BlocksSettings blocks = new BlocksSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public WineSettings wine = new WineSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public WanderingTraderSettings wanderingTrader = new WanderingTraderSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public VillagerTradesSettings villagerTrades = new VillagerTradesSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InfoTooltipSettings infoTooltips = new InfoTooltipSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public EffectsSettings effects = new EffectsSettings();

    public static class BlocksSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 72000)
        public int totalFermentationTime = 6000;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 10, max = 1000)
        public int maxFluidLevel = 100;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public int maxFluidIncrease = 25;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 72000)
        public int applePressMashingTime = 600;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 72000)
        public int applePressFermentationTime = 800;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int cherryGrowthChance = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int appleGrowthChance = 40;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int grapeGrowthChance = 50;

        @ConfigEntry.Gui.Tooltip
        public double grapeGrowthMultiplier = 1.0;

        @ConfigEntry.Gui.Tooltip
        public boolean enableGrapevineLeaves = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int grapevinePotStomps = 12;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int grapevinePotHeavyArmorBonus = 10;

        @ConfigEntry.Gui.Tooltip
        public boolean enableGrapevinePotSplash = true;
    }

    public static class WineSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableWineAging = true;

        @ConfigEntry.Gui.Tooltip
        public boolean ageOnlyInStorage = false;

        @ConfigEntry.Gui.Tooltip
        public double cellarAgingMultiplier = 1.5;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int daysPerYear = 24;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int yearsPerEffectLevel = 6;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 10)
        public int maxLevel = 5;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 10000)
        public int durationPerYear = 200;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100000)
        public int maxDuration = 15000;

        @ConfigEntry.Gui.Tooltip
        public List<String> effects = new ArrayList<>(WineEffects.DEFAULTS);
    }

    public static class WanderingTraderSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int spawnChance = 50;

        @ConfigEntry.Gui.Tooltip
        public boolean spawnWithMules = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 168000)
        public int spawnDelay = 48000;
    }

    public static class VillagerTradesSettings {
        @ConfigEntry.Gui.Tooltip
        public List<String> level1Trades = new ArrayList<>(VineryTrades.LEVEL_1);

        @ConfigEntry.Gui.Tooltip
        public List<String> level2Trades = new ArrayList<>(VineryTrades.LEVEL_2);

        @ConfigEntry.Gui.Tooltip
        public List<String> level3Trades = new ArrayList<>(VineryTrades.LEVEL_3);

        @ConfigEntry.Gui.Tooltip
        public List<String> level4Trades = new ArrayList<>(VineryTrades.LEVEL_4);

        @ConfigEntry.Gui.Tooltip
        public List<String> level5Trades = new ArrayList<>(VineryTrades.LEVEL_5);
    }

    public static class InfoTooltipSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean needStrawHat = false;

        @ConfigEntry.Gui.Tooltip
        public boolean showGrapevinePotInfo = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showStorageInfo = true;
    }

    public static class EffectsSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public CompletionistBannerEffectSettings completionistBannerEffect = new CompletionistBannerEffectSettings();

        public static class CompletionistBannerEffectSettings {
            @ConfigEntry.Gui.Tooltip
            public boolean bannerGiveEffect = true;

            @ConfigEntry.Gui.Tooltip
            public boolean bannerShowTooltip = true;
        }
    }

    @Override
    public void validatePostLoad() {
        blocks.grapeGrowthMultiplier = Math.clamp(blocks.grapeGrowthMultiplier, 0.0, 10.0);
        wine.cellarAgingMultiplier = Math.clamp(wine.cellarAgingMultiplier, 1.0, 5.0);
        if (wine.effects == null) wine.effects = new ArrayList<>(WineEffects.DEFAULTS);
        if (villagerTrades.level1Trades == null) villagerTrades.level1Trades = new ArrayList<>(VineryTrades.LEVEL_1);
        if (villagerTrades.level2Trades == null) villagerTrades.level2Trades = new ArrayList<>(VineryTrades.LEVEL_2);
        if (villagerTrades.level3Trades == null) villagerTrades.level3Trades = new ArrayList<>(VineryTrades.LEVEL_3);
        if (villagerTrades.level4Trades == null) villagerTrades.level4Trades = new ArrayList<>(VineryTrades.LEVEL_4);
        if (villagerTrades.level5Trades == null) villagerTrades.level5Trades = new ArrayList<>(VineryTrades.LEVEL_5);
    }
}
