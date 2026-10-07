package net.satisfy.vinery.core.compat.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class ApplePressFermentingCategory implements IRecipeCategory<ApplePressFermentingRecipe> {
    public static final RecipeType<ApplePressFermentingRecipe> APPLE_PRESS_TYPE = RecipeType.create("vinery", "apple_press_fermenting", ApplePressFermentingRecipe.class);

    private static final int BACKGROUND_WIDTH = 160;
    private static final int BACKGROUND_HEIGHT = 70;
    private static final int X_OFFSET = 10;
    private static final int Y_OFFSET = 10;
    private static final Vector2i ARROW_POS = new Vector2i(101, 17);
    private static final int ARROW_U = 176;
    private static final int ARROW_V = 47;
    private static final int ARROW_WIDTH = 10;
    private static final int ARROW_HEIGHT = 28;
    private static final int MAX_TIME = 200;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public ApplePressFermentingCategory(IGuiHelper helper) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("vinery", "textures/gui/apple_press_gui.png");
        this.background = helper.createDrawable(texture, X_OFFSET, Y_OFFSET, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        this.arrow = helper.drawableBuilder(texture, ARROW_U, ARROW_V, ARROW_WIDTH, ARROW_HEIGHT)
                .buildAnimated(MAX_TIME, IDrawableAnimated.StartDirection.BOTTOM, false);
        ItemStack kettleStack = new ItemStack(ObjectRegistry.APPLE_PRESS.get());
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, kettleStack);
        this.title = ObjectRegistry.APPLE_PRESS.get().getName();
    }

    @NotNull
    @Override
    public RecipeType<ApplePressFermentingRecipe> getRecipeType() {
        return APPLE_PRESS_TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, ApplePressFermentingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 101 - X_OFFSET, 50 - Y_OFFSET)
                .addIngredients(recipe.getIngredients().get(0));

        assert Minecraft.getInstance().level != null;
        builder.addSlot(RecipeIngredientRole.OUTPUT, 119 - X_OFFSET, 18 - Y_OFFSET)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));

        if (recipe.requiresBottle()) {
            ItemStack wineBottle = new ItemStack(ObjectRegistry.WINE_BOTTLE.get());
            builder.addSlot(RecipeIngredientRole.INPUT, 119 - X_OFFSET, 50 - Y_OFFSET)
                    .addIngredients(Ingredient.of(wineBottle));
        }
    }

    @Override
    public void draw(ApplePressFermentingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        arrow.draw(guiGraphics, ARROW_POS.x() - X_OFFSET, ARROW_POS.y() - Y_OFFSET);
    }
}
