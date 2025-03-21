package com.hexagram2021.villagerarmor.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.AbstractIllager;

public class IllagerArmorModel<S extends IllagerRenderState> extends EntityModel<S> implements IHumanoidModel, VillagerHeadModel {
	protected final ModelPart head;
	protected final ModelPart body;
	protected final ModelPart leftLeg;
	protected final ModelPart rightLeg;
	protected final ModelPart arms;
	protected final ModelPart leftArm;
	protected final ModelPart rightArm;

	public IllagerArmorModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
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
		partdefinition.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(-0.25F)),
				PartPose.offset(-5.0F, 2.0F + y, 0.0F));
		partdefinition.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(-0.25F)),
				PartPose.offset(5.0F, 2.0F + y, 0.0F));
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
		this.leftArm.visible = this.rightArm.visible = this.arms.visible = visible;
	}

	@Override
	public void setLegsVisible(boolean visible) {
		this.leftLeg.visible = this.rightLeg.visible = visible;
	}

	@SuppressWarnings("unused")
	public void copyPropertiesTo(IllagerArmorModel<S> model) {
		model.head.copyFrom(this.head);
		model.body.copyFrom(this.body);
		model.rightArm.copyFrom(this.rightArm);
		model.leftArm.copyFrom(this.leftArm);
		model.arms.copyFrom(this.arms);
		model.rightLeg.copyFrom(this.rightLeg);
		model.leftLeg.copyFrom(this.leftLeg);
	}

	@Override
	public <E extends EntityRenderState> void propertiesCopyFrom(EntityModel<E> model) {
		if(model instanceof IllagerModel<?> illagerModel) {
			this.head.copyFrom(illagerModel.head);
			this.body.copyFrom(illagerModel.root().getChild("body"));
			this.rightArm.copyFrom(illagerModel.rightArm);
			this.leftArm.copyFrom(illagerModel.leftArm);
			this.arms.copyFrom(illagerModel.arms);
			this.rightLeg.copyFrom(illagerModel.rightLeg);
			this.leftLeg.copyFrom(illagerModel.leftLeg);
		}
	}

	@Override
	public <E extends EntityRenderState> void afterSetPartVisibility(EntityModel<E> model) {
		if(model instanceof IllagerModel<?> illagerModel) {
			this.rightArm.visible &= illagerModel.rightArm.visible;
			this.leftArm.visible &= illagerModel.leftArm.visible;
			this.arms.visible &= illagerModel.arms.visible;
		}
	}

	@Override
	public void hatVisible(boolean visible) {
		this.head.visible = visible;
	}

	@Override
	public void setupAnim(S state) {
		super.setupAnim(state);
		this.head.yRot = state.yRot * ((float)Math.PI / 180F);
		this.head.xRot = state.xRot * ((float)Math.PI / 180F);
		this.arms.y = 3.0F;
		this.arms.z = -1.0F;
		this.arms.xRot = -0.75F;
		if (state.isRiding) {
			this.rightArm.xRot = (-(float)Math.PI / 5F);
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = (-(float)Math.PI / 5F);
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = ((float)Math.PI / 10F);
			this.leftLeg.zRot = 0.07853982F;
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = (-(float)Math.PI / 10F);
			this.rightLeg.zRot = -0.07853982F;
		} else {
			this.rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float)Math.PI) * 2.0F * state.walkAnimationSpeed * 0.5F;
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 2.0F * state.walkAnimationSpeed * 0.5F;
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed * 0.5F;
			this.leftLeg.yRot = 0.0F;
			this.leftLeg.zRot = 0.0F;
			this.rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float)Math.PI) * 1.4F * state.walkAnimationSpeed * 0.5F;
			this.rightLeg.yRot = 0.0F;
			this.rightLeg.zRot = 0.0F;
		}

		AbstractIllager.IllagerArmPose armPose = state.armPose;
		if (armPose == AbstractIllager.IllagerArmPose.ATTACKING) {
			if (state.getMainHandItem().isEmpty()) {
				AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, state.attackAnim, state.ageInTicks);
			} else {
				AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, state.mainArm, state.attackAnim, state.ageInTicks);
			}
		} else if (armPose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.rightArm.xRot = Mth.cos(state.ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.xRot = Mth.cos(state.ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.zRot = (float) (Math.PI * 0.75D);
			this.leftArm.zRot = (float) (-Math.PI * 0.75D);
			this.rightArm.yRot = 0.0F;
			this.leftArm.yRot = 0.0F;
		} else if (armPose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
			this.rightArm.yRot = -0.1F + this.head.yRot;
			this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot = (float) (-Math.PI * 0.3D) + this.head.xRot;
			this.leftArm.yRot = this.head.yRot - 0.4F;
			this.leftArm.zRot = ((float)Math.PI / 2F);
		} else if (armPose == AbstractIllager.IllagerArmPose.CROSSBOW_HOLD) {
			AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
		} else if (armPose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE) {
			AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, state.maxCrossbowChargeDuration, state.ticksUsingItem, true);
		} else if (armPose == AbstractIllager.IllagerArmPose.CELEBRATING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.rightArm.xRot = Mth.cos(state.ageInTicks * 0.6662F) * 0.05F;
			this.rightArm.zRot = (float) (Math.PI * 0.85D);
			this.rightArm.yRot = 0.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.leftArm.xRot = Mth.cos(state.ageInTicks * 0.6662F) * 0.05F;
			this.leftArm.zRot = (float) (-Math.PI * 0.75D);
			this.leftArm.yRot = 0.0F;
		}

		boolean flag = armPose == AbstractIllager.IllagerArmPose.CROSSED;
		this.arms.visible = flag;
		this.leftArm.visible = !flag;
		this.rightArm.visible = !flag;
	}
}
