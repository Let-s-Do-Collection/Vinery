package net.satisfy.vinery.core.compat.rei.press;

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
import net.satisfy.vinery.core.registry.ObjectRegistry;

import java.util.List;

public class ApplePressCategory implements DisplayCategory<ApplePressDisplay> {
    private final CategoryIdentifier<ApplePressDisplay> id;

    public ApplePressCategory(CategoryIdentifier<ApplePressDisplay> id) {
        this.id = id;
    }

    @Override
    public CategoryIdentifier<ApplePressDisplay> getCategoryIdentifier() {
        return id;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.vinery.apple_press_category");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ObjectRegistry.APPLE_PRESS.get());
    }

    @Override
    public List<Widget> setupDisplay(ApplePressDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 18);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y)));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y))
                .animationDurationTicks(72));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 1))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1))
                .entries(display.getInputEntries().getFirst())
                .markInput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }
}
