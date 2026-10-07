package net.satisfy.vinery.client.render.block.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.foundation.render.ClientUtil;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.foundation.storage.StorageTypeRenderer;
import net.satisfy.vinery.core.block.BottleStorageBlock;
import net.satisfy.vinery.core.block.WineBottleBlock;

import java.util.Map;
import java.util.WeakHashMap;

public class BottleRenderer implements StorageTypeRenderer {
    private static final long INSERT_ANIMATION_MS = 250;
    private static final float INSERT_DISTANCE = 0.4f;

    private final float[][] offsets;
    private final Map<StorageBlockEntity, long[]> insertTimes = new WeakHashMap<>();

    public BottleRenderer(BottleStorageBlock.Layout layout) {
        this.offsets = layout.offsets;
    }

    @Override
    public void render(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> itemStacks) {
        matrices.translate(-0.13, 0.335, 0.125);
        matrices.scale(0.9f, 0.9f, 0.9f);

        long now = Util.getMillis();
        boolean firstRender = !insertTimes.containsKey(entity);
        long[] times = insertTimes.computeIfAbsent(entity, e -> new long[offsets.length]);

        for (int i = 0; i < itemStacks.size() && i < offsets.length; i++) {
            ItemStack stack = itemStacks.get(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
                times[i] = 0;
                continue;
            }
            if (times[i] == 0) times[i] = firstRender ? now - INSERT_ANIMATION_MS : now;

            float progress = Math.min(1f, (now - times[i]) / (float) INSERT_ANIMATION_MS);
            float eased = 1f - (1f - progress) * (1f - progress) * (1f - progress);

            matrices.pushPose();
            matrices.translate(offsets[i][0], offsets[i][1], -INSERT_DISTANCE * (1f - eased));
            matrices.mulPose(Axis.XN.rotationDegrees(90f));

            BlockState state = blockItem.getBlock().defaultBlockState();
            if (state.hasProperty(WineBottleBlock.FAKE_MODEL)) {
                state = state.setValue(WineBottleBlock.FAKE_MODEL, false);
            }
            ClientUtil.renderBlock(state, matrices, vertexConsumers, entity);
            matrices.popPose();
        }
    }
}
