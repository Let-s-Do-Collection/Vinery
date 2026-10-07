package net.satisfy.vinery.core.block;

import net.satisfy.foundation.block.LineConnectingBlock;
import net.satisfy.foundation.block.LineConnectingType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TableBlock extends LineConnectingBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    public static final VoxelShape TOP_SHAPE;
    public static final VoxelShape[] LEG_SHAPES;

    public TableBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        LineConnectingType type = state.getValue(TYPE);

        if (type == LineConnectingType.MIDDLE) {
            return TOP_SHAPE;
        } else if (direction == Direction.NORTH && type == LineConnectingType.LEFT || direction == Direction.SOUTH && type == LineConnectingType.RIGHT) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[3]);
        } else if ((direction != Direction.NORTH || type != LineConnectingType.RIGHT) && (direction != Direction.SOUTH || type != LineConnectingType.LEFT)) {
            if ((direction != Direction.EAST || type != LineConnectingType.LEFT) && (direction != Direction.WEST || type != LineConnectingType.RIGHT)) {
                return (direction != Direction.EAST || type != LineConnectingType.RIGHT) && (direction != Direction.WEST || type != LineConnectingType.LEFT)
                        ? Shapes.or(TOP_SHAPE, LEG_SHAPES)
                        : Shapes.or(TOP_SHAPE, LEG_SHAPES[2], LEG_SHAPES[3]);
            } else {
                return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[1]);
            }
        } else {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[1], LEG_SHAPES[2]);
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(WATERLOGGED, level.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        TOP_SHAPE = box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);
        LEG_SHAPES = new VoxelShape[]{
                box(1.0, 0.0, 1.0, 4.0, 13.0, 4.0),
                box(12.0, 0.0, 1.0, 15.0, 13.0, 4.0),
                box(12.0, 0.0, 12.0, 15.0, 13.0, 15.0),
                box(1.0, 0.0, 12.0, 4.0, 13.0, 15.0)
        };
    }
}
