package net.satisfy.vinery.core.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.foundation.recipe.book.StationRecipeBookMenu;
import net.satisfy.vinery.core.registry.RecipeTypeRegistry;
import net.satisfy.vinery.core.registry.MenuTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ApplePressMenu extends AbstractContainerMenu implements StationRecipeBookMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    private final Inventory playerInventory;

    private static final int MASHING_BAR_HEIGHT = 41;
    private static final int FERMENTING_BAR_HEIGHT = 30;
    private static final int[] MASHING_SLOTS = {0};
    private static final int[] FERMENTING_SLOTS = {1};
    private static final int BOTTLE_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    public ApplePressMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(4), new SimpleContainerData(4));
    }

    public ApplePressMenu(int syncId, Inventory playerInventory, Container inventory, ContainerData delegate) {
        super(MenuTypeRegistry.APPLE_PRESS_MENU.get(), syncId);
        checkContainerSize(inventory, 4);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = delegate;
        this.playerInventory = playerInventory;

        this.addSlot(new Slot(inventory, 0, 44, 34));
        this.addSlot(new Slot(inventory, 1, 101, 50));
        this.addSlot(new Slot(inventory, 2, 119, 50));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, inventory, 3, 119, 18));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(delegate);
    }

    public boolean isCrafting(int index) {
        return propertyDelegate.get(index * 2) > 0;
    }

    public int getScaledProgress(int index) {
        int progress = this.propertyDelegate.get(index * 2);
        int maxProgress = this.propertyDelegate.get(index * 2 + 1);
        int barHeight = index == 0 ? MASHING_BAR_HEIGHT : FERMENTING_BAR_HEIGHT;
        return maxProgress != 0 && progress != 0 ? progress * barHeight / maxProgress : 0;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public List<RecipeType<?>> recipeBookTypes() {
        return List.of(RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get(), RecipeTypeRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get());
    }

    @Override
    public int[] recipeBookInputSlots(RecipeType<?> type) {
        return type == RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get() ? MASHING_SLOTS : FERMENTING_SLOTS;
    }

    @Override
    public Component recipeBookTabName(RecipeType<?> type) {
        return Component.translatable(type == RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get()
                ? "gui.vinery.recipe_book.mashing" : "gui.vinery.recipe_book.fermenting");
    }

    @Override
    public Map<Integer, Ingredient> recipeBookExtraInputs(RecipeHolder<?> recipe) {
        if (recipe.value() instanceof ApplePressFermentingRecipe fermenting && fermenting.requiresBottle()) {
            return Map.of(BOTTLE_SLOT, Ingredient.of(ObjectRegistry.WINE_BOTTLE.get()));
        }
        return Map.of();
    }

    @Override
    public int recipeBookResultSlot(RecipeType<?> type) {
        return type == RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get() ? FERMENTING_SLOTS[0] : OUTPUT_SLOT;
    }

    @Override
    public void appendRecipeBookTooltip(RecipeHolder<?> recipe, List<Component> tooltip) {
        if (recipe.value() instanceof ApplePressFermentingRecipe fermenting && fermenting.requiresBottle()) {
            tooltip.add(Component.literal(" 1× ").append(ObjectRegistry.WINE_BOTTLE.get().getDescription()).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }
}
