package com.hexagram2021.villagerarmor.client;

import com.google.common.collect.Maps;
import com.hexagram2021.villagerarmor.client.models.IHumanoidModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.neoforged.neoforge.client.ClientHooks;

import javax.annotation.Nullable;
import java.util.Map;

public class VillagerArmorLayer<T extends LivingEntity, M extends HierarchicalModel<T>, A extends HierarchicalModel<T> & IHumanoidModel> extends RenderLayer<T, M> {
	private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
	private final A innerModel;
	private final A outerModel;
	private final TextureAtlas armorTrimAtlas;

	public VillagerArmorLayer(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager) {
		super(renderer);
		this.innerModel = innerModel;
		this.outerModel = outerModel;
		this.armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
	}

	@Override
	public void render(PoseStack transform, MultiBufferSource buffer, int uv2, T entity, float f1, float f2, float ticks, float f3, float f4, float xRot) {
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.CHEST, uv2, this.getArmorModel(EquipmentSlot.CHEST));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.LEGS, uv2, this.getArmorModel(EquipmentSlot.LEGS));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.FEET, uv2, this.getArmorModel(EquipmentSlot.FEET));
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.HEAD, uv2, this.getArmorModel(EquipmentSlot.HEAD));
	}

	private void renderArmorPiece(PoseStack transform, MultiBufferSource buffer, T entity, EquipmentSlot slotType, int uv2, A model) {
		ItemStack itemstack = entity.getItemBySlot(slotType);
		if (itemstack.getItem() instanceof ArmorItem armoritem) {
			if (armoritem.getEquipmentSlot() == slotType) {
				model.propertiesCopyFrom(this.getParentModel());
				this.setPartVisibility(model, slotType);
				model.afterSetPartVisibility(this.getParentModel());
				boolean inner = this.usesInnerModel(slotType);
				if (armoritem instanceof DyeableLeatherItem leatherItem) {
					int i = leatherItem.getColor(itemstack);
					float r = (float)(i >> 16 & 255) / 255.0F;
					float g = (float)(i >> 8 & 255) / 255.0F;
					float b = (float)(i & 255) / 255.0F;
					this.renderModel(transform, buffer, uv2, armoritem, model, inner, r, g, b, this.getArmorResource(entity, itemstack, slotType, null));
					this.renderModel(transform, buffer, uv2, armoritem, model, inner, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slotType, "overlay"));
				} else {
					this.renderModel(transform, buffer, uv2, armoritem, model, inner, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, slotType, null));
				}
				
				ArmorTrim.getTrim(entity.level().registryAccess(), itemstack, true).ifPresent(trim ->
						this.renderTrim(armoritem.getMaterial(), transform, buffer, uv2, trim, model, inner));
				if (itemstack.hasFoil()) {
					this.renderGlint(transform, buffer, uv2, model);
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
	
	@SuppressWarnings("unused")
	private void renderModel(PoseStack transform, MultiBufferSource multiBufferSource, int uv2, ArmorItem armorItem, Model model, boolean inner, float r, float g, float b, ResourceLocation armorResource) {
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(armorResource));
		model.renderToBuffer(transform, vertexconsumer, uv2, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
	}
	
	private void renderTrim(ArmorMaterial armorMaterial, PoseStack transform, MultiBufferSource multiBufferSource, int uv2, ArmorTrim trim, Model model, boolean inner) {
		TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(inner ? trim.innerTexture(armorMaterial) : trim.outerTexture(armorMaterial));
		VertexConsumer vertexconsumer = textureatlassprite.wrap(multiBufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
		model.renderToBuffer(transform, vertexconsumer, uv2, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void renderGlint(PoseStack transform, MultiBufferSource multiBufferSource, int uv2, Model model) {
		model.renderToBuffer(transform, multiBufferSource.getBuffer(RenderType.armorEntityGlint()), uv2, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
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

		s1 = ClientHooks.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
		}

		return resourcelocation;
	}
}
