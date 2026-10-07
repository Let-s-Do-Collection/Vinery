package net.satisfy.vinery.core.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.vinery.core.registry.ObjectRegistry;

public class ApplePressEmiRecipe extends BasicEmiRecipe {
    private final boolean requiresBottle;

    public ApplePressEmiRecipe(EmiRecipeCategory category, ResourceLocation id, Ingredient input, ItemStack output, boolean requiresBottle) {
        super(category, id, 82, 26);
        this.requiresBottle = requiresBottle;
        this.inputs.add(EmiIngredient.of(input));
        if (requiresBottle) this.inputs.add(EmiStack.of(ObjectRegistry.WINE_BOTTLE.get()));
        this.outputs.add(EmiStack.of(output));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 0, 4);
        if (requiresBottle) widgets.addSlot(inputs.get(1), 18, 4);
        widgets.addFillingArrow(requiresBottle ? 38 : 24, 5, 3600);
        widgets.addSlot(outputs.get(0), 60, 0).large(true).recipeContext(this);
    }
}
