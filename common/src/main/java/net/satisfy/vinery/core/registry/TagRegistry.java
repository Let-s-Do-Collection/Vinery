package net.satisfy.vinery.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.satisfy.vinery.core.Vinery;

public class TagRegistry {
    public static final TagKey<Block> CAN_NOT_CONNECT = TagKey.create(Registries.BLOCK, Vinery.identifier("can_not_connect"));
    public static final TagKey<Item> IGNORE_BLOCK_ITEM = TagKey.create(Registries.ITEM, Vinery.identifier("ignore_block_item"));
    public static final TagKey<Item> SMALL_BOTTLE = TagKey.create(Registries.ITEM, Vinery.identifier("small_bottle"));
    public static final TagKey<Item> LARGE_BOTTLE = TagKey.create(Registries.ITEM, Vinery.identifier("large_bottle"));
    public static final TagKey<Item> WHITE_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("white_grapejuice"));
    public static final TagKey<Item> WHITE_SAVANNA_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("white_savanna_grapejuice"));
    public static final TagKey<Item> WHITE_TAIGA_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("white_taiga_grapejuice"));
    public static final TagKey<Item> WHITE_JUNGLE_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("white_jungle_grapejuice"));
    public static final TagKey<Item> RED_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("red_grapejuice"));
    public static final TagKey<Item> RED_SAVANNA_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("red_savanna_grapejuice"));
    public static final TagKey<Item> RED_TAIGA_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("red_taiga_grapejuice"));
    public static final TagKey<Item> RED_JUNGLE_GRAPEJUICE = TagKey.create(Registries.ITEM, Vinery.identifier("red_jungle_grapejuice"));
    public static final TagKey<Item> GRAPE_SEEDS = TagKey.create(Registries.ITEM, Vinery.identifier("grape_seeds"));
}
