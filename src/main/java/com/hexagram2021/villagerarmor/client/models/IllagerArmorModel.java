package com.hexagram2021.villagerarmor.client.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class IllagerArmorModel extends SegmentedModel<AbstractIllagerEntity> implements IHumanoidModel, IHeadToggle {
	protected ModelRenderer head;
	protected final ModelRenderer body;
	protected final ModelRenderer leftLeg;
	protected final ModelRenderer rightLeg;
	protected final ModelRenderer arms;
	protected final ModelRenderer leftArm;
	protected final ModelRenderer rightArm;

	public IllagerArmorModel(float root) {
		this.texWidth = 64;
		this.texHeight = 32;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, root);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, root + 1.0F);
		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, root - 0.1F);
		this.leftLeg = new ModelRenderer(this, 0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, root - 0.1F);
		this.arms = new ModelRenderer(this, 40, 16);
		this.arms.setPos(0.0F, 2.0F, 0.0F);
		this.arms.addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, root);
		this.arms.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, root, true);
		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, root);
		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, root, true);
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
	public void copyPropertiesTo(IllagerArmorModel model) {
		super.copyPropertiesTo(model);
		model.head.copyFrom(this.head);
		model.body.copyFrom(this.body);
		model.rightArm.copyFrom(this.rightArm);
		model.leftArm.copyFrom(this.leftArm);
		model.arms.copyFrom(this.arms);
		model.rightLeg.copyFrom(this.rightLeg);
		model.leftLeg.copyFrom(this.leftLeg);
	}

	@Override
	public <E extends Entity> void propertiesCopyFrom(EntityModel<E> model) {
		this.attackTime = model.attackTime;
		this.riding = model.riding;
		this.young = model.young;
		if(model instanceof IllagerModel) {
			IllagerModel<?> illagerModel = (IllagerModel<?>) model;
			this.head.copyFrom(illagerModel.head);
			this.body.copyFrom(illagerModel.body);
			this.rightArm.copyFrom(illagerModel.rightArm);
			this.leftArm.copyFrom(illagerModel.leftArm);
			this.arms.copyFrom(illagerModel.arms);
			this.rightLeg.copyFrom(illagerModel.rightLeg);
			this.leftLeg.copyFrom(illagerModel.leftLeg);
		}
	}

	@Override
	public <E extends Entity> void afterSetPartVisibility(EntityModel<E> model) {
		if(model instanceof IllagerModel) {
			IllagerModel<?> illagerModel = (IllagerModel<?>) model;
			this.rightArm.visible &= illagerModel.rightArm.visible;
			this.leftArm.visible &= illagerModel.leftArm.visible;
			this.arms.visible &= illagerModel.arms.visible;
		}
	}

	@Override
	public void renderModelToBuffer(MatrixStack transform, IVertexBuilder builder, int uv2, int overlayType, float r, float g, float b, float a) {
		this.renderToBuffer(transform, builder, uv2, overlayType, r, g, b, a);
	}

	@Override
	public void hatVisible(boolean visible) {
		this.head.visible = visible;
	}

	@Override @Nonnull
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.head, this.body, this.leftLeg, this.rightLeg, this.arms, this.leftArm, this.rightArm);
	}

	@Override
	public void setupAnim(@Nonnull AbstractIllagerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		this.arms.y = 3.0F;
		this.arms.z = -1.0F;
		this.arms.xRot = -0.75F;
		if (this.riding) {
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
			this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.leftLeg.yRot = 0.0F;
			this.leftLeg.zRot = 0.0F;
			this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.rightLeg.yRot = 0.0F;
			this.rightLeg.zRot = 0.0F;
		}

		AbstractIllagerEntity.ArmPose armPose = entity.getArmPose();
		if (armPose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (entity.getMainHandItem().isEmpty()) {
				ModelHelper.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, ageInTicks);
			} else {
				ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, ageInTicks);
			}
		} else if (armPose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.zRot = 2.3561945F;
			this.leftArm.zRot = -2.3561945F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.yRot = 0.0F;
		} else if (armPose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
			this.rightArm.yRot = -0.1F + this.head.yRot;
			this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot = -0.9424779F + this.head.xRot;
			this.leftArm.yRot = this.head.yRot - 0.4F;
			this.leftArm.zRot = ((float)Math.PI / 2F);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
			ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
			ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.rightArm.zRot = 2.670354F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.leftArm.zRot = -2.3561945F;
			this.leftArm.yRot = 0.0F;
		}

		boolean flag = armPose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.visible = flag;
		this.leftArm.visible = !flag;
		this.rightArm.visible = !flag;
	}
}
