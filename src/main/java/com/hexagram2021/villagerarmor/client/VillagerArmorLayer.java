package com.hexagram2021.villagerarmor.client;

import com.hexagram2021.villagerarmor.client.models.IHumanoidModel;
import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentModel;
import net.minecraft.world.item.equipment.Equippable;

public class VillagerArmorLayer<S extends LivingEntityRenderState & IHumanoidRenderState, M extends EntityModel<S>, A extends EntityModel<S> & IHumanoidModel> extends RenderLayer<S, M> {
	private final A innerModel;
	private final A outerModel;
	private final EquipmentLayerRenderer equipmentRenderer;

	public VillagerArmorLayer(RenderLayerParent<S, M> renderer, A innerModel, A outerModel, EquipmentLayerRenderer equipmentRenderer) {
		super(renderer);
		this.innerModel = innerModel;
		this.outerModel = outerModel;
		this.equipmentRenderer = equipmentRenderer;
	}

	private static boolean shouldRender(Equippable equippable, EquipmentSlot equipmentSlot) {
		return equippable.model().isPresent() && equippable.slot() == equipmentSlot;
	}

	@Override
	public void render(PoseStack transform, MultiBufferSource buffer, int uv2, S state, float netHeadYaw, float headPitch) {
		this.renderArmorPiece(transform, buffer, state.val$chestItem(), EquipmentSlot.CHEST, uv2, this.getArmorModel(EquipmentSlot.CHEST));
		this.renderArmorPiece(transform, buffer, state.val$legItem(), EquipmentSlot.LEGS, uv2, this.getArmorModel(EquipmentSlot.LEGS));
		this.renderArmorPiece(transform, buffer, state.val$feetItem(), EquipmentSlot.FEET, uv2, this.getArmorModel(EquipmentSlot.FEET));
		this.renderArmorPiece(transform, buffer, state.headItem, EquipmentSlot.HEAD, uv2, this.getArmorModel(EquipmentSlot.HEAD));
	}

	private void renderArmorPiece(PoseStack transform, MultiBufferSource buffer, ItemStack itemStack, EquipmentSlot slotType, int uv2, A model) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		if (equippable != null && shouldRender(equippable, slotType)) {
			model.propertiesCopyFrom(this.getParentModel());
			this.setPartVisibility(model, slotType);
			model.afterSetPartVisibility(this.getParentModel());
			ResourceLocation resourcelocation = equippable.model().orElseThrow();
			EquipmentModel.LayerType equipmentmodel$layertype = this.usesInnerModel(slotType)
					? EquipmentModel.LayerType.HUMANOID_LEGGINGS
					: EquipmentModel.LayerType.HUMANOID;
			this.equipmentRenderer.renderLayers(equipmentmodel$layertype, resourcelocation, model, itemStack, transform, buffer, uv2);
		}
	}

	protected void setPartVisibility(A model, EquipmentSlot slotType) {
		model.setAllVisible(false);
		switch (slotType) {
			case HEAD -> {
				model.setHeadVisible(true);
				model.setHatVisible(true);
			}
			case CHEST -> {
				model.setBodyVisible(true);
				model.setArmsVisible(true);
			}
			case LEGS -> {
				model.setBodyVisible(true);
				model.setLegsVisible(true);
			}
			case FEET -> model.setLegsVisible(true);
		}
	}

	private A getArmorModel(EquipmentSlot slotType) {
		return this.usesInnerModel(slotType) ? this.innerModel : this.outerModel;
	}

	private boolean usesInnerModel(EquipmentSlot slotType) {
		return slotType == EquipmentSlot.LEGS;
	}
}
