package net.satisfy.vinery.core.registry;

import net.satisfy.foundation.wood.WoodStandingSignBlock;
import net.satisfy.foundation.wood.WoodWallSignBlock;
import net.satisfy.foundation.wood.WoodCeilingHangingSignBlock;
import net.satisfy.foundation.wood.WoodWallHangingSignBlock;
import net.satisfy.foundation.wood.WoodBoatItem;
import net.satisfy.foundation.storage.WallShelfBlock;
import net.satisfy.foundation.block.WindowBlock;
import net.satisfy.vinery.platform.PlatformHelper;
import net.minecraft.world.effect.MobEffects;
import net.satisfy.foundation.banner.CompletionistWallBannerBlock;
import net.satisfy.foundation.banner.CompletionistBannerBlock;
import net.satisfy.foundation.banner.BannerSettings;
import net.satisfy.foundation.block.BigTableBlock;
import net.satisfy.foundation.block.CabinetBlock;
import net.satisfy.foundation.block.ChairBlock;
import net.satisfy.foundation.block.FacingBlock;
import net.satisfy.foundation.util.LibUtil;

import net.satisfy.foundation.util.RegistryUtil;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.block.*;
import net.satisfy.vinery.core.block.state.properties.VineryWoodType;
import net.satisfy.vinery.core.item.*;
import net.satisfy.foundation.armor.TexturedArmorItem;
import net.satisfy.vinery.core.util.WineSettings;
import net.satisfy.vinery.core.world.feature.VineryConfiguredFeatures;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Vinery.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Vinery.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> DARK_CHERRY_SIGN = registerWithoutItem("dark_cherry_sign", () -> new WoodStandingSignBlock(VineryWoodType.DARK_CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN), EntityTypeRegistry.MOD_SIGN));
    public static final RegistrySupplier<Block> DARK_CHERRY_WALL_SIGN = registerWithoutItem("dark_cherry_wall_sign", () -> new WoodWallSignBlock(VineryWoodType.DARK_CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN), EntityTypeRegistry.MOD_SIGN));
    public static final RegistrySupplier<Block> DARK_CHERRY_HANGING_SIGN = registerWithoutItem("dark_cherry_hanging_sign", () -> new WoodCeilingHangingSignBlock(VineryWoodType.DARK_CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN), EntityTypeRegistry.MOD_HANGING_SIGN));
    public static final RegistrySupplier<Block> DARK_CHERRY_WALL_HANGING_SIGN = registerWithoutItem("dark_cherry_wall_hanging_sign", () -> new WoodWallHangingSignBlock(VineryWoodType.DARK_CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN), EntityTypeRegistry.MOD_HANGING_SIGN));
    public static final RegistrySupplier<Item> DARK_CHERRY_SIGN_ITEM = ITEMS.register("dark_cherry_sign", () -> new SignItem(new Item.Properties().stacksTo(16), ObjectRegistry.DARK_CHERRY_SIGN.get(), ObjectRegistry.DARK_CHERRY_WALL_SIGN.get()));
    public static final RegistrySupplier<Item> DARK_CHERRY_HANGING_SIGN_ITEM = ITEMS.register("dark_cherry_hanging_sign", () -> new HangingSignItem(ObjectRegistry.DARK_CHERRY_HANGING_SIGN.get(), ObjectRegistry.DARK_CHERRY_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final RegistrySupplier<Block> RED_GRAPE_BUSH = registerWithoutItem("red_grape_bush", () -> new GrapeBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.RED));
    public static final RegistrySupplier<Item> RED_GRAPE_SEEDS = registerItem("red_grape_seeds", () -> new GrapeBushSeedItem(RED_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.RED));
    public static final RegistrySupplier<Item> RED_GRAPE = registerItem("red_grape", () -> new GrapeItem(getSettings().food(Foods.SWEET_BERRIES), GrapeTypeRegistry.RED, RED_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> WHITE_GRAPE_BUSH = registerWithoutItem("white_grape_bush", () -> new GrapeBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.WHITE));
    public static final RegistrySupplier<Item> WHITE_GRAPE_SEEDS = registerItem("white_grape_seeds", () -> new GrapeBushSeedItem(WHITE_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.WHITE));
    public static final RegistrySupplier<Item> WHITE_GRAPE = registerItem("white_grape", () -> new GrapeItem(getSettings().food(Foods.SWEET_BERRIES), GrapeTypeRegistry.WHITE, WHITE_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> SAVANNA_RED_GRAPE_BUSH = registerWithoutItem("savanna_grape_bush_red", () -> new GrapeBush.SavannaGrapeBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.SAVANNA_RED));
    public static final RegistrySupplier<Item> SAVANNA_RED_GRAPE_SEEDS = registerItem("savanna_grape_seeds_red", () -> new GrapeBushSeedItem(SAVANNA_RED_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.SAVANNA_RED));
    public static final RegistrySupplier<Item> SAVANNA_RED_GRAPE = registerItem("savanna_grapes_red", () -> new GrapeItem(getSettings().food(Foods.SWEET_BERRIES), GrapeTypeRegistry.SAVANNA_RED, ObjectRegistry.SAVANNA_RED_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> SAVANNA_WHITE_GRAPE_BUSH = registerWithoutItem("savanna_grape_bush_white", () -> new GrapeBush.SavannaGrapeBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.SAVANNA_WHITE));
    public static final RegistrySupplier<Item> SAVANNA_WHITE_GRAPE_SEEDS = registerItem("savanna_grape_seeds_white", () -> new GrapeBushSeedItem(SAVANNA_WHITE_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.SAVANNA_WHITE));
    public static final RegistrySupplier<Item> SAVANNA_WHITE_GRAPE = registerItem("savanna_grapes_white", () -> new GrapeItem(getSettings().food(Foods.SWEET_BERRIES), GrapeTypeRegistry.SAVANNA_WHITE, ObjectRegistry.SAVANNA_WHITE_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> TAIGA_RED_GRAPE_BUSH = registerWithoutItem("taiga_grape_bush_red", () -> new GrapeBush.TaigaGrapeBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.TAIGA_RED));
    public static final RegistrySupplier<Item> TAIGA_RED_GRAPE_SEEDS = registerItem("taiga_grape_seeds_red", () -> new GrapeBushSeedItem(TAIGA_RED_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.TAIGA_RED));
    public static final RegistrySupplier<Item> TAIGA_RED_GRAPE = registerItem("taiga_grapes_red", () -> new GrapeItem(getSettings().food(Foods.SWEET_BERRIES), GrapeTypeRegistry.TAIGA_RED, ObjectRegistry.TAIGA_RED_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> TAIGA_WHITE_GRAPE_BUSH = registerWithoutItem("taiga_grape_bush_white", () -> new GrapeBush.TaigaGrapeBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.TAIGA_WHITE));
    public static final RegistrySupplier<Item> TAIGA_WHITE_GRAPE_SEEDS = registerItem("taiga_grape_seeds_white", () -> new GrapeBushSeedItem(TAIGA_WHITE_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.TAIGA_WHITE));
    public static final RegistrySupplier<Item> TAIGA_WHITE_GRAPE = registerItem("taiga_grapes_white", () -> new GrapeItem(getSettings().food(Foods.SWEET_BERRIES), GrapeTypeRegistry.TAIGA_WHITE, ObjectRegistry.TAIGA_WHITE_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> JUNGLE_RED_GRAPE_BUSH = registerWithoutItem("jungle_grape_bush_red", () -> new GrapeVineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.JUNGLE_RED));
    public static final RegistrySupplier<Item> JUNGLE_RED_GRAPE_SEEDS = registerItem("jungle_grape_seeds_red", () -> new GrapeBushSeedItem(JUNGLE_RED_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.JUNGLE_RED));
    public static final RegistrySupplier<Item> JUNGLE_RED_GRAPE = registerItem("jungle_grapes_red", () -> new GrapeItem(getSettings().food(Foods.BAKED_POTATO), GrapeTypeRegistry.JUNGLE_RED, ObjectRegistry.JUNGLE_RED_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> JUNGLE_WHITE_GRAPE_BUSH = registerWithoutItem("jungle_grape_bush_white", () -> new GrapeVineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), GrapeTypeRegistry.JUNGLE_WHITE));
    public static final RegistrySupplier<Item> JUNGLE_WHITE_GRAPE_SEEDS = registerItem("jungle_grape_seeds_white", () -> new GrapeBushSeedItem(JUNGLE_WHITE_GRAPE_BUSH.get(), getSettings(), GrapeTypeRegistry.JUNGLE_WHITE));
    public static final RegistrySupplier<Item> JUNGLE_WHITE_GRAPE = registerItem("jungle_grapes_white", () -> new GrapeItem(getSettings().food(Foods.BAKED_POTATO), GrapeTypeRegistry.JUNGLE_WHITE, ObjectRegistry.JUNGLE_WHITE_GRAPE_SEEDS.get()));
    public static final RegistrySupplier<Block> DARK_CHERRY_SAPLING = registerWithItem("dark_cherry_sapling", () -> new SaplingBlock(new TreeGrower("dark_cherry_tree",Optional.of(VineryConfiguredFeatures.DARK_CHERRY_KEY),Optional.of(VineryConfiguredFeatures.DARK_CHERRY_KEY), Optional.empty()) , BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> APPLE_TREE_SAPLING = registerWithItem("apple_tree_sapling", () -> new SaplingBlock(new TreeGrower("apple_tree",Optional.of(VineryConfiguredFeatures.APPLE_KEY),Optional.of(VineryConfiguredFeatures.APPLE_VARIANT_KEY),Optional.empty()),BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final RegistrySupplier<Item> CHERRY = registerItem("cherry", () -> new Item(getSettings().food(Foods.COOKIE)));
    public static final RegistrySupplier<Item> ROTTEN_CHERRY = registerItem("rotten_cherry", () -> new RottenCherryItem(getSettings().food(Foods.POISONOUS_POTATO)));
    public static final RegistrySupplier<Block> GRAPEVINE_LEAVES = registerWithItem("grapevine_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final RegistrySupplier<Block> DARK_CHERRY_LEAVES = registerWithItem("dark_cherry_leaves", () -> new DarkCherryLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final RegistrySupplier<Block> APPLE_LEAVES = registerWithItem("apple_leaves", () -> new AppleLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final RegistrySupplier<Block> WHITE_GRAPE_BAG = registerWithItem("white_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> RED_GRAPE_BAG = registerWithItem("red_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> CHERRY_BAG = registerWithItem("cherry_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> APPLE_BAG = registerWithItem("apple_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> GRAPEVINE_POT = registerWithItem("grapevine_pot", () -> new GrapevinePotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final RegistrySupplier<Block> FERMENTATION_BARREL = registerWithItem("fermentation_barrel", () -> new FermentationBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).noOcclusion()));
    public static final RegistrySupplier<Block> APPLE_PRESS = registerWithItem("apple_press", () -> new ApplePressBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_CHERRY_CHAIR = registerWithItem("dark_cherry_chair", () -> new ChairBlock(BlockBehaviour.Properties.of().strength(2.0f, 3.0f).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> DARK_CHERRY_TABLE = registerWithItem("dark_cherry_table", () -> new TableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> DARK_CHERRY_CABINET = registerWithItem("dark_cherry_cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD), EntityTypeRegistry.CABINET_BLOCK_ENTITY, SoundEventRegistry.CABINET_OPEN.get(), SoundEventRegistry.CABINET_CLOSE.get()));
    public static final RegistrySupplier<Block> DARK_CHERRY_DRAWER = registerWithItem("dark_cherry_drawer", () -> new CabinetBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD), EntityTypeRegistry.CABINET_BLOCK_ENTITY, SoundEventRegistry.DRAWER_OPEN.get(), SoundEventRegistry.DRAWER_CLOSE.get()));
    public static final RegistrySupplier<Block> DARK_CHERRY_WINE_RACK_BIG = registerWithItem("dark_cherry_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_CHERRY_WINE_RACK_SMALL = registerWithItem("dark_cherry_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_CHERRY_WINE_RACK_MID = registerWithItem("dark_cherry_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_CHERRY_BARREL = registerWithItem("dark_cherry_barrel", () -> new DarkCherryBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
    public static final RegistrySupplier<Block> APPLE_LOG = registerWithItem("apple_log", LibUtil::logBlock);
    public static final RegistrySupplier<Block> APPLE_WOOD = registerWithItem("apple_wood", LibUtil::logBlock);
    public static final RegistrySupplier<Block> STRIPPED_DARK_CHERRY_LOG = registerWithItem("stripped_dark_cherry_log", LibUtil::logBlock);
    public static final RegistrySupplier<Block> DARK_CHERRY_LOG = registerWithItem("dark_cherry_log", LibUtil::logBlock);
    public static final RegistrySupplier<Block> STRIPPED_DARK_CHERRY_WOOD = registerWithItem("stripped_dark_cherry_wood", LibUtil::logBlock);
    public static final RegistrySupplier<Block> DARK_CHERRY_WOOD = registerWithItem("dark_cherry_wood", LibUtil::logBlock);
    public static final RegistrySupplier<Block> DARK_CHERRY_BEAM = registerWithItem("dark_cherry_beam", LibUtil::logBlock);
    public static final RegistrySupplier<Block> DARK_CHERRY_PLANKS = registerWithItem("dark_cherry_planks", () -> new Block(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BIT).mapColor(MapColor.TERRACOTTA_RED)));
    public static final RegistrySupplier<Block> DARK_CHERRY_FLOORBOARD = registerWithItem("dark_cherry_floorboard", () -> new Block(BlockBehaviour.Properties.ofFullCopy(DARK_CHERRY_PLANKS.get())));
    public static final RegistrySupplier<Block> DARK_CHERRY_STAIRS = registerWithItem("dark_cherry_stairs", () -> new StairBlock(DARK_CHERRY_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DARK_CHERRY_PLANKS.get())));
    public static final RegistrySupplier<Block> DARK_CHERRY_SLAB = registerWithItem("dark_cherry_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DARK_CHERRY_PLANKS.get())));
    public static final RegistrySupplier<Block> DARK_CHERRY_FENCE = registerWithItem("dark_cherry_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final RegistrySupplier<Block> DARK_CHERRY_FENCE_GATE = registerWithItem("dark_cherry_fence_gate", () -> new FenceGateBlock(WoodType.CHERRY,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final RegistrySupplier<Block> DARK_CHERRY_BUTTON = registerWithItem("dark_cherry_button", () -> woodenButton(FeatureFlags.VANILLA));
    public static final RegistrySupplier<Block> DARK_CHERRY_PRESSURE_PLATE = registerWithItem("dark_cherry_pressure_plate", () -> new PressurePlateBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final RegistrySupplier<Block> DARK_CHERRY_DOOR = registerWithItem("dark_cherry_door", () -> new DoorBlock( BlockSetType.CHERRY,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final RegistrySupplier<Block> DARK_CHERRY_TRAPDOOR = registerWithItem("dark_cherry_trapdoor", () -> new TrapDoorBlock(BlockSetType.CHERRY,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));
    public static final RegistrySupplier<Item> DARK_CHERRY_BOAT = ITEMS.register("dark_cherry_boat", () -> new WoodBoatItem(EntityTypeRegistry.DARK_CHERRY_BOAT, VineryWoodType.DARK_CHERRY_ID, new Item.Properties()));
    public static final RegistrySupplier<Item> DARK_CHERRY_CHEST_BOAT = ITEMS.register("dark_cherry_chest_boat", () -> new WoodBoatItem(EntityTypeRegistry.DARK_CHERRY_CHEST_BOAT, VineryWoodType.DARK_CHERRY_ID, new Item.Properties()));
    public static final RegistrySupplier<Block> WINDOW_BLOCK = registerWithItem("window_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> WINDOW = registerWithItem("window", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));
    public static final RegistrySupplier<Block> COARSE_DIRT_SLAB = registerWithItem("coarse_dirt_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COARSE_DIRT)));
    public static final RegistrySupplier<Block> DIRT_SLAB = registerWithItem("dirt_slab", () -> new DirtSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final RegistrySupplier<Block> GRASS_SLAB = registerWithItem("grass_slab", () -> new SpreadableGrassSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)));
    public static final RegistrySupplier<Block> DIRT_PATH_SLAB = registerWithItem("dirt_path_slab", () -> new DirtPathSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT_PATH)));
    public static final RegistrySupplier<Item> WINE_BOTTLE = registerItem("wine_bottle", () -> new Item(getSettings()));
    public static final RegistrySupplier<Item> APPLE_JUICE = registerItem("apple_juice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> RED_GRAPEJUICE = registerItem("red_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> WHITE_GRAPEJUICE = registerItem("white_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> RED_SAVANNA_GRAPEJUICE = registerItem("red_savanna_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> WHITE_SAVANNA_GRAPEJUICE = registerItem("white_savanna_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> RED_TAIGA_GRAPEJUICE = registerItem("red_taiga_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> WHITE_TAIGA_GRAPEJUICE = registerItem("white_taiga_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> RED_JUNGLE_GRAPEJUICE = registerItem("red_jungle_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Item> WHITE_JUNGLE_GRAPEJUICE = registerItem("white_jungle_grapejuice", () -> new GrapejuiceBottleItem(getSettings().craftRemainder(ObjectRegistry.WINE_BOTTLE.get().asItem())));
    public static final RegistrySupplier<Block> CHORUS_WINE = registerWithoutItem("chorus_wine", () -> new WineBottleBlock(getWineSettings(), 1));
    public static final RegistrySupplier<Block> CHERRY_WINE = registerWithoutItem("cherry_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> MAGNETIC_WINE = registerWithoutItem("magnetic_wine", () -> new WineBottleBlock(getWineSettings(), 1));
    public static final RegistrySupplier<Block> JO_SPECIAL_MIXTURE = registerWithoutItem("jo_special_mixture", () -> new WineBottleBlock(getWineSettings(), 1));
    public static final RegistrySupplier<Block> CRISTEL_WINE = registerWithoutItem("cristel_wine", () -> new WineBottleBlock(getWineSettings(), 1));
    public static final RegistrySupplier<Block> GLOWING_WINE = registerWithoutItem("glowing_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> CREEPERS_CRUSH = registerWithoutItem("creepers_crush", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> MEAD = registerWithoutItem("mead", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> RED_WINE = registerWithoutItem("red_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> JELLIE_WINE = registerWithoutItem("jellie_wine", () -> new WineBottleBlock(getWineSettings(), 1));
    public static final RegistrySupplier<Block> STAL_WINE = registerWithoutItem("stal_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> NOIR_WINE = registerWithoutItem("noir_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> BOLVAR_WINE = registerWithoutItem("bolvar_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> SOLARIS_WINE = registerWithoutItem("solaris_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> EISWEIN = registerWithoutItem("eiswein", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> CHENET_WINE = registerWithoutItem("chenet_wine", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> KELP_CIDER = registerWithoutItem("kelp_cider", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> AEGIS_WINE = registerWithoutItem("aegis_wine", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> CLARK_WINE = registerWithoutItem("clark_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> MELLOHI_WINE = registerWithoutItem("mellohi_wine", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> STRAD_WINE = registerWithoutItem("strad_wine", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> APPLE_CIDER = registerWithoutItem("apple_cider", () -> new WineBottleBlock(getWineSettings(), 2));
    public static final RegistrySupplier<Block> APPLE_WINE = registerWithoutItem("apple_wine", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> LILITU_WINE = registerWithoutItem("lilitu_wine", () -> new WineBottleBlock(getWineSettings(), 1));
    public static final RegistrySupplier<Block> BOTTLE_MOJANG_NOIR = registerWithoutItem("bottle_mojang_noir", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Block> VILLAGERS_FRIGHT = registerWithoutItem("villagers_fright", () -> new WineBottleBlock(getWineSettings(), 3));
    public static final RegistrySupplier<Item> APPLE_MASH = registerItem("apple_mash", () -> new Item(getSettings().food(Foods.APPLE)));
    public static final RegistrySupplier<Block> GRAPEVINE_STEM = registerWithItem("grapevine_stem", () -> new PaleStemBlock(getGrapevineSettings()));
    public static final RegistrySupplier<Block> STORAGE_POT = registerWithItem("storage_pot", () -> new StoragePotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD), SoundEvents.BRUSH_GENERIC, SoundEvents.BRUSH_GENERIC));
    public static final RegistrySupplier<Block> WINE_BOX = registerWithItem("wine_box", () -> new WineBoxBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_CHERRY_BIG_TABLE = registerWithItem("dark_cherry_big_table", () -> new BigTableBlock(BlockBehaviour.Properties.of().strength(2.0F, 2.0F).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> DARK_CHERRY_SHELF = registerWithItem("dark_cherry_shelf", () -> new WallShelfBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion(), EntityTypeRegistry.STORAGE_ENTITY, StorageTypeRegistry.SHELF, stack -> !(stack.getItem() instanceof BlockItem) || stack.is(TagRegistry.IGNORE_BLOCK_ITEM)));
    public static final RegistrySupplier<Block> STACKABLE_LOG = registerWithItem("stackable_log", () -> new StackableLogBlock(getLogBlockSettings().noOcclusion().lightLevel(state -> state.getValue(StackableLogBlock.FIRED) ? 13 : 0)));
    public static final RegistrySupplier<Item> STRAW_HAT = registerItem("straw_hat", () -> new TexturedArmorItem(ArmorMaterialRegistry.WINEMAKER_ARMOR, ArmorItem.Type.HELMET, getSettings().rarity(Rarity.EPIC).durability(ArmorItem.Type.HELMET.getDurability(5)), Vinery.identifier("textures/models/armor/winemaker.png")));
    public static final RegistrySupplier<Item> WINEMAKER_APRON = registerItem("winemaker_apron", () -> new TexturedArmorItem(ArmorMaterialRegistry.WINEMAKER_ARMOR, ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.EPIC).durability(ArmorItem.Type.CHESTPLATE.getDurability(5)), Vinery.identifier("textures/models/armor/winemaker.png")));
    public static final RegistrySupplier<Item> WINEMAKER_LEGGINGS = registerItem("winemaker_leggings", () -> new TexturedArmorItem(ArmorMaterialRegistry.WINEMAKER_ARMOR, ArmorItem.Type.LEGGINGS, getSettings().rarity(Rarity.RARE).durability(ArmorItem.Type.LEGGINGS.getDurability(5)), Vinery.identifier("textures/models/armor/winemaker.png")));
    public static final RegistrySupplier<Item> WINEMAKER_BOOTS = registerItem("winemaker_boots", () -> new TexturedArmorItem(ArmorMaterialRegistry.WINEMAKER_ARMOR, ArmorItem.Type.BOOTS, getSettings().rarity(Rarity.RARE).durability(ArmorItem.Type.BOOTS.getDurability(5)), Vinery.identifier("textures/models/armor/winemaker.png")));
    public static final RegistrySupplier<Item> MULE_SPAWN_EGG = registerItem("mule_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityTypeRegistry.MULE, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> WANDERING_WINEMAKER_SPAWN_EGG = registerItem("wandering_winemaker_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityTypeRegistry.WANDERING_WINEMAKER, -1, -1, getSettings()));
    public static final RegistrySupplier<Block> POTTED_APPLE_TREE_SAPLING = registerWithoutItem("potted_apple_tree_sapling", () -> new FlowerPotBlock(ObjectRegistry.APPLE_TREE_SAPLING.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final RegistrySupplier<Block> POTTED_DARK_CHERRY_TREE_SAPLING = registerWithoutItem("potted_dark_cherry_tree_sapling", () -> new FlowerPotBlock(ObjectRegistry.DARK_CHERRY_SAPLING.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final RegistrySupplier<Block> OAK_WINE_RACK_BIG = registerWithItem("oak_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> OAK_WINE_RACK_SMALL = registerWithItem("oak_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> OAK_WINE_RACK_MID = registerWithItem("oak_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> BIRCH_WINE_RACK_BIG = registerWithItem("birch_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> BIRCH_WINE_RACK_SMALL = registerWithItem("birch_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> BIRCH_WINE_RACK_MID = registerWithItem("birch_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> SPRUCE_WINE_RACK_BIG = registerWithItem("spruce_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> SPRUCE_WINE_RACK_SMALL = registerWithItem("spruce_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> SPRUCE_WINE_RACK_MID = registerWithItem("spruce_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_OAK_WINE_RACK_BIG = registerWithItem("dark_oak_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_OAK_WINE_RACK_SMALL = registerWithItem("dark_oak_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_OAK_WINE_RACK_MID = registerWithItem("dark_oak_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> JUNGLE_WINE_RACK_BIG = registerWithItem("jungle_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> JUNGLE_WINE_RACK_SMALL = registerWithItem("jungle_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> JUNGLE_WINE_RACK_MID = registerWithItem("jungle_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> ACACIA_WINE_RACK_BIG = registerWithItem("acacia_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> ACACIA_WINE_RACK_SMALL = registerWithItem("acacia_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> ACACIA_WINE_RACK_MID = registerWithItem("acacia_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> MANGROVE_WINE_RACK_BIG = registerWithItem("mangrove_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> MANGROVE_WINE_RACK_SMALL = registerWithItem("mangrove_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> MANGROVE_WINE_RACK_MID = registerWithItem("mangrove_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> BAMBOO_WINE_RACK_BIG = registerWithItem("bamboo_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> BAMBOO_WINE_RACK_SMALL = registerWithItem("bamboo_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> BAMBOO_WINE_RACK_MID = registerWithItem("bamboo_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> CHERRY_WINE_RACK_BIG = registerWithItem("cherry_wine_rack_big", () -> new NineBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> CHERRY_WINE_RACK_SMALL = registerWithItem("cherry_wine_rack_small", () -> new FourBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> CHERRY_WINE_RACK_MID = registerWithItem("cherry_wine_rack_mid", () -> new BigBottleStorageBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    private static final BannerSettings BANNER_SETTINGS = new BannerSettings(
            () -> EntityTypeRegistry.VINERY_STANDARD.get(),
            () -> ObjectRegistry.VINERY_WALL_STANDARD.get(),
            Vinery.identifier("textures/banner/vinery_banner.png"),
            "tooltip.vinery.banner",
            MobEffects.REGENERATION,
            () -> PlatformHelper.shouldGiveEffect() ? 8 : 0,
            () -> 1);
    public static final RegistrySupplier<Block> VINERY_STANDARD = registerWithItem("vinery_standard", () -> new CompletionistBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD), BANNER_SETTINGS));
    public static final RegistrySupplier<Block> VINERY_WALL_STANDARD = registerWithoutItem("vinery_wall_standard", () -> new CompletionistWallBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD), BANNER_SETTINGS));
    public static final RegistrySupplier<Block> OAK_LATTICE = registerWithItem("oak_lattice", () -> new LatticeBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(Blocks.OAK_PLANKS.defaultBlockState().getSoundType()).noOcclusion()));
    public static final RegistrySupplier<Block> SPRUCE_LATTICE = registerWithItem("spruce_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.SPRUCE_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> CHERRY_LATTICE = registerWithItem("cherry_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.CHERRY_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> BIRCH_LATTICE = registerWithItem("birch_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.BIRCH_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> DARK_OAK_LATTICE = registerWithItem("dark_oak_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.DARK_OAK_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> ACACIA_LATTICE = registerWithItem("acacia_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.ACACIA_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> BAMBOO_LATTICE = registerWithItem("bamboo_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.BAMBOO_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> JUNGLE_LATTICE = registerWithItem("jungle_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.JUNGLE_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> MANGROVE_LATTICE = registerWithItem("mangrove_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.MANGROVE_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> DARK_CHERRY_LATTICE = registerWithItem("dark_cherry_lattice",
            () -> new LatticeBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(Blocks.CHERRY_PLANKS.defaultBlockState().getSoundType())
                    .noOcclusion()));

    public static final RegistrySupplier<Block> WHITE_SAVANNA_GRAPE_BAG = registerWithItem("white_savanna_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> RED_SAVANNA_GRAPE_BAG = registerWithItem("red_savanna_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> WHITE_TAIGA_GRAPE_BAG = registerWithItem("white_taiga_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> RED_TAIGA_GRAPE_BAG = registerWithItem("red_taiga_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> WHITE_JUNGLE_GRAPE_BAG = registerWithItem("white_jungle_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Block> RED_JUNGLE_GRAPE_BAG = registerWithItem("red_jungle_grape_bag", () -> new FacingBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOL)));
    public static final RegistrySupplier<Item> APPLE_CIDER_ITEM = registerWineItem("apple_cider", APPLE_CIDER, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> APPLE_WINE_ITEM = registerWineItem("apple_wine", APPLE_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> MEAD_ITEM = registerWineItem("mead", MEAD, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> GLOWING_WINE_ITEM = registerWineItem("glowing_wine", GLOWING_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> SOLARIS_WINE_ITEM = registerWineItem("solaris_wine", SOLARIS_WINE, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> KELP_CIDER_ITEM = registerWineItem("kelp_cider", KELP_CIDER, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> EISWEIN_ITEM = registerWineItem("eiswein", EISWEIN, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> AEGIS_WINE_ITEM = registerWineItem("aegis_wine", AEGIS_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> VILLAGERS_FRIGHT_ITEM = registerWineItem("villagers_fright", VILLAGERS_FRIGHT, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> CLARK_WINE_ITEM = registerWineItem("clark_wine", CLARK_WINE, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> JELLIE_WINE_ITEM = registerWineItem("jellie_wine", JELLIE_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> NOIR_WINE_ITEM = registerWineItem("noir_wine", NOIR_WINE, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> RED_WINE_ITEM = registerWineItem("red_wine", RED_WINE, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> STRAD_WINE_ITEM = registerWineItem("strad_wine", STRAD_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> CHERRY_WINE_ITEM = registerWineItem("cherry_wine", CHERRY_WINE, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> CRISTEL_WINE_ITEM = registerWineItem("cristel_wine", CRISTEL_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> LILITU_WINE_ITEM = registerWineItem("lilitu_wine", LILITU_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> JO_SPECIAL_MIXTURE_ITEM = registerWineItem("jo_special_mixture", JO_SPECIAL_MIXTURE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> BOLVAR_WINE_ITEM = registerWineItem("bolvar_wine", BOLVAR_WINE, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> MAGNETIC_WINE_ITEM = registerWineItem("magnetic_wine", MAGNETIC_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> STAL_WINE_ITEM = registerWineItem("stal_wine", STAL_WINE, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> CHENET_WINE_ITEM = registerWineItem("chenet_wine", CHENET_WINE, true, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> BOTTLE_MOJANG_NOIR_ITEM = registerWineItem("bottle_mojang_noir", BOTTLE_MOJANG_NOIR, true, DrinkBlockItem.BottleSize.SMALL);
    public static final RegistrySupplier<Item> CHORUS_WINE_ITEM = registerWineItem("chorus_wine", CHORUS_WINE, false, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> CREEPERS_CRUSH_ITEM = registerWineItem("creepers_crush", CREEPERS_CRUSH, false, DrinkBlockItem.BottleSize.BIG);
    public static final RegistrySupplier<Item> MELLOHI_WINE_ITEM = registerWineItem("mellohi_wine", MELLOHI_WINE, false, DrinkBlockItem.BottleSize.BIG);
    public static void init() {
        ITEMS.register();
        BLOCKS.register();
    }

    public static BlockBehaviour.Properties properties(float strength) {
        return properties(strength, strength);
    }

    public static BlockBehaviour.Properties properties(float breakSpeed, float explosionResist) {
        return BlockBehaviour.Properties.of().strength(breakSpeed, explosionResist);
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }

    private static WineSettings createWineSettings(Holder<MobEffect> effect, int duration, int strength) {
        return new WineSettings(effect, duration, strength);
    }

    private static RegistrySupplier<Item> registerWineItem(String name, Supplier<Block> wineBlock, boolean scaleDurationWithAge, DrinkBlockItem.BottleSize size) {
        return registerItem(name, () -> new DrinkBlockItem(
                wineBlock.get(),
                new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().build()),
                scaleDurationWithAge,
                size
        ));
    }
    //private static RegistrySupplier<Item> registerFixedDurationWineItem(String name, Supplier<Block> wineBlock, int fixedDurationTicks, Holder<MobEffect> effectSupplier, int amplifier, DrinkBlockItem.BottleSize size) { return registerItem(name, () -> new DrinkBlockItem(wineBlock.get(), new Item.Properties().food(new FoodProperties.Builder().effect(new MobEffectInstance(effectSupplier, fixedDurationTicks, amplifier), 1.0f).saturationModifier(0.3f).alwaysEdible().build()), fixedDurationTicks, false, size)); }


    private static BlockBehaviour.Properties getGrapevineSettings() {
        return BlockBehaviour.Properties.of().strength(2.0F).randomTicks().sound(SoundType.WOOD).noOcclusion();
    }

    private static BlockBehaviour.Properties getLogBlockSettings() {
        return BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties getWineSettings() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().instabreak();
    }

    private static ButtonBlock woodenButton(FeatureFlag... featureFlags) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
        if (featureFlags.length > 0) {
            properties = properties.requiredFeatures(featureFlags);
        }

        return new ButtonBlock( BlockSetType.CHERRY, 30, properties);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return RegistryUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, Vinery.identifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return RegistryUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, Vinery.identifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return RegistryUtil.registerItem(ITEMS, ITEM_REGISTRAR, Vinery.identifier(path), itemSupplier);
    }
}
