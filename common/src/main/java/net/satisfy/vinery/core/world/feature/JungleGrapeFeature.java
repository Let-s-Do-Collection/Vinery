package net.satisfy.vinery.core.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.satisfy.vinery.core.block.GrapeVineBlock;

public class JungleGrapeFeature extends Feature<BlockStateConfiguration> {
    public JungleGrapeFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        int tries = 12;

        int xz = 7;
        int height = 10;

        int length = 12;

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for(int i = 0; i < tries; i++) {
            mutable.set(context.origin()).move(
                    context.random().nextInt((xz * 2) + 1) - xz,
                    context.random().nextInt(height) - 1,
                    context.random().nextInt((xz * 2) + 1) - xz
            );

            if(!context.level().isEmptyBlock(mutable)) {
                continue;
            }

            BlockPos.MutableBlockPos vineMutablePos = new BlockPos.MutableBlockPos().set(mutable);
            ChunkPos currentChunkPos = new ChunkPos(vineMutablePos);
            BlockState currentBlockstate;
            BlockState aboveBlockstate;

            int maxLength = length - context.random().nextInt(context.random().nextInt(length) + 1);
            int targetY = vineMutablePos.getY() - maxLength;

            for (; vineMutablePos.getY() >= targetY; vineMutablePos.move(Direction.DOWN)) {
                if (context.level().isEmptyBlock(vineMutablePos)) {
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        mutable.set(vineMutablePos).move(direction);
                        ChunkPos newChunkPos = new ChunkPos(mutable);

                        if(newChunkPos.x != currentChunkPos.x || newChunkPos.z != currentChunkPos.z) continue;

                        currentBlockstate = context.config().state.setValue(GrapeVineBlock.getPropertyForFace(direction), true);
                        aboveBlockstate = context.level().getBlockState(vineMutablePos.above());

                        if (currentBlockstate.canSurvive(context.level(), vineMutablePos) && context.level().getBlockState(vineMutablePos.relative(direction)).getBlock() != Blocks.MOSS_CARPET) {
                            context.level().setBlock(vineMutablePos, currentBlockstate.setValue(VineBlock.UP, aboveBlockstate.canOcclude()).setValue(GrapeVineBlock.AGE, context.random().nextInt(3)), 2);
                            break;
                        }
                        else if (aboveBlockstate.is(context.config().state.getBlock())) {
                            context.level().setBlock(vineMutablePos, aboveBlockstate.setValue(VineBlock.UP, false).setValue(GrapeVineBlock.AGE, context.random().nextInt(3)), 2);
                            break;
                        }
                    }
                }
                else {
                    break;
                }
            }
        }

        return true;
    }
}
