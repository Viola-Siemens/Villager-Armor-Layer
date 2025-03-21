package com.hexagram2021.villagerarmor.mixin.renderer;

import com.hexagram2021.villagerarmor.client.VALModelLayers;
import com.hexagram2021.villagerarmor.client.VillagerArmorLayer;
import com.hexagram2021.villagerarmor.client.VillagerWingsLayer;
import com.hexagram2021.villagerarmor.client.models.IronGolemArmorModel;
import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.IronGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(IronGolemRenderer.class)
public class IronGolemRendererMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addIllagerArmorLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
		IronGolemRenderer current = ((IronGolemRenderer)(Object)this);
		current.addLayer(new VillagerArmorLayer<>(
				(RenderLayerParent)current,
				new IronGolemArmorModel<>(context.bakeLayer(VALModelLayers.IRON_GOLEM_INNER_ARMOR)),
				new IronGolemArmorModel<>(context.bakeLayer(VALModelLayers.IRON_GOLEM_OUTER_ARMOR)),
				context.getEquipmentRenderer()
		));
		current.addLayer(new VillagerWingsLayer<>((RenderLayerParent)current, context.getModelSet(), context.getEquipmentRenderer()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/IronGolem;Lnet/minecraft/client/renderer/entity/state/IronGolemRenderState;F)V", at = @At(value = "TAIL"))
	public void extractHumanoidRenderState(IronGolem entity, IronGolemRenderState state, float partialTick, CallbackInfo ci) {
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
