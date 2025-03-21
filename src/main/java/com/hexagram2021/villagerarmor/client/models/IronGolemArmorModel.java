package com.hexagram2021.villagerarmor.client.models;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.util.Mth;

public class IronGolemArmorModel<S extends IronGolemRenderState> extends EntityModel<S> implements IHumanoidModel {
	protected final ModelPart head;
	protected final ModelPart body;
	protected final ModelPart leftArm;
	protected final ModelPart rightArm;
	protected final ModelPart leftLeg;
	protected final ModelPart rightLeg;
	
	public IronGolemArmorModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}
	
	public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 8.0F, 8.0F, cubeDeformation.extend(-0.75F, 0.25F, -0.75F)),
				PartPose.offset(0.0F, -7.0F, -2.0F));
		partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -2.0F, -2.5F, 8.0F, 12.0F, 4.0F, cubeDeformation.extend(4.05F, -0.5F, 2.75F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 16).addBox(-13.0F, 6.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(-0.5F, 8.25F, 0.25F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(40, 16).mirror().addBox(9.0F, 6.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(-0.5F, 8.25F, 0.25F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -1.0F, -2.75F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(1.25F, 2.0F, 0.75F)),
				PartPose.offset(-4.0F, 11.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.5F, -1.0F, -2.75F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(1.25F, 2.0F, 0.75F)),
				PartPose.offset(5.0F, 11.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setHeadVisible(boolean visible) {
		this.head.visible = visible;
	}

	@Override
	public void setHatVisible(boolean visible) {
	}

	@Override
	public void setBodyVisible(boolean visible) {
		this.body.visible = visible;
	}

	@Override
	public void setArmsVisible(boolean visible) {
		this.rightArm.visible = visible;
		this.leftArm.visible = visible;
	}

	@Override
	public void setLegsVisible(boolean visible) {
		this.leftLeg.visible = this.rightLeg.visible = visible;
	}

	@SuppressWarnings("unused")
	public void copyPropertiesTo(IronGolemArmorModel<S> model) {
		model.head.copyFrom(this.head);
		model.body.copyFrom(this.body);
		model.rightArm.copyFrom(this.rightArm);
		model.leftArm.copyFrom(this.leftArm);
		model.rightLeg.copyFrom(this.rightLeg);
		model.leftLeg.copyFrom(this.leftLeg);
	}

	@Override
	public <E extends EntityRenderState> void propertiesCopyFrom(EntityModel<E> model) {
		if(model instanceof IronGolemModel ironGolemModel) {
			this.head.copyFrom(ironGolemModel.head);
			this.body.copyFrom(ironGolemModel.root().getChild("body"));
			this.rightArm.copyFrom(ironGolemModel.rightArm);
			this.leftArm.copyFrom(ironGolemModel.leftArm);
			this.rightLeg.copyFrom(ironGolemModel.rightLeg);
			this.leftLeg.copyFrom(ironGolemModel.leftLeg);
		}
	}

	@Override
	public void setupAnim(S state) {
		super.setupAnim(state);
		float f = state.attackTicksRemaining;
		float f1 = state.walkAnimationSpeed;
		float f2 = state.walkAnimationPos;
		if (f > 0.0F) {
			this.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave(f, 10.0F);
			this.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave(f, 10.0F);
		} else {
			int i = state.offerFlowerTick;
			if (i > 0) {
				this.rightArm.xRot = -0.8F + 0.025F * Mth.triangleWave((float)i, 70.0F);
				this.leftArm.xRot = 0.0F;
			} else {
				this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(f2, 13.0F)) * f1;
				this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(f2, 13.0F)) * f1;
			}
		}

		this.head.yRot = state.yRot * (float) (Math.PI / 180.0D);
		this.head.xRot = state.xRot * (float) (Math.PI / 180.0D);
		this.rightLeg.xRot = -1.5F * Mth.triangleWave(f2, 13.0F) * f1;
		this.leftLeg.xRot = 1.5F * Mth.triangleWave(f2, 13.0F) * f1;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
	}
}
