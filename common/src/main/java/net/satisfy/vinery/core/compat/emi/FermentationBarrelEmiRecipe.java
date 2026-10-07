package net.satisfy.vinery.core.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.vinery.core.recipe.FermentationBarrelRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.wine.JuiceUtil;

public class FermentationBarrelEmiRecipe extends BasicEmiRecipe {
    private final EmiStack juice;
    private final int juiceAmount;
    private final int ingredientCount;

    public FermentationBarrelEmiRecipe(ResourceLocation id, FermentationBarrelRecipe recipe) {
        super(VineryEmiPlugin.FERMENTATION_CATEGORY, id, 126, 44);
        recipe.getIngredients().forEach(ingredient -> this.inputs.add(EmiIngredient.of(ingredient)));
        this.ingredientCount = this.inputs.size();
        if (recipe.isWineBottleRequired()) this.inputs.add(EmiStack.of(ObjectRegistry.WINE_BOTTLE.get()));
        this.juiceAmount = recipe.getJuiceData().amount();
        this.juice = juiceAmount > 0 ? EmiStack.of(JuiceUtil.juiceItem(recipe.getJuiceData().type())) : EmiStack.EMPTY;
        if (!juice.isEmpty()) this.catalysts.add(juice);
        this.outputs.add(EmiStack.of(recipe.getResultItem(null)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        if (!juice.isEmpty()) {
            widgets.addSlot(juice, 0, 0).drawBack(false);
            widgets.addText(Component.translatable("emi.vinery.juice_amount", juiceAmount), 20, 5, 0x404040, false);
        }
        for (int i = 0; i < inputs.size(); i++) {
            widgets.addSlot(inputs.get(i), i * 18, 22);
        }
        widgets.addFillingArrow(Math.max(inputs.size(), 3) * 18 + 4, 23, 2500);
        widgets.addSlot(outputs.get(0), 100, 18).large(true).recipeContext(this);
    }
}
