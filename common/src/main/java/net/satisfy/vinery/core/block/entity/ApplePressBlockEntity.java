package net.satisfy.vinery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.client.gui.handler.ApplePressGuiHandler;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.recipe.input.ApplePressFermentingRecipeInput;
import net.satisfy.vinery.core.recipe.input.ApplePressMashingRecipeInput;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.RecipeTypesRegistry;
import net.satisfy.foundation.util.ImplementedInventory;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ApplePressBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory, BlockEntityTicker<ApplePressBlockEntity> {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    protected final ContainerData propertyDelegate;
    private int progress1 = 0;
    private int maxProgress1 = PlatformHelper.getApplePressMashingTime();
    private int progress2 = 0;
    private int maxProgress2 = PlatformHelper.getApplePressFermentationTime();

    public ApplePressBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.APPLE_PRESS_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ApplePressBlockEntity.this.progress1;
                    case 1 -> ApplePressBlockEntity.this.maxProgress1;
                    case 2 -> ApplePressBlockEntity.this.progress2;
                    case 3 -> ApplePressBlockEntity.this.maxProgress2;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        ApplePressBlockEntity.this.progress1 = value;
                        break;
                    case 1:
                        ApplePressBlockEntity.this.maxProgress1 = value;
                        break;
                    case 2:
                        ApplePressBlockEntity.this.progress2 = value;
                        break;
                    case 3:
                        ApplePressBlockEntity.this.maxProgress2 = value;
                        break;
                }
            }

            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{3};
        } else if (side.getAxis().isHorizontal()) {
            return new int[]{0, 1, 2};
        }
        return new int[]{};
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new ApplePressGuiHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt,provider);
        ContainerHelper.saveAllItems(nbt, inventory,provider);
        nbt.putInt("apple_press.progress1", progress1);
        nbt.putInt("apple_press.progress2", progress2);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt,provider);
        ContainerHelper.loadAllItems(nbt, inventory,provider);
        progress1 = nbt.getInt("apple_press.progress1");
        progress2 = nbt.getInt("apple_press.progress2");
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, ApplePressBlockEntity entity) {
        if (world.isClientSide()) return;

        boolean dirty = false;

        if (hasInput(entity, 0)) {
            ApplePressMashingRecipeInput input = new ApplePressMashingRecipeInput(entity.getItem(0));
            if(world.getRecipeManager().getRecipeFor(RecipeTypesRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get(),input,world).isEmpty()) {
                return;
            }
            Recipe<?> recipe1 = world.getRecipeManager().getRecipeFor(RecipeTypesRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get(),input,level).get().value();
            if (recipe1 instanceof ApplePressMashingRecipe mashingRecipe) {
                if (canProcessMashing(entity, mashingRecipe)) {
                    entity.progress1++;
                    if (entity.progress1 >= entity.maxProgress1) {
                        processMashing(entity, mashingRecipe);
                        dirty = true;
                    }
                } else {
                    entity.progress1 = 0;
                }
            } else {
                entity.progress1 = 0;
            }
        } else {
            entity.progress1 = 0;
        }

        if (hasInput(entity, 1)) {
            ApplePressFermentingRecipeInput input = new ApplePressFermentingRecipeInput(entity.getItem(1));
            if(world.getRecipeManager().getRecipeFor(RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get(), input, world).isEmpty()) return;
            Recipe<?> recipe2 = world.getRecipeManager().getRecipeFor(RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get(), input, world).get().value();
            if (recipe2 instanceof ApplePressFermentingRecipe fermentingRecipe) {
                if (canProcessFermenting(entity, fermentingRecipe)) {
                    entity.progress2++;
                    if (entity.progress2 >= entity.maxProgress2) {
                        processFermenting(entity, fermentingRecipe);
                        dirty = true;
                    }
                } else {
                    entity.progress2 = 0;
                }
            } else {
                entity.progress2 = 0;
            }
        } else {
            entity.progress2 = 0;
        }

        if (dirty) {
            setChanged(world, pos, state);
        }
    }

    private static boolean hasInput(ApplePressBlockEntity entity, int slot) {
        return !entity.getItem(slot).isEmpty();
    }

    private static boolean canProcessMashing(ApplePressBlockEntity entity, ApplePressMashingRecipe recipe) {
        ItemStack input = entity.getItem(0);
        ItemStack output = entity.getItem(1);
        if (!recipe.matches(new ApplePressMashingRecipeInput(input), entity.level)) return false;
        if (output.isEmpty()) return true;
        assert entity.level != null;
        return output.getItem() == recipe.getResultItem(entity.level.registryAccess()).getItem();
    }

    private static void processMashing(ApplePressBlockEntity entity, ApplePressMashingRecipe recipe) {
        entity.removeItem(0, 1);
        assert entity.level != null;
        ItemStack result = recipe.getResultItem(entity.level.registryAccess()).copy();
        ItemStack outputSlot = entity.getItem(1);
        if (outputSlot.isEmpty()) {
            entity.setItem(1, result);
        } else {
            outputSlot.grow(result.getCount());
        }
        entity.progress1 = 0;
    }

    private static boolean canProcessFermenting(ApplePressBlockEntity entity, ApplePressFermentingRecipe recipe) {
        if (!recipe.matches(new ApplePressFermentingRecipeInput(entity.getItem(1)), entity.level)) return false;
        if (recipe.requiresBottle()) {
            ItemStack bottle = entity.getItem(2);
            if (!isWineBottle(bottle)) return false;
        }
        ItemStack output = entity.getItem(3);
        if (output.isEmpty()) return true;
        assert entity.level != null;
        return output.getItem() == recipe.getResultItem(entity.level.registryAccess()).getItem();
    }

    private static void processFermenting(ApplePressBlockEntity entity, ApplePressFermentingRecipe recipe) {
        entity.removeItem(1, 1);
        if (recipe.requiresBottle()) {
            entity.removeItem(2, 1);
        }
        assert entity.level != null;
        ItemStack result = recipe.getResultItem(entity.level.registryAccess()).copy();
        ItemStack outputSlot = entity.getItem(3);
        if (outputSlot.isEmpty()) {
            entity.setItem(3, result);
        } else {
            outputSlot.grow(result.getCount());
        }
        entity.progress2 = 0;
    }

    private static boolean isWineBottle(ItemStack stack) {
        return stack.getItem() == ObjectRegistry.WINE_BOTTLE.get();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.level != null && this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr((double) this.worldPosition.getX() + 0.5, (double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return switch (index) {
            case 0 -> true;
            case 2 -> stack.getItem() == ObjectRegistry.WINE_BOTTLE.get();
            default -> false;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        assert direction != null;
        if (direction.getAxis().isHorizontal()) {
            return switch (index) {
                case 0 -> isValidForApplePressMashing(stack);
                case 1 -> isValidForApplePressFermenting(stack);
                case 2 -> isWineBottle(stack); 
                default -> false;
            };
        }
        return false;
    }


    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 3 && (direction == Direction.DOWN || direction.getAxis().isHorizontal());
    }

    private boolean isValidForApplePressMashing(ItemStack stack) {
        if (level == null) return false;
        return level.getRecipeManager()
                .getAllRecipesFor(RecipeTypesRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get())
                .stream()
                .anyMatch(recipe -> recipe.value().getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack)));
    }

    private boolean isValidForApplePressFermenting(ItemStack stack) {
        if (level == null) return false;
        return level.getRecipeManager()
                .getAllRecipesFor(RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get())
                .stream()
                .anyMatch(recipe -> recipe.value().getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack)));
    }
}