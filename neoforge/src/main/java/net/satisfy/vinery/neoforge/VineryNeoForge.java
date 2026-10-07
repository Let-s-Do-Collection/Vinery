package net.satisfy.vinery.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.neoforge.core.config.VineryNeoForgeConfig;
import net.satisfy.vinery.neoforge.core.registry.VineryNeoForgeVillagers;
import net.satisfy.vinery.platform.neoforge.PlatformHelperImpl;

@Mod(Vinery.MOD_ID)
public class VineryNeoForge {
    public VineryNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        PlatformHelperImpl.ENTITY_TYPES.register();
        Vinery.init();

        modContainer.registerConfig(ModConfig.Type.COMMON, VineryNeoForgeConfig.COMMON_CONFIG, "vinery.toml");

        modEventBus.register(VineryNeoForgeConfig.class);

        VineryNeoForgeVillagers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Vinery.commonSetup();
        });
    }
}