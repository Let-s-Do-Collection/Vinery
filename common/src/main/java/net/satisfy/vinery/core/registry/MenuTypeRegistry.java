package net.satisfy.vinery.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.satisfy.vinery.core.menu.ApplePressMenu;
import net.satisfy.vinery.core.menu.FermentationBarrelMenu;
import net.satisfy.vinery.core.Vinery;

import java.util.function.Supplier;

public class MenuTypeRegistry {
    private static final Registrar<MenuType<?>> MENU_TYPES = DeferredRegister.create(Vinery.MOD_ID, Registries.MENU).getRegistrar();

    public static final RegistrySupplier<MenuType<FermentationBarrelMenu>> FERMENTATION_BARREL_MENU = register("fermentation_barrel_gui_handler", () -> new MenuType<>(FermentationBarrelMenu::new, FeatureFlags.VANILLA_SET));
    public static final RegistrySupplier<MenuType<ApplePressMenu>> APPLE_PRESS_MENU = register("apple_press_gui_handler", () -> new MenuType<>(ApplePressMenu::new, FeatureFlags.VANILLA_SET));

    public static <T extends AbstractContainerMenu> RegistrySupplier<MenuType<T>> register(String name, Supplier<MenuType<T>> menuType){
        return MENU_TYPES.register(Vinery.identifier(name), menuType);
    }

    public static void init() {
    }
}
