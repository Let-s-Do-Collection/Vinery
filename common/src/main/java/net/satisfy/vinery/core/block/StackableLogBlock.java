package net.satisfy.vinery.core.block;

import net.satisfy.foundation.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation, unused")
public class StackableLogBlock extends SlabBlock {
    public static final BooleanProperty FIRED = BooleanProperty.create("fired");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    public StackableLogBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(FIRED, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack,BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        final SlabType stackSize = state.getValue(TYPE);
        if (stack.is(Items.FLINT_AND_STEEL) && stackSize == SlabType.DOUBLE) {
            world.setBlock(pos, state.setValue(FIRED, true), Block.UPDATE_ALL);
            world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.NEUTRAL, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        } else if (stack.getItem() instanceof ShovelItem && stackSize == SlabType.DOUBLE && state.getValue(FIRED)) {
            world.setBlockAndUpdate(pos, state.setValue(FIRED, false));
            world.playSound(player, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            final boolean clientSide = world.isClientSide();
            if (clientSide) {
                for (int i = 0; i < 20; ++i) {
                    CampfireBlock.makeParticles(world, pos, false, false);
                }
            }
            return ItemInteractionResult.sidedSuccess(clientSide);
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        BlockState blockState = ctx.getLevel().getBlockState(blockPos);
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(FIRED, false).setValue(WATERLOGGED, false).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
        } else {
            FluidState fluidState = ctx.getLevel().getFluidState(blockPos);
            BlockState blockState2 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
            Direction direction = ctx.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(ctx.getClickLocation().y() - (double) blockPos.getY() > 0.5)) ? blockState2 : blockState2.setValue(TYPE, SlabType.TOP);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FIRED, FACING);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if ((state.getValue(FIRED)))
            displayTickLikeCampfire(world, pos, random, world.getBlockState(pos.below()).is(Blocks.HAY_BLOCK));
    }

    public static void displayTickLikeCampfire(Level world, BlockPos pos, RandomSource random, boolean isSignal) {
        if (random.nextFloat() < 0.80f) {
            for (int i = 0; i < random.nextInt(5) + 3; ++i) {
                CampfireBlock.makeParticles(world, pos, isSignal, true);

            }

        }
        if (random.nextInt(10) == 0) {
            world.playLocalSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }
        if (random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(4) + 3; ++i) {
                world.addParticle(ParticleTypes.LAVA, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, random.nextFloat() / 2.0f, 5.0E-5, random.nextFloat() / 2.0f);
            }
        }
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        boolean isLit = world.getBlockState(pos).getValue(FIRED);

        if (isLit && !entity.fireImmune() && entity instanceof LivingEntity livingEntity)
        {
            Holder<Enchantment> frostWalker = livingEntity.level()
                    .registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(Enchantments.FROST_WALKER);
            if(livingEntity.getItemBySlot(EquipmentSlot.FEET).getEnchantments().getLevel(frostWalker) < 0){
                entity.hurt(world.damageSources().inFire(), 1.f);
            }
        }

        super.stepOn(world, pos, state, entity);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        SlabType slabType = blockState.getValue(TYPE);
        Direction facing = blockState.getValue(FACING);

        return switch (slabType) {
            case DOUBLE -> SHAPE.get(facing).get(SlabType.DOUBLE);
            case TOP -> SHAPE.get(facing).get(SlabType.TOP);
            default -> SHAPE.get(facing).get(SlabType.BOTTOM);
        };
    }

    private static final Supplier<VoxelShape> BOTTOM_AABB_SUPPLIER = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0, 0, 0.3125, 0.25, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.25, 0.6875, 1, 0.5, 0.9375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.6875, 0, 0, 0.9375, 0.25, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.25, 0.0625, 1, 0.5, 0.3125), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> TOP_AABB_SUPPLIER = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0.5, 0, 0.3125, 0.75, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.75, 0.6875, 1, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.6875, 0.5, 0, 0.9375, 0.75, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.75, 0.0625, 1, 1, 0.3125), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> DOUBLE_SUPPLIER = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0, 0, 0.3125, 0.25, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0.5, 0, 0.3125, 0.75, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.25, 0.6875, 1, 0.5, 0.9375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.75, 0.6875, 1, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.6875, 0, 0, 0.9375, 0.25, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.6875, 0.5, 0, 0.9375, 0.75, 1), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.25, 0.0625, 1, 0.5, 0.3125), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.75, 0.0625, 1, 1, 0.3125), BooleanOp.OR);
        return shape;
    };

    protected static final VoxelShape BOTTOM_AABB = BOTTOM_AABB_SUPPLIER.get();
    protected static final VoxelShape TOP_AABB = TOP_AABB_SUPPLIER.get();
    protected static final VoxelShape DOUBLE = DOUBLE_SUPPLIER.get();

    public static final Map<Direction, Map<SlabType, VoxelShape>> SHAPE = net.minecraft.Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, new HashMap<>());
            map.get(direction).put(SlabType.DOUBLE, ShapeUtil.rotateShape(Direction.NORTH, direction, DOUBLE_SUPPLIER.get()));
            map.get(direction).put(SlabType.TOP, ShapeUtil.rotateShape(Direction.NORTH, direction, TOP_AABB_SUPPLIER.get()));
            map.get(direction).put(SlabType.BOTTOM, ShapeUtil.rotateShape(Direction.NORTH, direction, BOTTOM_AABB_SUPPLIER.get()));
        }
    });

}

