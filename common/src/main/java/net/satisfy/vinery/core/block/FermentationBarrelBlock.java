package net.satisfy.vinery.core.block;

import net.minecraft.Util;
import net.satisfy.foundation.util.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.satisfy.foundation.registry.FoundationParticles;
import net.satisfy.vinery.core.wine.JuiceUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.block.entity.FermentationBarrelBlockEntity;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FermentationBarrelBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.125, 0, 0.9375, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0.25, 0.9375, 0.125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.75, 0.9375, 0.125, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.125, 0.25), BooleanOp.OR);
        return shape;
    };

    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, ShapeUtil.rotateShape(Direction.SOUTH, direction, voxelShapeSupplier.get()));
        }
    });

    /** Drip spawn point inside the block for facing south, rotated for the other directions. */
    private static final double TAP_X = 0.34;
    private static final double TAP_Y = 0.38;
    private static final double TAP_Z = -0.02;

    public FermentationBarrelBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(FermentationBarrelBlock::new);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FermentationBarrelBlockEntity barrelBlockEntity) {
            if (player.isShiftKeyDown()) {
                if (barrelBlockEntity.getFluidLevel() > 0) {
                    barrelBlockEntity.setFluidLevel(0);
                    barrelBlockEntity.setJuiceType("");
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.sendBlockUpdated(pos, state, state, 3);
                    return InteractionResult.SUCCESS;
                }
            } else {
                player.openMenu(barrelBlockEntity);
                return InteractionResult.CONSUME;
            }
        }

        return super.useWithoutItem(state, level, pos, player, hit);
    }

    public static Vec3 tapPosition(BlockState state, BlockPos pos) {
        double dx = TAP_X - 0.5;
        double dz = TAP_Z - 0.5;
        for (int i = 0; i < state.getValue(FACING).get2DDataValue(); i++) {
            double rotated = -dz;
            dz = dx;
            dx = rotated;
        }
        return new Vec3(pos.getX() + 0.5 + dx, pos.getY() + TAP_Y, pos.getZ() + 0.5 + dz);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(8) != 0) return;
        if (!(level.getBlockEntity(pos) instanceof FermentationBarrelBlockEntity barrel) || barrel.getFluidLevel() <= 0) return;
        Vec3 tap = tapPosition(state, pos);
        double x = tap.x;
        double y = tap.y;
        double z = tap.z;
        level.addParticle(ColorParticleOption.create(FoundationParticles.COLORED_DRIP.get(), FastColor.ARGB32.opaque(JuiceUtil.color(barrel.getJuiceType()))), x, y, z, 0, 0, 0);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            final BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FermentationBarrelBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, (Container) blockEntity);
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTicker(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTicker(type, EntityTypeRegistry.FERMENTATION_BARREL_ENTITY.get(), (level1, pos, state1, blockEntity) -> FermentationBarrelBlockEntity.tick(level1, pos, blockEntity));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FermentationBarrelBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}