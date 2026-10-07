package net.satisfy.vinery.core.registry;

import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.effect.*;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MobEffectRegistry {
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Vinery.MOD_ID,Registries.MOB_EFFECT);

    public static final ResourceLocation ARMOR_EFFECT = Vinery.identifier("armor_effect");
    public static final ResourceLocation HEALTH_EFFECT = Vinery.identifier("health_effect");
    public static final ResourceLocation LUCK_EFFECT = Vinery.identifier("luck_effect");
    public static final ResourceLocation RESISTANCE_EFFECT = Vinery.identifier("resistance_effect");
    public static final ResourceLocation EXPERIENCE_EFFECT = Vinery.identifier("experience_effect");
    public static final ResourceLocation IMPROVED_JUMP_BOOST = Vinery.identifier("double_jump");
    public static final ResourceLocation PARTY_EFFECT = Vinery.identifier("party_effect");
    public static final ResourceLocation TELEPORT = Vinery.identifier("teleport");
    public static final ResourceLocation CREEPER_EFFECT = Vinery.identifier("creeper_effect");
    public static final ResourceLocation CLIMBING_EFFECT = Vinery.identifier("climbing_effect");
    public static final ResourceLocation FROSTY_ARMOR_EFFECT = Vinery.identifier("frosty_armor");
    public static final ResourceLocation JELLIE = Vinery.identifier("jellie");
    public static final ResourceLocation LAVA_WALKER = Vinery.identifier("lava_walker");
    public static final ResourceLocation MAGNET = Vinery.identifier("magnet");
    public static final ResourceLocation WATER_WALKER = Vinery.identifier("water_walker");

    public static void register() {
        EFFECTS.register();
        EFFECTS.register(ARMOR_EFFECT, () -> new VineryEffect(MobEffectCategory.BENEFICIAL, 0x56CBFD).withModifier(Attributes.ARMOR, "armor", 2.0).withModifier(Attributes.ARMOR_TOUGHNESS, "armor_toughness", 3.0));
        EFFECTS.register(HEALTH_EFFECT, () -> new VineryEffect(MobEffectCategory.BENEFICIAL, 0x56CBFD).withModifier(Attributes.MAX_HEALTH, "max_health", 4.0));
        EFFECTS.register(LUCK_EFFECT, () -> new VineryEffect(MobEffectCategory.BENEFICIAL, 0x56CBFD).withModifier(Attributes.LUCK, "luck", 2.0));
        EFFECTS.register(RESISTANCE_EFFECT, () -> new ResistanceEffect());
        EFFECTS.register(EXPERIENCE_EFFECT, () -> new VineryEffect(MobEffectCategory.BENEFICIAL, 0x00FF00));
        EFFECTS.register(IMPROVED_JUMP_BOOST, () -> new VineryEffect(MobEffectCategory.BENEFICIAL, 0x00FF00));
        EFFECTS.register(PARTY_EFFECT, () -> new VineryEffect(MobEffectCategory.BENEFICIAL, 0xFF0000));
        EFFECTS.register(TELEPORT, () -> new TeleportEffect());
        EFFECTS.register(CREEPER_EFFECT, () -> new CreeperEffect());
        EFFECTS.register(CLIMBING_EFFECT, () -> new ClimbingEffect());
        EFFECTS.register(FROSTY_ARMOR_EFFECT, () -> new FrostyArmorEffect());
        EFFECTS.register(JELLIE, () -> new JellyEffect());
        EFFECTS.register(LAVA_WALKER, () -> new LavaWalkerEffect());
        EFFECTS.register(MAGNET, () -> new MagnetEffect());
        EFFECTS.register(WATER_WALKER, () -> new WaterWalkerEffect());
    }

    public static Holder<MobEffect> getHolder(ResourceLocation id) {
        Holder<MobEffect> holder = EFFECTS.getRegistrar().getHolder(id);
        if (holder == null) {
            throw new IllegalArgumentException("MobEffect with id " + id + " does not exist");
        }
        return holder;
    }
}
