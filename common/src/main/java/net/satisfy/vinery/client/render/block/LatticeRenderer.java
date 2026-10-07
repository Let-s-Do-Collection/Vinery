package net.satisfy.vinery.client.render.block;

import net.satisfy.foundation.block.LineConnectingType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.block.LatticeBlock;
import net.satisfy.vinery.core.block.entity.LatticeBlockEntity;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.wine.GrapeType;

import java.util.HashMap;
import java.util.Map;

import static net.satisfy.vinery.core.registry.ObjectRegistry.*;

public class LatticeRenderer implements BlockEntityRenderer<LatticeBlockEntity> {
    private static Map<Block, ResourceLocation> textureMap;

    private static Map<Block, ResourceLocation> getTextureMap() {
        if (textureMap == null) {
            textureMap = new HashMap<>();
            textureMap.put(OAK_LATTICE.get(), Vinery.identifier("textures/block/lattice/oak_lattice.png"));
            textureMap.put(SPRUCE_LATTICE.get(), Vinery.identifier("textures/block/lattice/spruce_lattice.png"));
            textureMap.put(CHERRY_LATTICE.get(), Vinery.identifier("textures/block/lattice/cherry_lattice.png"));
            textureMap.put(BIRCH_LATTICE.get(), Vinery.identifier("textures/block/lattice/birch_lattice.png"));
            textureMap.put(DARK_OAK_LATTICE.get(), Vinery.identifier("textures/block/lattice/dark_oak_lattice.png"));
            textureMap.put(ACACIA_LATTICE.get(), Vinery.identifier("textures/block/lattice/acacia_lattice.png"));
            textureMap.put(BAMBOO_LATTICE.get(), Vinery.identifier("textures/block/lattice/bamboo_lattice.png"));
            textureMap.put(JUNGLE_LATTICE.get(), Vinery.identifier("textures/block/lattice/jungle_lattice.png"));
            textureMap.put(MANGROVE_LATTICE.get(), Vinery.identifier("textures/block/lattice/mangrove_lattice.png"));
            textureMap.put(DARK_CHERRY_LATTICE.get(), Vinery.identifier("textures/block/lattice/dark_cherry_lattice.png"));
        }
        return textureMap;
    }

    private final ModelPart growing_red;
    private final ModelPart sprout;
    private final ModelPart growing_white;
    private final ModelPart mesh;
    private final ModelPart support_right;
    private final ModelPart corner_braces_right;
    private final ModelPart support_left;
    private final ModelPart corner_braces_left;
    private final ModelPart growing_red_floor;
    private final ModelPart sprout_floor;
    private final ModelPart growing_white_floor;
    private final ModelPart lattice_parts;
    private final ModelPart hanging_1_r1;
    private final ModelPart hanging_2_r1;

    private static final CubeDeformation WALL_LEAF_DEFORMATION = new CubeDeformation(-0.05F, -0.1F, 0.0F);
    private static final CubeDeformation FLOOR_LEAF_DEFORMATION = new CubeDeformation(-0.1F, 0.0F, -0.1F);

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Vinery.identifier("lattice"), "main");

    public LatticeRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(LAYER_LOCATION);

        ModelPart lattice_wall = root.getChild("lattice_wall");
        ModelPart grape_cluster = lattice_wall.getChild("grape_cluster");
        this.growing_red = grape_cluster.getChild("growing_red");
        this.sprout = grape_cluster.getChild("sprout");
        this.growing_white = grape_cluster.getChild("growing_white");
        this.mesh = lattice_wall.getChild("mesh");
        this.support_right = lattice_wall.getChild("support_right");
        this.corner_braces_right = lattice_wall.getChild("corner_braces_right");
        this.support_left = lattice_wall.getChild("support_left");
        this.corner_braces_left = lattice_wall.getChild("corner_braces_left");

        ModelPart lattice_floor = root.getChild("lattice_floor");
        ModelPart grape_cluster_floor = lattice_floor.getChild("grape_cluster_floor");
        this.growing_red_floor = grape_cluster_floor.getChild("growing_red_floor");
        this.sprout_floor = grape_cluster_floor.getChild("sprout_floor");
        this.growing_white_floor = grape_cluster_floor.getChild("growing_white_floor");
        this.lattice_parts = lattice_floor.getChild("lattice_parts");
        this.hanging_1_r1 = grape_cluster_floor.getChild("hanging_1_r1");
        this.hanging_2_r1 = grape_cluster_floor.getChild("hanging_2_r1");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition lattice_wall = partdefinition.addOrReplaceChild("lattice_wall", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition grape_cluster = lattice_wall.addOrReplaceChild("grape_cluster", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -10.0F));

        PartDefinition growing_red = grape_cluster.addOrReplaceChild("growing_red", CubeListBuilder.create().texOffs(46, 17).addBox(-15.0F, -16.0F, -10.5F, 16.0F, 16.0F, 1.0F, WALL_LEAF_DEFORMATION), PartPose.offset(7.0F, 0.0F, 17.0F));

        PartDefinition sprout = grape_cluster.addOrReplaceChild("sprout", CubeListBuilder.create().texOffs(46, 0).addBox(-15.0F, -16.0F, -10.5F, 16.0F, 16.0F, 1.0F, WALL_LEAF_DEFORMATION), PartPose.offset(7.0F, 0.0F, 17.0F));

        PartDefinition growing_white = grape_cluster.addOrReplaceChild("growing_white", CubeListBuilder.create().texOffs(46, 34).addBox(-15.0F, -16.0F, -10.5F, 16.0F, 16.0F, 1.0F, WALL_LEAF_DEFORMATION), PartPose.offset(7.0F, 0.0F, 17.0F));

        PartDefinition mesh = lattice_wall.addOrReplaceChild("mesh", CubeListBuilder.create().texOffs(48, 64).addBox(-30.0F, -12.0F, 2.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(22.0F, -4.0F, 5.0F));

        PartDefinition support_right = lattice_wall.addOrReplaceChild("support_right", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -9.0F, 7.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(14.0F, -7.0F, -1.0F));

        PartDefinition corner_braces_right = lattice_wall.addOrReplaceChild("corner_braces_right", CubeListBuilder.create().texOffs(8, 0).addBox(6.0F, -16.0F, -2.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition corner_braces_right_bottom_r1 = corner_braces_right.addOrReplaceChild("corner_braces_right_bottom_r1", CubeListBuilder.create().texOffs(8, 10).addBox(-7.99F, -4.0F, 6.0F, 1.98F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -7.0F, -1.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition corner_braces_right_top_r1 = corner_braces_right.addOrReplaceChild("corner_braces_right_top_r1", CubeListBuilder.create().texOffs(3, 10).addBox(-7.99F, -6.0F, -5.5F, 1.98F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -7.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition support_left = lattice_wall.addOrReplaceChild("support_left", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -16.0F, 14.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition corner_braces_left = lattice_wall.addOrReplaceChild("corner_braces_left", CubeListBuilder.create().texOffs(8, 0).addBox(-8.0F, -16.0F, -2.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition corner_braces_left_top_r1 = corner_braces_left.addOrReplaceChild("corner_braces_left_top_r1", CubeListBuilder.create().texOffs(8, 10).addBox(-7.99F, -4.0F, 6.0F, 1.98F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -1.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition corner_braces_left_bottom_r1 = corner_braces_left.addOrReplaceChild("corner_braces_left_bottom_r1", CubeListBuilder.create().texOffs(3, 10).addBox(-7.99F, -6.0F, -5.5F, 1.98F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition lattice_floor = partdefinition.addOrReplaceChild("lattice_floor", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition grape_cluster_floor = lattice_floor.addOrReplaceChild("grape_cluster_floor", CubeListBuilder.create(), PartPose.offset(0.0F, -4.5F, 4.0F));

        PartDefinition hanging_2_r1 = grape_cluster_floor.addOrReplaceChild("hanging_2_r1", CubeListBuilder.create().texOffs(32, 14).addBox(-7.0F, -11.5F, -1.0F, 8.0F, 12.0F, 0.0F), PartPose.offsetAndRotation(2.8284F, 14.5F, -3.4142F, 0.0F, 0.7854F, 0.0F));

        PartDefinition hanging_1_r1 = grape_cluster_floor.addOrReplaceChild("hanging_1_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-7.0F, -9.5F, -1.0F, 8.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(1.4142F, 12.5F, 0.8284F, 0.0F, -0.7854F, 0.0F));

        PartDefinition growing_red_floor = grape_cluster_floor.addOrReplaceChild("growing_red_floor", CubeListBuilder.create(), PartPose.offset(0.0F, -3.5F, 1.0F));

        PartDefinition growing_red_floor_r1 = growing_red_floor.addOrReplaceChild("growing_red_floor_r1", CubeListBuilder.create().texOffs(2, 52).addBox(-18.0F, -30.5F, -3.0F, 16.0F, 1.0F, 12.0F, FLOOR_LEAF_DEFORMATION), PartPose.offsetAndRotation(3.0F, 32.5F, 8.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition sprout_floor = grape_cluster_floor.addOrReplaceChild("sprout_floor", CubeListBuilder.create(), PartPose.offset(0.0F, -3.5F, 1.0F));

        PartDefinition sprouting_grapes_floor_r1 = sprout_floor.addOrReplaceChild("sprouting_grapes_floor_r1", CubeListBuilder.create().texOffs(2, 39).addBox(-18.0F, -30.5F, -3.0F, 16.0F, 1.0F, 12.0F, FLOOR_LEAF_DEFORMATION), PartPose.offsetAndRotation(3.0F, 32.5F, 8.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition growing_white_floor = grape_cluster_floor.addOrReplaceChild("growing_white_floor", CubeListBuilder.create(), PartPose.offset(0.0F, -3.5F, 1.0F));

        PartDefinition growing_white_floor_r1 = growing_white_floor.addOrReplaceChild("growing_white_floor_r1", CubeListBuilder.create().texOffs(2, 65).addBox(-18.0F, -30.5F, -3.0F, 16.0F, 1.0F, 12.0F, FLOOR_LEAF_DEFORMATION), PartPose.offsetAndRotation(3.0F, 32.5F, 8.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition lattice_parts = lattice_floor.addOrReplaceChild("lattice_parts", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cross_brace_r1 = lattice_parts.addOrReplaceChild("cross_brace_r1", CubeListBuilder.create().texOffs(-12, 24).addBox(-18.0F, -29.0F, -3.0F, 16.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 28.0F, 10.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition support_floor_left_r1 = lattice_parts.addOrReplaceChild("support_floor_left_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -16.0F, 14.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-30.0F, -16.0F, 14.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(22.0F, -16.0F, -8.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    @Override
    public void render(LatticeBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        BlockState state = blockEntity.getBlockState();
        Direction direction = state.getValue(LatticeBlock.FACING);
        boolean support = state.getValue(LatticeBlock.SUPPORT);
        boolean bottom = state.getValue(LatticeBlock.BOTTOM);
        LineConnectingType type = state.getValue(LatticeBlock.TYPE);

        int age = state.getValue(LatticeBlock.AGE);
        GrapeType grapeType = state.getValue(LatticeBlock.GRAPE);

        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        poseStack.scale(1.0f, -1.0f, -1.0f);

        Block block = state.getBlock();
        ResourceLocation texture = getTextureMap().getOrDefault(block, Vinery.identifier("textures/entity/lattice/default_lattice.png"));
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

        if (bottom) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
            poseStack.scale(1.0f, -1.0f, -1.0f);
            lattice_parts.render(poseStack, consumer, packedLight, packedOverlay);
        } else {
            mesh.render(poseStack, consumer, packedLight, packedOverlay);

            if (type != LineConnectingType.MIDDLE && type != LineConnectingType.LEFT) {
                support_right.render(poseStack, consumer, packedLight, packedOverlay);
                if (support) {
                    corner_braces_right.render(poseStack, consumer, packedLight, packedOverlay);
                }
            }

            if (type != LineConnectingType.MIDDLE && type != LineConnectingType.RIGHT) {
                support_left.render(poseStack, consumer, packedLight, packedOverlay);
                if (support) {
                    corner_braces_left.render(poseStack, consumer, packedLight, packedOverlay);
                }
            }
        }

        if (!grapeType.equals(GrapeTypeRegistry.NONE)) {
            boolean isRed = grapeType.isRed();

            if (bottom) {
                if (age < 4) {
                    sprout_floor.render(poseStack, consumer, packedLight, packedOverlay);
                } else {
                    if (isRed) {
                        growing_red_floor.render(poseStack, consumer, packedLight, packedOverlay);
                    } else {
                        growing_white_floor.render(poseStack, consumer, packedLight, packedOverlay);
                    }
                }
            } else {
                if (age < 4) {
                    sprout.render(poseStack, consumer, packedLight, packedOverlay);
                } else {
                    if (isRed) {
                        growing_red.render(poseStack, consumer, packedLight, packedOverlay);
                    } else {
                        growing_white.render(poseStack, consumer, packedLight, packedOverlay);
                    }
                }
            }
        }
        if (bottom && blockEntity.shouldShowHanging()) {
            poseStack.pushPose();
            poseStack.translate(0.0, 0, 0.0);
            poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            poseStack.scale(1.0f, -1.0f, -1.0f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180f));

            RandomSource random = RandomSource.create(blockEntity.getBlockPos().asLong());
            float offsetX = Mth.lerp(random.nextFloat(), -0.02f, 0.0f);
            float offsetZ = Mth.lerp(random.nextFloat(), -0.02f, 0.0f);

            poseStack.translate(offsetX, -0.2f, offsetZ);

            hanging_1_r1.render(poseStack, consumer, packedLight, packedOverlay);
            hanging_2_r1.render(poseStack, consumer, packedLight, packedOverlay);

            poseStack.popPose();
        }

        poseStack.popPose();
    }
}