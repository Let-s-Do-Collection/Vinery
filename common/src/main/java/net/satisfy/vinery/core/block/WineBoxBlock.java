package net.satisfy.vinery.core.block;

import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.foundation.util.ShapeUtil;
import net.satisfy.foundation.storage.StorageBlock;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.registry.StorageTypeRegistry;
import net.satisfy.vinery.core.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WineBoxBlock extends StorageBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private static final Supplier<VoxelShape> shapeOpen = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Block.box(1, 0, 4, 15, 5, 5), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 0, 11, 15, 5, 12), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 0, 5, 2, 5, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(14, 0, 5, 15, 5, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(2, 0, 5, 14, 2, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 5, 12, 15, 6, 13), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 6, 12, 2, 12, 13), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(14, 6, 12, 15, 12, 13), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 12, 12, 15, 13, 13), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(7, 13, 11, 9, 14, 13), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> shapeClosed = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Block.box(1, 0, 4, 15, 5, 5), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 0, 11, 15, 5, 12), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 0, 5, 2, 5, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(14, 0, 5, 15, 5, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(2, 0, 5, 14, 2, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 5, 11, 15, 6, 12), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 5, 5, 2, 6, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(14, 5, 5, 15, 6, 11), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(1, 5, 4, 15, 6, 5), BooleanOp.OR);
        shape = Shapes.join(shape, Block.box(7, 4, 3, 9, 6, 4), BooleanOp.OR);
        return shape;
    };

    public static final Map<Direction, VoxelShape> SHAPE_OPEN = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, shapeOpen.get()));
        }
    });

    public static final Map<Direction, VoxelShape> SHAPE_CLOSED = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, shapeClosed.get()));
        }
    });

    public WineBoxBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() && stack.isEmpty()) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), Block.UPDATE_ALL);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        } else if (state.getValue(OPEN)) {
            return super.useItemOn(stack,state, level, pos, player, hand, hit);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public boolean canInsertStack(ItemStack stack) {
        return stack.is(TagRegistry.SMALL_BOTTLE);
    }

    @Override
    public Direction[] unAllowedDirections() {
        return new Direction[]{Direction.DOWN, Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
    }

    @Override
    public BlockEntityType<?> blockEntityType() {
        return EntityTypeRegistry.STORAGE_ENTITY.get();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ResourceLocation type() {
        return StorageTypeRegistry.WINE_BOX;
    }

    @Override
    public int getSection(Float x, Float y) {
        return 0;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean isOpen = state.getValue(OPEN);
        return isOpen ? SHAPE_OPEN.get(facing) : SHAPE_CLOSED.get(facing);
    }
}
