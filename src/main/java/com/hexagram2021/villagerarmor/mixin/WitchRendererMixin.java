package com.hexagram2021.villagerarmor.mixin;

import com.hexagram2021.villagerarmor.client.VALModelLayers;
import com.hexagram2021.villagerarmor.client.VillagerArmorLayer;
import com.hexagram2021.villagerarmor.client.models.VillagerArmorModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitchRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchRenderer.class)
public class WitchRendererMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addWitchArmorLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
		WitchRenderer current = ((WitchRenderer)(Object)this);
		current.addLayer(new VillagerArmorLayer<>(current,
				new VillagerArmorModel(context.bakeLayer(VALModelLayers.VILLAGER_INNER_ARMOR)),
				new VillagerArmorModel(context.bakeLayer(VALModelLayers.VILLAGER_OUTER_ARMOR))
		));
		current.addLayer(new ElytraLayer<>(current, context.getModelSet()));
	}
}
