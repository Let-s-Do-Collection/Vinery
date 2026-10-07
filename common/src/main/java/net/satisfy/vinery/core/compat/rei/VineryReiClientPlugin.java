package net.satisfy.vinery.core.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.satisfy.vinery.core.compat.rei.press.ApplePressCategory;
import net.satisfy.vinery.core.compat.rei.press.ApplePressDisplay;
import net.satisfy.vinery.core.compat.rei.wine.FermentationBarrelCategory;
import net.satisfy.vinery.core.compat.rei.wine.FermentationBarrelDisplay;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.recipe.FermentationBarrelRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;

import java.util.ArrayList;
import java.util.List;

public class VineryReiClientPlugin {
    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new FermentationBarrelCategory());
        registry.add(new ApplePressCategory(ApplePressDisplay.MASHING));
        registry.add(new ApplePressCategory(ApplePressDisplay.FERMENTING));
        registry.addWorkstations(FermentationBarrelDisplay.FERMENTATION_BARREL_DISPLAY, EntryStacks.of(ObjectRegistry.FERMENTATION_BARREL.get()));
        registry.addWorkstations(ApplePressDisplay.MASHING, EntryStacks.of(ObjectRegistry.APPLE_PRESS.get()));
        registry.addWorkstations(ApplePressDisplay.FERMENTING, EntryStacks.of(ObjectRegistry.APPLE_PRESS.get()));
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FermentationBarrelRecipe.class,FermentationBarrelRecipe.Type ,FermentationBarrelDisplay::new);
        registry.registerRecipeFiller(ApplePressMashingRecipe.class, ApplePressMashingRecipe.Type ,ApplePressDisplay::mashing);
        registry.registerRecipeFiller(ApplePressFermentingRecipe.class, ApplePressFermentingRecipe.Type , ApplePressDisplay::fermenting);
    }

    public static List<Ingredient> ingredients(Recipe<RecipeInput> recipe, ItemStack stack){
        List<Ingredient> l = new ArrayList<>(recipe.getIngredients());
        l.add(0, Ingredient.of(stack.getItem()));
        return l;
    }

}
