package net.satisfy.vinery.core.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.satisfy.vinery.core.registry.MobEffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {
    @ModifyVariable(
            method = "playerTouch",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;giveExperiencePoints(I)V"),
            ordinal = 0
    )
    private int modifyExperienceAmount(int originalAmount, Player player) {
        MobEffectInstance effect = player.getEffect(MobEffectRegistry.getHolder(MobEffectRegistry.EXPERIENCE_EFFECT));
        if (effect == null) {
            return originalAmount;
        }
        return (int) (originalAmount + originalAmount * (1 + effect.getAmplifier()) * 0.5);
    }
}
