package com.hexagram2021.villagerarmor.mixin.renderer;

import com.hexagram2021.villagerarmor.client.VALModelLayers;
import com.hexagram2021.villagerarmor.client.VillagerArmorLayer;
import com.hexagram2021.villagerarmor.client.VillagerWingsLayer;
import com.hexagram2021.villagerarmor.client.models.WitchArmorModel;
import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.WitchRenderer;
import net.minecraft.client.renderer.entity.state.WitchRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Witch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(WitchRenderer.class)
public class WitchRendererMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addWitchArmorLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
		WitchRenderer current = ((WitchRenderer)(Object)this);
		current.addLayer(new VillagerArmorLayer<>(
				(RenderLayerParent)current,
				new WitchArmorModel<>(context.bakeLayer(VALModelLayers.WITCH_INNER_ARMOR)),
				new WitchArmorModel<>(context.bakeLayer(VALModelLayers.WITCH_OUTER_ARMOR)),
				context.getEquipmentRenderer()
		));
		current.addLayer(new VillagerWingsLayer<>((RenderLayerParent)current, context.getModelSet(), context.getEquipmentRenderer()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/Witch;Lnet/minecraft/client/renderer/entity/state/WitchRenderState;F)V", at = @At(value = "TAIL"))
	public void extractHumanoidRenderState(Witch entity, WitchRenderState state, float partialTick, CallbackInfo ci) {
		IHumanoidRenderState humanoidRenderState = (IHumanoidRenderState)state;
		humanoidRenderState.val$setChestItem(entity.getItemBySlot(EquipmentSlot.CHEST).copy());
		humanoidRenderState.val$setLegItem(entity.getItemBySlot(EquipmentSlot.LEGS).copy());
		humanoidRenderState.val$setFeetItem(entity.getItemBySlot(EquipmentSlot.FEET).copy());
		humanoidRenderState.val$setIsCrouching(entity.isCrouching());
		humanoidRenderState.val$setElytraRotX(entity.elytraAnimationState.getRotX(partialTick));
		humanoidRenderState.val$setElytraRotY(entity.elytraAnimationState.getRotY(partialTick));
		humanoidRenderState.val$setElytraRotZ(entity.elytraAnimationState.getRotZ(partialTick));
	}
}
