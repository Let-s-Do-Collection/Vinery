package net.satisfy.vinery.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.item.GrapeBushSeedItem;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class PaleStemBlock extends StemBlock {
    private static final VoxelShape PALE_SHAPE = Block.box(6.0, 0, 6.0, 10.0, 16.0, 10.0);
    public static final BooleanProperty LEAVES_PENDING = BooleanProperty.create("leaves_pending");
    public static final BooleanProperty LEAVES_DONE = BooleanProperty.create("leaves_done");

    public PaleStemBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(GRAPE, GrapeTypeRegistry.NONE)
                .setValue(AGE, 0)
                .setValue(LEAVES_PENDING, false)
                .setValue(LEAVES_DONE, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return PALE_SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        if (blockState.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
            return blockState;
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if (livingEntity instanceof Player player) {
            if (itemStack != null && (player.isCreative() || itemStack.getCount() >= 2) && level.getBlockState(blockPos.below()).getBlock() != this && blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced()) {
                level.setBlock(blockPos.above(), this.defaultBlockState(), 3);
                itemStack.shrink(1);
            }
        }
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.OFF_HAND) {
            return super.useItemOn(stack,state, world, pos, player, hand, hit);
        }
        final int age = state.getValue(AGE);
        if (age > 0 && player.getItemInHand(hand).getItem() == Items.SHEARS) {
            if (age > 2) {
                dropGrapes(world, state, pos, hit.getDirection());
            }
            dropGrapeSeeds(world, state, pos, hit.getDirection());
            BlockState sheared = withAge(state, Math.max(0, age - 1), state.getValue(GRAPE));
            if (sheared.getValue(AGE) == 0) {
                sheared = sheared.setValue(LEAVES_PENDING, false).setValue(LEAVES_DONE, false);
            }
            world.setBlock(pos, sheared, 3);
            world.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_BREAK, SoundSource.AMBIENT, 1.0F, 1.0F);
            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        }
        if (stack.getItem() instanceof GrapeBushSeedItem seed && hasTrunk(world, pos)) {
            if (age == 0) {
                if (!seed.getType().isLattice()) {
                    boolean schedule = (seed.getType() == GrapeTypeRegistry.WHITE || seed.getType() == GrapeTypeRegistry.RED) && PlatformHelper.shouldGrapevineLeavesGrow();
                    BlockState ns = withAge(state, 1, seed.getType());
                    if (schedule && !state.getValue(LEAVES_PENDING) && !state.getValue(LEAVES_DONE)) {
                        ns = ns.setValue(LEAVES_PENDING, true);
                        int delay = 4800 + world.random.nextInt(4801);
                        world.scheduleTick(pos, this, delay);
                    }
                    world.setBlock(pos, ns, 3);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    world.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundSource.AMBIENT, 1.0F, 1.0F);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack,state, world, pos, player, hand, hit);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        super.onPlace(state, world, pos, oldState, moved);
        if (!world.isClientSide && (state.getValue(GRAPE) == GrapeTypeRegistry.WHITE || state.getValue(GRAPE) == GrapeTypeRegistry.RED) && !state.getValue(LEAVES_PENDING) && !state.getValue(LEAVES_DONE) && PlatformHelper.shouldGrapevineLeavesGrow()) {
            world.setBlock(pos, state.setValue(LEAVES_PENDING, true), 3);
            int delay = 4800 + world.random.nextInt(4801);
            world.scheduleTick(pos, this, delay);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            if (state.getValue(AGE) > 0) {
                dropGrapeSeeds(world, state, pos, null);
            }
            if (state.getValue(AGE) > 2) {
                dropGrapes(world, state, pos, null);
            }
            world.destroyBlock(pos, true);
            return;
        }

        if (state.getValue(LEAVES_PENDING)) {
            boolean isWhite = state.getValue(GRAPE) == GrapeTypeRegistry.WHITE;
            boolean isRed = state.getValue(GRAPE) == GrapeTypeRegistry.RED;

            if (isWhite || isRed) {
                List<Vector3i> offsets = Arrays.asList(
                        new Vector3i(-2, 0, -1),
                        new Vector3i(-1, 0, -2),
                        new Vector3i(1, 0, -2),
                        new Vector3i(2, 0, -1),
                        new Vector3i(-2, 0, 1),
                        new Vector3i(-1, 0, 0),
                        new Vector3i(1, 0, 0),
                        new Vector3i(2, 0, 1),
                        new Vector3i(-1, 0, 2),
                        new Vector3i(0, 0, 1),
                        new Vector3i(1, 0, 2)
                );

                for (Vector3i v : offsets) {
                    if (random.nextFloat() > 0.4f) continue;
                    BlockPos ground = pos.offset(v.x, 0, v.z);
                    while (world.isInWorldBounds(ground) && world.getBlockState(ground).isAir()) {
                        ground = ground.below();
                    }
                    BlockPos placePos = ground.above();
                    if (world.getBlockState(placePos).canBeReplaced()) {
                        world.setBlock(placePos, ObjectRegistry.GRAPEVINE_LEAVES.get()
                                .defaultBlockState()
                                .setValue(LeavesBlock.PERSISTENT, true), 3);
                    }
                }
            }
            world.setBlock(pos, state.setValue(LEAVES_PENDING, false).setValue(LEAVES_DONE, true), 3);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        Random rand = new Random();
        if (rand.nextInt(100) >= 98) return;
        if (!isMature(state) && hasTrunk(world, pos) && state.getValue(AGE) > 0) {
            final int i;
            if (world.getRawBrightness(pos, 0) >= 9 && (i = state.getValue(AGE)) < 4) {
                world.setBlock(pos, this.withAge(state, i + 1, state.getValue(GRAPE)), Block.UPDATE_CLIENTS);
            }
        }
        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isRedstoneConductor(world, pos) || world.getBlockState(pos.below()).getBlock() == this;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEAVES_PENDING, LEAVES_DONE);
    }
}
