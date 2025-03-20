package com.hexagram2021.villagerarmor.client;

import com.hexagram2021.villagerarmor.client.models.IHumanoidModel;
import com.hexagram2021.villagerarmor.utils.VALLogger;
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
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

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
	public void render(PoseStack transform, MultiBufferSource buffer, int uv2, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.CHEST, uv2, this.getArmorModel(EquipmentSlot.CHEST), limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.LEGS, uv2, this.getArmorModel(EquipmentSlot.LEGS), limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.FEET, uv2, this.getArmorModel(EquipmentSlot.FEET), limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
		this.renderArmorPiece(transform, buffer, entity, EquipmentSlot.HEAD, uv2, this.getArmorModel(EquipmentSlot.HEAD), limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
	}

	private static boolean WARN_NEOFORGE_UNDER_21_1_18 = false;
	private void renderArmorPiece(PoseStack transform, MultiBufferSource buffer, T entity, EquipmentSlot slotType, int uv2, A model,
								  float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = entity.getItemBySlot(slotType);
		if (itemstack.getItem() instanceof ArmorItem armoritem) {
			if (armoritem.getEquipmentSlot() == slotType) {
				model.propertiesCopyFrom(this.getParentModel());
				this.setPartVisibility(model, slotType);
				model.afterSetPartVisibility(this.getParentModel());
				boolean inner = this.usesInnerModel(slotType);

				ArmorMaterial armormaterial = armoritem.getMaterial().value();
				try {
					IClientItemExtensions extensions = IClientItemExtensions.of(itemstack);
					extensions.setupModelAnimations(entity, itemstack, slotType, model, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
					int fallbackColor = extensions.getDefaultDyeColor(itemstack);
					for (int layerIdx = 0; layerIdx < armormaterial.layers().size(); layerIdx++) {
						ArmorMaterial.Layer armormaterial$layer = armormaterial.layers().get(layerIdx);
						int rgba = extensions.getArmorLayerTintColor(itemstack, entity, armormaterial$layer, layerIdx, fallbackColor);
						if (rgba != 0) {
							ResourceLocation texture = ClientHooks.getArmorTexture(entity, itemstack, armormaterial$layer, inner, slotType);
							this.renderModel(transform, buffer, uv2, model, rgba, texture);
						}
					}
				} catch (NoSuchMethodError e) {
					if(!WARN_NEOFORGE_UNDER_21_1_18) {
						VALLogger.warn("Using neoforge before 21.1.18, disabling per-render-layer armor coloring feature and using legacy method.", e);
						WARN_NEOFORGE_UNDER_21_1_18 = true;
					}
					int i = itemstack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemstack, DyedItemColor.LEATHER_COLOR)) : 0xffffffff;
					for (ArmorMaterial.Layer layer : armormaterial.layers()) {
						int rgba = layer.dyeable() ? i : 0xffffffff;
						this.renderModel(transform, buffer, uv2, model, rgba, layer.texture(inner));
					}
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

	private void renderModel(PoseStack transform, MultiBufferSource multiBufferSource, int uv2, Model model, int rgba, ResourceLocation armorResource) {
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(armorResource));
		model.renderToBuffer(transform, vertexconsumer, uv2, OverlayTexture.NO_OVERLAY, rgba);
	}
	
	private void renderTrim(Holder<ArmorMaterial> armorMaterial, PoseStack transform, MultiBufferSource multiBufferSource, int uv2, ArmorTrim trim, Model model, boolean inner) {
		TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(inner ? trim.innerTexture(armorMaterial) : trim.outerTexture(armorMaterial));
		VertexConsumer vertexconsumer = textureatlassprite.wrap(multiBufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
		model.renderToBuffer(transform, vertexconsumer, uv2, OverlayTexture.NO_OVERLAY, 0xffffffff);
	}
	
	private void renderGlint(PoseStack transform, MultiBufferSource multiBufferSource, int uv2, Model model) {
		model.renderToBuffer(transform, multiBufferSource.getBuffer(RenderType.armorEntityGlint()), uv2, OverlayTexture.NO_OVERLAY, 0xffffffff);
	}

	private A getArmorModel(EquipmentSlot slotType) {
		return this.usesInnerModel(slotType) ? this.innerModel : this.outerModel;
	}

	private boolean usesInnerModel(EquipmentSlot slotType) {
		return slotType == EquipmentSlot.LEGS;
	}
}
