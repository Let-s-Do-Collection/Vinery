package net.satisfy.vinery.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.component.WineYearComponent;
import net.satisfy.vinery.core.item.FoodComponent;

public class DataComponentRegistry
{
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Vinery.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<FoodComponent>> CUSTOM_FOOD = COMPONENTS.register("custom_food",
            () -> DataComponentType.<FoodComponent>builder().persistent(FoodComponent.DIRECT_CODEC).networkSynchronized(FoodComponent.DIRECT_STREAM_CODEC).build());

    public static final RegistrySupplier<DataComponentType<WineYearComponent>> WINE_YEAR =
            COMPONENTS.register("wine_year", () ->
                    DataComponentType.<WineYearComponent>builder()
                            .persistent(WineYearComponent.CODEC)
                            .networkSynchronized(WineYearComponent.STREAM_CODEC)
                            .build()
            );

}
