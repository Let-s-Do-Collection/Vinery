package net.satisfy.vinery.fabric.core.registry;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.util.VineryTrades;
import net.satisfy.vinery.platform.PlatformHelper;

public class VineryFabricVillagers {
    private static final ResourceLocation WINEMAKER_POI_IDENTIFIER = Vinery.identifier("winemaker_poi");
    public static final PoiType WINEMAKER_POI;
    public static final VillagerProfession WINEMAKER;

    static {
        WINEMAKER_POI = PointOfInterestHelper.register(
                WINEMAKER_POI_IDENTIFIER, 1, 12, ObjectRegistry.FERMENTATION_BARREL.get()
        );

        WINEMAKER = Registry.register(
                BuiltInRegistries.VILLAGER_PROFESSION, ResourceLocation.fromNamespaceAndPath("vinery", "winemaker"), VillagerProfessionBuilder.create().id(ResourceLocation.fromNamespaceAndPath("vinery", "winemaker")).workstation(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, WINEMAKER_POI_IDENTIFIER)).build()
        );
    }

    public static void registerPOIAndProfession() {
    }

    public static void init(MinecraftServer server) {
        for (int level = 1; level <= 5; level++) {
            int tradeLevel = level;
            TradeOfferHelper.registerVillagerOffers(WINEMAKER, tradeLevel, factories -> factories.addAll(VineryTrades.parse(PlatformHelper.getVillagerTrades(tradeLevel))));
        }
    }
}
