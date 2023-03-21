package com.hexagram2021.villagerarmor.mixin;

import com.hexagram2021.villagerarmor.client.HumanoidArmorLayer;
import com.hexagram2021.villagerarmor.client.models.IHumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.resources.IReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerRenderer.class)
public class VillagerRendererMixin {
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void addVillagerArmorLayer(EntityRendererManager rendererManager, IReloadableResourceManager resourceManager, CallbackInfo ci) {
		VillagerRenderer current = ((VillagerRenderer)(Object)this);
		current.addLayer(new HumanoidArmorLayer<>(current, (IHumanoidModel)(new VillagerModel<>(0.5F)), (IHumanoidModel)(new VillagerModel<>(1.0F))));
	}
}
/*
/summon minecraft:villager ~ ~ ~ {ArmorItems: [{id: "iron_boots", Count: 1}, {id: "golden_leggings", Count: 1}, {id: "diamond_chestplate", Count: 1}, {id: "turtle_helmet", Count: 1}]}
 */