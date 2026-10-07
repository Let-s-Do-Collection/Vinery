package net.satisfy.vinery.core.block;

import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.foundation.storage.StorageBlock;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.vinery.core.item.DrinkBlockItem;
import net.satisfy.vinery.core.registry.StorageTypeRegistry;
import net.satisfy.vinery.core.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;

public class WineBottleBlock extends StorageBlock {
    private static final VoxelShape SHAPE = Shapes.box(0.125, 0, 0.125, 0.875, 0.875, 0.875);

    public static final BooleanProperty FAKE_MODEL = BooleanProperty.create("fake_model");

    private final int maxCount;

    public WineBottleBlock(Properties settings, int maxCount) {
        super(settings);
        this.maxCount = maxCount;
        this.registerDefaultState(this.defaultBlockState().setValue(FAKE_MODEL, true));
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if(blockEntity instanceof StorageBlockEntity wineEntity){
            NonNullList<ItemStack> inventory = wineEntity.getInventory();

            if (canInsertStack(stack) && willFitStack(stack, inventory)) {
                int posInE = getFirstEmptySlot(inventory);
                if(posInE == Integer.MIN_VALUE) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                if(!level.isClientSide()){
                    wineEntity.setStack(posInE, stack.split(1));
                    if (player.isCreative()) {
                        stack.grow(1);
                    }
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            } else if (stack.isEmpty() && !isEmpty(inventory)) {
                int posInE = getLastFullSlot(inventory);
                if(posInE == Integer.MIN_VALUE) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                if(!level.isClientSide()){
                    ItemStack wine = wineEntity.removeStack(posInE);
                    if (!player.getInventory().add(wine)) {
                        player.drop(wine, false);
                    }
                    if (isEmpty(inventory)) {
                        level.destroyBlock(pos, false);
                    }
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public boolean isEmpty(NonNullList<ItemStack> inventory){
        for(ItemStack stack : inventory){
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    public int getFirstEmptySlot(NonNullList<ItemStack> inventory){
        for(ItemStack stack : inventory){
            if(stack.isEmpty()) return inventory.indexOf(stack);
        }
        return Integer.MIN_VALUE;
    }

    public int getLastFullSlot(NonNullList<ItemStack> inventory){
        for(int i = inventory.size() - 1; i >=0; i--){
            if(!inventory.get(i).isEmpty()) return i;
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FAKE_MODEL);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos)) {
            levelAccessor.destroyBlock(blockPos, true);
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }
    @Override
    public BlockEntityType<?> blockEntityType() {
        return EntityTypeRegistry.STORAGE_ENTITY.get();
    }

    @Override
    public int size() {
        return maxCount;
    }
    @Override
    public ResourceLocation type() {
        return StorageTypeRegistry.WINE_BOTTLE;
    }

    @Override
    public boolean canInsertStack(ItemStack stack) {
        return stack.is(TagRegistry.SMALL_BOTTLE);
    }

    public boolean willFitStack(ItemStack itemStack, NonNullList<ItemStack> inventory) {
        Pair<Integer, Integer> p = getFilledAmountAndBiggest(inventory);
        int biggest = p.getSecond();
        int count = p.getFirst();
        int stackCount = getCount(itemStack);
        if(biggest == Integer.MAX_VALUE) return true;

        return stackCount > count && count < biggest;
    }

    public static Pair<Integer, Integer> getFilledAmountAndBiggest(NonNullList<ItemStack> inventory){
        int count = 0;
        int biggest = Integer.MAX_VALUE;
        for(ItemStack stack : inventory){
            if(!stack.isEmpty()){
                count++;
                if(stack.getItem() instanceof DrinkBlockItem item && item.getBlock() instanceof WineBottleBlock wine && wine.maxCount < biggest){
                    biggest = wine.maxCount;
                }
            }
        }
        return new Pair<>(count, biggest);
    }

    public static int getCount(ItemStack itemStack){
        if(itemStack.getItem() instanceof DrinkBlockItem item && item.getBlock() instanceof WineBottleBlock wine){
            return wine.maxCount;
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public int getSection(Float aFloat, Float aFloat1) {
        return 0;
    }

    @Override
    public Direction[] unAllowedDirections() {
        return new Direction[0];
    }
}
