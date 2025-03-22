package com.hexagram2021.villagerarmor.client;

import com.hexagram2021.villagerarmor.client.models.VillagerElytraModel;
import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VillagerWingsLayer<S extends LivingEntityRenderState & IHumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
	private final VillagerElytraModel<S> elytraModel;
	private final VillagerElytraModel<S> elytraBabyModel;
	private final EquipmentLayerRenderer equipmentRenderer;

	public VillagerWingsLayer(RenderLayerParent<S, M> parent, EntityModelSet modelSet, EquipmentLayerRenderer equipmentLayerRenderer) {
		super(parent);
		this.elytraModel = new VillagerElytraModel<>(modelSet.bakeLayer(VALModelLayers.ELYTRA));
		this.elytraBabyModel = new VillagerElytraModel<>(modelSet.bakeLayer(VALModelLayers.ELYTRA_BABY));
		this.equipmentRenderer = equipmentLayerRenderer;
	}

	@Override
	public void render(PoseStack transform, MultiBufferSource bufferSource, int uv2, S renderState, float netHeadYaw, float headPitch) {
		ItemStack itemstack = renderState.val$chestEquipment();
		Equippable equippable = itemstack.get(DataComponents.EQUIPPABLE);
		if (equippable != null && equippable.assetId().isPresent()) {
			VillagerElytraModel<S> model = renderState.isBaby ? this.elytraBabyModel : this.elytraModel;
			transform.pushPose();
			transform.translate(0.0F, 0.0F, 0.125F);
			model.setupAnim(renderState);
			this.equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.WINGS, equippable.assetId().get(), model, itemstack, transform, bufferSource, uv2, null);
			transform.popPose();
		}
	}
}
