package net.satisfy.vinery.core.compat.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.satisfy.vinery.client.gui.FermentationBarrelGui;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.compat.jei.VineryJEIPlugin;
import net.satisfy.vinery.core.recipe.FermentationBarrelRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class FermentationBarrelCategory implements IRecipeCategory<FermentationBarrelRecipe> {
    public static final RecipeType<FermentationBarrelRecipe> FERMENTATION_BARREL = RecipeType.create(Vinery.MOD_ID, "wine_fermentation", FermentationBarrelRecipe.class);
    public static final int WIDTH = 124;
    public static final int HEIGHT = 70;
    public static final int WIDTH_OF = 26;
    public static final int HEIGHT_OF = 13;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public FermentationBarrelCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(FermentationBarrelGui.BACKGROUND, WIDTH_OF, HEIGHT_OF, WIDTH, HEIGHT);
        IDrawableAnimated arrow = helper.drawableBuilder(FermentationBarrelGui.BACKGROUND, 177, 17, 23, 10)
                .buildAnimated(50, IDrawableAnimated.StartDirection.LEFT, false);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.FERMENTATION_BARREL.get().asItem().getDefaultInstance());
        this.localizedName = Component.translatable("rei.vinery.fermentation_barrel_category");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FermentationBarrelRecipe recipe, IFocusGroup focuses) {
        VineryJEIPlugin.buildSlotsFromRecipe(builder, recipe);
    }

    @Override
    public @NotNull RecipeType<FermentationBarrelRecipe> getRecipeType() {
        return FERMENTATION_BARREL;
    }

    @Override
    public @NotNull Component getTitle() {
        return this.localizedName;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    private boolean isMouseOverFluidArea(int mouseX, int mouseY) {
        int fluidAreaLeft = 56 - 1;
        int fluidAreaTop = 31 - 5;
        int fluidAreaRight = 56 + 1;
        int fluidAreaBottom = 31 + 5;

        return mouseX >= fluidAreaLeft && mouseX <= fluidAreaRight &&
                mouseY >= fluidAreaTop && mouseY <= fluidAreaBottom;
    }

    private Component getFluidTooltip(String juiceType, int fluidLevel) {
        int maxFluidLevel = PlatformHelper.getMaxFluidLevel();

        double percentage = (double) fluidLevel / maxFluidLevel * 100;
        String percentageStr = String.format("%.2f", percentage);

        if (juiceType.startsWith("red")) {
            String region = juiceType.substring(4);
            return Component.translatable("tooltip.vinery.fermentation_barrel.red_" + region + "_juice_with_percentage", percentageStr);
        }
        else if (juiceType.startsWith("white")) {
            String region = juiceType.substring(6);
            return Component.translatable("tooltip.vinery.fermentation_barrel.white_" + region + "_juice_with_percentage", percentageStr);
        }
        else if (juiceType.equals("apple")) {
            return Component.translatable("tooltip.vinery.fermentation_barrel.apple_juice_with_percentage", percentageStr);
        }
        else {
            return Component.translatable("tooltip.vinery.fermentation_barrel.empty");
        }
    }

    @Override
    public void draw(FermentationBarrelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        if (recipe.getJuiceData().amount() > 0) {
            FermentationBarrelGui.drawJuiceBar(guiGraphics, recipe.getJuiceData().type(), recipe.getJuiceData().amount(), 56, 31);

            if (isMouseOverFluidArea((int) mouseX, (int) mouseY)) {
                Component tooltip = getFluidTooltip(recipe.getJuiceData().type(), recipe.getJuiceData().amount());
                guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltip, (int) mouseX, (int) mouseY);
            }
        }
    }
}
