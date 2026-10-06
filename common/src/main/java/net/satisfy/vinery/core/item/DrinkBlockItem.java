package net.satisfy.vinery.core.item;

import net.satisfy.foundation.util.LibUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
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
import net.satisfy.vinery.core.util.WineYears;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class DrinkBlockItem extends BlockItem {
    private int baseDuration;
    private final boolean scaleDurationWithAge;
    private final BottleSize bottleSize;
    private Supplier<Holder<MobEffect>> effectSupplier;
    private int baseAmplifier;

    public DrinkBlockItem(Block block, Properties settings, boolean scaleDurationWithAge, BottleSize bottleSize) {
        super(block, settings);
        this.baseDuration = 0;
        this.scaleDurationWithAge = scaleDurationWithAge;
        this.bottleSize = bottleSize;
        this.effectSupplier = null;
        this.baseAmplifier = 0;
    }

    public void setEffectSupplier(Supplier<Holder<MobEffect>> effectSupplier, int baseDuration, int baseAmplifier) {
        this.effectSupplier = effectSupplier;
        this.baseDuration = baseDuration;
        this.baseAmplifier = baseAmplifier;
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
        Level world = null;
        if (tooltipContext.registries() != null) {
            world = getLevel();
        }

        if (effectSupplier != null && world != null) {
            Holder<MobEffect> effectHolder = effectSupplier.get();
            MobEffect effect = effectHolder.value();

            String effectName = effect.getDisplayName().getString();
            int amplifier = Math.max(0, WineYears.getEffectLevel(stack, world));
            String amplifierRoman = amplifier > 0 ? " " + toRoman(amplifier) : "";
            int durationTicks = scaleDurationWithAge ? WineYears.getEffectDuration(stack, world) : baseDuration;
            durationTicks = Math.max(0, durationTicks);
            String formattedDuration = formatDuration(durationTicks);
            String tooltipText = effectName + amplifierRoman + " (" + formattedDuration + ")";
            tooltip.add(Component.literal(tooltipText).withStyle(effect.getCategory().getTooltipFormatting()));
        } else {
            tooltip.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        }

        tooltip.add(Component.empty());
        if (world != null && stack.get(DataComponentRegistry.WINE_YEAR.get()) != null) {
            int ageYears = Math.max(0, WineYears.getWineAgeYears(stack, world));
            int ageDays = WineYears.getWineAgeDays(stack, world);
            tooltip.add(Component.translatable("tooltip.vinery.age", ageYears).withStyle(ChatFormatting.WHITE));
            tooltip.add(Component.empty());

            int daysPerYear = stack.get(DataComponentRegistry.WINE_YEAR.get()).daysPerYear();
            int yearsPerLevel = stack.get(DataComponentRegistry.WINE_YEAR.get()).yearsPerEffectLevel();
            int cycle = Math.max(1, daysPerYear * Math.max(1, yearsPerLevel));
            int daysToNextUpgrade = cycle - (ageDays % cycle);

            tooltip.add(Component.translatable("tooltip.vinery.next_upgrade", daysToNextUpgrade)
                    .withStyle(style -> style.withColor(TextColor.fromRgb(0x93c47d))));
        }
        tooltip.add(Component.translatable("tooltip.vinery.bottle_size." + bottleSize.name().toLowerCase())
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }

    @Environment(EnvType.CLIENT)
    private Level getLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && effectSupplier != null) {
            if (itemStack.get(DataComponentRegistry.WINE_YEAR.get()) == null) {
                WineYears.setWineYear(itemStack, level);
            }

            int duration = scaleDurationWithAge ? Math.max(0, WineYears.getEffectDuration(itemStack, level)) : baseDuration;
            int amplifier = scaleDurationWithAge ? Math.max(0, WineYears.getEffectLevel(itemStack, level)) : baseAmplifier;

            Holder<MobEffect> effectHolder = effectSupplier.get();
            MobEffect effect = effectHolder.value();

            Holder<MobEffect> registryHolder = level.registryAccess()
                    .registryOrThrow(Registries.MOB_EFFECT)
                    .wrapAsHolder(effect);

            livingEntity.addEffect(new MobEffectInstance(registryHolder, duration, amplifier));
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
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
        super.onCraftedBy(stack, world, player);
        if (world != null && !world.isClientSide) {
            WineYears.setWineYear(stack, world);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world != null && !world.isClientSide) {
            if (stack.get(DataComponentRegistry.WINE_YEAR.get()) == null) {
                WineYears.setWineYear(stack, world);
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