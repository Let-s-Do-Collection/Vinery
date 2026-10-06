package net.satisfy.vinery.client.render.block.storage;

import net.satisfy.foundation.storage.StorageTypeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class BigBottleRenderer implements StorageTypeRenderer {
    @Override
    public void render(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> itemStacks) {
        matrices.translate(-0.4, 0.07, -0.5);
        matrices.scale(0.8f, 0.8f, 0.9f);
        ItemStack stack = itemStacks.get(0);
        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            if (state.hasProperty(WineBottleBlock.FAKE_MODEL)) {
                state = state.setValue(WineBottleBlock.FAKE_MODEL, false);
            }
            ClientUtil.renderBlock(state, matrices, vertexConsumers, entity);
        }
    }
}