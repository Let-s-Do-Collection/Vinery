package net.satisfy.vinery.core.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.wine.GrapeType;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class GrapeBushBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    private static final VoxelShape SHAPE;

    public final GrapeType type;
    public static final MapCodec<GrapeBushBlock> CODEC = RecordCodecBuilder.mapCodec(inst-> inst.group(
            Properties.CODEC.fieldOf("settings").forGetter(GrapeBushBlock::properties),
            GrapeType.CODEC.fieldOf("type").forGetter(GrapeBushBlock::grapeType)
    ).apply(inst,GrapeBushBlock::new));
    public GrapeBushBlock(Properties settings, GrapeType type) {
        super(settings);
        this.type = type;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(this.grapeType().getSeeds());
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack,BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int i = state.getValue(AGE);
        boolean bl = i == 3;
        if (!bl && stack.is(Items.BONE_MEAL)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (i > 1) {
            int x = level.random.nextInt(2);
            popResource(level, pos, new ItemStack(getGrapeType().getItem(), x + (bl ? 1 : 0)));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(AGE, 1), 2);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useItemOn(stack,state, level, pos, player, hand, hit);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        double growthChance = PlatformHelper.getGrapeGrowthChance() * PlatformHelper.getGrapeGrowthMultiplier();
        if (age < 3 && random.nextDouble() < growthChance && canGrowPlace(level, pos, state)) {
            BlockState newState = state.setValue(AGE, age + 1);
            level.setBlock(pos, newState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(AGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public int minLight() {
        return 10;
    }

    public boolean canGrowPlace(LevelReader level, BlockPos blockPos, BlockState blockState) {
        return level.getRawBrightness(blockPos, 0) >= minLight();
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        boolean soilValid = this.mayPlaceOn(level.getBlockState(blockPos.below()), level, blockPos);
        if (!soilValid) return false;

        if (level.getChunk(blockPos).getPersistedStatus().getIndex() < ChunkStatus.FULL.getIndex()) {
            return true;
        }
        return canGrowPlace(level, blockPos, blockState);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter level, BlockPos pos) {
        return floor.isSolidRender(level, pos);
    }

    public GrapeType grapeType() {
        return this.type;
    }

    public ItemStack getGrapeType() {
        return new ItemStack(this.grapeType().getFruit());
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.getValue(AGE) + 1);
        level.setBlock(pos, state.setValue(AGE, i), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    static {
        AGE = BlockStateProperties.AGE_3;
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    }

    public static class SavannaGrapeBush extends GrapeBushBlock {
        public SavannaGrapeBush(Properties settings, GrapeType type) {
            super(settings, type);
        }

        @Override
        public int minLight() {
            return 14;
        }
    }

    public static class TaigaGrapeBush extends GrapeBushBlock {
        public TaigaGrapeBush(Properties settings, GrapeType type) {
            super(settings, type);
        }

        private static final int SOIL_RADIUS = 4;

        @Override
        public int minLight() {
            return 5;
        }

        @Override
        public boolean canGrowPlace(LevelReader level, BlockPos blockPos, BlockState blockState) {
            return super.canGrowPlace(level, blockPos, blockState) && hasTaigaSoil(level, blockPos);
        }

        public static boolean hasTaigaSoil(LevelReader level, BlockPos blockPos) {
            for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-SOIL_RADIUS, -2, -SOIL_RADIUS), blockPos.offset(SOIL_RADIUS, 1, SOIL_RADIUS))) {
                BlockState soil = level.getBlockState(pos);
                if (soil.is(Blocks.PODZOL) || soil.is(Blocks.COARSE_DIRT) || soil.is(Blocks.GRASS_BLOCK)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
            return false;
        }
    }
}
