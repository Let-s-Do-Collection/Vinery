package net.satisfy.vinery.client.render.block.storage;

import net.satisfy.foundation.storage.StorageTypeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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

public class FourBottleRenderer implements StorageTypeRenderer {
    @Override
    public void render(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> itemStacks) {
        matrices.translate(-0.13, 0.335, 0.125);
        matrices.scale(0.9f, 0.9f, 0.9f);
        for (int i = 0; i < itemStacks.size(); i++) {
            ItemStack stack = itemStacks.get(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
                matrices.pushPose();
                if(i == 0){
                    matrices.translate(-0.35f, 0, 0f);
                }
                else if(i == 1){
                    matrices.translate(0, -0.33f, 0f);
                }
                else if(i == 2){
                    matrices.translate(-0.7f, -0.33f, 0f);
                }
                else if(i == 3){
                    matrices.translate(-0.35f, -0.66f, 0f);
                }
                else {
                    matrices.popPose();
                    continue;
                }
                matrices.mulPose(Axis.XN.rotationDegrees(90));

                BlockState state = blockItem.getBlock().defaultBlockState();
                if (state.hasProperty(WineBottleBlock.FAKE_MODEL)) {
                    state = state.setValue(WineBottleBlock.FAKE_MODEL, false);
                }
                ClientUtil.renderBlock(state, matrices, vertexConsumers, entity);
                matrices.popPose();
            }
        }
    }
}