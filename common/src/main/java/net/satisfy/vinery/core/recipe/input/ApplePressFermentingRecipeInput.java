package net.satisfy.vinery.core.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record ApplePressFermentingRecipeInput(ItemStack input) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int i) {
        return input();
    }

    @Override
    public int size() {
        return 1;
    }
}
