package net.satisfy.vinery.core;

import dev.architectury.hooks.item.tool.AxeItemHooks;
import dev.architectury.hooks.item.tool.ShovelItemHooks;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.satisfy.vinery.core.command.WineDebugCommands;
import net.satisfy.vinery.core.event.EventHandler;
import net.satisfy.vinery.core.registry.*;
import net.satisfy.vinery.core.util.WineEffectSetup;
import net.satisfy.vinery.core.world.feature.VineryFeatures;

public class Vinery {
    public static final String MOD_ID = "vinery";

    public static void init() {
        MobEffectRegistry.register();
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        ScreenhandlerTypeRegistry.init();
        RecipeTypesRegistry.init();
        VineryFeatures.init();
        SoundEventRegistry.init();
        EventHandler.init();
        TabRegistry.init();
        WineDebugCommands.init();
        DataComponentRegistry.COMPONENTS.register();
        ArmorMaterialRegistry.ARMOR_MATERIALS.register();
        ArmorSetRegistry.init();
    }

    public static ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void commonSetup() {
        FlammableBlockRegistry.init();
        GrapeTypeRegistry.addGrapeAttributes();
        WineEffectSetup.setupWineEffects();
        FuelRegistry.register(1000, ObjectRegistry.DARK_CHERRY_FENCE.get(), ObjectRegistry.DARK_CHERRY_FENCE_GATE.get(), ObjectRegistry.STACKABLE_LOG.get(), ObjectRegistry.FERMENTATION_BARREL.get());

        AxeItemHooks.addStrippable(ObjectRegistry.DARK_CHERRY_LOG.get(), ObjectRegistry.STRIPPED_DARK_CHERRY_LOG.get());
        AxeItemHooks.addStrippable(ObjectRegistry.DARK_CHERRY_WOOD.get(), ObjectRegistry.STRIPPED_DARK_CHERRY_WOOD.get());
        AxeItemHooks.addStrippable(ObjectRegistry.APPLE_LOG.get(), Blocks.STRIPPED_OAK_LOG);
        AxeItemHooks.addStrippable(ObjectRegistry.APPLE_WOOD.get(), Blocks.STRIPPED_OAK_WOOD);
        
        ShovelItemHooks.addFlattenable(ObjectRegistry.GRASS_SLAB.get(), Blocks.DIRT_PATH.defaultBlockState());
    }
}
