package net.satisfy.vinery.core.effect;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.satisfy.vinery.core.Vinery;

import java.util.function.BiConsumer;

public class ResistanceEffect extends MobEffect {
    private static final ResourceLocation KNOCKBACK_RESISTANCE_ID = ResourceLocation.fromNamespaceAndPath(Vinery.MOD_ID, "knockback_resistance");
    private static final ResourceLocation ARMOR_TOUGHNESS_ID = ResourceLocation.fromNamespaceAndPath(Vinery.MOD_ID, "armor_toughness_resistance");

    public ResistanceEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x56CBFD);
        this.addAttributeModifier(
                Attributes.KNOCKBACK_RESISTANCE,
                KNOCKBACK_RESISTANCE_ID,
                0.2,
                AttributeModifier.Operation.ADD_VALUE
        );
        this.addAttributeModifier(
                Attributes.ARMOR_TOUGHNESS,
                ARMOR_TOUGHNESS_ID,
                2.0,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    public void createModifiers(int pAmplifier, BiConsumer<Holder<Attribute>, AttributeModifier> biConsumer) {
        super.createModifiers(pAmplifier, biConsumer);
    }
}