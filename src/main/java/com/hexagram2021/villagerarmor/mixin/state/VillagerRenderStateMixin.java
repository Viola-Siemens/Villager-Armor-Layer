package com.hexagram2021.villagerarmor.mixin.state;

import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VillagerRenderState.class)
public class VillagerRenderStateMixin implements IHumanoidRenderState {
	@Unique
	private ItemStack val$chestItem = ItemStack.EMPTY;
	@Unique
	private ItemStack val$legItem = ItemStack.EMPTY;
	@Unique
	private ItemStack val$feetItem = ItemStack.EMPTY;
	@Unique
	private boolean val$isCrouching;
	@Unique
	private float val$elytraRotX;
	@Unique
	private float val$elytraRotY;
	@Unique
	private float val$elytraRotZ;

	@Override
	public ItemStack val$chestItem() {
		return this.val$chestItem;
	}

	@Override
	public ItemStack val$legItem() {
		return this.val$legItem;
	}

	@Override
	public ItemStack val$feetItem() {
		return this.val$feetItem;
	}

	@Override
	public boolean val$isCrouching() {
		return this.val$isCrouching;
	}

	@Override
	public float val$elytraRotX() {
		return this.val$elytraRotX;
	}

	@Override
	public float val$elytraRotY() {
		return this.val$elytraRotY;
	}

	@Override
	public float val$elytraRotZ() {
		return this.val$elytraRotZ;
	}

	@Override
	public void val$setChestItem(ItemStack chestItem) {
		this.val$chestItem = chestItem;
	}

	@Override
	public void val$setLegItem(ItemStack legItem) {
		this.val$legItem = legItem;
	}

	@Override
	public void val$setFeetItem(ItemStack feetItem) {
		this.val$feetItem = feetItem;
	}

	@Override
	public void val$setIsCrouching(boolean isCrouching) {
		this.val$isCrouching = isCrouching;
	}

	@Override
	public void val$setElytraRotX(float elytraRotX) {
		this.val$elytraRotX = elytraRotX;
	}

	@Override
	public void val$setElytraRotY(float elytraRotY) {
		this.val$elytraRotY = elytraRotY;
	}

	@Override
	public void val$setElytraRotZ(float elytraRotZ) {
		this.val$elytraRotZ = elytraRotZ;
	}
}
