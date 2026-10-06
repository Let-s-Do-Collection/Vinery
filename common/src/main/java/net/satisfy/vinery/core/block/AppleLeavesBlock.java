package net.satisfy.vinery.core.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.satisfy.foundation.block.FruitLeavesBlock;
import net.minecraft.world.item.Items;
import net.satisfy.vinery.platform.PlatformHelper;

public class AppleLeavesBlock extends FruitLeavesBlock {
    public static final BooleanProperty CAN_GROW_APPLES = BooleanProperty.create("can_grow_apples");
    public static final BooleanProperty HAS_APPLES = BooleanProperty.create("has_apples");

    public AppleLeavesBlock(Properties settings) {
        super(settings, true);
    }

    @Override
    protected BooleanProperty canGrowFruitProperty() {
        return CAN_GROW_APPLES;
    }

    @Override
    protected BooleanProperty hasFruitProperty() {
        return HAS_APPLES;
    }

    @Override
    protected Item fruitItem() {
        return Items.APPLE;
    }

    @Override
    protected double growthChance() {
        return PlatformHelper.getAppleGrowthChance();
    }
}
