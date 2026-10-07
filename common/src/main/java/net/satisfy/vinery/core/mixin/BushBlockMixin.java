package net.satisfy.vinery.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.satisfy.vinery.core.block.SpreadableGrassSlabBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public class BushBlockMixin extends Block {
    public BushBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void injected(BlockState floor, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if(floor.is(BlockTags.DIRT)){
            if(floor.getBlock() instanceof SpreadableGrassSlabBlock && floor.getValue(SlabBlock.TYPE) == SlabType.BOTTOM)
                cir.setReturnValue(false);
        }
    }
}