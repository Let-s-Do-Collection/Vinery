package net.satisfy.vinery.client.render.block.storage;

import net.satisfy.foundation.storage.StorageTypeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.foundation.render.ClientUtil;
import net.satisfy.vinery.core.block.WineBottleBlock;
import net.satisfy.foundation.storage.StorageBlockEntity;

public class NineBottleRenderer implements StorageTypeRenderer {
    @Override
    public void render(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> itemStacks) {
        matrices.translate(-0.13, 0.335, 0.125);
        matrices.scale(0.9f, 0.9f, 0.9f);

        for (int i = 0; i < itemStacks.size(); i++) {
            ItemStack stack = itemStacks.get(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
                continue;
            }

            matrices.pushPose();

            int line = i >= 6 ? 3 : i >= 3 ? 2 : 1;
            float x;
            float y;

            if (line == 1) {
                x = -0.35f * i;
                y = 0f;
            } else if (line == 2) {
                x = -0.35f * (i - 3);
                y = -0.33f;
            } else {
                x = -0.35f * (i - 6);
                y = -0.66f;
            }

            matrices.translate(x, y, 0f);
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
