package net.satisfy.vinery.fabric.core.world;

import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.world.VineryWorldGen;

import java.util.function.Predicate;

public class VineryBiomeModification {
    public static void init() {
        BiomeModification world = BiomeModifications.create(Vinery.identifier("world_features"));
        Predicate<BiomeSelectionContext> plainsBiomes = getVinerySelector("spawns_grape");
        Predicate<BiomeSelectionContext> savannaBiomes = getVinerySelector("spawns_savanna_grape");
        Predicate<BiomeSelectionContext> taigaBiomes = getVinerySelector("spawns_taiga_grape");
        Predicate<BiomeSelectionContext> jungleBiomes = getVinerySelector("spawns_jungle_grape");

        Predicate<BiomeSelectionContext> treeBiomes = getVinerySelector("spawns_cherry_tree");

        world.add(ModificationPhase.ADDITIONS, plainsBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.RED_GRAPE_PATCH_CHANCE_KEY));
        world.add(ModificationPhase.ADDITIONS, plainsBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.WHITE_GRAPE_PATCH_CHANCE_KEY));
        world.add(ModificationPhase.ADDITIONS, savannaBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.SAVANNA_RED_GRAPE_PATCH_CHANCE_KEY));
        world.add(ModificationPhase.ADDITIONS, savannaBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.SAVANNA_WHITE_GRAPE_PATCH_CHANCE_KEY));
        world.add(ModificationPhase.ADDITIONS, taigaBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.TAIGA_RED_GRAPE_PATCH_CHANCE_KEY));
        world.add(ModificationPhase.ADDITIONS, taigaBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.TAIGA_WHITE_GRAPE_PATCH_CHANCE_KEY));

        world.add(ModificationPhase.ADDITIONS, jungleBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.JUNGLE_RED_GRAPE_PATCH_CHANCE_KEY));
        world.add(ModificationPhase.ADDITIONS, jungleBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.JUNGLE_WHITE_GRAPE_PATCH_CHANCE_KEY));

        world.add(ModificationPhase.ADDITIONS, treeBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.TREE_CHERRY_PLACED_KEY));
        world.add(ModificationPhase.ADDITIONS, treeBiomes, ctx -> ctx.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VineryWorldGen.TREE_APPLE_PLACED_KEY));
    }

    private static Predicate<BiomeSelectionContext> getVinerySelector(String path) {
        return BiomeSelectors.tag(TagKey.create(Registries.BIOME, Vinery.identifier(path)));
    }

}
