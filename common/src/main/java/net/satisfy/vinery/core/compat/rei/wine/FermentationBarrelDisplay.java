package net.satisfy.vinery.core.compat.rei.wine;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.recipe.FermentationBarrelRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FermentationBarrelDisplay extends BasicDisplay {
    public static final CategoryIdentifier<FermentationBarrelDisplay> FERMENTATION_BARREL_DISPLAY =
            CategoryIdentifier.of(Vinery.MOD_ID, "fermentation_barrel_display");

    private final int juiceAmount;
    private final String juiceType;

    public FermentationBarrelDisplay(RecipeHolder<FermentationBarrelRecipe> recipe) {
        this(prepareInputs(recipe.value()), prepareOutputs(recipe.value()), recipe.value().getJuiceData().amount(), recipe.value().getJuiceData().type());
    }

    public FermentationBarrelDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int juiceAmount, String juiceType) {
        super(inputs, outputs);
        this.juiceAmount = juiceAmount;
        this.juiceType = juiceType;
    }

    public int getJuiceAmount() {
        return juiceAmount;
    }

    public String getJuiceType() {
        return juiceType;
    }

    private static List<EntryIngredient> prepareInputs(FermentationBarrelRecipe recipe) {
        List<EntryIngredient> ingredients = new ArrayList<>(EntryIngredients.ofIngredients(recipe.getIngredients()));
        ingredients.add(EntryIngredients.of(new ItemStack(ObjectRegistry.WINE_BOTTLE.get())));
        return ingredients;
    }

    private static List<EntryIngredient> prepareOutputs(FermentationBarrelRecipe recipe) {
        return Collections.singletonList(EntryIngredients.of(recipe.getResultItem(null)));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FERMENTATION_BARREL_DISPLAY;
    }
}
