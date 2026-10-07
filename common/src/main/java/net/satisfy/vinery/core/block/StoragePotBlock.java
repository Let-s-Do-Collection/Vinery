package net.satisfy.vinery.core.block;

import net.satisfy.vinery.core.registry.EntityTypeRegistry;

import net.satisfy.foundation.util.ShapeUtil;
import net.satisfy.foundation.block.CabinetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.vinery.core.block.entity.StoragePotBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StoragePotBlock extends CabinetBlock {
    public StoragePotBlock(Properties settings, SoundEvent openSound, SoundEvent closeSound) {
        super(settings, EntityTypeRegistry.STORAGE_POT_ENTITY, openSound, closeSound);
    }

    private static final VoxelShape VOXEL_SHAPE = createVoxelShape();

    private static VoxelShape createVoxelShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.9375, 0, 0, 1, 0.5, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0, 0, 0.0625, 0.5, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0, 0, 0.9375, 0.625, 0.0625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0, 0.9375, 0.9375, 0.625, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.0625, 0.9375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.5, 0, 0.0625, 0.625, 0.375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.9375, 0.5, 0, 1, 0.625, 0.375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.5, 0.625, 0.0625, 0.625, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.9375, 0.5, 0.625, 1, 0.625, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.5625, 0.375, 0.0625, 0.625, 0.625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.9375, 0.5625, 0.375, 1, 0.625, 0.625), BooleanOp.OR);
        return shape;
    }

    public static final Map<Direction, VoxelShape> SHAPE = new HashMap<>();

    static {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPE.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, VOXEL_SHAPE));
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoragePotBlockEntity(pos,state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }
}
