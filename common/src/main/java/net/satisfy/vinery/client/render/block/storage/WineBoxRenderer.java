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

public class WineBoxRenderer implements StorageTypeRenderer {
    @Override
    public void render(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> itemStacks) {
        matrices.translate(0.35, 0.6, -0.35);
        matrices.scale(0.7f, 0.7f, 0.7f);

        ItemStack stack = itemStacks.get(0);
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
            return;
        }

        matrices.mulPose(Axis.ZP.rotationDegrees(90f));
        matrices.mulPose(Axis.YN.rotationDegrees(90f));

        BlockState renderState = blockItem.getBlock().defaultBlockState();
        if (renderState.hasProperty(WineBottleBlock.FAKE_MODEL)) {
            renderState = renderState.setValue(WineBottleBlock.FAKE_MODEL, false);
        }

        ClientUtil.renderBlock(renderState, matrices, vertexConsumers, entity);
    }
}
