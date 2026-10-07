package net.satisfy.vinery.core.compat.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class ApplePressMashingCategory implements IRecipeCategory<ApplePressMashingRecipe> {
    public static final RecipeType<ApplePressMashingRecipe> APPLE_PRESS_MASHING_TYPE = RecipeType.create("vinery", "apple_press_mashing", ApplePressMashingRecipe.class);

    private static final int BACKGROUND_WIDTH = 160;
    private static final int BACKGROUND_HEIGHT = 70;
    private static final int X_OFFSET = 10;
    private static final int Y_OFFSET = 10;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public ApplePressMashingCategory(IGuiHelper helper) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("vinery", "textures/gui/apple_press_gui.png");
        this.background = helper.createDrawable(texture, X_OFFSET, Y_OFFSET, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        ItemStack pressStack = new ItemStack(ObjectRegistry.APPLE_PRESS.get());
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, pressStack);
        this.title = ObjectRegistry.APPLE_PRESS.get().getName();
    }

    @NotNull
    @Override
    public RecipeType<ApplePressMashingRecipe> getRecipeType() {
        return APPLE_PRESS_MASHING_TYPE;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return BACKGROUND_WIDTH;
    }

    @Override
    public int getHeight() {
        return BACKGROUND_HEIGHT;
    }

    @Override
    @NotNull
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ApplePressMashingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44 - X_OFFSET, 34 - Y_OFFSET)
                .addIngredients(recipe.getIngredients().get(0));

        assert Minecraft.getInstance().level != null;
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101 - X_OFFSET, 50 - Y_OFFSET)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(ApplePressMashingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }
}
