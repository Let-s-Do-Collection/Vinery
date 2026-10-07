package net.satisfy.vinery.core.block.entity;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.satisfy.foundation.registry.FoundationParticles;
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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.core.menu.ApplePressMenu;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.recipe.input.ApplePressFermentingRecipeInput;
import net.satisfy.vinery.core.recipe.input.ApplePressMashingRecipeInput;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.SoundEventRegistry;
import net.satisfy.vinery.core.registry.RecipeTypeRegistry;
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
    private final RecipeManager.CachedCheck<ApplePressMashingRecipeInput, ApplePressMashingRecipe> mashingCheck = RecipeManager.createCheck(RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get());
    private final RecipeManager.CachedCheck<ApplePressFermentingRecipeInput, ApplePressFermentingRecipe> fermentingCheck = RecipeManager.createCheck(RecipeTypeRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get());

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
        return new ApplePressMenu(syncId, inv, this, this.propertyDelegate);
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

    /** Lets mashing and fermenting finish on the next tick, false if both slots are empty. */
    public boolean finishProcessing() {
        boolean mashing = hasInput(this, 0);
        boolean fermenting = hasInput(this, 1);
        if (mashing) {
            progress1 = Math.max(progress1, maxProgress1 - 1);
        }
        if (fermenting) {
            progress2 = Math.max(progress2, maxProgress2 - 1);
        }
        return mashing || fermenting;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, ApplePressBlockEntity entity) {
        if (level.isClientSide()) return;

        boolean dirty = false;

        ApplePressMashingRecipe mashing = hasInput(entity, 0)
                ? entity.mashingCheck.getRecipeFor(new ApplePressMashingRecipeInput(entity.getItem(0)), level).map(holder -> holder.value()).orElse(null)
                : null;
        if (mashing != null && canProcessMashing(entity, mashing)) {
            entity.progress1++;
            if (level.getGameTime() % 30 == 0) {
                entity.playWorkingEffects(SoundEventRegistry.BLOCK_GRAPEVINE_POT_SQUEEZE.get(), 0.5F, 0xD8B84C, 3);
            }
            if (entity.progress1 >= entity.maxProgress1) {
                processMashing(entity, mashing);
                dirty = true;
            }
        } else {
            entity.progress1 = 0;
        }

        ApplePressFermentingRecipe fermenting = hasInput(entity, 1)
                ? entity.fermentingCheck.getRecipeFor(new ApplePressFermentingRecipeInput(entity.getItem(1)), level).map(holder -> holder.value()).orElse(null)
                : null;
        if (fermenting != null && canProcessFermenting(entity, fermenting)) {
            entity.progress2++;
            if (level.getGameTime() % 40 == 0) {
                entity.playWorkingEffects(SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, 0.4F, 0xC98A2E, 2);
            }
            if (entity.progress2 >= entity.maxProgress2) {
                processFermenting(entity, fermenting);
                dirty = true;
            }
        } else {
            entity.progress2 = 0;
        }

        if (dirty) {
            setChanged(level, pos, state);
        }
    }

    private static boolean fits(ItemStack output, ItemStack result) {
        return output.isEmpty() || ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private static boolean hasInput(ApplePressBlockEntity entity, int slot) {
        return !entity.getItem(slot).isEmpty();
    }

    private static boolean canProcessMashing(ApplePressBlockEntity entity, ApplePressMashingRecipe recipe) {
        ItemStack input = entity.getItem(0);
        ItemStack output = entity.getItem(1);
        if (!recipe.matches(new ApplePressMashingRecipeInput(input), entity.level)) return false;
        assert entity.level != null;
        return fits(output, recipe.getResultItem(entity.level.registryAccess()));
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
        entity.playProcessEffects(SoundEventRegistry.BLOCK_GRAPEVINE_POT_SQUEEZE.get(), 0xD8B84C);
    }

    private static boolean canProcessFermenting(ApplePressBlockEntity entity, ApplePressFermentingRecipe recipe) {
        if (!recipe.matches(new ApplePressFermentingRecipeInput(entity.getItem(1)), entity.level)) return false;
        if (recipe.requiresBottle()) {
            ItemStack bottle = entity.getItem(2);
            if (!isWineBottle(bottle)) return false;
        }
        assert entity.level != null;
        return fits(entity.getItem(3), recipe.getResultItem(entity.level.registryAccess()));
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
        entity.playProcessEffects(SoundEvents.BOTTLE_FILL, 0xC98A2E);
    }

    private void playWorkingEffects(SoundEvent sound, float volume, int color, int particles) {
        if (!(this.level instanceof ServerLevel serverLevel)) return;
        BlockPos pos = this.worldPosition;
        ColorParticleOption splash = ColorParticleOption.create(FoundationParticles.DYE_SPLASH.get(), FastColor.ARGB32.opaque(color));
        serverLevel.sendParticles(splash, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, particles, 0.2, 0.05, 0.2, 0.05);
        serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS, volume, 0.9F + serverLevel.random.nextFloat() * 0.2F);
    }

    private void playProcessEffects(SoundEvent sound, int color) {
        if (!(this.level instanceof ServerLevel serverLevel)) return;
        BlockPos pos = this.worldPosition;
        ColorParticleOption splash = ColorParticleOption.create(FoundationParticles.DYE_SPLASH.get(), FastColor.ARGB32.opaque(color));
        serverLevel.sendParticles(splash, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 12, 0.25, 0.05, 0.25, 0.15);
        serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 0.9F + serverLevel.random.nextFloat() * 0.2F);
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
                .getAllRecipesFor(RecipeTypeRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get())
                .stream()
                .anyMatch(recipe -> recipe.value().getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack)));
    }

    private boolean isValidForApplePressFermenting(ItemStack stack) {
        if (level == null) return false;
        return level.getRecipeManager()
                .getAllRecipesFor(RecipeTypeRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get())
                .stream()
                .anyMatch(recipe -> recipe.value().getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack)));
    }
}