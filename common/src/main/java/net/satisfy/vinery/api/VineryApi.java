package net.satisfy.vinery.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.wine.GrapeType;
import net.satisfy.vinery.core.wine.JuiceUtil;

import java.util.function.Supplier;

public final class VineryApi {
    private VineryApi() {
    }

    public static GrapeType registerGrapeType(String id, boolean lattice, boolean red) {
        return GrapeTypeRegistry.registerGrapeType(id, lattice, red);
    }

    public static void registerJuice(String type, TagKey<Item> tag) {
        JuiceUtil.registerJuice(type, tag);
    }

    public static void registerJuice(String type, Supplier<Item> item) {
        JuiceUtil.registerJuice(type, item);
    }
}
