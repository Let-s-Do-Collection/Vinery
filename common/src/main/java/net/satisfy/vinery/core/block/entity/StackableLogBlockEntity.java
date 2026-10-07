package net.satisfy.vinery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.vinery.core.block.StackableLogBlock;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StackableLogBlockEntity extends BlockEntity implements Clearable {
    public static final int SLOTS = 4;
    public static final int MAX_BURN_TIME = 12000;
    private static final String BURN_TIME_KEY = "BurnTime";
    private static final String PROGRESS_KEY = "CookingTimes";
    private static final String TOTAL_KEY = "CookingTotalTimes";

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[SLOTS];
    private final int[] cookingTime = new int[SLOTS];
    private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    private int burnTime = MAX_BURN_TIME;

    public StackableLogBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.STACKABLE_LOG.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StackableLogBlockEntity entity) {
        if (!state.getValue(StackableLogBlock.FIRED)) {
            entity.cooldownTick();
            return;
        }
        if (--entity.burnTime <= 0 || level.isRainingAt(pos.above()) && level.random.nextInt(100) == 0) {
            StackableLogBlock.extinguish(level, pos, state);
            return;
        }
        if (entity.cookTick(level, pos)) {
            setChanged(level, pos, state);
        }
    }

    private boolean cookTick(Level level, BlockPos pos) {
        boolean changed = false;
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            changed = true;
            if (++cookingProgress[i] < cookingTime[i]) {
                continue;
            }
            SingleRecipeInput input = new SingleRecipeInput(stack);
            ItemStack result = quickCheck.getRecipeFor(input, level)
                    .map(recipe -> recipe.value().assemble(input, level.registryAccess()))
                    .orElse(stack);
            if (result.isItemEnabled(level.enabledFeatures())) {
                Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), result);
                items.set(i, ItemStack.EMPTY);
                level.sendBlockUpdated(pos, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(getBlockState()));
            }
        }
        return changed;
    }

    private void cooldownTick() {
        boolean changed = false;
        for (int i = 0; i < SLOTS; i++) {
            if (cookingProgress[i] > 0) {
                cookingProgress[i] = Math.max(0, cookingProgress[i] - 2);
                changed = true;
            }
        }
        if (changed) {
            setChanged();
        }
    }

    public Optional<RecipeHolder<CampfireCookingRecipe>> getCookableRecipe(ItemStack stack) {
        if (level == null || items.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }
        return quickCheck.getRecipeFor(new SingleRecipeInput(stack), level);
    }

    public boolean placeFood(@Nullable LivingEntity entity, ItemStack stack, int time) {
        for (int i = 0; i < SLOTS; i++) {
            if (items.get(i).isEmpty()) {
                cookingTime[i] = time;
                cookingProgress[i] = 0;
                items.set(i, stack.consumeAndReturn(1, entity));
                if (level != null) {
                    level.gameEvent(GameEvent.BLOCK_CHANGE, worldPosition, GameEvent.Context.of(entity, getBlockState()));
                }
                markUpdated();
                return true;
            }
        }
        return false;
    }

    public void refuel() {
        burnTime = MAX_BURN_TIME;
        setChanged();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, items, true, provider);
        tag.putIntArray(PROGRESS_KEY, cookingProgress);
        tag.putIntArray(TOTAL_KEY, cookingTime);
        tag.putInt(BURN_TIME_KEY, burnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        items.clear();
        ContainerHelper.loadAllItems(tag, items, provider);
        int[] progress = tag.getIntArray(PROGRESS_KEY);
        System.arraycopy(progress, 0, cookingProgress, 0, Math.min(SLOTS, progress.length));
        int[] total = tag.getIntArray(TOTAL_KEY);
        System.arraycopy(total, 0, cookingTime, 0, Math.min(SLOTS, total.length));
        if (tag.contains(BURN_TIME_KEY)) {
            burnTime = tag.getInt(BURN_TIME_KEY);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, items, true, provider);
        return tag;
    }
}
