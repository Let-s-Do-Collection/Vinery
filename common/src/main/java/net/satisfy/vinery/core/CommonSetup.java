package net.satisfy.vinery.core;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.satisfy.foundation.rarity.FoundationRarities;
import net.satisfy.foundation.rarity.FoundationRarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;

import java.util.List;

import static net.satisfy.vinery.core.registry.ObjectRegistry.*;

public final class CommonSetup {
    private CommonSetup() {
    }

    public static void init() {
        registerFlammables();
        registerCompostables();
        registerRarities();
    }

    private static void registerRarities() {
        FoundationRarities.register(VINERY_STANDARD.get(), FoundationRarity.LEGENDARY);
        for (RegistrySupplier<Item> piece : List.of(STRAW_HAT, WINEMAKER_APRON, WINEMAKER_LEGGINGS, WINEMAKER_BOOTS)) {
            FoundationRarities.register(piece.get(), FoundationRarity.RARE);
        }
    }

    private static void registerFlammables() {
        addFlammable(5, 20, DARK_CHERRY_PLANKS.get(), DARK_CHERRY_SLAB.get(), DARK_CHERRY_STAIRS.get(), DARK_CHERRY_FENCE.get(),
                DARK_CHERRY_FENCE_GATE.get());
        addFlammable(5, 5, STRIPPED_DARK_CHERRY_LOG.get(), DARK_CHERRY_LOG.get(), APPLE_LOG.get(),
                STRIPPED_DARK_CHERRY_WOOD.get(), DARK_CHERRY_WOOD.get(), APPLE_WOOD.get());
        addFlammable(30, 60, DARK_CHERRY_LEAVES.get(), GRAPEVINE_LEAVES.get(), APPLE_LEAVES.get());
    }

    private static void addFlammable(int burnOdd, int igniteOdd, Block... blocks) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        for (Block block : blocks) {
            fireBlock.setFlammable(block, burnOdd, igniteOdd);
        }
    }

    private static void registerCompostables() {
        addCompostable(0.4F,
                WHITE_GRAPE,
                WHITE_GRAPE_SEEDS,
                RED_GRAPE,
                RED_GRAPE_SEEDS,
                DARK_CHERRY_LEAVES,
                GRAPEVINE_LEAVES,
                CHERRY,
                ROTTEN_CHERRY,
                APPLE_TREE_SAPLING,
                APPLE_LEAVES,
                DARK_CHERRY_SAPLING,
                APPLE_MASH,
                STRAW_HAT,
                JUNGLE_RED_GRAPE_SEEDS,
                JUNGLE_RED_GRAPE,
                JUNGLE_WHITE_GRAPE_SEEDS,
                JUNGLE_WHITE_GRAPE,
                TAIGA_RED_GRAPE_SEEDS,
                TAIGA_RED_GRAPE,
                TAIGA_WHITE_GRAPE_SEEDS,
                TAIGA_WHITE_GRAPE,
                SAVANNA_RED_GRAPE_SEEDS,
                SAVANNA_RED_GRAPE,
                SAVANNA_WHITE_GRAPE_SEEDS,
                SAVANNA_WHITE_GRAPE);
    }

    @SafeVarargs
    private static void addCompostable(float chance, RegistrySupplier<? extends ItemLike>... items) {
        for (RegistrySupplier<? extends ItemLike> item : items) {
            ComposterBlock.COMPOSTABLES.put(item.get().asItem(), chance);
        }
    }
}
