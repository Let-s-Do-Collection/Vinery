package net.satisfy.vinery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class GrapevinePotBlockEntity extends BlockEntity {
    public static final int POINTS_PER_STOMP = 10;
    public static final int EVENT_STOMP = 1;
    public static final int EVENT_BOTTLE = 2;

    private int progress;
    private long stompStart = Long.MIN_VALUE / 2;
    private long bottleStart = Long.MIN_VALUE / 2;

    public GrapevinePotBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.GRAPEVINE_POT.get(), pos, state);
    }

    public static int stompsNeeded() {
        return Math.max(1, PlatformHelper.getGrapevinePotStomps());
    }

    public static int pointsNeeded() {
        return stompsNeeded() * POINTS_PER_STOMP;
    }

    public int getProgress() {
        return progress;
    }

    public int getStomps() {
        return Math.min(stompsNeeded(), progress / POINTS_PER_STOMP);
    }

    public boolean isStomped() {
        return progress >= pointsNeeded();
    }

    public long getStompStart() {
        return stompStart;
    }

    public long getBottleStart() {
        return bottleStart;
    }

    public void setProgress(int progress) {
        this.progress = Math.min(pointsNeeded(), Math.max(0, progress));
        setChanged();
        sync();
    }

    @Override
    public boolean triggerEvent(int id, int param) {
        if (id == EVENT_STOMP) {
            if (level != null) {
                stompStart = level.getGameTime();
            }
            return true;
        }
        if (id == EVENT_BOTTLE) {
            if (level != null) {
                bottleStart = level.getGameTime();
            }
            return true;
        }
        return super.triggerEvent(id, param);
    }

    private void sync() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.progress = tag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("Progress", progress);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
