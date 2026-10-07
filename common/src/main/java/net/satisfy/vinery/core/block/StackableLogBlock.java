package net.satisfy.vinery.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.foundation.util.ShapeUtil;
import net.satisfy.vinery.core.block.entity.StackableLogBlockEntity;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class StackableLogBlock extends SlabBlock implements EntityBlock {
    public static final BooleanProperty FIRED = BooleanProperty.create("fired");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public StackableLogBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(FIRED, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    public static boolean canLight(BlockState state) {
        return state.getValue(TYPE) == SlabType.DOUBLE && !state.getValue(FIRED) && !state.getValue(WATERLOGGED);
    }

    public static void ignite(Level level, BlockPos pos, BlockState state, @Nullable Entity source) {
        level.setBlock(pos, state.setValue(FIRED, true), Block.UPDATE_ALL_IMMEDIATE);
        level.gameEvent(source, GameEvent.BLOCK_CHANGE, pos);
        if (level.getBlockEntity(pos) instanceof StackableLogBlockEntity entity) {
            entity.refuel();
        }
    }

    public static void extinguish(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(FIRED, false), Block.UPDATE_ALL);
        level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(TYPE) != SlabType.DOUBLE) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        boolean fired = state.getValue(FIRED);
        if (canLight(state) && (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE))) {
            if (!level.isClientSide) {
                if (stack.is(Items.FIRE_CHARGE)) {
                    level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    stack.consume(1, player);
                } else {
                    level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                }
                ignite(level, pos, state, player);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (fired && stack.is(ItemTags.SHOVELS)) {
            if (level.isClientSide) {
                for (int i = 0; i < 20; ++i) {
                    CampfireBlock.makeParticles(level, pos, false, false);
                }
            } else {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                extinguish(level, pos, state);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (fired && isWaterBottle(stack)) {
            if (!level.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                extinguish(level, pos, state);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!(level.getBlockEntity(pos) instanceof StackableLogBlockEntity entity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (fired && stack.is(ItemTags.LOGS_THAT_BURN)) {
            if (!level.isClientSide) {
                stack.consume(1, player);
                entity.refuel();
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return entity.getCookableRecipe(stack).map(recipe -> {
            if (!level.isClientSide && entity.placeFood(player, stack, recipe.value().getCookingTime())) {
                player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.CONSUME;
        }).orElse(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
    }

    private static boolean isWaterBottle(ItemStack stack) {
        return stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos pos = hit.getBlockPos();
        if (level.isClientSide || !projectile.mayInteract(level, pos)) {
            return;
        }
        if (canLight(state) && (projectile.isOnFire() || projectile instanceof SmallFireball)) {
            ignite(level, pos, state, projectile);
        } else if (state.getValue(FIRED) && projectile instanceof ThrownPotion potion && isWaterBottle(potion.getItem())) {
            extinguish(level, pos, state);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof StackableLogBlockEntity entity) {
            Containers.dropContents(level, pos, entity.getItems());
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockState current = ctx.getLevel().getBlockState(pos);
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        if (current.is(this)) {
            return current.setValue(TYPE, SlabType.DOUBLE).setValue(FIRED, false).setValue(WATERLOGGED, false).setValue(FACING, facing);
        }
        BlockState state = defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, ctx.getLevel().getFluidState(pos).getType() == Fluids.WATER);
        Direction face = ctx.getClickedFace();
        boolean top = face == Direction.DOWN || face != Direction.UP && ctx.getClickLocation().y() - pos.getY() > 0.5;
        return state.setValue(TYPE, top ? SlabType.TOP : SlabType.BOTTOM);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FIRED, FACING);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(FIRED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.getBlockEntity(pos);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(FIRED)) {
            displayTickLikeCampfire(level, pos, random, level.getBlockState(pos.below()).is(Blocks.HAY_BLOCK));
        }
    }

    public static void displayTickLikeCampfire(Level level, BlockPos pos, RandomSource random, boolean isSignal) {
        if (random.nextFloat() < 0.8F) {
            for (int i = 0; i < random.nextInt(5) + 3; ++i) {
                CampfireBlock.makeParticles(level, pos, isSignal, true);
            }
        }
        if (random.nextInt(10) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
        }
        if (random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(4) + 3; ++i) {
                level.addParticle(ParticleTypes.LAVA, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextFloat() / 2.0F, 5.0E-5, random.nextFloat() / 2.0F);
            }
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(FIRED) && !entity.isSteppingCarefully() && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().campfire(), 1.0F);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StackableLogBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide || type != EntityTypeRegistry.STACKABLE_LOG.get()) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<StackableLogBlockEntity>) StackableLogBlockEntity::serverTick;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING)).get(state.getValue(TYPE));
    }

    private static final VoxelShape BOTTOM_SHAPE = Shapes.or(
            Shapes.box(0.0625, 0, 0, 0.3125, 0.25, 1),
            Shapes.box(0, 0.25, 0.6875, 1, 0.5, 0.9375),
            Shapes.box(0.6875, 0, 0, 0.9375, 0.25, 1),
            Shapes.box(0, 0.25, 0.0625, 1, 0.5, 0.3125));

    private static final VoxelShape TOP_SHAPE = BOTTOM_SHAPE.move(0, 0.5, 0);

    private static final VoxelShape DOUBLE_SHAPE = Shapes.or(BOTTOM_SHAPE, TOP_SHAPE);

    public static final Map<Direction, Map<SlabType, VoxelShape>> SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Map<SlabType, VoxelShape> shapes = new EnumMap<>(SlabType.class);
            shapes.put(SlabType.BOTTOM, ShapeUtil.rotateShape(Direction.NORTH, direction, BOTTOM_SHAPE));
            shapes.put(SlabType.TOP, ShapeUtil.rotateShape(Direction.NORTH, direction, TOP_SHAPE));
            shapes.put(SlabType.DOUBLE, ShapeUtil.rotateShape(Direction.NORTH, direction, DOUBLE_SHAPE));
            map.put(direction, shapes);
        }
    });
}
