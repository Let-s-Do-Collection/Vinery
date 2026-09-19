package net.satisfy.vinery.core.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class TeleportEffect extends InstantenousMobEffect {

    public TeleportEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF69B4);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        teleport(source);
    }

    @Override
    public boolean applyEffectTick(LivingEntity source, int i) {
        teleport(source);
        return true;
    }

    private void teleport(Entity source) {
        if (!(source instanceof Player player)) return;

        Level world = player.level();

        for (int attempt = 0; attempt < 16; attempt++) {
            double x = player.getX() + (player.getRandom().nextDouble() - 0.5) * 16.0;
            double y = player.getY() + (player.getRandom().nextDouble() - 0.5) * 16.0;
            double z = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 16.0;

            BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));

            if (world.isInWorldBounds(pos) &&
                    !world.getBlockState(pos).liquid() &&
                    !world.getBlockState(pos.above()).liquid() &&
                    !fullBlockAt(world, pos) &&
                    !fullBlockAt(world, pos.above()) &&
                    fullBlockAt(world, pos.below())) {

                if (!player.level().isClientSide) {
                    player.teleportTo(x + 0.5, pos.getY() + 0.5, z + 0.5);
                }
                player.fallDistance = 0;
                player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                return;
            }
        }
    }

    private static boolean fullBlockAt(Level world, BlockPos target){
        return Block.isShapeFullBlock(world.getBlockState(target).getCollisionShape(world, target));
    }
}
