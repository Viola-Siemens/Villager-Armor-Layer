package com.hexagram2021.villagerarmor.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.WitchRenderState;
import net.minecraft.util.Mth;

public class WitchArmorModel<S extends WitchRenderState> extends EntityModel<S> implements IHumanoidModel, VillagerHeadModel {
	protected final ModelPart head;
	protected final ModelPart body;
	protected final ModelPart leftLeg;
	protected final ModelPart rightLeg;
	protected final ModelPart arms;

	public WitchArmorModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.arms = root.getChild("arms");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}
	
	public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation, float y, float extend) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation),
				PartPose.offset(0.0F, 0.0F + y, 0.0F));
		partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F, 1.0F, 1.0F)),
				PartPose.offset(0.0F, 0.0F + y, 0.0F));
		partdefinition.addOrReplaceChild("arms",
				CubeListBuilder.create()
						.texOffs(40, 16).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, cubeDeformation.extend(-0.25F))
						.texOffs(40, 16).mirror().addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, cubeDeformation.extend(-0.25F)),
				PartPose.offset(0.0F, 2.0F + y, 0.0F));
		partdefinition.addOrReplaceChild("right_leg",
				CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(extend)),
				PartPose.offset(-2.0F, 12.0F + y, 0.0F));
		partdefinition.addOrReplaceChild("left_leg",
				CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(extend)),
				PartPose.offset(2.0F, 12.0F + y, 0.0F));
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
		this.arms.visible = visible;
	}

	@Override
	public void setLegsVisible(boolean visible) {
		this.leftLeg.visible = this.rightLeg.visible = visible;
	}

	@SuppressWarnings("unused")
	public void copyPropertiesTo(WitchArmorModel<S> model) {
		model.head.copyFrom(this.head);
		model.body.copyFrom(this.body);
		model.arms.copyFrom(this.arms);
		model.rightLeg.copyFrom(this.rightLeg);
		model.leftLeg.copyFrom(this.leftLeg);
	}

	@Override
	public <E extends EntityRenderState> void propertiesCopyFrom(EntityModel<E> model) {
		if(model instanceof WitchModel witchModel) {
			this.head.copyFrom(witchModel.getHead());
			this.body.copyFrom(witchModel.root().getChild("body"));
			this.arms.copyFrom(witchModel.root().getChild("arms"));
			this.rightLeg.copyFrom(witchModel.rightLeg);
			this.leftLeg.copyFrom(witchModel.leftLeg);
		}
	}

	@Override
	public <E extends EntityRenderState> void afterSetPartVisibility(EntityModel<E> model) {
		if(model instanceof WitchModel witchModel) {
			this.arms.visible &= witchModel.root().getChild("arms").visible;
		}
	}

	@Override
	public void hatVisible(boolean visible) {
		this.head.visible = visible;
	}

	@Override
	public void setupAnim(S state) {
		super.setupAnim(state);
		this.head.yRot = state.yRot * (float) (Math.PI / 180.0D);
		this.head.xRot = state.xRot * (float) (Math.PI / 180.0D);
		this.rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed * 0.5F;
		this.leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed * 0.5F;
	}
}
