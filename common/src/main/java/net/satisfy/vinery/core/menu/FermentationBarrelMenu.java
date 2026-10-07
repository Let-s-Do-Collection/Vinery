package net.satisfy.vinery.core.menu;

import net.satisfy.foundation.menu.ExtendedSlot;
import net.satisfy.foundation.menu.OutputSlot;
import net.satisfy.foundation.recipe.book.StationRecipeBookMenu;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.vinery.core.recipe.FermentationBarrelRecipe;
import net.satisfy.vinery.core.recipe.input.FermentationBarrelRecipeInput;
import net.satisfy.vinery.platform.PlatformHelper;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.RecipeTypeRegistry;
import net.satisfy.vinery.core.registry.MenuTypeRegistry;
import net.satisfy.vinery.core.wine.JuiceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FermentationBarrelMenu extends AbstractContainerMenu implements StationRecipeBookMenu {
    private final Container inventory;
    private final Level level;
    private final Inventory playerInventory;
    public final ContainerData data;

    private static final int WINE_BOTTLE_SLOT = 4;
    private static final int OUTPUT_SLOT_GENERAL = 5;
    private static final int[] INGREDIENT_SLOTS = {1, 2, 3};

    public FermentationBarrelMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(6), new SimpleContainerData(4));
    }

    public FermentationBarrelMenu(int syncId, Inventory playerInventory, Container inventory, ContainerData data) {
        super(MenuTypeRegistry.FERMENTATION_BARREL_MENU.get(), syncId);
        this.inventory = inventory;
        this.level = playerInventory.player.level();
        this.playerInventory = playerInventory;
        this.data = data;

        this.addDataSlots(data);
        this.addBlockEntitySlots(playerInventory);
        this.addPlayerInventory(playerInventory);
    }

    private void addBlockEntitySlots(Inventory playerInventory) {
        this.addSlot(new ExtendedSlot(inventory, 0, 39, 17, stack -> JuiceUtil.isJuice(stack) && canAddJuice(stack)));
        this.addSlot(new ExtendedSlot(inventory, 1, 67, 58, this::isIngredient));
        this.addSlot(new ExtendedSlot(inventory, 2, 85, 58, this::isIngredient));
        this.addSlot(new ExtendedSlot(inventory, 3, 103, 58, this::isIngredient));
        this.addSlot(new ExtendedSlot(inventory, WINE_BOTTLE_SLOT, 123, 58, stack -> stack.is(ObjectRegistry.WINE_BOTTLE.get())));
        this.addSlot(new OutputSlot(playerInventory.player, inventory, OUTPUT_SLOT_GENERAL, 103, 17));
    }

    private boolean canAddJuice(ItemStack stack) {
        String newJuiceType = JuiceUtil.getJuiceType(stack);
        String currentJuiceType = getJuiceType();
        return currentJuiceType.isEmpty() || currentJuiceType.equals(newJuiceType);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    private boolean isIngredient(ItemStack stack) {
        return this.level.getRecipeManager()
                .getAllRecipesFor(RecipeTypeRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get())
                .stream()
                .anyMatch(recipe -> recipe.value().getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack)));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemStack = stackInSlot.copy();
            int containerSlots = 6;
            if (index < containerSlots) {
                if (!this.moveItemStackTo(stackInSlot, containerSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (JuiceUtil.isJuice(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stackInSlot.is(ObjectRegistry.WINE_BOTTLE.get())) {
                    if (!this.moveItemStackTo(stackInSlot, WINE_BOTTLE_SLOT, WINE_BOTTLE_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isIngredient(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 1, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < this.slots.size() - 9) {
                    if (!this.moveItemStackTo(stackInSlot, this.slots.size() - 9, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(stackInSlot, containerSlots, this.slots.size() - 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (stackInSlot.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
                if (stackInSlot.getCount() == itemStack.getCount()) {
                    return ItemStack.EMPTY;
                }
                slot.onTake(player, stackInSlot);
            }
        }
        return itemStack;
    }

    @Override
    public RecipeType<?> recipeBookType() {
        return RecipeTypeRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get();
    }

    @Override
    public int[] recipeBookInputSlots() {
        return INGREDIENT_SLOTS;
    }

    @Override
    public boolean recipeBookExtrasMet(RecipeHolder<?> recipe) {
        if (!(recipe.value() instanceof FermentationBarrelRecipe barrelRecipe)) {
            return true;
        }
        FermentationBarrelRecipeInput.JuiceData juice = barrelRecipe.getJuiceData();
        boolean juiceMet = juice.amount() <= 0 || (getJuiceType().equals(juice.type()) && getFluidLevel() >= juice.amount());
        return juiceMet;
    }

    @Override
    public Map<Integer, Ingredient> recipeBookExtraInputs(RecipeHolder<?> recipe) {
        if (recipe.value() instanceof FermentationBarrelRecipe barrelRecipe && barrelRecipe.isWineBottleRequired()) {
            return Map.of(WINE_BOTTLE_SLOT, Ingredient.of(ObjectRegistry.WINE_BOTTLE.get()));
        }
        return Map.of();
    }

    @Override
    public void appendRecipeBookTooltip(RecipeHolder<?> recipe, List<Component> tooltip) {
        if (!(recipe.value() instanceof FermentationBarrelRecipe barrelRecipe)) {
            return;
        }
        FermentationBarrelRecipeInput.JuiceData juice = barrelRecipe.getJuiceData();
        if (barrelRecipe.isWineBottleRequired()) {
            tooltip.add(Component.literal(" 1× ").append(ObjectRegistry.WINE_BOTTLE.get().getDescription()).withStyle(ChatFormatting.DARK_GRAY));
        }
        if (juice.amount() <= 0) {
            return;
        }
        int juices = Math.ceilDiv(juice.amount(), Math.max(1, PlatformHelper.getMaxFluidIncrease()));
        tooltip.add(Component.literal(" " + juices + "× ").append(juiceName(juice.type())).withStyle(ChatFormatting.DARK_GRAY));
        boolean sameJuice = getJuiceType().equals(juice.type());
        if (getFluidLevel() > 0 && !sameJuice) {
            tooltip.add(Component.translatable("gui.vinery.recipe_book.wrong_juice").withStyle(ChatFormatting.RED));
        } else {
            int level = sameJuice ? getFluidLevel() : 0;
            tooltip.add(Component.translatable("gui.vinery.recipe_book.fluid", level, juice.amount())
                    .withStyle(level >= juice.amount() ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
    }

    @Override
    public Map<Integer, Ingredient> recipeBookExtraGhosts(RecipeHolder<?> recipe) {
        Map<Integer, Ingredient> ghosts = new HashMap<>();
        if (recipe.value() instanceof FermentationBarrelRecipe barrelRecipe) {
            Item juice = JuiceUtil.juiceItem(barrelRecipe.getJuiceData().type());
            if (barrelRecipe.getJuiceData().amount() > 0 && juice != Items.AIR) {
                ghosts.put(0, Ingredient.of(juice));
            }
        }
        return ghosts;
    }

    @Override
    public int recipeBookResultSlot(RecipeType<?> type) {
        return OUTPUT_SLOT_GENERAL;
    }

    public static Component neededJuice(FermentationBarrelRecipe recipe) {
        FermentationBarrelRecipeInput.JuiceData juice = recipe.getJuiceData();
        int juices = Math.ceilDiv(juice.amount(), Math.max(1, PlatformHelper.getMaxFluidIncrease()));
        return Component.translatable("gui.vinery.recipe_book.needs", juices, juiceName(juice.type()), juice.amount()).withStyle(ChatFormatting.GRAY);
    }

    private static Component juiceName(String type) {
        Item item = JuiceUtil.juiceItem(type);
        return item == Items.AIR ? Component.literal(type) : item.getDescription();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    public String getJuiceType() {
        return JuiceUtil.typeFromId(this.data.get(3));
    }

    public int getFluidLevel() {
        return this.data.get(2);
    }

    public int getScaledProgress(int maxProgress) {
        int progress = this.data.get(0);
        int totalProgress = this.data.get(1);
        if (progress == 0 || totalProgress == 0) {
            return 0;
        }
        return (int) ((double) progress / totalProgress * maxProgress);
    }
}
