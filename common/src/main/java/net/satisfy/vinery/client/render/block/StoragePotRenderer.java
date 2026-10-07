package net.satisfy.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.core.block.entity.StoragePotBlockEntity;
import org.jetbrains.annotations.NotNull;

public class StoragePotRenderer implements BlockEntityRenderer<StoragePotBlockEntity> {
    private static final float FLOOR = 1.5F / 16.0F;
    private static final float LAYER_HEIGHT = 0.008F;
    private static final float SPREAD = 0.3F;
    private static final float SCALE = 0.36F;
    private static final float MAX_TILT = 30.0F;

    public StoragePotRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StoragePotBlockEntity entity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffers, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        long posSeed = entity.getBlockPos().asLong();
        RandomSource random = RandomSource.create();
        for (int layer = 0; layer < entity.getFillCount(); layer++) {
            ItemStack stack = entity.getItem(entity.getFillSlot(layer));
            if (stack.isEmpty()) {
                continue;
            }
            int seed = entity.getFillSeed(layer);
            random.setSeed(Mth.getSeed(entity.getBlockPos()) * 31L + Mth.murmurHash3Mixer(seed * 0x9E3779B9));
            float offsetX = (random.nextFloat() * 2.0F - 1.0F) * SPREAD;
            float offsetZ = (random.nextFloat() * 2.0F - 1.0F) * SPREAD;

            poseStack.pushPose();
            poseStack.translate(0.5F + offsetX, FLOOR + layer * LAYER_HEIGHT, 0.5F + offsetZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F + (random.nextFloat() * 2.0F - 1.0F) * MAX_TILT));
            poseStack.mulPose(Axis.YP.rotationDegrees((random.nextFloat() * 2.0F - 1.0F) * MAX_TILT));
            poseStack.scale(SCALE, SCALE, SCALE);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffers, entity.getLevel(), (int) posSeed + seed);
            poseStack.popPose();
        }
    }
}
