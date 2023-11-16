package com.hexagram2021.villagerarmor.mixin;

import com.hexagram2021.villagerarmor.client.VALModelLayers;
import com.hexagram2021.villagerarmor.client.VillagerArmorLayer;
import com.hexagram2021.villagerarmor.client.models.IronGolemArmorModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemRenderer.class)
public class IronGolemRendererMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addIllagerArmorLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
		IronGolemRenderer current = ((IronGolemRenderer)(Object)this);
		current.addLayer(new VillagerArmorLayer<>(
				current,
				new IronGolemArmorModel(context.bakeLayer(VALModelLayers.IRON_GOLEM_INNER_ARMOR)),
				new IronGolemArmorModel(context.bakeLayer(VALModelLayers.IRON_GOLEM_OUTER_ARMOR)),
				context.getModelManager()
		));
		current.addLayer(new ElytraLayer<>(current, context.getModelSet()));
	}
}
