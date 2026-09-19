package net.satisfy.vinery.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.core.block.GrapeBush;
import net.satisfy.vinery.core.util.GrapeType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Fox.FoxEatBerriesGoal.class)
public abstract class FoxEntityEatSweetBerriesGoalMixin extends MoveToBlockGoal {
    @Final
    @Shadow
    Fox field_17975;

    public FoxEntityEatSweetBerriesGoalMixin(PathfinderMob mob, double speed, int range) {
        super(mob, speed, range);
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
    private void isTargetPos(LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof GrapeBush) {
            cir.setReturnValue(state.getValue(GrapeBush.AGE) >= 2);
        }
    }

    @Inject(method = "onReachedTarget", at = @At("TAIL"))
    private void eatGrapes(CallbackInfo ci) {
        final BlockState state = field_17975.level().getBlockState(this.blockPos);
        if (state.getBlock() instanceof GrapeBush bush) {
            pickGrapes(state, bush.grapeType());
        }
    }

    @Unique
    private void pickGrapes(BlockState state, GrapeType type) {
        final int age = state.getValue(GrapeBush.AGE);
        int j = 1 + field_17975.level().random.nextInt(2) + (age == 3 ? 1 : 0);
        ItemStack itemStack = field_17975.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack grape = getGrapeFor(type);
        if (itemStack.isEmpty()) {
            field_17975.setItemSlot(EquipmentSlot.MAINHAND, grape);
            --j;
        }
        if (j > 0) {
            Block.popResource(field_17975.level(), this.blockPos, new ItemStack(grape.getItem(), j));
        }
        field_17975.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
        field_17975.level().setBlock(this.blockPos, state.setValue(GrapeBush.AGE, 1), 2);
    }

    @Unique
    private static ItemStack getGrapeFor(GrapeType type) {
        return type.getFruit().getDefaultInstance();
    }
}