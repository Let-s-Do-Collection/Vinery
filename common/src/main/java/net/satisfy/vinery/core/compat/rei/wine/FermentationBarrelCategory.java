package net.satisfy.vinery.core.compat.rei.wine;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.wine.JuiceUtil;

import java.util.List;

public class FermentationBarrelCategory implements DisplayCategory<FermentationBarrelDisplay> {
    private static final int SLOT_SPACING = 18;

    @Override
    public CategoryIdentifier<FermentationBarrelDisplay> getCategoryIdentifier() {
        return FermentationBarrelDisplay.FERMENTATION_BARREL_DISPLAY;
    }

    @Override
    public Component getTitle() {
        return ObjectRegistry.FERMENTATION_BARREL.get().getName();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ObjectRegistry.FERMENTATION_BARREL.get());
    }

    @Override
    public List<Widget> setupDisplay(FermentationBarrelDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();

        Point origin = new Point(bounds.getMinX() + 10, bounds.getMinY() + 10);

        widgets.add(Widgets.createRecipeBase(bounds));

        for (int i = 0; i < Math.min(display.getInputEntries().size(), 4); i++) {
            widgets.add(Widgets.createSlot(new Point(origin.x + SLOT_SPACING * i, origin.y + SLOT_SPACING + 4))
                    .entries(display.getInputEntries().get(i)).markInput());
        }

        widgets.add(Widgets.createSlot(new Point(origin.x, origin.y))
                .entry(EntryStacks.of(getJuiceItemForType(display.getJuiceType())))
                .disableBackground()
                .markInput());
        widgets.add(Widgets.createLabel(new Point(origin.x + (SLOT_SPACING * 3) - 8, origin.y + 5),
                Component.literal("Amount: " + display.getJuiceAmount())));

        widgets.add(Widgets.createArrow(new Point(origin.x + (SLOT_SPACING * 4), origin.y + 8))
                .animationDurationTicks(50));

        widgets.add(Widgets.createResultSlotBackground(
                new Point(bounds.getMaxX() - 26 - 10, origin.y + 8))
        );
        widgets.add(Widgets.createSlot(
                        new Point(bounds.getMaxX() - 26 - 10, origin.y + 8))
                .entries(display.getOutputEntries().get(0)).disableBackground().markOutput()
        );

        return widgets;
    }

    private ItemStack getJuiceItemForType(String juiceType) {
        return new ItemStack(JuiceUtil.juiceItem(juiceType));
    }
}
