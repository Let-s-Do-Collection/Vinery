package net.satisfy.vinery.core.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

public enum InfoOverlayMode {
    OFF,
    ON,
    STRAW_HAT;

    public static boolean isVisible(@Nullable Player player) {
        return switch (PlatformHelper.getInfoOverlayMode()) {
            case OFF -> false;
            case ON -> true;
            case STRAW_HAT -> player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(ObjectRegistry.STRAW_HAT.get());
        };
    }
}
