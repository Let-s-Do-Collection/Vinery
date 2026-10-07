package net.satisfy.vinery.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.fabric.config.VineryFabricConfig;
import net.satisfy.vinery.fabric.core.registry.VineryFabricVillagers;
import net.satisfy.vinery.fabric.core.world.VineryBiomeModification;

import java.util.Optional;

public class VineryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(VineryFabricConfig.class, GsonConfigSerializer::new);
        VineryFabricVillagers.registerPOIAndProfession();

        Vinery.init();
        VineryBiomeModification.init();
        Vinery.commonSetup();

        ServerLifecycleEvents.SERVER_STARTED.register(VineryFabricVillagers::init);

        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(Vinery.MOD_ID);
        modContainer.ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(
                ResourceLocation.fromNamespaceAndPath(Vinery.MOD_ID, "bushy_leaves"),
                container,
                ResourcePackActivationType.NORMAL
        ));
    }
}