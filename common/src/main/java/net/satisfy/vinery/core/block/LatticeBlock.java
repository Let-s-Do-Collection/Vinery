package net.satisfy.vinery.core.block;

import net.satisfy.foundation.block.LineConnectingType;
import net.satisfy.foundation.block.LineConnectingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.block.entity.LatticeBlockEntity;
import net.satisfy.vinery.core.item.GrapeBushSeedItem;
import net.satisfy.vinery.core.util.GrapeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class LatticeBlock extends StemBlock implements EntityBlock {
    public static final BooleanProperty SUPPORT = BooleanProperty.create("support");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<LineConnectingType> TYPE = LineConnectingBlock.TYPE;

    protected static final VoxelShape EAST = box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST = box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH = box(0.0D, 0.0D, 0.01D, 16.0D, 16.0D, 2.0D);
    protected static final VoxelShape NORTH = box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape FLOOR = box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    private static final SoundEvent BREAK_SOUND_EVENT = SoundEvents.SWEET_BERRY_BUSH_BREAK;
    private static final SoundEvent PLACE_SOUND_EVENT = SoundEvents.SWEET_BERRY_BUSH_PLACE;

    public LatticeBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(SUPPORT, true)
                .setValue(BOTTOM, false)
                .setValue(TYPE, LineConnectingType.NONE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos clickedPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos clickedFacingPos = clickedPos.relative(clickedFace.getOpposite());
        BlockState clickedFacingState = level.getBlockState(clickedFacingPos);
        if (context.getPlayer() != null && !context.getPlayer().isCrouching() && clickedFacingState.getBlock() instanceof LatticeBlock) {
            Direction clickedFacingFace = clickedFacingState.getValue(FACING);
            if (clickedFacingFace != clickedFace && clickedFacingFace.getOpposite() != clickedFace) facing = clickedFacingFace;
        }
        boolean bottom = clickedFace == Direction.DOWN || clickedFace == Direction.UP;
        BlockState state = this.defaultBlockState().setValue(FACING, facing).setValue(BOTTOM, bottom);
        if (!bottom) state = getConnection(state, level, clickedPos);
        return state;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (Boolean.TRUE.equals(state.getValue(BOTTOM))) return FLOOR;
        return switch (state.getValue(FACING)) {
            case WEST -> WEST;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            default -> NORTH;
        };
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack , BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        int age = state.getValue(AGE);

        if (stack.getItem() instanceof AxeItem) {
            BlockState newState = state.setValue(SUPPORT, !state.getValue(SUPPORT));
            BlockState updateState = getConnection(newState, world, pos);
            world.setBlock(pos, updateState, 3);
            return ItemInteractionResult.SUCCESS;
        }

        if (stack.getItem() instanceof GrapeBushSeedItem seedItem) {
            GrapeType type = seedItem.getType();
            if (age == 0 && type.isLattice()) {
                BlockState newState = withAge(state, 1, type);
                world.setBlock(pos, newState, 3);

                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof LatticeBlockEntity lattice) {
                    lattice.setAge(1);
                    lattice.setGrapeType(type);
                }

                if (!player.isCreative()) stack.shrink(1);
                world.playSound(null, pos, PLACE_SOUND_EVENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
        }

        if (age > 0 && stack.getItem() == Items.SHEARS) {
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            if (age > 2) dropGrapes(world, state, pos, hit.getDirection());
            dropGrapeSeeds(world, state, pos, hit.getDirection());
            world.setBlock(pos, state.setValue(AGE, 0), 3);

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof LatticeBlockEntity lattice) {
                lattice.setAge(0);
            }

            world.playSound(player, pos, BREAK_SOUND_EVENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (age > 2) {
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            dropGrapes(world, state, pos, hit.getDirection());
            world.setBlock(pos, state.setValue(AGE, 1), 3);

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof LatticeBlockEntity lattice) {
                lattice.setAge(1);
            }

            world.playSound(player, pos, BREAK_SOUND_EVENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        Random rand = new Random();
        if (rand.nextInt(100) >= 98 || isMature(state)) return;
        int age = state.getValue(AGE);
        GrapeType type = state.getValue(GRAPE);

        BlockState newState = withAge(state, age + 1, type);
        world.setBlock(pos, newState, UPDATE_CLIENTS);

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof LatticeBlockEntity lattice) {
            lattice.setAge(age + 1);
            lattice.setGrapeType(type);
        }

        super.randomTick(state, world, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            if (state.getValue(AGE) > 0) dropGrapeSeeds(world, state, pos, null);
            if (state.getValue(AGE) > 2) dropGrapes(world, state, pos, null);
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        int age = state.getValue(AGE);
        return (!isMature(state) && age > 0 && age < 4);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) world.scheduleTick(pos, this, 1);
        return getConnection(state, world, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos pos, BlockState state) {
        return !isMature(state) && state.getValue(AGE) > 0;
    }

    public BlockState getConnection(BlockState state, LevelAccessor level, BlockPos currentPos) {
        Direction facing = state.getValue(FACING);
        boolean bottom = state.getValue(BOTTOM);

        BlockState stateL = level.getBlockState(currentPos.relative(facing.getClockWise()));
        BlockState stateR = level.getBlockState(currentPos.relative(facing.getCounterClockWise()));

        boolean sideL = stateL.getBlock() instanceof LatticeBlock
                && stateL.getValue(FACING) == facing
                && stateL.getValue(BOTTOM) == bottom;

        boolean sideR = stateR.getBlock() instanceof LatticeBlock
                && stateR.getValue(FACING) == facing
                && stateR.getValue(BOTTOM) == bottom;

        LineConnectingType type = sideL && sideR ? LineConnectingType.MIDDLE
                : (sideR ? LineConnectingType.LEFT
                : (sideL ? LineConnectingType.RIGHT
                : LineConnectingType.NONE));

        return state.setValue(TYPE, type);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, TYPE, SUPPORT, BOTTOM);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new LatticeBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
