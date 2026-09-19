package net.satisfy.vinery.neoforge.core.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.MobEffectRegistry;
import net.satisfy.vinery.core.util.VillagerUtil;
import net.satisfy.vinery.neoforge.core.config.VineryForgeConfig;
import net.satisfy.vinery.neoforge.core.registry.VineryNeoForgeVillagers;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@EventBusSubscriber(modid = Vinery.MOD_ID)
public class VineryForgeEventhandler {


    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType().equals(VineryNeoForgeVillagers.WINEMAKER.get())) {
            Map<Integer, List<VillagerTrades.ItemListing>> trades = new HashMap<>();

            loadTradesFromConfig(trades, VineryForgeConfig.level1TradesCache, 1);
            loadTradesFromConfig(trades, VineryForgeConfig.level2TradesCache, 2);
            loadTradesFromConfig(trades, VineryForgeConfig.level3TradesCache, 3);
            loadTradesFromConfig(trades, VineryForgeConfig.level4TradesCache, 4);
            loadTradesFromConfig(trades, VineryForgeConfig.level5TradesCache, 5);

            event.getTrades().clear();
            event.getTrades().putAll(trades);
        }
    }

    private static void loadTradesFromConfig(Map<Integer, List<VillagerTrades.ItemListing>> trades, List<? extends String> configList, int level) {
        List<VillagerTrades.ItemListing> tradeList = new ArrayList<>();
        for (String entry : configList) {
            String[] parts = entry.split("\\|");
            if (parts.length != 5) continue;

            String itemName = parts[0];
            int price = Integer.parseInt(parts[1]);
            int quantity = Integer.parseInt(parts[2]);
            int maxUses = Integer.parseInt(parts[3]);
            boolean isSelling = Boolean.parseBoolean(parts[4]);

            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemName));
            if (item != null && item != Items.AIR) {
                VillagerTrades.ItemListing listing;
                if (isSelling) {
                    listing = new VillagerUtil.SellItemFactory(item, price, quantity, maxUses);
                } else {
                    listing = new VillagerUtil.BuyForOneEmeraldFactory(item, price, quantity, maxUses);
                }
                tradeList.add(listing);
            }
        }
        trades.put(level, tradeList);
    }


}