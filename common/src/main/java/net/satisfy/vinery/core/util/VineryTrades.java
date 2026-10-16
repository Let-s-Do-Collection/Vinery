package net.satisfy.vinery.core.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.satisfy.foundation.trade.BuyForOneEmeraldFactory;
import net.satisfy.foundation.trade.SellItemFactory;

import java.util.ArrayList;
import java.util.List;

public final class VineryTrades {
    public static final List<String> LEVEL_1 = List.of(
            "vinery:red_grape|buy|15|1|4|5",
            "vinery:white_grape|buy|15|1|4|5",
            "vinery:red_grape_seeds|sell|2|1|1|5",
            "vinery:white_grape_seeds|sell|2|1|1|5"
    );
    public static final List<String> LEVEL_2 = List.of(
            "vinery:wine_bottle|sell|1|1|4|7",
            "vinery:cherry|buy|12|1|4|6",
            "vinery:apple_mash|sell|1|1|4|6"
    );
    public static final List<String> LEVEL_3 = List.of(
            "vinery:white_grape_bag|sell|7|1|2|10",
            "vinery:red_grape_bag|sell|7|1|2|10",
            "vinery:cherry_bag|sell|7|1|2|10",
            "vinery:apple_bag|sell|7|1|2|10"
    );
    public static final List<String> LEVEL_4 = List.of(
            "vinery:window|sell|12|1|2|10",
            "vinery:dark_cherry_beam|sell|6|4|2|10",
            "vinery:grapevine_pot|sell|6|1|2|10",
            "vinery:taiga_grape_seeds_red|sell|2|1|2|5",
            "vinery:taiga_grape_seeds_white|sell|2|1|2|5"
    );
    public static final List<String> LEVEL_5 = List.of(
            "vinery:wine_box|sell|10|1|2|10",
            "vinery:lilitu_wine|sell|4|1|2|10",
            "vinery:winemaker_apron|sell|18|1|1|15",
            "vinery:straw_hat|sell|12|1|1|12"
    );

    private VineryTrades() {
    }

    public static boolean isValid(Object entry) {
        if (!(entry instanceof String string)) {
            return false;
        }
        String[] parts = string.split("\\|");
        if (parts.length != 6 || ResourceLocation.tryParse(parts[0]) == null || !(parts[1].equals("buy") || parts[1].equals("sell"))) {
            return false;
        }
        try {
            for (int i = 2; i < 6; i++) {
                Integer.parseInt(parts[i]);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static List<VillagerTrades.ItemListing> parse(List<? extends String> entries) {
        List<VillagerTrades.ItemListing> listings = new ArrayList<>();
        for (String entry : entries) {
            if (!isValid(entry)) {
                continue;
            }
            String[] parts = entry.split("\\|");
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(parts[0]));
            int price = Integer.parseInt(parts[2]);
            int count = Integer.parseInt(parts[3]);
            int maxUses = Integer.parseInt(parts[4]);
            int experience = Integer.parseInt(parts[5]);
            if (item == Items.AIR || price < 1 || count < 1 || maxUses < 1) {
                continue;
            }
            listings.add(parts[1].equals("buy")
                    ? new BuyForOneEmeraldFactory(item, price, maxUses, experience)
                    : new SellItemFactory(item, price, count, maxUses, experience));
        }
        return listings;
    }
}
