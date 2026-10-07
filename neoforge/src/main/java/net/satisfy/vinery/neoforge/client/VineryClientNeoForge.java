package net.satisfy.vinery.neoforge.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.satisfy.foundation.neoforge.client.FoundationArmorExtensions;
import net.satisfy.vinery.client.VineryClient;
import net.satisfy.vinery.client.gui.ApplePressGui;
import net.satisfy.vinery.client.gui.FermentationBarrelGui;
import net.satisfy.vinery.client.util.ExtraModels;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.MenuTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

@EventBusSubscriber(modid = Vinery.MOD_ID, value = Dist.CLIENT)
public class VineryClientNeoForge {
    @SubscribeEvent
    public static void onClientSetup(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            VineryClient.preInitClient();
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        VineryClient.onInitializeClient();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path packPath = ModList.get().getModFileById(Vinery.MOD_ID)
                    .getFile()
                    .findResource("resourcepacks/bushy_leaves");

            event.addRepositorySource(consumer -> {
                PackLocationInfo packLocationInfo = new PackLocationInfo(
                        ResourceLocation.fromNamespaceAndPath(Vinery.MOD_ID, "bushy_leaves").toString(),
                        Component.literal("Bushy Leaves for Vinery"),
                        PackSource.BUILT_IN,
                        Optional.empty()
                );

                Pack.ResourcesSupplier resourcesSupplier = new Pack.ResourcesSupplier() {
                    @Override
                    public @NotNull PathPackResources openPrimary(PackLocationInfo info) {
                        return new PathPackResources(info, packPath);
                    }

                    @Override
                    public @NotNull PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
                        return new PathPackResources(info, packPath);
                    }
                };

                Pack pack = Pack.readMetaAndCreate(
                        packLocationInfo,
                        resourcesSupplier,
                        PackType.CLIENT_RESOURCES,
                        new PackSelectionConfig(false, Pack.Position.TOP, false)
                );

                if (pack != null) {
                    consumer.accept(pack);
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        ExtraModels.all().forEach(id -> event.register(ModelResourceLocation.standalone(id)));
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.APPLE_PRESS_MENU.get(), ApplePressGui::new);
        event.register(MenuTypeRegistry.FERMENTATION_BARREL_MENU.get(), FermentationBarrelGui::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(FoundationArmorExtensions.INSTANCE, ObjectRegistry.STRAW_HAT.get(), ObjectRegistry.WINEMAKER_APRON.get(), ObjectRegistry.WINEMAKER_LEGGINGS.get(), ObjectRegistry.WINEMAKER_BOOTS.get());
    }
}
