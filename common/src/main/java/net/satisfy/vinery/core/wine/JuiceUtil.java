package net.satisfy.vinery.core.wine;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.TagRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class JuiceUtil {
    private static final Map<String, TagKey<Item>> JUICE_TAGS = new LinkedHashMap<>();
    private static final Map<String, Supplier<Item>> JUICE_ITEMS = new LinkedHashMap<>();

    private static final List<String> TYPE_IDS = new ArrayList<>();

    static {
        registerJuice("white_general", TagRegistry.WHITE_GRAPEJUICE);
        registerJuice("red_general", TagRegistry.RED_GRAPEJUICE);
        registerJuice("white_savanna", TagRegistry.WHITE_SAVANNA_GRAPEJUICE);
        registerJuice("red_savanna", TagRegistry.RED_SAVANNA_GRAPEJUICE);
        registerJuice("white_taiga", TagRegistry.WHITE_TAIGA_GRAPEJUICE);
        registerJuice("red_taiga", TagRegistry.RED_TAIGA_GRAPEJUICE);
        registerJuice("white_jungle", TagRegistry.WHITE_JUNGLE_GRAPEJUICE);
        registerJuice("red_jungle", TagRegistry.RED_JUNGLE_GRAPEJUICE);
        registerJuice("apple", ObjectRegistry.APPLE_JUICE);
    }

    public static void registerJuice(String type, TagKey<Item> tag) {
        addType(type);
        JUICE_TAGS.put(type, tag);
    }

    public static void registerJuice(String type, Supplier<Item> item) {
        addType(type);
        JUICE_ITEMS.put(type, item);
    }

    private static void addType(String type) {
        if (TYPE_IDS.contains(type)) {
            throw new IllegalArgumentException("Juice type '" + type + "' is already registered");
        }
        TYPE_IDS.add(type);
    }

    public static boolean isJuice(ItemStack stack) {
        return !getJuiceType(stack).isEmpty();
    }

    public static String getJuiceType(ItemStack stack) {
        if (stack.isEmpty()) {
            return "";
        }
        for (Map.Entry<String, TagKey<Item>> entry : JUICE_TAGS.entrySet()) {
            if (stack.is(entry.getValue())) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, Supplier<Item>> entry : JUICE_ITEMS.entrySet()) {
            if (stack.is(entry.getValue().get())) {
                return entry.getKey();
            }
        }
        return "";
    }

    public static int color(String type) {
        if (type.startsWith("red")) return 0x7A2E8C;
        if (type.equals("apple")) return 0xD8B84C;
        return 0xC9CF62;
    }

    public static List<String> types() {
        return List.copyOf(TYPE_IDS);
    }

    public static int typeId(String type) {
        return TYPE_IDS.indexOf(type);
    }

    public static String typeFromId(int id) {
        return id >= 0 && id < TYPE_IDS.size() ? TYPE_IDS.get(id) : "";
    }

    public static Item juiceItem(String type) {
        Supplier<Item> item = JUICE_ITEMS.get(type);
        if (item != null) {
            return item.get();
        }
        TagKey<Item> tag = JUICE_TAGS.get(type);
        if (tag != null) {
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                return holder.value();
            }
        }
        return Items.AIR;
    }
}
