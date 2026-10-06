package net.satisfy.vinery.core.block.state.properties;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.satisfy.foundation.wood.BoatWood;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ObjectRegistry;

public class VineryWoodType {
    public static final ResourceLocation DARK_CHERRY_ID = ResourceLocation.fromNamespaceAndPath(Vinery.MOD_ID, "dark_cherry");
    public static final WoodType DARK_CHERRY = WoodType.register(new WoodType(DARK_CHERRY_ID.toString(), BlockSetType.OAK));
    public static final BoatWood DARK_CHERRY_BOAT = BoatWood.register(DARK_CHERRY_ID, () -> ObjectRegistry.DARK_CHERRY_BOAT.get(), () -> ObjectRegistry.DARK_CHERRY_CHEST_BOAT.get());
}
