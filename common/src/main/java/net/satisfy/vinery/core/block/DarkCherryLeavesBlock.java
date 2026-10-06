package net.satisfy.vinery.core.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.satisfy.foundation.block.FruitLeavesBlock;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.platform.PlatformHelper;

public class DarkCherryLeavesBlock extends FruitLeavesBlock {
    public static final BooleanProperty CAN_GROW_CHERRIES = BooleanProperty.create("can_grow_cherries");
    public static final BooleanProperty HAS_CHERRIES = BooleanProperty.create("has_cherries");

    public DarkCherryLeavesBlock(Properties settings) {
        super(settings, false);
    }

    @Override
    protected BooleanProperty canGrowFruitProperty() {
        return CAN_GROW_CHERRIES;
    }

    @Override
    protected BooleanProperty hasFruitProperty() {
        return HAS_CHERRIES;
    }

    @Override
    protected Item fruitItem() {
        return ObjectRegistry.CHERRY.get();
    }

    @Override
    protected double growthChance() {
        return PlatformHelper.getCherryGrowthChance();
    }
}
