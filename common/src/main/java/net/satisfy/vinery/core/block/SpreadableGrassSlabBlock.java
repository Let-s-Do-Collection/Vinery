package net.satisfy.vinery.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class SpreadableGrassSlabBlock extends SlabBlock implements BonemealableBlock {
    public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;
    public static final Block GRASS_BLOCK = Blocks.GRASS_BLOCK;

    public static Block getDirtSlabBlock(){
        return ObjectRegistry.DIRT_SLAB.get();
    }

    public static Block getGrassSlabBlock(){
        return ObjectRegistry.GRASS_SLAB.get();
    }

    public SpreadableGrassSlabBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, false).setValue(SNOWY, false));
    }

    public static boolean canSurviveNew(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = level.getBlockState(blockPos);

        if (state.getBlock().equals(getDirtSlabBlock()) && state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
            return !state.getValue(SlabBlock.WATERLOGGED);
        }

        if (blockState.is(Blocks.SNOW) && blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        }

        if (blockState.getFluidState().getAmount() == 8) {
            return false;
        }

        if (state.is(BlockTags.WALLS) && blockState.is(BlockTags.WALLS) && blockState.canOcclude()) {
            return false;
        }

        if (blockState.getBlock() instanceof SlabBlock && blockState.getValue(SlabBlock.TYPE) == SlabType.TOP){
            return true;
        }

        if (blockState.getBlock() instanceof StairBlock && blockState.getValue(StairBlock.HALF) == Half.TOP){
            return true;
        }

        int i = LightEngine.getLightBlockInto(level, GRASS_BLOCK.defaultBlockState(), pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(level, blockPos));
        return i < level.getMaxLightLevel();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if(blockState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) return false;

        return levelReader.getBlockState(blockPos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        GrassBlock block = (GrassBlock) GRASS_BLOCK;
        block.performBonemeal(level, random, pos, state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurviveNew(state, level, pos)) {
            level.setBlock(pos, getDirtSlabBlock().withPropertiesOf(state), Block.UPDATE_CLIENTS);
        } else {
            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                for(int i = 0; i < 4; ++i) {
                    BlockPos blockPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    trySpread(level, blockPos);
                }
            }
        }
    }

    public static void trySpread(ServerLevel level, BlockPos spreadPos) {
        BlockState oldState = level.getBlockState(spreadPos);

        if (oldState.is(Blocks.DIRT) || oldState.is(getDirtSlabBlock())) {
            BlockState aboveState = level.getBlockState(spreadPos.above());
            boolean isSnowy = aboveState.is(Blocks.SNOW);
            BlockState newState = null;

            if (oldState.is(Blocks.DIRT)) {
                newState = GRASS_BLOCK.defaultBlockState().setValue(BlockStateProperties.SNOWY, isSnowy);
            } else if(oldState.is(getDirtSlabBlock())){
                newState = getGrassSlabBlock().withPropertiesOf(oldState).setValue(BlockStateProperties.SNOWY, isSnowy);
            }

            if (newState != null && canSurviveNew(newState, level, spreadPos) && !level.getFluidState(spreadPos.above()).is(FluidTags.WATER)) {
                level.setBlockAndUpdate(spreadPos, newState);
            }
        }
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (heldItem.is(ItemTags.SHOVELS)) {
            if (!level.isClientSide) {
                BlockState pathState = ObjectRegistry.DIRT_PATH_SLAB.get().defaultBlockState()
                        .setValue(TYPE, state.getValue(TYPE))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));

                level.setBlock(pos, pathState, Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!player.isCreative()) {
                    heldItem.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SNOWY);
    }

    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        state = state.setValue(SNOWY, level.getBlockState(pos.above()).is(BlockTags.SNOW));

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
