package net.satisfy.vinery.core.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.RecipeTypeRegistry;

@EmiEntrypoint
public class VineryEmiPlugin implements EmiPlugin {
    public static final EmiStack FERMENTATION_BARREL = EmiStack.of(ObjectRegistry.FERMENTATION_BARREL.get());
    public static final EmiStack APPLE_PRESS = EmiStack.of(ObjectRegistry.APPLE_PRESS.get());

    public static final EmiRecipeCategory FERMENTATION_CATEGORY = new EmiRecipeCategory(Vinery.identifier("wine_fermentation"), FERMENTATION_BARREL);
    public static final EmiRecipeCategory APPLE_PRESS_MASHING_CATEGORY = new EmiRecipeCategory(Vinery.identifier("apple_press_mashing"), APPLE_PRESS);
    public static final EmiRecipeCategory APPLE_PRESS_FERMENTING_CATEGORY = new EmiRecipeCategory(Vinery.identifier("apple_press_fermenting"), APPLE_PRESS);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(FERMENTATION_CATEGORY);
        registry.addCategory(APPLE_PRESS_MASHING_CATEGORY);
        registry.addCategory(APPLE_PRESS_FERMENTING_CATEGORY);

        registry.addWorkstation(FERMENTATION_CATEGORY, FERMENTATION_BARREL);
        registry.addWorkstation(APPLE_PRESS_MASHING_CATEGORY, APPLE_PRESS);
        registry.addWorkstation(APPLE_PRESS_FERMENTING_CATEGORY, APPLE_PRESS);

        RecipeManager rm = registry.getRecipeManager();
        for (var holder : rm.getAllRecipesFor(RecipeTypeRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get())) {
            registry.addRecipe(new FermentationBarrelEmiRecipe(holder.id(), holder.value()));
        }
        for (var holder : rm.getAllRecipesFor(RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get())) {
            registry.addRecipe(new ApplePressEmiRecipe(APPLE_PRESS_MASHING_CATEGORY, holder.id(), holder.value().input, holder.value().getResultItem(null), false));
        }
        for (var holder : rm.getAllRecipesFor(RecipeTypeRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get())) {
            registry.addRecipe(new ApplePressEmiRecipe(APPLE_PRESS_FERMENTING_CATEGORY, holder.id(), holder.value().input, holder.value().getResultItem(null), holder.value().requiresBottle()));
        }
    }
}
