package com.hexagram2021.villagerarmor.mixin.renderer;

import com.hexagram2021.villagerarmor.client.VALModelLayers;
import com.hexagram2021.villagerarmor.client.VillagerArmorLayer;
import com.hexagram2021.villagerarmor.client.VillagerWingsLayer;
import com.hexagram2021.villagerarmor.client.models.VillagerArmorModel;
import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(VillagerRenderer.class)
public class VillagerRendererMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addVillagerArmorLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
		VillagerRenderer current = ((VillagerRenderer)(Object)this);
		current.addLayer(new VillagerArmorLayer<>(
				(RenderLayerParent)current,
				new VillagerArmorModel<>(context.bakeLayer(VALModelLayers.VILLAGER_INNER_ARMOR)),
				new VillagerArmorModel<>(context.bakeLayer(VALModelLayers.VILLAGER_OUTER_ARMOR)),
				context.getEquipmentRenderer()
		));
		current.addLayer(new VillagerWingsLayer<>((RenderLayerParent)current, context.getModelSet(), context.getEquipmentRenderer()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/npc/Villager;Lnet/minecraft/client/renderer/entity/state/VillagerRenderState;F)V", at = @At(value = "TAIL"))
	public void extractHumanoidRenderState(Villager entity, VillagerRenderState state, float partialTick, CallbackInfo ci) {
		IHumanoidRenderState humanoidRenderState = (IHumanoidRenderState)state;
		humanoidRenderState.val$setHeadEquipment(entity.getItemBySlot(EquipmentSlot.HEAD).copy());
		humanoidRenderState.val$setChestEquipment(entity.getItemBySlot(EquipmentSlot.CHEST).copy());
		humanoidRenderState.val$setLegEquipment(entity.getItemBySlot(EquipmentSlot.LEGS).copy());
		humanoidRenderState.val$setFeetEquipment(entity.getItemBySlot(EquipmentSlot.FEET).copy());
		humanoidRenderState.val$setIsCrouching(entity.isCrouching());
		humanoidRenderState.val$setElytraRotX(entity.elytraAnimationState.getRotX(partialTick));
		humanoidRenderState.val$setElytraRotY(entity.elytraAnimationState.getRotY(partialTick));
		humanoidRenderState.val$setElytraRotZ(entity.elytraAnimationState.getRotZ(partialTick));
	}
}
/*
/summon minecraft:villager ~ ~ ~ {ArmorItems: [{components: {"minecraft:trim": {material: "minecraft:amethyst", pattern: "minecraft:rib"}}, count: 1, id: "minecraft:iron_boots"}, {id: "golden_leggings", Count: 1, tag: {Trim: {material: "redstone", pattern: "host"}}}, {id: "diamond_chestplate", Count: 1, tag: {Trim: {material: "gold", pattern: "silence"}}}, {id: "turtle_helmet", Count: 1, tag: {Trim: {material: "iron", pattern: "eye"}}}]}
 */