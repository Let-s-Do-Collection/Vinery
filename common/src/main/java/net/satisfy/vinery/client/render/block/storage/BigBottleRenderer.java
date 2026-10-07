package net.satisfy.vinery.client.render.block.storage;

import net.satisfy.foundation.storage.StorageTypeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.foundation.render.ClientUtil;
import net.satisfy.vinery.core.block.WineBottleBlock;
import net.satisfy.foundation.storage.StorageBlockEntity;

import java.util.Map;
import java.util.WeakHashMap;

public class BigBottleRenderer implements StorageTypeRenderer {
    private static final long WOBBLE_MS = 600L;
    private static final float WOBBLE_DEGREES = 6f;

    private record Tracked(ItemStack stack, long wobbleStart) {
    }

    private final Map<StorageBlockEntity, Tracked> tracked = new WeakHashMap<>();

    @Override
    public void render(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> itemStacks) {
        ItemStack stack = itemStacks.get(0);
        long now = Util.getMillis();
        Tracked previous = tracked.get(entity);
        long start = previous == null ? Long.MIN_VALUE : previous.wobbleStart();
        if (previous != null && !stack.isEmpty() && !ItemStack.isSameItemSameComponents(previous.stack(), stack)) {
            start = now;
        }
        tracked.put(entity, new Tracked(stack.copy(), start));

        long elapsed = now - start;
        if (start != Long.MIN_VALUE && elapsed < WOBBLE_MS) {
            float progress = elapsed / (float) WOBBLE_MS;
            float angle = (float) Math.sin(progress * Math.PI * 6) * (1f - progress) * WOBBLE_DEGREES;
            matrices.translate(0, 0.07, 0);
            matrices.mulPose(Axis.ZP.rotationDegrees(angle));
            matrices.mulPose(Axis.XP.rotationDegrees(angle * 0.5f));
            matrices.translate(0, -0.07, 0);
        }

        matrices.translate(-0.4, 0.07, -0.5);
        matrices.scale(0.8f, 0.8f, 0.9f);
        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            if (state.hasProperty(WineBottleBlock.FAKE_MODEL)) {
                state = state.setValue(WineBottleBlock.FAKE_MODEL, false);
            }
            ClientUtil.renderBlock(state, matrices, vertexConsumers, entity);
        }
    }
}