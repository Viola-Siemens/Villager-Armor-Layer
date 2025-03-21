package com.hexagram2021.villagerarmor.client.state;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IHumanoidRenderState {
	ItemStack val$chestItem();
	ItemStack val$legItem();
	ItemStack val$feetItem();
	boolean val$isCrouching();
	float val$elytraRotX();
	float val$elytraRotY();
	float val$elytraRotZ();

	void val$setChestItem(ItemStack chestItem);
	void val$setLegItem(ItemStack legItem);
	void val$setFeetItem(ItemStack feetItem);
	void val$setIsCrouching(boolean isCrouching);
	void val$setElytraRotX(float elytraRotX);
	void val$setElytraRotY(float elytraRotY);
	void val$setElytraRotZ(float elytraRotZ);
}
