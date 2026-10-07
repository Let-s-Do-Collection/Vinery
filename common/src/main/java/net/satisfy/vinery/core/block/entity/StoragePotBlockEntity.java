package net.satisfy.vinery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.foundation.block.CabinetBlockEntity;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StoragePotBlockEntity extends CabinetBlockEntity {
    private static final String ORDER_KEY = "FillOrder";
    private static final String SEED_KEY = "FillSeeds";

    private final List<Integer> fillOrder = new ArrayList<>();
    private final List<Integer> fillSeeds = new ArrayList<>();
    private int nextSeed;

    public StoragePotBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.STORAGE_POT_ENTITY.get(), pos, state);
    }

    public int getFillCount() {
        return fillOrder.size();
    }

    public int getFillSlot(int layer) {
        return fillOrder.get(layer);
    }

    public int getFillSeed(int layer) {
        return fillSeeds.get(layer);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide && updateFillOrder()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private boolean updateFillOrder() {
        boolean changed = false;
        for (int i = fillOrder.size() - 1; i >= 0; i--) {
            int slot = fillOrder.get(i);
            if (slot >= getContainerSize() || getItem(slot).isEmpty()) {
                fillOrder.remove(i);
                fillSeeds.remove(i);
                changed = true;
            }
        }
        for (int slot = 0; slot < getContainerSize(); slot++) {
            if (!getItem(slot).isEmpty() && !fillOrder.contains(slot)) {
                fillOrder.add(slot);
                fillSeeds.add(nextSeed++);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putIntArray(ORDER_KEY, fillOrder);
        tag.putIntArray(SEED_KEY, fillSeeds);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        fillOrder.clear();
        fillSeeds.clear();
        int[] order = tag.getIntArray(ORDER_KEY);
        int[] seeds = tag.getIntArray(SEED_KEY);
        for (int i = 0; i < order.length; i++) {
            fillOrder.add(order[i]);
            int seed = i < seeds.length ? seeds[i] : i;
            fillSeeds.add(seed);
            nextSeed = Math.max(nextSeed, seed + 1);
        }
        if (level == null || !level.isClientSide) {
            updateFillOrder();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveCustomOnly(provider);
    }
}
