package net.satisfy.vinery.core.registry;

import net.minecraft.world.entity.LivingEntity;
import net.satisfy.foundation.armor.ArmorSet;

public final class ArmorSetRegistry {
    private static ArmorSet winemaker;

    private ArmorSetRegistry() {
    }

    public static void init() {
        winemaker = ArmorSet.builder("tooltip.vinery.armor.winemaker_set")
                .piece(ObjectRegistry.STRAW_HAT)
                .piece(ObjectRegistry.WINEMAKER_APRON)
                .piece(ObjectRegistry.WINEMAKER_LEGGINGS)
                .piece(ObjectRegistry.WINEMAKER_BOOTS)
                .bonus("tooltip.vinery.armor.winemaker_set.bonus")
                .register();
    }

    public static boolean hasWinemakerSet(LivingEntity entity) {
        return winemaker != null && winemaker.isComplete(entity);
    }
}
