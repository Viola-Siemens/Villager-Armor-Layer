package com.hexagram2021.villagerarmor.mixin;

import com.hexagram2021.villagerarmor.client.models.IHumanoidModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VillagerModel.class)
public class VillagerModelMixin implements IHumanoidModel {
	@Shadow protected ModelRenderer head;
	@Shadow protected ModelRenderer hat;
	@Shadow @Final protected ModelRenderer hatRim;
	@Shadow @Final protected ModelRenderer body;
	@Shadow @Final protected ModelRenderer arms;
	@Shadow @Final protected ModelRenderer leg0;
	@Shadow @Final protected ModelRenderer leg1;

	@Override
	public void setHeadVisible(boolean visible) {
		this.head.visible = visible;
	}

	@Override
	public void setHatVisible(boolean visible) {
		this.hat.visible = visible;
		this.hatRim.visible = visible;
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
		this.leg0.visible = this.leg1.visible = visible;
	}

	@Override
	public <E extends Entity> void propertiesCopyFrom(EntityModel<E> model) {
		VillagerModel<?> current = (VillagerModel<?>)(Object)this;
		current.attackTime = model.attackTime;
		current.riding = model.riding;
		current.young = model.young;
	}

	@Override
	public void renderModelToBuffer(MatrixStack transform, IVertexBuilder builder, int uv2, int overlayType, float r, float g, float b, float a) {
		((VillagerModel<?>)(Object)this).renderToBuffer(transform, builder, uv2, overlayType, r, g, b, a);
	}
}
