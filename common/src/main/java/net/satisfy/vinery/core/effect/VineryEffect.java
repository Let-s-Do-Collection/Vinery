package net.satisfy.vinery.core.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.satisfy.vinery.core.Vinery;

public class VineryEffect extends MobEffect {
    public VineryEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public VineryEffect withModifier(Holder<Attribute> attribute, String id, double amount) {
        addAttributeModifier(attribute, Vinery.identifier(id), amount, AttributeModifier.Operation.ADD_VALUE);
        return this;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
