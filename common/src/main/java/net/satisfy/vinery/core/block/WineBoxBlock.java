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
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.25, 0.9375, 0.3125, 0.3125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.6875, 0.9375, 0.3125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.3125, 0.125, 0.3125, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.3125, 0.875, 0.125, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.3125, 0.75, 0.9375, 0.8125, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.8125, 0.6875, 0.5625, 0.875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.3125, 0.9375, 0.3125, 0.6875), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> shapeClosed = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.25, 0.9375, 0.3125, 0.3125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.6875, 0.9375, 0.3125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.3125, 0.125, 0.3125, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.3125, 0.9375, 0.3125, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.3125, 0.875, 0.125, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.3125, 0.25, 0.9375, 0.375, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.25, 0.1875, 0.5625, 0.375, 0.25), BooleanOp.OR);
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
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() && stack.isEmpty()) {
            if (!world.isClientSide()) {
                world.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), Block.UPDATE_ALL);
            }
            return ItemInteractionResult.sidedSuccess(world.isClientSide());
        } else if (state.getValue(OPEN)) {
            return super.useItemOn(stack,state, world, pos, player, hand, hit);
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
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean isOpen = state.getValue(OPEN);
        return isOpen ? SHAPE_OPEN.get(facing) : SHAPE_CLOSED.get(facing);
    }
}
