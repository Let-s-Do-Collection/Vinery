package net.satisfy.vinery.core.block;

import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.foundation.storage.StorageBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.satisfy.vinery.core.registry.StorageTypeRegistry;
import net.satisfy.vinery.core.registry.TagRegistry;

import java.util.List;

public class NineBottleStorageBlock extends StorageBlock {


    public NineBottleStorageBlock(Properties settings) {
        super(settings);
    }
    @Override
    public BlockEntityType<?> blockEntityType() {
        return EntityTypeRegistry.STORAGE_ENTITY.get();
    }

    @Override
    public int size(){
        return 9;
    }

    @Override
    public boolean canInsertStack(ItemStack stack) {
        return stack.is(TagRegistry.SMALL_BOTTLE);
    }

    @Override
    public ResourceLocation type() {
        return StorageTypeRegistry.NINE_BOTTLE;
    }

    @Override
    public Direction[] unAllowedDirections() {
        return new Direction[]{Direction.DOWN, Direction.UP};
    }

    @Override
    public int getSection(Float x, Float y) {

        float l = (float) 1/3;

        int nSection;
        if (x < 0.375F) {
            nSection = 0;
        } else {
            nSection = x < 0.6875F ? 1 : 2;
        }

        int i = y >= l*2 ? 0 : y >= l ? 1 : 2;
        return nSection + i * 3;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        MutableComponent allBold = Component.translatable("tooltip.vinery.small_bottle_first")
                .withStyle(style -> style.withBold(true).withColor(ChatFormatting.GRAY));
        MutableComponent allRest = Component.translatable("tooltip.vinery.small_bottle_rest")
                .withStyle(ChatFormatting.GRAY);

        MutableComponent combined = Component.empty().append(allBold).append(" ").append(allRest);
        MutableComponent full = Component.translatable("tooltip.vinery.storage", combined)
                .withStyle(ChatFormatting.GRAY);

        tooltip.add(full);
    }
}
