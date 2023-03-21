package com.hexagram2021.villagerarmor.client;

import com.google.common.collect.Maps;
import com.hexagram2021.villagerarmor.client.models.IHumanoidModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class HumanoidArmorLayer<T extends LivingEntity, M extends SegmentedModel<T>, A extends IHumanoidModel> extends LayerRenderer<T, M> {
	private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
	private final A innerModel;
	private final A outerModel;

	public HumanoidArmorLayer(IEntityRenderer<T, M> renderer, A innerModel, A outerModel) {
		super(renderer);
		this.innerModel = innerModel;
		this.outerModel = outerModel;
	}

	@Override
	public void render(@Nonnull MatrixStack transform, @Nonnull IRenderTypeBuffer buffer, int uv2, @Nonnull T entity, float f1, float f2, float ticks, float f3, float f4, float xRot) {
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlotType.CHEST, uv2, this.getArmorModel(EquipmentSlotType.CHEST));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlotType.LEGS, uv2, this.getArmorModel(EquipmentSlotType.LEGS));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlotType.FEET, uv2, this.getArmorModel(EquipmentSlotType.FEET));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlotType.HEAD, uv2, this.getArmorModel(EquipmentSlotType.HEAD));
	}

	private void renderArmorPiece(MatrixStack transform, IRenderTypeBuffer buffer, T entity, EquipmentSlotType slotType, int uv2, A model) {
		ItemStack itemstack = entity.getItemBySlot(slotType);
		if (itemstack.getItem() instanceof ArmorItem) {
			ArmorItem armoritem = (ArmorItem)itemstack.getItem();
			if (armoritem.getSlot() == slotType) {
				model.propertiesCopyFrom(this.getParentModel());
				this.setPartVisibility(model, slotType);
				boolean foil = itemstack.hasFoil();
				if (armoritem instanceof IDyeableArmorItem) {
					int i = ((IDyeableArmorItem)armoritem).getColor(itemstack);
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

	protected void setPartVisibility(A model, EquipmentSlotType slotType) {
		model.setAllVisible(false);
		switch(slotType) {
			case HEAD:
				model.setHeadVisible(true);
				model.setHatVisible(true);
				break;
			case CHEST:
				model.setBodyVisible(true);
				model.setArmsVisible(true);
				break;
			case LEGS:
				model.setBodyVisible(true);
				model.setLegsVisible(true);
				break;
			case FEET:
				model.setLegsVisible(true);
		}
	}

	private void renderModel(MatrixStack transform, IRenderTypeBuffer buffer, int uv2, boolean foil, A model, float r, float g, float b, ResourceLocation armorResource) {
		IVertexBuilder ivertexbuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorResource), false, foil);
		model.renderModelToBuffer(transform, ivertexbuilder, uv2, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
	}

	private A getArmorModel(EquipmentSlotType slotType) {
		return this.usesInnerModel(slotType) ? this.innerModel : this.outerModel;
	}

	private boolean usesInnerModel(EquipmentSlotType slotType) {
		return slotType == EquipmentSlotType.LEGS;
	}

	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlotType slot, @Nullable String type) {
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
