package net.satisfy.vinery.neoforge.core.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ObjectRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class VineryNeoForgeVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE,Vinery.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION,Vinery.MOD_ID);

    public static final Supplier<PoiType> WINEMAKER_POI = POI_TYPES.register("winemaker_poi", () ->
            new PoiType(ImmutableSet.copyOf(ObjectRegistry.FERMENTATION_BARREL.get().getStateDefinition().getPossibleStates()), 1, 12));

    public static final Supplier<VillagerProfession> WINEMAKER = VILLAGER_PROFESSIONS.register("winemaker", () ->
            new VillagerProfession("winemaker", x -> x.value() == WINEMAKER_POI.get(), x -> x.value() == WINEMAKER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FARMER));


    public static void registerPOIs(){
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class).invoke(null, WINEMAKER_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception){
            exception.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
