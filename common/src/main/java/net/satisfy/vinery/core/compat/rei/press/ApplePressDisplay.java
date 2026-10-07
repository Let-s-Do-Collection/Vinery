package net.satisfy.vinery.core.compat.rei.press;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;

import java.util.Collections;

public class ApplePressDisplay extends BasicDisplay {
    public static final CategoryIdentifier<ApplePressDisplay> MASHING = CategoryIdentifier.of(Vinery.MOD_ID, "apple_press_display");
    public static final CategoryIdentifier<ApplePressDisplay> FERMENTING = CategoryIdentifier.of(Vinery.MOD_ID, "apple_press_fermenting_display");

    private final CategoryIdentifier<ApplePressDisplay> category;

    private ApplePressDisplay(CategoryIdentifier<ApplePressDisplay> category, Ingredient input, ItemStack output) {
        super(Collections.singletonList(EntryIngredients.ofIngredient(input)), Collections.singletonList(EntryIngredients.of(output)));
        this.category = category;
    }

    public static ApplePressDisplay mashing(RecipeHolder<ApplePressMashingRecipe> recipe) {
        return new ApplePressDisplay(MASHING, recipe.value().input, recipe.value().getResultItem(null));
    }

    public static ApplePressDisplay fermenting(RecipeHolder<ApplePressFermentingRecipe> recipe) {
        return new ApplePressDisplay(FERMENTING, recipe.value().input, recipe.value().getResultItem(null));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return category;
    }
}
