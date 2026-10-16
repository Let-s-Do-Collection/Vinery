package net.satisfy.vinery.core.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

public final class InfoTooltips {
    private InfoTooltips() {
    }

    public static boolean canSee(@Nullable Player player) {
        return !PlatformHelper.infoTooltipsNeedStrawHat() || player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(ObjectRegistry.STRAW_HAT.get());
    }
}
