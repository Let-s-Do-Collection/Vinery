package net.satisfy.vinery.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.core.Vinery;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Vinery.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> VINERY_TAB = CREATIVE_MODE_TABS.register("vinery", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ObjectRegistry.CHENET_WINE_ITEM.get()))
            .title(Component.translatable("creativetab.vinery.tab"))
            .displayItems((parameters, output) -> {
                acceptEssentials(output);
                acceptWines(output);
                acceptDarkCherry(output);
                acceptDecoration(output);
            })
            .build());

    public static void acceptEssentials(CreativeModeTab.Output output) {
        output.accept(ObjectRegistry.COARSE_DIRT_SLAB.get());
        output.accept(ObjectRegistry.DIRT_SLAB.get());
        output.accept(ObjectRegistry.GRASS_SLAB.get());
        output.accept(ObjectRegistry.DIRT_PATH_SLAB.get());
        output.accept(ObjectRegistry.WHITE_GRAPE_BAG.get());
        output.accept(ObjectRegistry.RED_GRAPE_BAG.get());
        output.accept(ObjectRegistry.APPLE_BAG.get());
        output.accept(ObjectRegistry.CHERRY_BAG.get());
        output.accept(ObjectRegistry.APPLE_LEAVES.get());
        output.accept(ObjectRegistry.DARK_CHERRY_LEAVES.get());
        output.accept(ObjectRegistry.GRAPEVINE_LEAVES.get());
        output.accept(ObjectRegistry.APPLE_TREE_SAPLING.get());
        output.accept(ObjectRegistry.DARK_CHERRY_SAPLING.get());
        output.accept(ObjectRegistry.RED_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.RED_GRAPE.get());
        output.accept(ObjectRegistry.WHITE_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.WHITE_GRAPE.get());
        output.accept(ObjectRegistry.SAVANNA_RED_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.SAVANNA_RED_GRAPE.get());
        output.accept(ObjectRegistry.SAVANNA_WHITE_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.SAVANNA_WHITE_GRAPE.get());
        output.accept(ObjectRegistry.TAIGA_RED_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.TAIGA_RED_GRAPE.get());
        output.accept(ObjectRegistry.TAIGA_WHITE_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.TAIGA_WHITE_GRAPE.get());
        output.accept(ObjectRegistry.JUNGLE_RED_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.JUNGLE_RED_GRAPE.get());
        output.accept(ObjectRegistry.JUNGLE_WHITE_GRAPE_SEEDS.get());
        output.accept(ObjectRegistry.JUNGLE_WHITE_GRAPE.get());
        output.accept(ObjectRegistry.CHERRY.get());
        output.accept(ObjectRegistry.ROTTEN_CHERRY.get());
        output.accept(ObjectRegistry.GRAPEVINE_POT.get());
        output.accept(ObjectRegistry.FERMENTATION_BARREL.get());
        output.accept(ObjectRegistry.APPLE_PRESS.get());
        output.accept(ObjectRegistry.SPRUCE_LATTICE.get());
        output.accept(ObjectRegistry.SPRUCE_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.SPRUCE_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.SPRUCE_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.GRAPEVINE_STEM.get());
        output.accept(ObjectRegistry.STRAW_HAT.get());
        output.accept(ObjectRegistry.WINEMAKER_APRON.get());
        output.accept(ObjectRegistry.WINEMAKER_LEGGINGS.get());
        output.accept(ObjectRegistry.WINEMAKER_BOOTS.get());
        output.accept(ObjectRegistry.STORAGE_POT.get());
        output.accept(ObjectRegistry.WINE_BOX.get());
        output.accept(ObjectRegistry.STACKABLE_LOG.get());
        output.accept(ObjectRegistry.VINERY_STANDARD.get());
        output.accept(ObjectRegistry.MULE_SPAWN_EGG.get());
        output.accept(ObjectRegistry.WANDERING_WINEMAKER_SPAWN_EGG.get());
    }

    public static void acceptWines(CreativeModeTab.Output output) {
        output.accept(ObjectRegistry.APPLE_MASH.get());
        output.accept(ObjectRegistry.RED_GRAPEJUICE.get());
        output.accept(ObjectRegistry.WHITE_GRAPEJUICE.get());
        output.accept(ObjectRegistry.RED_TAIGA_GRAPEJUICE.get());
        output.accept(ObjectRegistry.WHITE_TAIGA_GRAPEJUICE.get());
        output.accept(ObjectRegistry.RED_JUNGLE_GRAPEJUICE.get());
        output.accept(ObjectRegistry.WHITE_JUNGLE_GRAPEJUICE.get());
        output.accept(ObjectRegistry.RED_SAVANNA_GRAPEJUICE.get());
        output.accept(ObjectRegistry.WHITE_SAVANNA_GRAPEJUICE.get());
        output.accept(ObjectRegistry.APPLE_JUICE.get());
        output.accept(ObjectRegistry.MEAD.get());
        output.accept(ObjectRegistry.APPLE_CIDER.get());
        output.accept(ObjectRegistry.APPLE_WINE.get());
        output.accept(ObjectRegistry.MELLOHI_WINE.get());
        output.accept(ObjectRegistry.GLOWING_WINE.get());
        output.accept(ObjectRegistry.SOLARIS_WINE.get());
        output.accept(ObjectRegistry.NOIR_WINE.get());
        output.accept(ObjectRegistry.RED_WINE.get());
        output.accept(ObjectRegistry.STRAD_WINE.get());
        output.accept(ObjectRegistry.CHERRY_WINE.get());
        output.accept(ObjectRegistry.CRISTEL_WINE.get());
        output.accept(ObjectRegistry.CREEPERS_CRUSH.get());
        output.accept(ObjectRegistry.KELP_CIDER.get());
        output.accept(ObjectRegistry.LILITU_WINE.get());
        output.accept(ObjectRegistry.JO_SPECIAL_MIXTURE.get());
        output.accept(ObjectRegistry.EISWEIN.get());
        output.accept(ObjectRegistry.AEGIS_WINE.get());
        output.accept(ObjectRegistry.BOLVAR_WINE.get());
        output.accept(ObjectRegistry.CHORUS_WINE.get());
        output.accept(ObjectRegistry.VILLAGERS_FRIGHT.get());
        output.accept(ObjectRegistry.CLARK_WINE.get());
        output.accept(ObjectRegistry.MAGNETIC_WINE_ITEM.get());
        output.accept(ObjectRegistry.STAL_WINE.get());
        output.accept(ObjectRegistry.CHENET_WINE.get());
        output.accept(ObjectRegistry.BOTTLE_MOJANG_NOIR.get());
        output.accept(ObjectRegistry.JELLIE_WINE.get());
        output.accept(ObjectRegistry.WINE_BOTTLE.get());
    }

    public static void acceptDarkCherry(CreativeModeTab.Output output) {
        output.accept(ObjectRegistry.DARK_CHERRY_LOG.get());
        output.accept(ObjectRegistry.DARK_CHERRY_WOOD.get());
        output.accept(ObjectRegistry.STRIPPED_DARK_CHERRY_LOG.get());
        output.accept(ObjectRegistry.STRIPPED_DARK_CHERRY_WOOD.get());
        output.accept(ObjectRegistry.DARK_CHERRY_PLANKS.get());
        output.accept(ObjectRegistry.DARK_CHERRY_STAIRS.get());
        output.accept(ObjectRegistry.DARK_CHERRY_SLAB.get());
        output.accept(ObjectRegistry.DARK_CHERRY_FENCE.get());
        output.accept(ObjectRegistry.DARK_CHERRY_FENCE_GATE.get());
        output.accept(ObjectRegistry.DARK_CHERRY_DOOR.get());
        output.accept(ObjectRegistry.DARK_CHERRY_TRAPDOOR.get());
        output.accept(ObjectRegistry.DARK_CHERRY_PRESSURE_PLATE.get());
        output.accept(ObjectRegistry.DARK_CHERRY_BUTTON.get());
        output.accept(ObjectRegistry.DARK_CHERRY_BEAM.get());
        output.accept(ObjectRegistry.DARK_CHERRY_FLOORBOARD.get());
        output.accept(ObjectRegistry.DARK_CHERRY_SIGN_ITEM.get());
        output.accept(ObjectRegistry.DARK_CHERRY_HANGING_SIGN_ITEM.get());
        output.accept(ObjectRegistry.DARK_CHERRY_BOAT.get());
        output.accept(ObjectRegistry.DARK_CHERRY_CHEST_BOAT.get());
        output.accept(ObjectRegistry.DARK_CHERRY_TABLE.get());
        output.accept(ObjectRegistry.DARK_CHERRY_BIG_TABLE.get());
        output.accept(ObjectRegistry.DARK_CHERRY_CHAIR.get());
        output.accept(ObjectRegistry.DARK_CHERRY_SHELF.get());
        output.accept(ObjectRegistry.DARK_CHERRY_DRAWER.get());
        output.accept(ObjectRegistry.DARK_CHERRY_CABINET.get());
        output.accept(ObjectRegistry.DARK_CHERRY_BARREL.get());
        output.accept(ObjectRegistry.DARK_CHERRY_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.DARK_CHERRY_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.DARK_CHERRY_WINE_RACK_BIG.get());
    }

    public static void acceptDecoration(CreativeModeTab.Output output) {
        output.accept(ObjectRegistry.OAK_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.OAK_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.OAK_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.BIRCH_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.BIRCH_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.BIRCH_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.JUNGLE_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.JUNGLE_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.JUNGLE_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.ACACIA_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.ACACIA_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.ACACIA_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.DARK_OAK_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.DARK_OAK_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.DARK_OAK_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.MANGROVE_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.MANGROVE_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.MANGROVE_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.BAMBOO_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.BAMBOO_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.BAMBOO_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.CHERRY_WINE_RACK_SMALL.get());
        output.accept(ObjectRegistry.CHERRY_WINE_RACK_MID.get());
        output.accept(ObjectRegistry.CHERRY_WINE_RACK_BIG.get());
        output.accept(ObjectRegistry.OAK_LATTICE.get());
        output.accept(ObjectRegistry.BIRCH_LATTICE.get());
        output.accept(ObjectRegistry.JUNGLE_LATTICE.get());
        output.accept(ObjectRegistry.ACACIA_LATTICE.get());
        output.accept(ObjectRegistry.DARK_OAK_LATTICE.get());
        output.accept(ObjectRegistry.MANGROVE_LATTICE.get());
        output.accept(ObjectRegistry.BAMBOO_LATTICE.get());
        output.accept(ObjectRegistry.CHERRY_LATTICE.get());
        output.accept(ObjectRegistry.DARK_CHERRY_LATTICE.get());
        output.accept(ObjectRegistry.WINDOW.get());
        output.accept(ObjectRegistry.WINDOW_BLOCK.get());
    }

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
