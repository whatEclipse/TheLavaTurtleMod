package net.whateclipse.lavaturtlemod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.whateclipse.lavaturtlemod.entity.LavaTurtleEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class LavaTurtleModelAndAnimations<T extends LavaTurtleEntity> extends EntityModel<T> {

	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart eggbelly;
	private final ModelPart leg0;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;

	public LavaTurtleModelAndAnimations(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.eggbelly = this.body.getChild("eggbelly");
		this.leg0 = this.body.getChild("leg0");
		this.leg1 = this.body.getChild("leg1");
		this.leg2 = this.body.getChild("leg2");
		this.leg3 = this.body.getChild("leg3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(),
				PartPose.offset(0.0F, 11.0F, -10.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1",
				CubeListBuilder.create().texOffs(0, 26)
						.addBox(-5.5F, 3.0F, -13.0F, 11.0F, 18.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-9.5F, 3.0F, -10.0F, 19.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(48, 49).addBox(-3.0F,
				-1.0F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition eggbelly = body
				.addOrReplaceChild("eggbelly",
						CubeListBuilder.create().texOffs(28, 38).addBox(-4.5F, 3.0F, -14.0F, 9.0F, 18.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition leg0 = body.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 47).addBox(-2.0F, 0.0F,
				0.0F, 4.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 11.0F, 21.0F));

		PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(48, 38).addBox(-2.0F,
				0.0F, 0.0F, 4.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 11.0F, 21.0F));

		PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(28, 26).addBox(-13.0F,
				0.0F, -2.0F, 13.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 10.0F, 6.0F));

		PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(28, 32).addBox(0.0F, 0.0F,
				-2.0F, 13.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 10.0F, 6.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public void setupAnim(T entity, float f0, float g, float h, float i, float j) {
		this.leg0.xRot = Mth.cos(f0 * 0.6662F * 0.6F) * 0.5F * g;
		this.leg1.xRot = Mth.cos(f0 * 0.6662F * 0.6F + (float) Math.PI) * 0.5F * g;
		this.leg2.zRot = Mth.cos(f0 * 0.6662F * 0.6F + (float) Math.PI) * 0.5F * g;
		this.leg3.zRot = Mth.cos(f0 * 0.6662F * 0.6F) * 0.5F * g;
		this.leg2.xRot = 0.0F;
		this.leg3.xRot = 0.0F;
		this.leg2.yRot = 0.0F;
		this.leg3.yRot = 0.0F;
		this.leg0.yRot = 0.0F;
		this.leg1.yRot = 0.0F;

		if (!entity.isInWater() && !entity.isInLava() && entity.onGround()) {
			float f = entity.isLayingEgg() ? 4.0F : 1.0F;
			float f1 = entity.isLayingEgg() ? 2.0F : 1.0F;
			float f2 = 5.0F;
			this.leg2.yRot = Mth.cos(f * f0 * 5.0F + (float) Math.PI) * 8.0F * g * f1;
			this.leg2.zRot = 0.0F;
			this.leg3.yRot = Mth.cos(f * f0 * 5.0F) * 8.0F * g * f1;
			this.leg3.zRot = 0.0F;
			this.leg0.yRot = Mth.cos(f0 * 5.0F + (float) Math.PI) * 3.0F * g;
			this.leg0.xRot = 0.0F;
			this.leg1.yRot = Mth.cos(f0 * 5.0F) * 3.0F * g;
			this.leg1.xRot = 0.0F;
		}

		this.eggbelly.visible = !this.young && entity.hasEgg();
	}
}