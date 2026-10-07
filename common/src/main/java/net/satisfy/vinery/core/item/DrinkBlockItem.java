package net.satisfy.vinery.core.item;

import net.satisfy.foundation.util.LibUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.vinery.core.registry.DataComponentRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.wine.WineEffects;
import net.satisfy.vinery.core.wine.WineYears;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DrinkBlockItem extends BlockItem {
    private final boolean scaleDurationWithAge;
    private final BottleSize bottleSize;

    public DrinkBlockItem(Block block, Properties settings, boolean scaleDurationWithAge, BottleSize bottleSize) {
        super(block, settings);
        this.scaleDurationWithAge = scaleDurationWithAge;
        this.bottleSize = bottleSize;
    }

    private boolean ages() {
        return scaleDurationWithAge && WineYears.isAgingEnabled();
    }

    private int duration(ItemStack stack, Level level, WineEffects.WineEffect wineEffect) {
        return ages() ? WineYears.getEffectDuration(stack, level, wineEffect.duration()) : wineEffect.duration();
    }

    private int amplifier(ItemStack stack, Level level, WineEffects.WineEffect wineEffect) {
        return wineEffect.amplifier() + (ages() ? WineYears.getEffectLevel(stack, level) : 0);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        if (!Objects.requireNonNull(context.getPlayer()).isCrouching()) {
            return null;
        }
        BlockState blockState = this.getBlock().getStateForPlacement(context);
        return blockState != null && this.canPlace(context, blockState) ? blockState : null;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos blockPos, Level level, @Nullable Player player, ItemStack itemStack, BlockState blockState) {
        if (level.getBlockEntity(blockPos) instanceof StorageBlockEntity wineEntity) {
            wineEntity.setStack(0, itemStack.copyWithCount(1));
        }
        return super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        Level level = null;
        if (tooltipContext.registries() != null) {
            level = getLevel();
        }

        WineEffects.WineEffect wineEffect = WineEffects.get(this);
        if (wineEffect != null && level != null) {
            MobEffect effect = wineEffect.effect().value();

            String effectName = effect.getDisplayName().getString();
            int amplifier = amplifier(stack, level, wineEffect);
            String amplifierRoman = amplifier > 0 ? " " + toRoman(amplifier + 1) : "";
            String tooltipText = effect.isInstantenous()
                    ? effectName + amplifierRoman
                    : effectName + amplifierRoman + " (" + formatDuration(duration(stack, level, wineEffect)) + ")";
            tooltip.add(Component.literal(tooltipText).withStyle(effect.getCategory().getTooltipFormatting()));
        } else {
            tooltip.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        }

        if (level != null && ages() && stack.get(DataComponentRegistry.WINE_YEAR.get()) != null) {
            tooltip.add(Component.empty());
            int ageYears = Math.max(0, WineYears.getWineAgeYears(stack, level));
            int ageDays = WineYears.getWineAgeDays(stack, level);
            tooltip.add(Component.translatable("tooltip.vinery.age", ageYears).withStyle(ChatFormatting.WHITE));
            tooltip.add(Component.empty());

            int daysPerYear = stack.get(DataComponentRegistry.WINE_YEAR.get()).daysPerYear();
            int yearsPerLevel = stack.get(DataComponentRegistry.WINE_YEAR.get()).yearsPerEffectLevel();
            int cycle = Math.max(1, daysPerYear * Math.max(1, yearsPerLevel));
            int daysToNextUpgrade = cycle - (ageDays % cycle);

            if (WineYears.getEffectLevel(stack, level) >= stack.get(DataComponentRegistry.WINE_YEAR.get()).maxLevel()) {
                tooltip.add(Component.translatable("hud.vinery.wine.fully_aged").withStyle(ChatFormatting.GOLD));
            } else {
                tooltip.add(Component.translatable("tooltip.vinery.next_upgrade", daysToNextUpgrade)
                        .withStyle(style -> style.withColor(TextColor.fromRgb(0x93c47d))));
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private Level getLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        WineEffects.WineEffect wineEffect = WineEffects.get(this);
        if (!level.isClientSide && wineEffect != null) {
            if (itemStack.get(DataComponentRegistry.WINE_YEAR.get()) == null) {
                WineYears.setWineYear(itemStack, level);
            }
            livingEntity.addEffect(new MobEffectInstance(wineEffect.effect(), duration(itemStack, level, wineEffect), amplifier(itemStack, level, wineEffect)));
        }
        itemStack.shrink(1);
        return LibUtil.convertStackAfterFinishUsing(livingEntity, itemStack, ObjectRegistry.WINE_BOTTLE.get(), this);
    }

    private String formatDuration(int ticks) {
        int totalSeconds = Math.max(0, ticks) / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        if (level != null && !level.isClientSide) {
            WineYears.setWineYear(stack, level);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);

        if (level != null && !level.isClientSide) {
            if (stack.get(DataComponentRegistry.WINE_YEAR.get()) == null) {
                WineYears.setWineYear(stack, level);
            } else {
                WineYears.stopStorage(stack, level);
            }
        }
    }

    private String toRoman(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }

    public enum BottleSize {
        SMALL, BIG
    }
}