package net.satisfy.vinery.core.block;

import com.mojang.serialization.MapCodec;
import net.satisfy.vinery.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.foundation.registry.FoundationParticles;
import net.satisfy.vinery.core.block.entity.GrapevinePotBlockEntity;
import net.satisfy.vinery.core.block.state.properties.GrapeProperty;
import net.satisfy.vinery.core.item.GrapeItem;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.SoundEventRegistry;
import net.satisfy.vinery.core.wine.GrapeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrapevinePotBlock extends BaseEntityBlock {
    public static final MapCodec<GrapevinePotBlock> CODEC = simpleCodec(GrapevinePotBlock::new);

    private static final VoxelShape SHAPE = Shapes.or(
            box(15.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            box(0.0, 0.0, 0.0, 1.0, 10.0, 16.0),
            box(1.0, 0.0, 0.0, 15.0, 10.0, 1.0),
            box(1.0, 0.0, 15.0, 15.0, 10.0, 16.0),
            box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0)
    );
    private static final VoxelShape STOMPING_SHAPE = Shapes.or(SHAPE, box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0));

    public static final int MAX_GRAPES = 6;
    public static final int GRAPES_PER_BOTTLE = 3;
    public static final int FULL_STAGE = 3;
    public static final int MAX_STAGE = 6;
    public static final int HEAVY_ARMOR = 15;

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);
    public static final IntegerProperty STORAGE = IntegerProperty.create("storage", 0, MAX_GRAPES);
    public static final GrapeProperty GRAPEVINE_TYPE = GrapeProperty.create("type");

    public GrapevinePotBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(STAGE, 0).setValue(STORAGE, 0).setValue(GRAPEVINE_TYPE, GrapeTypeRegistry.NONE));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GrapevinePotBlockEntity(pos, state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public static boolean isStompable(BlockState state) {
        return state.getValue(STAGE) >= FULL_STAGE && state.getValue(STAGE) < MAX_STAGE;
    }

    public static boolean isHeavyArmored(LivingEntity entity) {
        return entity.getArmorValue() >= HEAVY_ARMOR;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
        if (!(level instanceof ServerLevel serverLevel) || !(entity instanceof LivingEntity living) || !isStompable(state)) {
            return;
        }
        if (!(level.getBlockEntity(pos) instanceof GrapevinePotBlockEntity pot)) {
            return;
        }
        int stage = state.getValue(STAGE);
        int progress = pot.getProgress();
        if (progress == 0 && stage > FULL_STAGE) {
            progress = (stage - FULL_STAGE) * GrapevinePotBlockEntity.pointsNeeded() / (MAX_STAGE - FULL_STAGE);
        }
        int points = GrapevinePotBlockEntity.POINTS_PER_STOMP;
        if (isHeavyArmored(living)) {
            points += GrapevinePotBlockEntity.POINTS_PER_STOMP * Math.max(0, PlatformHelper.getGrapevinePotHeavyArmorBonus()) / 100;
        }
        pot.setProgress(progress + points);

        int newStage = pot.isStomped() ? MAX_STAGE : FULL_STAGE + pot.getProgress() * (MAX_STAGE - FULL_STAGE) / GrapevinePotBlockEntity.pointsNeeded();
        if (newStage != stage) {
            level.setBlock(pos, state.setValue(STAGE, newStage), Block.UPDATE_ALL);
        }
        level.blockEvent(pos, this, GrapevinePotBlockEntity.EVENT_STOMP, 0);
        level.playSound(null, pos, SoundEventRegistry.BLOCK_GRAPEVINE_POT_SQUEEZE.get(), SoundSource.BLOCKS, 1.0F, 0.9F + level.random.nextFloat() * 0.2F);
        splash(serverLevel, pos, state.getValue(GRAPEVINE_TYPE));
    }

    private static void splash(ServerLevel level, BlockPos pos, GrapeType type) {
        if (!PlatformHelper.shouldShowGrapevinePotSplash()) {
            return;
        }
        ColorParticleOption particle = ColorParticleOption.create(FoundationParticles.DYE_SPLASH.get(), juiceColor(type));
        level.sendParticles(particle, pos.getX() + 0.5, pos.getY() + 0.45, pos.getZ() + 0.5, 12, 0.25, 0.05, 0.25, 0.15);
    }

    public static int juiceColor(GrapeType type) {
        return type.isRed() ? 0xFF7A1F3D : 0xFFD9D27C;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof GrapeItem grape) {
            return addGrape(stack, grape.getType(), state, level, pos, player);
        }
        if (stack.is(ObjectRegistry.WINE_BOTTLE.get().asItem()) && canTakeJuice(state)) {
            return takeJuice(stack, state, level, pos, player);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult addGrape(ItemStack stack, GrapeType type, BlockState state, Level level, BlockPos pos, Player player) {
        int storage = state.getValue(STORAGE);
        GrapeType current = state.getValue(GRAPEVINE_TYPE);
        if (state.getValue(STAGE) >= FULL_STAGE || storage >= MAX_GRAPES || storage > 0 && !current.equals(type)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide()) {
            int newStorage = storage + 1;
            level.setBlock(pos, state.setValue(STORAGE, newStorage).setValue(STAGE, fillStage(newStorage)).setValue(GRAPEVINE_TYPE, type), Block.UPDATE_ALL);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        level.playSound(player, pos, SoundEvents.CORAL_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private static int fillStage(int storage) {
        if (storage >= MAX_GRAPES) {
            return FULL_STAGE;
        }
        return storage >= GRAPES_PER_BOTTLE ? 2 : 1;
    }

    public static boolean canTakeJuice(BlockState state) {
        return state.getValue(STAGE) == MAX_STAGE && state.getValue(STORAGE) >= GRAPES_PER_BOTTLE;
    }

    public static int bottlesLeft(BlockState state) {
        return state.getValue(STAGE) == MAX_STAGE ? state.getValue(STORAGE) / GRAPES_PER_BOTTLE : 0;
    }

    private ItemInteractionResult takeJuice(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide()) {
            ItemStack output = state.getValue(GRAPEVINE_TYPE).getBottle().getDefaultInstance();
            int newStorage = state.getValue(STORAGE) - GRAPES_PER_BOTTLE;
            if (newStorage <= 0) {
                level.setBlock(pos, defaultBlockState(), Block.UPDATE_ALL);
                if (level.getBlockEntity(pos) instanceof GrapevinePotBlockEntity pot) {
                    pot.setProgress(0);
                }
            } else {
                level.setBlock(pos, state.setValue(STORAGE, newStorage), Block.UPDATE_ALL);
                level.blockEvent(pos, this, GrapevinePotBlockEntity.EVENT_BOTTLE, 0);
            }
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            if (!player.getInventory().add(output)) {
                player.drop(output, false);
            }
        }
        level.playSound(player, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && state.getValue(STAGE) <= FULL_STAGE && state.getValue(STORAGE) > 0
                && level.getBlockEntity(pos) instanceof GrapevinePotBlockEntity pot && pot.getProgress() == 0) {
            popResource(level, pos, new ItemStack(state.getValue(GRAPEVINE_TYPE).getFruit(), state.getValue(STORAGE)));
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(STAGE) < FULL_STAGE ? SHAPE : STOMPING_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, STORAGE, GRAPEVINE_TYPE);
    }
}
