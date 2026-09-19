package net.satisfy.vinery.core.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class BasketItem extends BlockItem {
    public BasketItem(Block block, Properties settings) {
        super(block, settings.stacksTo(1));
    }

    private static Stream<ItemStack> getContents(ItemStack itemStack, HolderLookup.Provider provider) {
        CompoundTag compoundTag = itemStack.get(DataComponents.CUSTOM_DATA).copyTag();
        if (compoundTag == null) return Stream.empty();

        CompoundTag blockEntityTag = compoundTag.getCompound("BlockEntityTag");
        if (blockEntityTag == null) return Stream.empty();

        ListTag itemsList = blockEntityTag.getList("Items", Tag.TAG_COMPOUND);
        if (itemsList == null) return Stream.empty();

        return itemsList.stream()
                .filter(Objects::nonNull)
                .map(Tag.class::cast)
                .map(CompoundTag.class::cast)
                .map(tag -> ItemStack.parseOptional(provider, tag));
    }

    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack,HolderLookup.Provider provider) {
        NonNullList<ItemStack> nonNullList = NonNullList.create();
        Stream<ItemStack> var10000 = getContents(itemStack,provider);
        Objects.requireNonNull(nonNullList);
        var10000.forEach(nonNullList::add);
        return Optional.of(new BundleTooltip(new BundleContents(nonNullList)));
    }
}