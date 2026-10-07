package net.satisfy.vinery.client.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.satisfy.vinery.core.Vinery;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MuleModel<T extends AbstractHorse> extends AgeableListModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Vinery.identifier("trader_mule"), "main");

	private static final float DEG_125 = 2.1816616F;
	private static final float DEG_60 = 1.0471976F;
	private static final float DEG_45 = 0.7853982F;
	private static final float DEG_30 = 0.5235988F;
	private static final float DEG_15 = 0.2617994F;
	protected static final String HEAD_PARTS = "head_parts";
	private static final String LEFT_HIND_BABY_LEG = "left_hind_baby_leg";
	private static final String RIGHT_HIND_BABY_LEG = "right_hind_baby_leg";
	private static final String LEFT_FRONT_BABY_LEG = "left_front_baby_leg";
	private static final String RIGHT_FRONT_BABY_LEG = "right_front_baby_leg";
	private static final String SADDLE = "saddle";
	private static final String LEFT_SADDLE_MOUTH = "left_saddle_mouth";
	private static final String LEFT_SADDLE_LINE = "left_saddle_line";
	private static final String RIGHT_SADDLE_MOUTH = "right_saddle_mouth";
	private static final String RIGHT_SADDLE_LINE = "right_saddle_line";
	private static final String HEAD_SADDLE = "head_saddle";
	private static final String MOUTH_SADDLE_WRAP = "mouth_saddle_wrap";
	protected final ModelPart body;
	protected final ModelPart headParts;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightHindBabyLeg;
	private final ModelPart leftHindBabyLeg;
	private final ModelPart rightFrontBabyLeg;
	private final ModelPart leftFrontBabyLeg;
	private final ModelPart tail;
	private final ModelPart[] saddleParts;
	private final ModelPart[] ridingParts;

	public MuleModel(ModelPart modelPart) {
		super(true, 16.2F, 1.36F, 2.7272F, 2.0F, 20.0F);
		this.body = modelPart.getChild("body");
		this.headParts = modelPart.getChild("head_parts");
		this.rightHindLeg = modelPart.getChild("right_hind_leg");
		this.leftHindLeg = modelPart.getChild("left_hind_leg");
		this.rightFrontLeg = modelPart.getChild("right_front_leg");
		this.leftFrontLeg = modelPart.getChild("left_front_leg");
		this.rightHindBabyLeg = modelPart.getChild("right_hind_baby_leg");
		this.leftHindBabyLeg = modelPart.getChild("left_hind_baby_leg");
		this.rightFrontBabyLeg = modelPart.getChild("right_front_baby_leg");
		this.leftFrontBabyLeg = modelPart.getChild("left_front_baby_leg");
		this.tail = this.body.getChild("tail");
		ModelPart modelPart2 = this.body.getChild("saddle");
		ModelPart modelPart3 = this.headParts.getChild("left_saddle_mouth");
		ModelPart modelPart4 = this.headParts.getChild("right_saddle_mouth");
		ModelPart modelPart5 = this.headParts.getChild("left_saddle_line");
		ModelPart modelPart6 = this.headParts.getChild("right_saddle_line");
		ModelPart modelPart7 = this.headParts.getChild("head_saddle");
		ModelPart modelPart8 = this.headParts.getChild("mouth_saddle_wrap");
		this.saddleParts = new ModelPart[]{modelPart2, modelPart3, modelPart4, modelPart7, modelPart8};
		this.ridingParts = new ModelPart[]{modelPart5, modelPart6};

	}

	public static LayerDefinition getTexturedModelData() {
		CubeDeformation cubeDeformation = new CubeDeformation(0.0F);
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();
		PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, -8.0F, -20.0F, 10.0F, 10.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(0, 84).addBox(-8.0F, -14.0F, -3.0F, 16.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 64).addBox(-11.0F, -9.0F, -13.0F, 6.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(32, 64).addBox(5.0F, -9.0F, -13.0F, 6.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 9.0F));
		PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("head_parts", CubeListBuilder.create().texOffs(0, 35).addBox(-2.05F, -6.0F, -2.0F, 4.0F, 12.0F, 7.0F), PartPose.offsetAndRotation(0.0F, 4.0F, -12.0F, 0.5235988F, 0.0F, 0.0F));
		PartDefinition partDefinition4 = partDefinition3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 5.0F, 7.0F, cubeDeformation), PartPose.ZERO);
		partDefinition3.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(56, 36).addBox(-1.0F, -11.0F, 5.01F, 2.0F, 16.0F, 2.0F, cubeDeformation), PartPose.ZERO);
		partDefinition3.addOrReplaceChild("upper_mouth", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 5.0F, 5.0F, cubeDeformation), PartPose.ZERO);
		partDefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, cubeDeformation), PartPose.offset(4.0F, 14.0F, 7.0F));
		partDefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, cubeDeformation), PartPose.offset(-4.0F, 14.0F, 7.0F));
		partDefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, cubeDeformation), PartPose.offset(4.0F, 14.0F, -12.0F));
		partDefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, cubeDeformation), PartPose.offset(-4.0F, 14.0F, -12.0F));
		CubeDeformation cubeDeformation2 = cubeDeformation.extend(0.0F, 5.5F, 0.0F);
		partDefinition.addOrReplaceChild("left_hind_baby_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, cubeDeformation2), PartPose.offset(4.0F, 14.0F, 7.0F));
		partDefinition.addOrReplaceChild("right_hind_baby_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, cubeDeformation2), PartPose.offset(-4.0F, 14.0F, 7.0F));
		partDefinition.addOrReplaceChild("left_front_baby_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, cubeDeformation2), PartPose.offset(4.0F, 14.0F, -12.0F));
		partDefinition.addOrReplaceChild("right_front_baby_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, cubeDeformation2), PartPose.offset(-4.0F, 14.0F, -12.0F));
		partDefinition2.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(42, 36).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 4.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, 0.5235988F, 0.0F, 0.0F));
		partDefinition2.addOrReplaceChild("saddle", CubeListBuilder.create().texOffs(26, 0).addBox(-5.0F, -8.0F, -9.0F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
		partDefinition3.addOrReplaceChild("left_saddle_mouth", CubeListBuilder.create().texOffs(29, 5).addBox(2.0F, -9.0F, -6.0F, 1.0F, 2.0F, 2.0F, cubeDeformation), PartPose.ZERO);
		partDefinition3.addOrReplaceChild("right_saddle_mouth", CubeListBuilder.create().texOffs(29, 5).addBox(-3.0F, -9.0F, -6.0F, 1.0F, 2.0F, 2.0F, cubeDeformation), PartPose.ZERO);
		partDefinition3.addOrReplaceChild("left_saddle_line", CubeListBuilder.create().texOffs(32, 2).addBox(3.1F, -6.0F, -8.0F, 0.0F, 3.0F, 16.0F), PartPose.rotation(-0.5235988F, 0.0F, 0.0F));
		partDefinition3.addOrReplaceChild("right_saddle_line", CubeListBuilder.create().texOffs(32, 2).addBox(-3.1F, -6.0F, -8.0F, 0.0F, 3.0F, 16.0F), PartPose.rotation(-0.5235988F, 0.0F, 0.0F));
		partDefinition3.addOrReplaceChild("head_saddle", CubeListBuilder.create().texOffs(1, 1).addBox(-3.0F, -11.0F, -1.9F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.22F)), PartPose.ZERO);
		partDefinition3.addOrReplaceChild("mouth_saddle_wrap", CubeListBuilder.create().texOffs(19, 0).addBox(-2.0F, -11.0F, -4.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.ZERO);
		partDefinition4.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(19, 16).addBox(0.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.ZERO);
		partDefinition4.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(19, 16).addBox(-2.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.ZERO);

		return LayerDefinition.create(meshDefinition, 128, 128);
	}

	public void setupAnim(T abstractHorse, float f, float g, float h, float i, float j) {
		boolean bl = abstractHorse.isSaddled();
		boolean bl2 = abstractHorse.isVehicle();
		ModelPart[] var9 = this.saddleParts;
		int var10 = var9.length;

		int var11;
		ModelPart modelPart;
		for (var11 = 0; var11 < var10; ++var11) {
			modelPart = var9[var11];
			modelPart.visible = bl;
		}

		var9 = this.ridingParts;
		var10 = var9.length;

		for (var11 = 0; var11 < var10; ++var11) {
			modelPart = var9[var11];
			modelPart.visible = bl2 && bl;
		}

		this.body.y = 11.0F;
	}

	public @NotNull Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.headParts);
	}

	@Override
	protected @NotNull Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.rightHindBabyLeg, this.leftHindBabyLeg, this.rightFrontBabyLeg, this.leftFrontBabyLeg);
	}

	public void prepareMobModel(T abstractHorse, float f, float g, float h) {
		super.prepareMobModel(abstractHorse, f, g, h);
		float i = Mth.rotLerp(h, abstractHorse.yBodyRotO, abstractHorse.yBodyRot);
		float j = Mth.rotLerp(h, abstractHorse.yHeadRotO, abstractHorse.yHeadRot);
		float k = Mth.lerp(h, abstractHorse.xRotO, abstractHorse.getXRot());
		float l = j - i;
		float m = k * 0.017453292F;
		if (l > 20.0F) {
			l = 20.0F;
		}

		if (l < -20.0F) {
			l = -20.0F;
		}

		if (g > 0.2F) {
			m += Mth.cos(f * 0.8F) * 0.15F * g;
		}

		float n = abstractHorse.getEatAnim(h);
		float o = abstractHorse.getStandAnim(h);
		float p = 1.0F - o;
		float q = abstractHorse.getMouthAnim(h);
		boolean bl = abstractHorse.tailCounter != 0;
		float r = (float) abstractHorse.tickCount + h;
		this.headParts.y = 4.0F;
		this.headParts.z = -12.0F;
		this.body.xRot = 0.0F;
		this.headParts.xRot = 0.5235988F + m;
		this.headParts.yRot = l * 0.017453292F;
		float s = abstractHorse.isInWater() ? 0.2F : 1.0F;
		float t = Mth.cos(s * f * 0.6662F + 3.1415927F);
		float u = t * 0.8F * g;
		float v = (1.0F - Math.max(o, n)) * (0.5235988F + m + q * Mth.sin(r) * 0.05F);
		this.headParts.xRot = o * (0.2617994F + m) + n * (2.1816616F + Mth.sin(r) * 0.05F) + v;
		this.headParts.yRot = o * l * 0.017453292F + (1.0F - Math.max(o, n)) * this.headParts.yRot;
		this.headParts.y = o * -4.0F + n * 11.0F + (1.0F - Math.max(o, n)) * this.headParts.y;
		this.headParts.z = o * -4.0F + n * -12.0F + (1.0F - Math.max(o, n)) * this.headParts.z;
		this.body.xRot = o * -0.7853982F + p * this.body.xRot;
		float w = 0.2617994F * o;
		float x = Mth.cos(r * 0.6F + 3.1415927F);
		this.leftFrontLeg.y = 2.0F * o + 14.0F * p;
		this.leftFrontLeg.z = -6.0F * o - 10.0F * p;
		this.rightFrontLeg.y = this.leftFrontLeg.y;
		this.rightFrontLeg.z = this.leftFrontLeg.z;
		float y = (-1.0471976F + x) * o + u * p;
		float z = (-1.0471976F - x) * o - u * p;
		this.leftHindLeg.xRot = w - t * 0.5F * g * p;
		this.rightHindLeg.xRot = w + t * 0.5F * g * p;
		this.leftFrontLeg.xRot = y;
		this.rightFrontLeg.xRot = z;
		this.tail.xRot = 0.5235988F + g * 0.75F;
		this.tail.y = -5.0F + g;
		this.tail.z = 2.0F + g * 2.0F;
		if (bl) {
			this.tail.yRot = Mth.cos(r * 0.7F);
		} else {
			this.tail.yRot = 0.0F;
		}

		this.rightHindBabyLeg.y = this.rightHindLeg.y;
		this.rightHindBabyLeg.z = this.rightHindLeg.z;
		this.rightHindBabyLeg.xRot = this.rightHindLeg.xRot;
		this.leftHindBabyLeg.y = this.leftHindLeg.y;
		this.leftHindBabyLeg.z = this.leftHindLeg.z;
		this.leftHindBabyLeg.xRot = this.leftHindLeg.xRot;
		this.rightFrontBabyLeg.y = this.rightFrontLeg.y;
		this.rightFrontBabyLeg.z = this.rightFrontLeg.z;
		this.rightFrontBabyLeg.xRot = this.rightFrontLeg.xRot;
		this.leftFrontBabyLeg.y = this.leftFrontLeg.y;
		this.leftFrontBabyLeg.z = this.leftFrontLeg.z;
		this.leftFrontBabyLeg.xRot = this.leftFrontLeg.xRot;
		boolean bl2 = abstractHorse.isBaby();
		this.rightHindLeg.visible = !bl2;
		this.leftHindLeg.visible = !bl2;
		this.rightFrontLeg.visible = !bl2;
		this.leftFrontLeg.visible = !bl2;
		this.rightHindBabyLeg.visible = bl2;
		this.leftHindBabyLeg.visible = bl2;
		this.rightFrontBabyLeg.visible = bl2;
		this.leftFrontBabyLeg.visible = bl2;
		this.body.y = bl2 ? 10.8F : 0.0F;
	}
}
