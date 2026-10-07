package net.satisfy.vinery.core.world.feature;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.satisfy.vinery.core.Vinery;

import java.util.function.Supplier;

public class VineryFeatures {
    private static final Registrar<Feature<?>> FEATURES = DeferredRegister.create(Vinery.MOD_ID, Registries.FEATURE).getRegistrar();
    public static final RegistrySupplier<Feature<BlockStateConfiguration>> JUNGLE_GRAPE_FEATURE = register("jungle_grape_feature", () -> new JungleGrapeFeature(BlockStateConfiguration.CODEC));

    public static void init(){
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistrySupplier<F> register(String name, Supplier<F> feature) {
        return FEATURES.register(Vinery.identifier(name), feature);
    }

}
