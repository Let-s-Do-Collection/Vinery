package net.satisfy.vinery.client.gui.overlay;

import com.google.common.base.Suppliers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import net.satisfy.vinery.core.block.GrapevinePotBlock;
import net.satisfy.vinery.core.block.entity.GrapevinePotBlockEntity;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.wine.GrapeType;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import net.satisfy.vinery.core.util.InfoOverlayMode;

public class GrapevinePotInfoProvider implements BlockInfoProvider {
    private static final Supplier<List<ItemStack>> ALL_GRAPES = Suppliers.memoize(GrapevinePotInfoProvider::allGrapes);

    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (hit == null || !(state.getBlock() instanceof GrapevinePotBlock) || !InfoOverlayMode.isVisible(Minecraft.getInstance().player)) {
            return List.of();
        }
        if (!(level.getBlockEntity(pos) instanceof GrapevinePotBlockEntity pot)) {
            return List.of();
        }
        int stage = state.getValue(GrapevinePotBlock.STAGE);
        int grapes = state.getValue(GrapevinePotBlock.STORAGE);
        GrapeType type = state.getValue(GrapevinePotBlock.GRAPEVINE_TYPE);
        List<InfoSection> sections = new ArrayList<>();

        if (stage == 0 || grapes == 0) {
            sections.add(InfoSection.icons(Component.translatable("hud.vinery.grapevine_pot.start"), ALL_GRAPES.get(), InfoSection.GRID_COLUMNS));
            sections.add(InfoSection.title(Component.translatable("hud.vinery.grapevine_pot.fill_hint").withStyle(ChatFormatting.GRAY)));
            return sections;
        }

        if (stage < GrapevinePotBlock.FULL_STAGE) {
            sections.add(InfoSection.icons(Component.translatable("hud.vinery.grapevine_pot.in_pot"), List.of(new ItemStack(type.getFruit(), grapes)), InfoSection.ROW_COLUMNS).withDecorations());
            sections.add(InfoSection.lines(Component.translatable("hud.vinery.grapevine_pot.filled", grapes, GrapevinePotBlock.MAX_GRAPES).withStyle(ChatFormatting.GRAY),
                    List.of(Component.translatable("hud.vinery.grapevine_pot.more_hint", type.getFruit().getDescription()).withStyle(ChatFormatting.GRAY))));
            return sections;
        }

        if (GrapevinePotBlock.isStompable(state)) {
            int stomps = pot.getProgress() > 0 ? pot.getStomps() : (stage - GrapevinePotBlock.FULL_STAGE) * GrapevinePotBlockEntity.stompsNeeded() / (GrapevinePotBlock.MAX_STAGE - GrapevinePotBlock.FULL_STAGE);
            List<Component> lines = new ArrayList<>();
            lines.add(Component.translatable("hud.vinery.grapevine_pot.stomped", stomps, GrapevinePotBlockEntity.stompsNeeded()).withStyle(ChatFormatting.GRAY));
            Player player = Minecraft.getInstance().player;
            int bonus = PlatformHelper.getGrapevinePotHeavyArmorBonus();
            if (player != null && bonus > 0 && GrapevinePotBlock.isHeavyArmored(player)) {
                lines.add(Component.translatable("hud.vinery.grapevine_pot.heavy_armor", bonus).withStyle(ChatFormatting.GOLD));
            }
            MutableComponent title = Component.translatable("hud.vinery.grapevine_pot.stomp").withStyle(ChatFormatting.BOLD, ChatFormatting.LIGHT_PURPLE);
            sections.add(InfoSection.lines(title, lines));
            return sections;
        }

        int bottles = GrapevinePotBlock.bottlesLeft(state);
        if (bottles > 0) {
            sections.add(InfoSection.icons(Component.translatable("hud.vinery.grapevine_pot.ready").withStyle(ChatFormatting.GREEN), List.of(new ItemStack(type.getBottle(), bottles)), InfoSection.ROW_COLUMNS).withDecorations());
            sections.add(InfoSection.title(Component.translatable("hud.vinery.grapevine_pot.bottle_hint", ObjectRegistry.WINE_BOTTLE.get().asItem().getDescription()).withStyle(ChatFormatting.GRAY)));
        }
        return sections;
    }

    private static List<ItemStack> allGrapes() {
        return GrapeTypeRegistry.GRAPE_TYPE_TYPES.stream()
                .filter(type -> !type.equals(GrapeTypeRegistry.NONE) && type.getFruit() != Items.AIR)
                .sorted(Comparator.comparing(GrapeType::getId))
                .map(type -> new ItemStack(type.getFruit()))
                .toList();
    }
}
