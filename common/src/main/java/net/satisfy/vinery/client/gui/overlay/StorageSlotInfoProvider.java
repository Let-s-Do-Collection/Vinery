package net.satisfy.vinery.client.gui.overlay;

import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import net.satisfy.foundation.storage.StorageBlock;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.foundation.util.ShapeUtil;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.block.BigBottleStorageBlock;
import net.satisfy.vinery.core.block.BottleStorageBlock;
import net.satisfy.vinery.core.block.WineBottleBlock;
import net.satisfy.vinery.core.block.WineBoxBlock;
import net.satisfy.vinery.core.component.WineYearComponent;
import net.satisfy.vinery.core.item.DrinkBlockItem;
import net.satisfy.vinery.core.registry.DataComponentRegistry;
import net.satisfy.vinery.core.registry.TagRegistry;
import net.satisfy.vinery.core.wine.WineYears;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.satisfy.vinery.core.util.InfoOverlayMode;
import net.minecraft.client.Minecraft;

public class StorageSlotInfoProvider implements BlockInfoProvider {
    private static final Map<StorageBlock, List<ItemStack>> FITTING = new HashMap<>();

    public StorageSlotInfoProvider() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> FITTING.clear());
    }

    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!InfoOverlayMode.isVisible(Minecraft.getInstance().player)) {
            return List.of();
        }
        if (!(state.getBlock() instanceof WineBoxBlock || state.getBlock() instanceof BigBottleStorageBlock)) {
            return describeSlot(level, pos, state, hit);
        }
        boolean open = state.getValue(BlockStateProperties.OPEN);
        InfoSection hint = InfoSection.title(Component.translatable(open ? "hud.vinery.wine_box.close_hint" : "hud.vinery.wine_box.open_hint").withStyle(ChatFormatting.GRAY));
        if (!open) {
            return List.of(hint);
        }
        List<InfoSection> sections = new ArrayList<>(describeSlot(level, pos, state, hit));
        sections.add(hint);
        return sections;
    }

    private List<InfoSection> describeSlot(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (hit == null || !(state.getBlock() instanceof StorageBlock block) || !isVineryStorage(block)) {
            return List.of();
        }
        if (!(level.getBlockEntity(pos) instanceof StorageBlockEntity storage)) {
            return List.of();
        }
        if (block instanceof WineBottleBlock) {
            List<InfoSection> bottles = new ArrayList<>();
            for (ItemStack bottle : storage.getInventory()) {
                if (!bottle.isEmpty()) {
                    bottles.add(describeStack(bottle, level));
                }
            }
            return bottles;
        }
        Optional<Tuple<Float, Float>> coordinates = ShapeUtil.getRelativeHitCoordinatesForBlockFace(hit, state.getValue(StorageBlock.FACING), block.unAllowedDirections());
        if (coordinates.isEmpty()) {
            return List.of();
        }
        int section = block.getSection(coordinates.get().getA(), coordinates.get().getB());
        if (section < 0 || section >= storage.getInventory().size()) {
            return List.of();
        }
        ItemStack stack = storage.getInventory().get(section);
        if (!stack.isEmpty()) {
            return List.of(describeStack(stack, level));
        }
        if (isWineRack(block)) {
            List<ItemStack> fitting = FITTING.computeIfAbsent(block, StorageSlotInfoProvider::fitting);
            if (!fitting.isEmpty()) {
                return List.of(InfoSection.icons(Component.translatable("hud.vinery.wine_storage.fits"), fitting, InfoSection.ROW_COLUMNS));
            }
        }
        return List.of();
    }

    private static InfoSection describeStack(ItemStack stack, Level level) {
        InfoSection slot = InfoSection.icons(stack.getHoverName(), List.of(stack), InfoSection.ROW_COLUMNS);
        if (stack.getItem() instanceof DrinkBlockItem && WineYears.isAgingEnabled()) {
            slot = slot.withLines(List.of(describeWine(stack, level)));
        }
        return slot;
    }

    private static Component describeWine(ItemStack wine, Level level) {
        WineYearComponent year = wine.get(DataComponentRegistry.WINE_YEAR.get());
        if (year == null) {
            return Component.translatable("hud.vinery.wine.fresh").withStyle(ChatFormatting.GRAY);
        }
        MutableComponent text = Component.translatable("hud.vinery.wine.age", WineYears.getWineAgeYears(wine, level));
        if (WineYears.getEffectLevel(wine, level) >= year.maxLevel()) {
            return text.append(" · ").append(Component.translatable("hud.vinery.wine.fully_aged").withStyle(ChatFormatting.GOLD));
        }
        return text.withStyle(ChatFormatting.GRAY);
    }

    private static boolean isVineryStorage(StorageBlock block) {
        return Vinery.MOD_ID.equals(block.arch$registryName().getNamespace());
    }

    private static boolean isWineRack(StorageBlock block) {
        return block instanceof BottleStorageBlock || block instanceof BigBottleStorageBlock || block instanceof WineBoxBlock;
    }

    private static List<ItemStack> fitting(StorageBlock block) {
        Set<Item> items = new LinkedHashSet<>();
        for (TagKey<Item> tag : List.of(TagRegistry.SMALL_BOTTLE, TagRegistry.LARGE_BOTTLE)) {
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                items.add(holder.value());
            }
        }
        return items.stream().map(Item::getDefaultInstance).filter(block::canInsertStack).toList();
    }
}
