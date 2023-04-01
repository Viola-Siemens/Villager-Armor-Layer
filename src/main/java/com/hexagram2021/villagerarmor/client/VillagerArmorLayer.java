package com.hexagram2021.villagerarmor.client;

import com.google.common.collect.Maps;
import com.hexagram2021.villagerarmor.client.models.IHumanoidModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class VillagerArmorLayer<T extends LivingEntity, M extends HierarchicalModel<T>, A extends IHumanoidModel> extends RenderLayer<T, M> {
	private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
	private final A innerModel;
	private final A outerModel;

	public VillagerArmorLayer(RenderLayerParent<T, M> renderer, A innerModel, A outerModel) {
		super(renderer);
		this.innerModel = innerModel;
		this.outerModel = outerModel;
	}

	@Override
	public void render(@Nonnull PoseStack transform, @Nonnull MultiBufferSource buffer, int uv2, @Nonnull T entity, float f1, float f2, float ticks, float f3, float f4, float xRot) {
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.CHEST, uv2, this.getArmorModel(EquipmentSlot.CHEST));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.LEGS, uv2, this.getArmorModel(EquipmentSlot.LEGS));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.FEET, uv2, this.getArmorModel(EquipmentSlot.FEET));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.HEAD, uv2, this.getArmorModel(EquipmentSlot.HEAD));
	}

	private void renderArmorPiece(PoseStack transform, MultiBufferSource buffer, T entity, EquipmentSlot slotType, int uv2, A model) {
		ItemStack itemstack = entity.getItemBySlot(slotType);
		if (itemstack.getItem() instanceof ArmorItem armoritem) {
			if (armoritem.getSlot() == slotType) {
				model.propertiesCopyFrom(this.getParentModel());
				this.setPartVisibility(model, slotType);
				model.afterSetPartVisibility(this.getParentModel());
				boolean foil = itemstack.hasFoil();
				if (armoritem instanceof DyeableLeatherItem leatherItem) {
					int i = leatherItem.getColor(itemstack);
					float r = (float)(i >> 16 & 255) / 255.0F;
					float g = (float)(i >> 8 & 255) / 255.0F;
					float b = (float)(i & 255) / 255.0F;
					this.renderModel(transform, buffer, uv2, foil, model, r, g, b, this.getArmorResource(entity, itemstack, slotType, null));
					this.renderModel(transform, buffer, uv2, foil, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slotType, "overlay"));
				} else {
					this.renderModel(transform, buffer, uv2, foil, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slotType, null));
				}

			}
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

	private void renderModel(PoseStack transform, MultiBufferSource buffer, int uv2, boolean foil, A model, float r, float g, float b, ResourceLocation armorResource) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorResource), false, foil);
		model.renderModelToBuffer(transform, vertexConsumer, uv2, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
	}

	private A getArmorModel(EquipmentSlot slotType) {
		return this.usesInnerModel(slotType) ? this.innerModel : this.outerModel;
	}

	private boolean usesInnerModel(EquipmentSlot slotType) {
		return slotType == EquipmentSlot.LEGS;
	}

	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
		ArmorItem item = (ArmorItem)stack.getItem();
		String texture = item.getMaterial().getName();
		String domain = "minecraft";
		int idx = texture.indexOf(':');
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}
		String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (usesInnerModel(slot) ? 2 : 1), type == null ? "" : String.format("_%s", type));

		s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
		}

		return resourcelocation;
	}
}
