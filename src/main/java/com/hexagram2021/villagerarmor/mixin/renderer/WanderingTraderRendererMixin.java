package com.hexagram2021.villagerarmor.mixin.renderer;

import com.hexagram2021.villagerarmor.client.VALModelLayers;
import com.hexagram2021.villagerarmor.client.VillagerArmorLayer;
import com.hexagram2021.villagerarmor.client.VillagerWingsLayer;
import com.hexagram2021.villagerarmor.client.models.VillagerArmorModel;
import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(WanderingTraderRenderer.class)
public class WanderingTraderRendererMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addVillagerArmorLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
		WanderingTraderRenderer current = ((WanderingTraderRenderer)(Object)this);
		current.addLayer(new VillagerArmorLayer<>(
				(RenderLayerParent)current,
				new VillagerArmorModel<>(context.bakeLayer(VALModelLayers.VILLAGER_INNER_ARMOR)),
				new VillagerArmorModel<>(context.bakeLayer(VALModelLayers.VILLAGER_OUTER_ARMOR)),
				context.getEquipmentRenderer()
		));
		current.addLayer(new VillagerWingsLayer<>((RenderLayerParent)current, context.getModelSet(), context.getEquipmentRenderer()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/npc/WanderingTrader;Lnet/minecraft/client/renderer/entity/state/VillagerRenderState;F)V", at = @At(value = "TAIL"))
	public void extractHumanoidRenderState(WanderingTrader entity, VillagerRenderState state, float partialTick, CallbackInfo ci) {
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
