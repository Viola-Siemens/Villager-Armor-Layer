package com.hexagram2021.villagerarmor.client.state;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IHumanoidRenderState {
	ItemStack val$headEquipment();
	ItemStack val$chestEquipment();
	ItemStack val$legEquipment();
	ItemStack val$feetEquipment();
	boolean val$isCrouching();
	float val$elytraRotX();
	float val$elytraRotY();
	float val$elytraRotZ();

	void val$setHeadEquipment(ItemStack headItem);
	void val$setChestEquipment(ItemStack chestItem);
	void val$setLegEquipment(ItemStack legItem);
	void val$setFeetEquipment(ItemStack feetItem);
	void val$setIsCrouching(boolean isCrouching);
	void val$setElytraRotX(float elytraRotX);
	void val$setElytraRotY(float elytraRotY);
	void val$setElytraRotZ(float elytraRotZ);
}
