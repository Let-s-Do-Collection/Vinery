package net.satisfy.vinery.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.satisfy.foundation.fabric.client.FoundationArmorRenderer;
import net.satisfy.vinery.client.VineryClient;
import net.satisfy.vinery.client.util.ExtraModels;
import net.satisfy.vinery.core.registry.ObjectRegistry;

public class VineryClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VineryClient.preInitClient();
        VineryClient.onInitializeClient();
        ModelLoadingPlugin.register(context -> context.addModels(ExtraModels.all()));
        ArmorRenderer.register(FoundationArmorRenderer.INSTANCE, ObjectRegistry.STRAW_HAT.get(), ObjectRegistry.WINEMAKER_APRON.get(), ObjectRegistry.WINEMAKER_LEGGINGS.get(), ObjectRegistry.WINEMAKER_BOOTS.get());
    }
}
