package net.satisfy.vinery.core.block;

import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.foundation.storage.StorageBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.vinery.core.registry.StorageTypeRegistry;
import net.satisfy.vinery.core.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BigBottleStorageBlock extends StorageBlock {

    public BigBottleStorageBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(BigBottleStorageBlock::new);
    }

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private static final SoundEvent OPEN_SOUND = SoundEvents.BAMBOO_WOOD_DOOR_OPEN;


    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(player.getUsedItemHand());
        if (player.isShiftKeyDown() && stack.isEmpty()) {
            if (!world.isClientSide()) {
                world.playSound(null, pos, OPEN_SOUND, SoundSource.BLOCKS, 0.4f, 0.4f);
                world.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), UPDATE_ALL);
            }
            return InteractionResult.sidedSuccess(world.isClientSide());
        } else if (state.getValue(OPEN)) {
            return super.useWithoutItem(state, world, pos, player, hit);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    public boolean canInsertStack(ItemStack stack) {
        return stack.is(TagRegistry.SMALL_BOTTLE) || stack.is(TagRegistry.LARGE_BOTTLE);
    }

    @Override
    public BlockEntityType<?> blockEntityType() {
        return EntityTypeRegistry.STORAGE_ENTITY.get();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ResourceLocation type() {
        return StorageTypeRegistry.BIG_BOTTLE;
    }

    @Override
    public Direction[] unAllowedDirections() {
        return new Direction[]{Direction.DOWN, Direction.UP};
    }

    @Override
    public int getSection(Float x, Float y) {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        MutableComponent allBold = Component.translatable("tooltip.vinery.large_bottle_first")
                .withStyle(style -> style.withBold(true).withColor(ChatFormatting.GRAY));
        MutableComponent allRest = Component.translatable("tooltip.vinery.large_bottle_rest")
                .withStyle(ChatFormatting.GRAY);

        MutableComponent combined = Component.empty().append(allBold).append(" ").append(allRest);
        MutableComponent full = Component.translatable("tooltip.vinery.storage", combined)
                .withStyle(ChatFormatting.GRAY);

        list.add(full);
    }
}
