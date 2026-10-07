package net.satisfy.vinery.core.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.wine.GrapeType;
import org.jetbrains.annotations.NotNull;

public class GrapeItem extends Item {
    private static final double CHANCE_OF_GETTING_SEEDS = 0.2;
    private final Item returnItem;
    private final GrapeType type;

    public GrapeItem(Properties settings, GrapeType type, Item returnItem) {
        super(settings);
        this.type = type;
        this.returnItem = returnItem;
    }

    public GrapeType getType() {
        return this.type;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!level.isClientSide() && entityLiving instanceof Player player) {
            if (stack.getItem() == this) {
                if (level.getRandom().nextFloat() < CHANCE_OF_GETTING_SEEDS) {
                    ItemStack returnStack = new ItemStack(returnItem);
                    if (!player.getInventory().add(returnStack)) {
                        player.drop(returnStack, false);
                    }
                }
            }
        }
        return super.finishUsingItem(stack, level, entityLiving);
    }

}