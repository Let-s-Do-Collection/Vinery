package net.satisfy.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.satisfy.vinery.core.block.StackableLogBlock;
import net.satisfy.vinery.core.block.entity.StackableLogBlockEntity;
import org.jetbrains.annotations.NotNull;

public class StackableLogRenderer implements BlockEntityRenderer<StackableLogBlockEntity> {
    private static final float HEIGHT = 1.0F + 1.0F / 32.0F;
    private static final float OFFSET = 0.3125F;
    private static final float SCALE = 0.375F;

    public StackableLogRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StackableLogBlockEntity entity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffers, int light, int overlay) {
        if (entity.getLevel() == null || entity.getBlockState().getValue(StackableLogBlock.TYPE) != SlabType.DOUBLE) {
            return;
        }
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Direction facing = entity.getBlockState().getValue(StackableLogBlock.FACING);
        int topLight = LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos().above());
        int seed = (int) entity.getBlockPos().asLong();
        for (int i = 0; i < StackableLogBlockEntity.SLOTS; i++) {
            ItemStack stack = entity.getItems().get(i);
            if (stack.isEmpty()) {
                continue;
            }
            Direction direction = Direction.from2DDataValue((i + facing.get2DDataValue()) % 4);
            poseStack.pushPose();
            poseStack.translate(0.5F, HEIGHT, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.translate(-OFFSET, -OFFSET, 0.0F);
            poseStack.scale(SCALE, SCALE, SCALE);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, topLight, overlay, poseStack, buffers, entity.getLevel(), seed + i);
            poseStack.popPose();
        }
    }
}
