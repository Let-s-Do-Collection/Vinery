package net.satisfy.vinery.core.block;

import net.satisfy.foundation.util.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.block.entity.ApplePressBlockEntity;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ApplePressBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);
	public static final Map<Direction, VoxelShape> TOP_SHAPES = new HashMap<>();
	public static final Map<Direction, VoxelShape> BOTTOM_SHAPES = new HashMap<>();

	public ApplePressBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(ApplePressBlock::new);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) {
			if (state.getValue(HALF) == DoubleBlockHalf.UPPER && state.getBlock() != newState.getBlock()) {
				BlockPos lowerPos = pos.below();
				BlockState lowerState = level.getBlockState(lowerPos);
				if (lowerState.getBlock() == this && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER) {
					level.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), 35);
				}
			} else if (state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getBlock() != newState.getBlock()) {
				BlockPos upperPos = pos.above();
				BlockState upperState = level.getBlockState(upperPos);
				if (upperState.getBlock() == this && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
					level.setBlock(upperPos, Blocks.AIR.defaultBlockState(), 35);
				}
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) {
			BlockPos otherPartPos;
			BlockState otherPartState;

			if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
				dropInventory(level, pos);
				popResource(level, pos, new ItemStack(this));
				otherPartPos = pos.above();
				otherPartState = level.getBlockState(otherPartPos);
				if (otherPartState.getBlock() == this) {
					level.setBlock(otherPartPos, Blocks.AIR.defaultBlockState(), 35);
				}
			} else if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				popResource(level, pos, new ItemStack(this));
				otherPartPos = pos.below();
				otherPartState = level.getBlockState(otherPartPos);
				if (otherPartState.getBlock() == this) {
					dropInventory(level, otherPartPos);
					level.setBlock(otherPartPos, Blocks.AIR.defaultBlockState(), 35);
				}
			}
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	private void dropInventory(Level level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof ApplePressBlockEntity applePress) {
			for (int i = 0; i < applePress.getItems().size(); i++) {
				ItemStack stack = applePress.getItem(i);
				if (!stack.isEmpty()) {
					popResource(level, pos, stack);
				}
			}
			applePress.clearContent();
		}
	}

	@Override
	public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (state.getValue(HALF) != DoubleBlockHalf.LOWER) {
			return InteractionResult.PASS;
		}

		if (!level.isClientSide) {
			MenuProvider screenHandlerFactory = state.getMenuProvider(level, pos);
			if (screenHandlerFactory != null) {
				player.openMenu(screenHandlerFactory);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ApplePressBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, EntityTypeRegistry.APPLE_PRESS_BLOCK_ENTITY.get(), (world1, pos, state1, be) -> be.tick(world1, pos, state1, be));
	}

	@Override
	public @NotNull RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockGetter level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(ctx)) {
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(HALF, DoubleBlockHalf.LOWER);
		}
		return null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			BlockPos upperPos = pos.above();
			BlockState upperState = state.setValue(HALF, DoubleBlockHalf.UPPER);
			level.setBlock(upperPos, upperState, 3);
		}
	}

	public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Direction facing = state.getValue(FACING);
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			return TOP_SHAPES.get(facing);
		} else {
			return BOTTOM_SHAPES.get(facing);
		}
	}

	static {
		Supplier<VoxelShape> topShapeSupplier = ApplePressBlock::makeTopShape;
		Supplier<VoxelShape> bottomShapeSupplier = ApplePressBlock::makeBottomShape;

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			TOP_SHAPES.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, topShapeSupplier.get()));
			BOTTOM_SHAPES.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, bottomShapeSupplier.get()));
		}
	}

	private static VoxelShape makeTopShape() {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.46875, 0.3125, 0.46875, 0.53125, 0.8125, 0.53125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 0.8125, 0.375, 0.625, 0.875, 0.625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0, 0, 0.40625, 0.125, 0.625, 0.59375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.40625, 1, 0.625, 0.59375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.40625, 0.875, 0.625, 0.59375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.3125, 0.8125), BooleanOp.OR);
		return shape;
	}

	private static VoxelShape makeBottomShape() {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.125, 0.3125, 0.46875, 0.875, 0.4375, 0.53125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0, 0, 0.0625, 0.125, 0.125, 0.9375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.0625, 1, 0.125, 0.9375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0, 0.125, 0.40625, 0.125, 1, 0.59375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.875, 0.125, 0.40625, 1, 1, 0.59375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.1875, 0.5625, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.4375, 0.125, 0.875, 0.5625, 0.875), BooleanOp.OR);
		return shape;
	}
}