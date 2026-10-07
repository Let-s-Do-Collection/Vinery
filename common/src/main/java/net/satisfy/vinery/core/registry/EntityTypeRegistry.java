package net.satisfy.vinery.core.registry;

import net.satisfy.foundation.wood.WoodBoat;
import net.satisfy.foundation.wood.WoodChestBoat;
import net.satisfy.foundation.wood.WoodSignBlockEntity;
import net.satisfy.foundation.wood.WoodHangingSignBlockEntity;
import net.satisfy.foundation.banner.CompletionistBannerEntity;
import net.satisfy.foundation.block.CabinetBlockEntity;
import net.satisfy.foundation.storage.StorageBlockEntity;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.block.entity.*;
import net.satisfy.vinery.core.entity.TraderMuleEntity;
import net.satisfy.vinery.core.entity.WanderingWinemakerEntity;
import net.satisfy.vinery.platform.PlatformHelper;

import java.util.HashSet;
import java.util.function.Supplier;

import static net.satisfy.vinery.core.registry.ObjectRegistry.*;

public class EntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Vinery.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Vinery.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<WoodSignBlockEntity>> MOD_SIGN = BLOCK_ENTITY_TYPES.register("mod_sign", () -> BlockEntityType.Builder.of((pos, state) -> new WoodSignBlockEntity(EntityTypeRegistry.MOD_SIGN.get(), pos, state), DARK_CHERRY_SIGN.get(), DARK_CHERRY_WALL_SIGN.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WoodHangingSignBlockEntity>> MOD_HANGING_SIGN = BLOCK_ENTITY_TYPES.register("mod_hanging_sign", () -> BlockEntityType.Builder.of((pos, state) -> new WoodHangingSignBlockEntity(EntityTypeRegistry.MOD_HANGING_SIGN.get(), pos, state), DARK_CHERRY_HANGING_SIGN.get(), DARK_CHERRY_WALL_HANGING_SIGN.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> STORAGE_ENTITY =
            registerBlockEntity("storage", () ->
                    BlockEntityType.Builder.of((pos, state) -> new StorageBlockEntity(EntityTypeRegistry.STORAGE_ENTITY.get(), pos, state), StorageTypeRegistry.registerBlocks(new HashSet<>()).toArray(new Block[0]))
                            .build(null)
            );

    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> {
        Block[] cabinetBlocks = StorageTypeRegistry.getCabinetBlocks();
        return BlockEntityType.Builder.of((pos, state) -> new CabinetBlockEntity(EntityTypeRegistry.CABINET_BLOCK_ENTITY.get(), pos, state), cabinetBlocks).build(null);
    });

    public static final RegistrySupplier<BlockEntityType<StoragePotBlockEntity>> STORAGE_POT_ENTITY =
            registerBlockEntity("storage_pot", () ->
                    BlockEntityType.Builder.of(StoragePotBlockEntity::new, STORAGE_POT.get()).build(null)
            );

    public static final RegistrySupplier<BlockEntityType<StackableLogBlockEntity>> STACKABLE_LOG = registerBlockEntity("stackable_log", () -> BlockEntityType.Builder.of(StackableLogBlockEntity::new, ObjectRegistry.STACKABLE_LOG.get()).build(null));

    public static final Supplier<EntityType<WoodBoat>> DARK_CHERRY_BOAT = PlatformHelper.registerBoatType("dark_cherry_boat", WoodBoat::new, MobCategory.MISC, 1.375F, 0.5625F, 10);
    public static final Supplier<EntityType<WoodBoat>> DARK_CHERRY_CHEST_BOAT = PlatformHelper.<WoodBoat>registerBoatType("dark_cherry_chest_boat", WoodChestBoat::new, MobCategory.MISC, 1.375F, 0.5625F, 10);

    public static final RegistrySupplier<BlockEntityType<ApplePressBlockEntity>> APPLE_PRESS_BLOCK_ENTITY = registerBlockEntity("apple_press", () -> BlockEntityType.Builder.of(ApplePressBlockEntity::new, APPLE_PRESS.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<FermentationBarrelBlockEntity>> FERMENTATION_BARREL_ENTITY = registerBlockEntity("fermentation_barrel", () -> BlockEntityType.Builder.of(FermentationBarrelBlockEntity::new, FERMENTATION_BARREL.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CompletionistBannerEntity>> VINERY_STANDARD = registerBlockEntity("vinery_standard", () -> BlockEntityType.Builder.of(CompletionistBannerEntity::new, ObjectRegistry.VINERY_STANDARD.get(), VINERY_WALL_STANDARD.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<DarkCherryBarrelBlockEntity>> DARK_CHERRY_BARREL_ENTITY =
            registerBlockEntity("dark_cherry_barrel", () ->
                    BlockEntityType.Builder.of(DarkCherryBarrelBlockEntity::new, DARK_CHERRY_BARREL.get()).build(null)
            );

    public static final RegistrySupplier<BlockEntityType<GrapevinePotBlockEntity>> GRAPEVINE_POT = registerBlockEntity("grapevine_pot", () -> BlockEntityType.Builder.of(GrapevinePotBlockEntity::new, ObjectRegistry.GRAPEVINE_POT.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<LatticeBlockEntity>> LATTICE = registerBlockEntity("lattice", () -> BlockEntityType.Builder.of(LatticeBlockEntity::new, OAK_LATTICE.get(), SPRUCE_LATTICE.get(), CHERRY_LATTICE.get(), BIRCH_LATTICE.get(), DARK_OAK_LATTICE.get(), ACACIA_LATTICE.get(), BAMBOO_LATTICE.get(), JUNGLE_LATTICE.get(), MANGROVE_LATTICE.get(), DARK_CHERRY_LATTICE.get()).build(null));

    public static final RegistrySupplier<EntityType<TraderMuleEntity>> MULE = registerEntity("mule", () -> EntityType.Builder.of(TraderMuleEntity::new, MobCategory.CREATURE).sized(0.9f, 1.4f).clientTrackingRange(10).build(Vinery.identifier("mule").toString()));
    public static final RegistrySupplier<EntityType<WanderingWinemakerEntity>> WANDERING_WINEMAKER = registerEntity("wandering_winemaker", () -> EntityType.Builder.of(WanderingWinemakerEntity::new, MobCategory.CREATURE).sized(0.6f, 1.95f).clientTrackingRange(10).build(Vinery.identifier("wandering_winemaker").toString()));

    public static <T extends EntityType<?>> RegistrySupplier<T> registerEntity(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(Vinery.identifier(path), type);
    }

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(Vinery.identifier(path), type);
    }

    static void registerAttributes() {
        EntityAttributeRegistry.register(MULE, () -> Llama.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.2f));
        EntityAttributeRegistry.register(WANDERING_WINEMAKER, () -> Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 16.0));
    }

    public static void init() {
        ENTITY_TYPES.register();
        BLOCK_ENTITY_TYPES.register();
        registerAttributes();
    }
}
