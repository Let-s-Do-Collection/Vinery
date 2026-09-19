package net.satisfy.vinery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

public class StorageBlockEntity extends BlockEntity implements Clearable {
    private int size;

    private NonNullList<ItemStack> inventory;

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.STORAGE_ENTITY.get(), pos, state);
        this.size = 0;
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
    }

    public StorageBlockEntity(BlockPos pos, BlockState state, int size) {
        super(EntityTypeRegistry.STORAGE_ENTITY.get(), pos, state);
        this.size = size;
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
    }

    public ItemStack removeStack(int slot) {
        ItemStack stack = inventory.set(slot, ItemStack.EMPTY);
        setChanged();
        return stack;
    }

    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        setChanged();
    }

    @Override
    public void setChanged() {
        if (level != null && !level.isClientSide()) {
            Packet<ClientGamePacketListener> updatePacket = getUpdatePacket();
            for (ServerPlayer player : GeneralUtil.tracking((ServerLevel) level, getBlockPos())) {
                player.connection.send(updatePacket);
            }
        }
        super.setChanged();
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt,provider);
        this.size = nbt.getInt("size");
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory,provider);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt,HolderLookup.Provider provider) {
        ContainerHelper.saveAllItems(nbt, this.inventory,provider);
        nbt.putInt("size", this.size);
        super.saveAdditional(nbt,provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            this.inventory.set(i, inventory.get(i));
        }
    }

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.size; i++) {
            this.inventory.set(i, ItemStack.EMPTY);
        }
    }
}
