package com.hexagram2021.villagerarmor.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;

public interface IHumanoidModel {
	void setHeadVisible(boolean visible);
	void setHatVisible(boolean visible);
	void setBodyVisible(boolean visible);
	void setArmsVisible(boolean visible);
	void setLegsVisible(boolean visible);

	default void setAllVisible(boolean visible) {
		this.setHeadVisible(visible);
		this.setHatVisible(visible);
		this.setBodyVisible(visible);
		this.setArmsVisible(visible);
		this.setLegsVisible(visible);
	}

	default <E extends Entity> void afterSetPartVisibility(EntityModel<E> model) {
	}

	<E extends Entity> void propertiesCopyFrom(EntityModel<E> model);

	void renderModelToBuffer(PoseStack transform, VertexConsumer builder, int uv2, int overlayType, float r, float g, float b, float a);
}
