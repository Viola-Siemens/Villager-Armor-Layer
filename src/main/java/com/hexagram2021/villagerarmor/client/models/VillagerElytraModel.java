package com.hexagram2021.villagerarmor.client.models;

import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VillagerElytraModel<T extends LivingEntityRenderState & IHumanoidRenderState> extends EntityModel<T> {
	private final ModelPart rightWing;
	private final ModelPart leftWing;

	public VillagerElytraModel(ModelPart root) {
		super(root);
		this.leftWing = root.getChild("left_wing");
		this.rightWing = root.getChild("right_wing");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		CubeDeformation cubeDeformation = new CubeDeformation(1.0F);
		root.addOrReplaceChild(
				"left_wing",
				CubeListBuilder.create().texOffs(22, 0).addBox(-10.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, cubeDeformation),
				PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, (float) (Math.PI / 12), 0.0F, (float) (-Math.PI / 12))
		);
		root.addOrReplaceChild(
				"right_wing",
				CubeListBuilder.create().texOffs(22, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, cubeDeformation),
				PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, (float) (Math.PI / 12), 0.0F, (float) (Math.PI / 12))
		);
		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);
		this.leftWing.y = state.val$isCrouching() ? 3.0F : 0.0F;
		this.leftWing.xRot = state.val$elytraRotX();
		this.leftWing.yRot = state.val$elytraRotY();
		this.leftWing.zRot = state.val$elytraRotZ();
		this.rightWing.yRot = -this.leftWing.yRot;
		this.rightWing.y = this.leftWing.y;
		this.rightWing.xRot = this.leftWing.xRot;
		this.rightWing.zRot = -this.leftWing.zRot;
	}
}
