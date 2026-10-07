package net.satisfy.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.foundation.client.render.WobbleAnimation;
import net.satisfy.vinery.client.util.ExtraModels;
import net.satisfy.vinery.core.block.GrapevinePotBlock;
import net.satisfy.vinery.core.block.entity.GrapevinePotBlockEntity;
import org.jetbrains.annotations.NotNull;

public class GrapevinePotRenderer implements BlockEntityRenderer<GrapevinePotBlockEntity> {
    private static final float SQUASH_TICKS = 8.0F;
    private static final float SQUASH_STRENGTH = 0.45F;
    private static final float BOTTLE_WOBBLE_STRENGTH = 0.6F;
    private static final float BOTTLE_DIP = 0.02F;
    private static final float FLOOR = 1.0F / 16.0F;

    public GrapevinePotRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(GrapevinePotBlockEntity entity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffers, int light, int overlay) {
        BlockState state = entity.getBlockState();
        int stage = state.getValue(GrapevinePotBlock.STAGE);
        Level level = entity.getLevel();
        if (stage == 0 || level == null) {
            return;
        }
        BakedModel model = ExtraModels.get(ExtraModels.potContent(state.getValue(GrapevinePotBlock.GRAPEVINE_TYPE).isRed(), stage));
        poseStack.pushPose();
        float squash = squash(level, entity.getStompStart(), partialTick);
        if (squash > 0.0F) {
            poseStack.translate(0.5F, FLOOR, 0.5F);
            poseStack.scale(1.0F + squash * 0.15F, 1.0F - squash, 1.0F + squash * 0.15F);
            poseStack.translate(-0.5F, -FLOOR, -0.5F);
        }
        if (WobbleAnimation.isActive(level, entity.getBottleStart())) {
            float progress = (level.getGameTime() - entity.getBottleStart() + partialTick) / WobbleAnimation.DURATION;
            poseStack.translate(0.0F, -Mth.sin(Mth.clamp(progress, 0.0F, 1.0F) * Mth.PI) * BOTTLE_DIP, 0.0F);
            WobbleAnimation.apply(poseStack, level, entity.getBottleStart(), partialTick, WobbleAnimation.seed(entity.getBlockPos().asLong()), BOTTLE_WOBBLE_STRENGTH);
        }
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), buffers.getBuffer(RenderType.cutout()), state, model, 1.0F, 1.0F, 1.0F, light, overlay);
        poseStack.popPose();
    }

    private static float squash(Level level, long start, float partialTick) {
        float progress = (level.getGameTime() - start + partialTick) / SQUASH_TICKS;
        if (progress < 0.0F || progress >= 1.0F) {
            return 0.0F;
        }
        return Mth.sin(progress * Mth.PI) * (1.0F - progress) * SQUASH_STRENGTH;
    }
}
