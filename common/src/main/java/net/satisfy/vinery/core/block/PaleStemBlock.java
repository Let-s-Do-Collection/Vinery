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

public class PaleStemBlock extends StemBlock {
    private static final VoxelShape PALE_SHAPE = Block.box(6.0, 0, 6.0, 10.0, 16.0, 10.0);
    public static final BooleanProperty LEAVES_PENDING = BooleanProperty.create("leaves_pending");
    public static final BooleanProperty LEAVES_DONE = BooleanProperty.create("leaves_done");
    public static final int MIN_LIGHT = 9;

    public PaleStemBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(GRAPE, GrapeTypeRegistry.NONE)
                .setValue(AGE, 0)
                .setValue(LEAVES_PENDING, false)
                .setValue(LEAVES_DONE, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.OFF_HAND) {
            return super.useItemOn(stack,state, level, pos, player, hand, hit);
        }
        final int age = state.getValue(AGE);
        if (age > 0 && player.getItemInHand(hand).getItem() == Items.SHEARS) {
            if (age > 2) {
                dropGrapes(level, state, pos, hit.getDirection());
            }
            dropGrapeSeeds(level, state, pos, hit.getDirection());
            BlockState sheared = withAge(state, Math.max(0, age - 1), state.getValue(GRAPE));
            if (sheared.getValue(AGE) == 0) {
                sheared = sheared.setValue(LEAVES_PENDING, false);
            }
            level.setBlock(pos, sheared, 3);
            level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_BREAK, SoundSource.AMBIENT, 1.0F, 1.0F);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (stack.getItem() instanceof GrapeBushSeedItem seed && hasTrunk(level, pos)) {
            if (age == 0) {
                if (!seed.getType().isLattice()) {
                    boolean schedule = (seed.getType() == GrapeTypeRegistry.WHITE || seed.getType() == GrapeTypeRegistry.RED) && PlatformHelper.shouldGrapevineLeavesGrow() && !(level.getBlockState(pos.below()).getBlock() instanceof PaleStemBlock);
                    BlockState ns = withAge(state, 1, seed.getType());
                    if (schedule && !state.getValue(LEAVES_PENDING) && !state.getValue(LEAVES_DONE)) {
                        ns = ns.setValue(LEAVES_PENDING, true);
                        int delay = 4800 + level.random.nextInt(4801);
                        level.scheduleTick(pos, this, delay);
                    }
                    level.setBlock(pos, ns, 3);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundSource.AMBIENT, 1.0F, 1.0F);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack,state, level, pos, player, hand, hit);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        super.onPlace(state, level, pos, oldState, moved);
        if (!level.isClientSide && (state.getValue(GRAPE) == GrapeTypeRegistry.WHITE || state.getValue(GRAPE) == GrapeTypeRegistry.RED) && !state.getValue(LEAVES_PENDING) && !state.getValue(LEAVES_DONE) && PlatformHelper.shouldGrapevineLeavesGrow() && !(level.getBlockState(pos.below()).getBlock() instanceof PaleStemBlock)) {
            level.setBlock(pos, state.setValue(LEAVES_PENDING, true), 3);
            int delay = 4800 + level.random.nextInt(4801);
            level.scheduleTick(pos, this, delay);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            if (state.getValue(AGE) > 0) {
                dropGrapeSeeds(level, state, pos, null);
            }
            if (state.getValue(AGE) > 2) {
                dropGrapes(level, state, pos, null);
            }
            level.destroyBlock(pos, true);
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
                    while (level.isInWorldBounds(ground) && level.getBlockState(ground).isAir()) {
                        ground = ground.below();
                    }
                    BlockPos placePos = ground.above();
                    if (level.getBlockState(placePos).canBeReplaced()) {
                        level.setBlock(placePos, ObjectRegistry.GRAPEVINE_LEAVES.get()
                                .defaultBlockState()
                                .setValue(LeavesBlock.PERSISTENT, true), 3);
                    }
                }
            }
            level.setBlock(pos, state.setValue(LEAVES_PENDING, false).setValue(LEAVES_DONE, true), 3);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextDouble() >= 0.98 * PlatformHelper.getGrapeGrowthMultiplier()) return;
        if (!isMature(state) && hasTrunk(level, pos) && state.getValue(AGE) > 0) {
            final int i;
            if (level.getRawBrightness(pos, 0) >= MIN_LIGHT && (i = state.getValue(AGE)) < 4) {
                level.setBlock(pos, this.withAge(state, i + 1, state.getValue(GRAPE)), Block.UPDATE_CLIENTS);
            }
        }
        super.randomTick(state, level, pos, random);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isRedstoneConductor(level, pos) || level.getBlockState(pos.below()).getBlock() == this;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEAVES_PENDING, LEAVES_DONE);
    }
}
