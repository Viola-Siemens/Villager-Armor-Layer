package com.hexagram2021.villagerarmor.client;

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
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.ClientHooks;

public class VillagerArmorLayer<T extends LivingEntity, M extends HierarchicalModel<T>, A extends HierarchicalModel<T> & IHumanoidModel> extends RenderLayer<T, M> {
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

				ArmorMaterial armormaterial = armoritem.getMaterial().value();
				int i = itemstack.is(ItemTags.DYEABLE) ? DyedItemColor.getOrDefault(itemstack, -6265536) : -1;

				for (ArmorMaterial.Layer armormaterial$layer : armormaterial.layers()) {
					float r;
					float g;
					float b;
					if (armormaterial$layer.dyeable() && i != -1) {
						r = (float) FastColor.ARGB32.red(i) / 255.0F;
						g = (float)FastColor.ARGB32.green(i) / 255.0F;
						b = (float)FastColor.ARGB32.blue(i) / 255.0F;
					} else {
						r = 1.0F;
						g = 1.0F;
						b = 1.0F;
					}

					ResourceLocation texture = ClientHooks.getArmorTexture(entity, itemstack, armormaterial$layer, inner, slotType);
					this.renderModel(transform, buffer, uv2, model, r, g, b, texture);
				}

				ArmorTrim armortrim = itemstack.get(DataComponents.TRIM);
				if (armortrim != null) {
					this.renderTrim(armoritem.getMaterial(), transform, buffer, uv2, armortrim, model, inner);
				}
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

	private void renderModel(PoseStack transform, MultiBufferSource multiBufferSource, int uv2, Model model, float r, float g, float b, ResourceLocation armorResource) {
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(armorResource));
		model.renderToBuffer(transform, vertexconsumer, uv2, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
	}
	
	private void renderTrim(Holder<ArmorMaterial> armorMaterial, PoseStack transform, MultiBufferSource multiBufferSource, int uv2, ArmorTrim trim, Model model, boolean inner) {
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
}
