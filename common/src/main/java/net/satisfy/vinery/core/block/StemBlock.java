package net.satisfy.vinery.core.block;

import net.satisfy.foundation.util.LibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.block.state.properties.GrapeProperty;
import net.satisfy.vinery.core.util.GrapeType;
import org.jetbrains.annotations.NotNull;

public abstract class StemBlock extends Block implements BonemealableBlock {
    public static final GrapeProperty GRAPE;
    public static final IntegerProperty AGE;

    static {
        GRAPE = GrapeProperty.create("grape");
        AGE = BlockStateProperties.AGE_4;
    }

    public void dropGrapes(Level world, BlockState state, BlockPos pos, Direction direction) {
        final int x = 1 + world.random.nextInt(this.isMature(state) ? 2 : 1);
        final int bonus = this.isMature(state) ? 2 : 1;
        Item grape = state.getValue(GRAPE).getFruit();
        ItemStack stack = new ItemStack(grape, x + bonus);

        if (direction == null) popResource(world, pos, stack);
        else LibUtil.popResourceFromFace(world, pos, direction, stack);

        world.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
    }

    public void dropGrapeSeeds(Level world, BlockState state, BlockPos pos, Direction direction) {
        Item grape = state.getValue(GRAPE).getSeeds();
        ItemStack stack = new ItemStack(grape);

        if (direction == null) popResource(world, pos, stack);
        else LibUtil.popResourceFromFace(world, pos, direction, stack);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        final int age = state.getValue(AGE);
        if (age > 3) {
            dropGrapes(world, state, pos, hit.getDirection());
            world.setBlock(pos, state.setValue(AGE, 2), 2);
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(AGE) > 2) {
            dropGrapes(world, state, pos, null);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    public boolean hasTrunk(Level world, BlockPos pos) {
        return world.getBlockState(pos.below()).getBlock() == this;
    }

    private void boneMealGrow(Level world, BlockState state, BlockPos pos) {
        int j;
        int age = state.getValue(AGE) + Mth.nextInt(world.getRandom(), 1, 2);
        if (age > (j = 4)) {
            age = j;
        }
        world.setBlock(pos, this.withAge(state, age, state.getValue(GRAPE)), Block.UPDATE_CLIENTS);
    }

    public StemBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(GRAPE, GrapeTypeRegistry.NONE).setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, GRAPE);
    }

    public boolean isMature(BlockState state) {
        return state.getValue(AGE) >= 4;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState state) {
        return !isMature(state) && levelReader.getBlockState(blockPos.below()).getBlock() == this && state.getValue(AGE) > 0;
    }
    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        boneMealGrow(world, state, pos);
    }

    public BlockState withAge(BlockState state, int age, GrapeType type) {
        return state.setValue(AGE, age).setValue(GRAPE, type);
    }
}