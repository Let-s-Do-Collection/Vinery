package net.satisfy.vinery.core.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.satisfy.vinery.core.item.DrinkBlockItem;
import net.satisfy.vinery.core.registry.MobEffectRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;

public class WineEffectSetup {

    public static void setupWineEffects() {
        setupWineItem(ObjectRegistry.APPLE_CIDER_ITEM.get(), MobEffects.DAMAGE_BOOST, 1600, 0);
        setupWineItem(ObjectRegistry.APPLE_WINE_ITEM.get(), MobEffects.DAMAGE_RESISTANCE, 1600, 0);
        setupWineItem(ObjectRegistry.MEAD_ITEM.get(), MobEffects.DIG_SPEED, 1600, 0);
        setupWineItem(ObjectRegistry.GLOWING_WINE_ITEM.get(), MobEffects.GLOWING, 1600, 0);
        setupWineItem(ObjectRegistry.SOLARIS_WINE_ITEM.get(), MobEffects.HEALTH_BOOST, 1600, 0);
        setupWineItem(ObjectRegistry.KELP_CIDER_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.WATER_WALKER), 1600, 0);
        setupWineItem(ObjectRegistry.EISWEIN_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.FROSTY_ARMOR_EFFECT), 1600, 0);
        setupWineItem(ObjectRegistry.AEGIS_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.ARMOR_EFFECT), 1600, 0);
        setupWineItem(ObjectRegistry.VILLAGERS_FRIGHT_ITEM.get(), MobEffects.BAD_OMEN, 1600, 0);
        setupWineItem(ObjectRegistry.CLARK_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.IMPROVED_JUMP_BOOST), 1600, 0);
        setupWineItem(ObjectRegistry.JELLIE_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.JELLIE), 1600, 0);
        setupWineItem(ObjectRegistry.NOIR_WINE_ITEM.get(), MobEffects.JUMP, 1600, 0);
        setupWineItem(ObjectRegistry.RED_WINE_ITEM.get(), MobEffects.SLOW_FALLING, 1600, 0);
        setupWineItem(ObjectRegistry.STRAD_WINE_ITEM.get(), MobEffects.NIGHT_VISION, 1600, 0);
        setupWineItem(ObjectRegistry.CHERRY_WINE_ITEM.get(), MobEffects.INVISIBILITY, 1600, 0);
        setupWineItem(ObjectRegistry.CRISTEL_WINE_ITEM.get(), MobEffects.WATER_BREATHING, 1600, 0);
        setupWineItem(ObjectRegistry.LILITU_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.PARTY_EFFECT), 1600, 0);
        setupWineItem(ObjectRegistry.JO_SPECIAL_MIXTURE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.CLIMBING_EFFECT), 1600, 0);
        setupWineItem(ObjectRegistry.BOLVAR_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.LAVA_WALKER), 1600, 0);
        setupWineItem(ObjectRegistry.MAGNETIC_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.MAGNET), 1600, 0);
        setupWineItem(ObjectRegistry.STAL_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.HEALTH_EFFECT), 1600, 0);
        setupWineItem(ObjectRegistry.CHENET_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.CLIMBING_EFFECT), 1600, 0);
        setupWineItem(ObjectRegistry.BOTTLE_MOJANG_NOIR_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.EXPERIENCE_EFFECT), 1600, 0);
        setupWineItem(ObjectRegistry.CHORUS_WINE_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.TELEPORT), 10, 0);
        setupWineItem(ObjectRegistry.CREEPERS_CRUSH_ITEM.get(), MobEffectRegistry.getHolder(MobEffectRegistry.CREEPER_EFFECT), 100, 0);
        setupWineItem(ObjectRegistry.MELLOHI_WINE_ITEM.get(), MobEffects.HEAL, 0, 0);
    }


    private static void setupWineItem(Object item, Holder<MobEffect> effect, int duration, int amplifier) {
        if (item instanceof DrinkBlockItem drinkItem) {
            drinkItem.setEffectSupplier(() -> effect, duration, amplifier);
        }
    }
}