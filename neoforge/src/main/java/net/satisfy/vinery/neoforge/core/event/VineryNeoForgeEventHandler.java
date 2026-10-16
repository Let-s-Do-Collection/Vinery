package net.satisfy.vinery.neoforge.core.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.util.VineryTrades;
import net.satisfy.vinery.neoforge.core.registry.VineryNeoForgeVillagers;
import net.satisfy.vinery.platform.PlatformHelper;

@EventBusSubscriber(modid = Vinery.MOD_ID)
public class VineryNeoForgeEventHandler {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType().equals(VineryNeoForgeVillagers.WINEMAKER.get())) {
            event.getTrades().clear();
            for (int level = 1; level <= 5; level++) {
                event.getTrades().put(level, VineryTrades.parse(PlatformHelper.getVillagerTrades(level)));
            }
        }
    }
}
