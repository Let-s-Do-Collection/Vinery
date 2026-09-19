package net.satisfy.vinery.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.item.GrapeItem;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.SoundEventRegistry;
import net.satisfy.vinery.core.block.state.properties.GrapeProperty;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class GrapevinePotBlock extends Block {
    private static final VoxelShape FILLING_SHAPE = Shapes.or(
            Block.box(15.0, 0.0, 0.0,  16.0, 10.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 1.0, 10.0,  16.0),
            Block.box(1.0, 0.0, 0.0, 15.0, 10.0, 1.0),
            Block.box(1.0, 0.0, 15.0, 15.0, 10.0, 16.0),
            Block.box(0, 0, 0, 16.0, 6, 0.0),
            Block.box(0, 0, 16, 16, 6,16),
            Block.box(16, 0, 0, 16, 6, 16),
            Block.box(0, 0, 0, 0, 5, 16),
            Block.box(1, 1, 1, 15, 1, 15)
    );

    private static final VoxelShape SMASHING_SHAPE = Shapes.or(
            FILLING_SHAPE,
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0)
    );
    private static final int MAX_STAGE = 6;
    private static final int MAX_STORAGE = 6;
    private static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);
    private static final IntegerProperty STORAGE = IntegerProperty.create("storage", 0, MAX_STORAGE);
    private static final int DECREMENT_PER_WINE_BOTTLE = 3;
    private static final GrapeProperty GRAPEVINE_TYPE = GrapeProperty.create("type");

    public GrapevinePotBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(STAGE, 0).setValue(STORAGE, 0).setValue(GRAPEVINE_TYPE, GrapeTypeRegistry.NONE));
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(STAGE) < 3) {
            return super.getCollisionShape(state, world, pos, context);
        } else {
            return SMASHING_SHAPE;
        }
    }
    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(world, state, pos, entity, fallDistance);
        if (entity instanceof LivingEntity) {
            final int activeStage = state.getValue(STAGE);
            if (activeStage >= 3) {
                if (activeStage < MAX_STAGE) {
                    world.setBlock(pos, state.setValue(STAGE, activeStage + 1), Block.UPDATE_ALL);
                }
                world.playSound(null, pos, SoundEventRegistry.BLOCK_GRAPEVINE_POT_SQUEEZE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    private boolean canTakeWine(BlockState state, ItemStack stackInHand) {
        final int storage = state.getValue(STORAGE);
        final int stage = state.getValue(STAGE);
        if (canTakeWine(storage) && stage == MAX_STAGE) {
            return stackInHand.is(ObjectRegistry.WINE_BOTTLE.get().asItem());
        } else {
            return false;
        }
    }
    private boolean canTakeWine(int storage) {
        return switch (storage) {
            case 3, 6 -> true;
            default -> false;
        };
    }
    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack,BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(STAGE) > 3 || state.getValue(STORAGE) >= MAX_STORAGE) {
            if (stack.getItem() instanceof GrapeItem) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        if (stack.getItem() instanceof GrapeItem grape) {
            if (!player.isCreative()) stack.shrink(1);
            final int stage = state.getValue(STAGE);
            final int storage = state.getValue(STORAGE);
            boolean playSound = false;
            if (stage == 0) {
                world.setBlock(pos, this.defaultBlockState().setValue(STAGE, 1).setValue(STORAGE, 1).setValue(GRAPEVINE_TYPE, grape.getType()), Block.UPDATE_ALL);
                playSound = true;
            }
            if (!isFilled(state)) {
                final BlockState newState = world.getBlockState(pos);
                world.setBlock(pos, newState.setValue(STORAGE, storage + 1), Block.UPDATE_ALL);
                playSound = true;
            }
            final BlockState newState = world.getBlockState(pos);
            final int newStage = newState.getValue(STAGE);
            final int newStorage = newState.getValue(STORAGE);
            switch (newStorage) {
                case 3, 6 -> {
                    if (newStage < 3) {
                        world.setBlock(pos, newState.setValue(STAGE, newStage + 1), Block.UPDATE_ALL);
                    }
                }
            }
            if (playSound) {
                world.playSound(player, pos, SoundEvents.CORAL_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.SUCCESS;
        } else if (stack.is(ObjectRegistry.WINE_BOTTLE.get().asItem())) {
            if (canTakeWine(state, stack)) {
                final ItemStack output = state.getValue(GRAPEVINE_TYPE).getBottle().getDefaultInstance();
                int storage = state.getValue(STORAGE);
                int newStorage = (storage - DECREMENT_PER_WINE_BOTTLE);
                if (newStorage == 0) {
                    world.setBlock(pos, world.getBlockState(pos).setValue(STORAGE,0).setValue(STAGE, 0), Block.UPDATE_ALL);
                } else {
                    world.setBlock(pos, world.getBlockState(pos).setValue(STORAGE, newStorage), Block.UPDATE_ALL);
                }
                if (!player.isCreative()) stack.shrink(1);
                if (!player.getInventory().add(output)) {
                    player.drop(output, false, false);
                }
                return ItemInteractionResult.SUCCESS;
            }


        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private boolean isFilled(BlockState state) {
        return state.getValue(STORAGE) >= MAX_STORAGE;
    }


    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0.9375, 0, 0, 1, 0.625, 1));
        shape = Shapes.or(shape, Shapes.box(0, 0, 0, 0.0625, 0.625, 1));
        shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0, 0.9375, 0.625, 0.0625));
        shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0.9375, 0.9375, 0.625, 1));
        shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.0625, 0.9375));

        return shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, STORAGE, GRAPEVINE_TYPE);
    }
}