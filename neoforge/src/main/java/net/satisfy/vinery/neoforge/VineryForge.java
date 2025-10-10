package net.satisfy.vinery.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.hooks.EventBusesHooks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.CompostableRegistry;
import net.satisfy.vinery.core.util.PreInit;
import net.satisfy.vinery.neoforge.core.config.VineryForgeConfig;
import net.satisfy.vinery.neoforge.core.registry.VineryNeoForgeVillagers;
import net.satisfy.vinery.platform.neoforge.PlatformHelperImpl;


@Mod(Vinery.MOD_ID)
public class VineryForge {
    public VineryForge(IEventBus modEventBus) {
        PlatformHelperImpl.ENTITY_TYPES.register();
        PreInit.preInit();
        Vinery.init();
        VineryForgeConfig.loadConfig(VineryForgeConfig.COMMON_CONFIG, Platform.getConfigFolder().resolve("vinery.toml").toString());
        VineryNeoForgeVillagers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CompostableRegistry.registerCompostable();
            Vinery.commonSetup();
        });
    }
}
