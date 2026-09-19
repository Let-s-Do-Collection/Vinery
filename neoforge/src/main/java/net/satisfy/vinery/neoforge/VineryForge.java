package net.satisfy.vinery.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.CompostableRegistry;
import net.satisfy.vinery.neoforge.core.config.VineryForgeConfig;
import net.satisfy.vinery.neoforge.core.datagen.ModAdvancementGen;
import net.satisfy.vinery.neoforge.core.registry.VineryNeoForgeVillagers;
import net.satisfy.vinery.platform.neoforge.PlatformHelperImpl;

import java.util.List;


@Mod(Vinery.MOD_ID)
public class VineryForge {
    public VineryForge(IEventBus modEventBus, ModContainer modContainer) {
        PlatformHelperImpl.ENTITY_TYPES.register();
        Vinery.init();

        modContainer.registerConfig(ModConfig.Type.COMMON, VineryForgeConfig.COMMON_CONFIG, "vinery.toml");

        modEventBus.register(VineryForgeConfig.class);

        VineryNeoForgeVillagers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CompostableRegistry.registerCompostable();
            Vinery.commonSetup();
            //VineryNeoForgeVillagers.registerPOIs();
        });
    }

    private void onGatherData(GatherDataEvent event){
        event.addProvider(new ModAdvancementGen(event.getGenerator().getPackOutput(),event.getLookupProvider(),event.getExistingFileHelper(), List.of(new ModAdvancementGen.MyAdvancementGenerator())));
    }
}