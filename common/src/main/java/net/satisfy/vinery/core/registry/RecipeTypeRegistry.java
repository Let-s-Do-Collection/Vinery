package net.satisfy.vinery.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.recipe.FermentationBarrelRecipe;

import java.util.function.Supplier;

public class RecipeTypeRegistry {
    private static final Registrar<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Vinery.MOD_ID, Registries.RECIPE_TYPE).getRegistrar();
    private static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Vinery.MOD_ID, Registries.RECIPE_SERIALIZER).getRegistrar();

    public static final RegistrySupplier<RecipeType<FermentationBarrelRecipe>> FERMENTATION_BARREL_RECIPE_TYPE = create("wine_fermentation");
    public static final RegistrySupplier<RecipeSerializer<FermentationBarrelRecipe>> FERMENTATION_BARREL_RECIPE_SERIALIZER = create("wine_fermentation", FermentationBarrelRecipe.Serializer::new);

    public static final RegistrySupplier<RecipeType<ApplePressMashingRecipe>> APPLE_PRESS_MASHING_RECIPE_TYPE = create("apple_mashing");
    public static final RegistrySupplier<RecipeSerializer<ApplePressMashingRecipe>> APPLE_PRESS_MASHING_RECIPE_SERIALIZER = create("apple_mashing", ApplePressMashingRecipe.Serializer::new);

    public static final RegistrySupplier<RecipeType<ApplePressFermentingRecipe>> APPLE_PRESS_FERMENTING_RECIPE_TYPE = create("apple_fermenting");
    public static final RegistrySupplier<RecipeSerializer<ApplePressFermentingRecipe>> APPLE_PRESS_FERMENTING_RECIPE_SERIALIZER = create("apple_fermenting", ApplePressFermentingRecipe.Serializer::new);

    private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> create(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RECIPE_SERIALIZERS.register(Vinery.identifier(name), serializer);
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> create(String name) {
        Supplier<RecipeType<T>> type = () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        };
        return RECIPE_TYPES.register(Vinery.identifier(name), type);
    }

    public static void init() {
    }
}
