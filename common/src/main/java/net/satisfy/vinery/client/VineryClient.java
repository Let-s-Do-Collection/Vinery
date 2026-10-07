package net.satisfy.vinery.client;

import net.satisfy.foundation.client.armor.ArmorModels;
import net.satisfy.foundation.client.creative.CreativeSideTabs;
import net.satisfy.foundation.overlay.BlockInfoOverlay;
import net.satisfy.vinery.client.event.DoubleJumpHandler;
import net.satisfy.vinery.client.gui.overlay.*;
import net.satisfy.vinery.client.render.block.GrapevinePotRenderer;
import net.satisfy.vinery.client.render.block.StoragePotRenderer;
import net.satisfy.vinery.client.render.block.StackableLogRenderer;
import net.minecraft.network.chat.Component;
import net.satisfy.vinery.core.registry.TabRegistry;
import net.satisfy.foundation.client.wood.WoodBoatRenderer;
import net.satisfy.foundation.client.wood.WoodClient;
import net.satisfy.foundation.storage.WallShelfRenderer;
import net.satisfy.vinery.core.block.state.properties.VineryWoodType;
import net.satisfy.foundation.storage.StorageBlockEntityRenderer;
import net.satisfy.foundation.storage.StorageTypeRenderer;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GrassColor;
import net.satisfy.vinery.client.gui.ApplePressGui;
import net.satisfy.vinery.client.gui.FermentationBarrelGui;
import net.satisfy.vinery.client.model.*;
import net.satisfy.foundation.banner.CompletionistBannerRenderer;
import net.satisfy.vinery.client.render.block.LatticeRenderer;
import net.satisfy.vinery.client.render.block.storage.*;
import net.satisfy.vinery.core.block.BottleStorageBlock;
import net.satisfy.vinery.client.render.entity.MuleRenderer;
import net.satisfy.vinery.client.render.entity.WanderingWinemakerRenderer;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.core.registry.MenuTypeRegistry;
import net.satisfy.vinery.core.registry.StorageTypeRegistry;

import static net.satisfy.vinery.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class VineryClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(),
                RED_GRAPE_BUSH.get(), WHITE_GRAPE_BUSH.get(), DARK_CHERRY_DOOR.get(), FERMENTATION_BARREL.get(),
                MELLOHI_WINE.get(), CLARK_WINE.get(), BOLVAR_WINE.get(), CHERRY_WINE.get(),
                LILITU_WINE.get(), CHENET_WINE.get(), NOIR_WINE.get(), APPLE_CIDER.get(),
                APPLE_WINE.get(), SOLARIS_WINE.get(), JELLIE_WINE.get(), AEGIS_WINE.get(), KELP_CIDER.get(),
                SAVANNA_RED_GRAPE_BUSH.get(), SAVANNA_WHITE_GRAPE_BUSH.get(),
                CHORUS_WINE.get(), STAL_WINE.get(), MAGNETIC_WINE.get(), STRAD_WINE.get(), JUNGLE_WHITE_GRAPE_BUSH.get(),
                JUNGLE_RED_GRAPE_BUSH.get(), TAIGA_RED_GRAPE_BUSH.get(), TAIGA_WHITE_GRAPE_BUSH.get(),
                GRAPEVINE_STEM.get(), WINE_BOX.get(), DARK_CHERRY_WINE_RACK_MID.get(), DARK_CHERRY_WINE_RACK_BIG.get(),
                APPLE_PRESS.get(), GRASS_SLAB.get(), DARK_CHERRY_SAPLING.get(), APPLE_TREE_SAPLING.get(),
                STACKABLE_LOG.get(), APPLE_LEAVES.get(), POTTED_APPLE_TREE_SAPLING.get(), DARK_CHERRY_WINE_RACK_SMALL.get(),
                POTTED_DARK_CHERRY_TREE_SAPLING.get(), RED_WINE.get(), DARK_CHERRY_CHAIR.get(), CRISTEL_WINE.get(),
                VILLAGERS_FRIGHT.get(), EISWEIN.get(), CREEPERS_CRUSH.get(),
                GLOWING_WINE.get(), JO_SPECIAL_MIXTURE.get(), MEAD.get(), BOTTLE_MOJANG_NOIR.get(),
                DARK_CHERRY_TABLE.get(), OAK_WINE_RACK_MID.get(), DARK_OAK_WINE_RACK_MID.get(), BIRCH_WINE_RACK_MID.get(),
                SPRUCE_WINE_RACK_MID.get(), JUNGLE_WINE_RACK_MID.get(), MANGROVE_WINE_RACK_MID.get(), BAMBOO_WINE_RACK_MID.get(),
                ACACIA_WINE_RACK_MID.get(), OAK_LATTICE.get(), SPRUCE_LATTICE.get(),
                BIRCH_LATTICE.get(), DARK_OAK_LATTICE.get(), CHERRY_LATTICE.get(), BAMBOO_LATTICE.get(), ACACIA_LATTICE.get(), JUNGLE_LATTICE.get(),
                MANGROVE_LATTICE.get(), DARK_CHERRY_LATTICE.get(), CHERRY_WINE_RACK_MID.get()
        );

        RenderTypeRegistry.register(RenderType.translucent(), WINDOW.get(), WINDOW_BLOCK.get());

        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> GrassColor.get(0.5, 1.0), GRASS_SLAB);
        ColorHandlerRegistry.registerBlockColors((state, level, pos, tintIndex) -> {
                    if (level == null || pos == null) {
                        return -1;
                    }
                    return BiomeColors.getAverageGrassColor(level, pos);
                }, GRASS_SLAB.get()
        );
        ColorHandlerRegistry.registerBlockColors((state, level, pos, tintIndex) -> {
            if (level == null || pos == null) {
                return -1;
            }
            return BiomeColors.getAverageFoliageColor(level, pos);
        }, JUNGLE_RED_GRAPE_BUSH.get(), JUNGLE_WHITE_GRAPE_BUSH.get());

        registerStorageType();
        registerScreenFactory();
        registerBlockEntityRenderer();
        registerArmorModels();
        DoubleJumpHandler.init();
        BlockInfoOverlay.init();
        BlockInfoOverlay.registerProvider(new GrapevinePotInfoProvider());
        BlockInfoOverlay.registerProvider(new StorageSlotInfoProvider());
        CreativeSideTabs.register(TabRegistry.VINERY_TAB.getKey(),
                CreativeSideTabs.SideTab.of(Component.translatable("creativetab.vinery.side.essentials"), RED_GRAPE.get(), TabRegistry::acceptEssentials),
                CreativeSideTabs.SideTab.of(Component.translatable("creativetab.vinery.side.wines"), CHENET_WINE_ITEM.get(), TabRegistry::acceptWines),
                CreativeSideTabs.SideTab.of(Component.translatable("creativetab.vinery.side.dark_cherry"), DARK_CHERRY_LOG.get(), TabRegistry::acceptDarkCherry),
                CreativeSideTabs.SideTab.of(Component.translatable("creativetab.vinery.side.decoration"), OAK_WINE_RACK_BIG.get(), TabRegistry::acceptDecoration));
    }

    public static void preInitClient() {
        registerEntityModelLayer();
        registerEntityRenderers();
    }

    public static void registerStorageTypes(ResourceLocation location, StorageTypeRenderer renderer){
        StorageBlockEntityRenderer.registerStorageType(location, renderer);
    }

    public static void registerStorageType(){
        registerStorageTypes(StorageTypeRegistry.BIG_BOTTLE, new BigBottleRenderer());
        registerStorageTypes(StorageTypeRegistry.FOUR_BOTTLE, new BottleRenderer(BottleStorageBlock.Layout.FOUR));
        registerStorageTypes(StorageTypeRegistry.NINE_BOTTLE, new BottleRenderer(BottleStorageBlock.Layout.NINE));
        registerStorageTypes(StorageTypeRegistry.SHELF, new WallShelfRenderer());
        registerStorageTypes(StorageTypeRegistry.WINE_BOX, new WineBoxRenderer());
        registerStorageTypes(StorageTypeRegistry.WINE_BOTTLE, new WineBottleRenderer());
    }

    public static void registerScreenFactory() {
        MenuRegistry.registerScreenFactory(MenuTypeRegistry.FERMENTATION_BARREL_MENU.get(), FermentationBarrelGui::new);
        MenuRegistry.registerScreenFactory(MenuTypeRegistry.APPLE_PRESS_MENU.get(), ApplePressGui::new);
    }

    public static void registerBlockEntityRenderer() {
        BlockEntityRendererRegistry.register(EntityTypeRegistry.VINERY_STANDARD.get(), CompletionistBannerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.STORAGE_ENTITY.get(), context -> new StorageBlockEntityRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.LATTICE.get(), LatticeRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.GRAPEVINE_POT.get(), GrapevinePotRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.STORAGE_POT_ENTITY.get(), StoragePotRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.STACKABLE_LOG.get(), StackableLogRenderer::new);
        WoodClient.registerSignMaterials(VineryWoodType.DARK_CHERRY);
        WoodClient.registerSignRenderers(EntityTypeRegistry.MOD_SIGN.get(), EntityTypeRegistry.MOD_HANGING_SIGN.get());

    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(MuleModel.LAYER_LOCATION, MuleModel::getTexturedModelData);
        EntityModelLayerRegistry.register(StrawHatModel.LAYER_LOCATION, StrawHatModel::createBodyLayer);
        EntityModelLayerRegistry.register(WinemakerChestplateModel.LAYER_LOCATION, WinemakerChestplateModel::createBodyLayer);
        EntityModelLayerRegistry.register(WinemakerLeggingsModel.LAYER_LOCATION, WinemakerLeggingsModel::createBodyLayer);
        EntityModelLayerRegistry.register(WinemakerBootsModel.LAYER_LOCATION, WinemakerBootsModel::createBodyLayer);
        EntityModelLayerRegistry.register(LatticeRenderer.LAYER_LOCATION, LatticeRenderer::getTexturedModelData);
        WoodClient.registerBoatLayers(VineryWoodType.DARK_CHERRY_BOAT);
    }

    public static void registerArmorModels() {
        ArmorModels.register(StrawHatModel.LAYER_LOCATION, StrawHatModel::new, STRAW_HAT.get());
        ArmorModels.register(WinemakerChestplateModel.LAYER_LOCATION, WinemakerChestplateModel::new, WINEMAKER_APRON.get());
        ArmorModels.register(WinemakerLeggingsModel.LAYER_LOCATION, WinemakerLeggingsModel::new, WINEMAKER_LEGGINGS.get());
        ArmorModels.register(WinemakerBootsModel.LAYER_LOCATION, WinemakerBootsModel::new, WINEMAKER_BOOTS.get());
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.MULE, MuleRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.WANDERING_WINEMAKER, WanderingWinemakerRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DARK_CHERRY_BOAT, context -> new WoodBoatRenderer(context, false));
        EntityRendererRegistry.register(EntityTypeRegistry.DARK_CHERRY_CHEST_BOAT, context -> new WoodBoatRenderer(context, true));
    }
}