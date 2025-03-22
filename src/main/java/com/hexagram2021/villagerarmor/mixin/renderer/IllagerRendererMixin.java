package com.hexagram2021.villagerarmor.mixin.renderer;

import com.hexagram2021.villagerarmor.client.VALModelLayers;
import com.hexagram2021.villagerarmor.client.VillagerArmorLayer;
import com.hexagram2021.villagerarmor.client.VillagerWingsLayer;
import com.hexagram2021.villagerarmor.client.models.IllagerArmorModel;
import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(IllagerRenderer.class)
public class IllagerRendererMixin<T extends AbstractIllager, S extends IllagerRenderState> {
	@SuppressWarnings("unchecked")
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addIllagerArmorLayer(EntityRendererProvider.Context context, IllagerModel<S> model, float shadowRadius, CallbackInfo ci) {
		IllagerRenderer<T, S> current = ((IllagerRenderer<T, S>)(Object)this);
		current.addLayer(new VillagerArmorLayer<>(
				(RenderLayerParent)current,
				new IllagerArmorModel<>(context.bakeLayer(VALModelLayers.ILLAGER_INNER_ARMOR)),
				new IllagerArmorModel<>(context.bakeLayer(VALModelLayers.ILLAGER_OUTER_ARMOR)),
				context.getEquipmentRenderer()
		));
		current.addLayer(new VillagerWingsLayer<>((RenderLayerParent)current, context.getModelSet(), context.getEquipmentRenderer()));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/AbstractIllager;Lnet/minecraft/client/renderer/entity/state/IllagerRenderState;F)V", at = @At(value = "TAIL"))
	public void extractHumanoidRenderState(T entity, S state, float partialTick, CallbackInfo ci) {
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
