package net.satisfy.vinery.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.vinery.core.item.DrinkBlockItem;
import net.satisfy.vinery.core.wine.WineYears;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StorageBlockEntity.class, remap = false)
public abstract class StorageBlockEntityMixin extends BlockEntity {
    public StorageBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "setStack", at = @At("HEAD"))
    private void vinery$startAging(int slot, ItemStack stack, CallbackInfo ci) {
        if (level != null && !level.isClientSide() && stack.getItem() instanceof DrinkBlockItem) {
            WineYears.startStorage(stack, level, worldPosition);
        }
    }

    @Inject(method = "removeStack", at = @At("RETURN"))
    private void vinery$stopAging(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();
        if (level != null && !level.isClientSide() && stack.getItem() instanceof DrinkBlockItem) {
            WineYears.stopStorage(stack, level);
        }
    }
}
