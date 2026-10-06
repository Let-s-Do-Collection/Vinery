package net.satisfy.vinery.client.render.block.storage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;
import net.satisfy.foundation.storage.BottleGroupRenderer;
import net.satisfy.vinery.core.registry.ObjectRegistry;

@Environment(EnvType.CLIENT)
public class WineBottleRenderer extends BottleGroupRenderer {
    @Override
    protected boolean isLying(ItemStack stack) {
        return stack.is(ObjectRegistry.KELP_CIDER.get().asItem());
    }
}
