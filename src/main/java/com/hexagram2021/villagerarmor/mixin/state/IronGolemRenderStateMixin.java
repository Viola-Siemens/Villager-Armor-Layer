package com.hexagram2021.villagerarmor.mixin.state;

import com.hexagram2021.villagerarmor.client.state.IHumanoidRenderState;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IronGolemRenderState.class)
public class IronGolemRenderStateMixin implements IHumanoidRenderState {
	@Unique
	private ItemStack val$headEquipment = ItemStack.EMPTY;
	@Unique
	private ItemStack val$chestEquipment = ItemStack.EMPTY;
	@Unique
	private ItemStack val$legEquipment = ItemStack.EMPTY;
	@Unique
	private ItemStack val$feetEquipment = ItemStack.EMPTY;
	@Unique
	private boolean val$isCrouching;
	@Unique
	private float val$elytraRotX;
	@Unique
	private float val$elytraRotY;
	@Unique
	private float val$elytraRotZ;

	@Override
	public ItemStack val$headEquipment() {
		return this.val$headEquipment;
	}

	@Override
	public ItemStack val$chestEquipment() {
		return this.val$chestEquipment;
	}

	@Override
	public ItemStack val$legEquipment() {
		return this.val$legEquipment;
	}

	@Override
	public ItemStack val$feetEquipment() {
		return this.val$feetEquipment;
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
	public void val$setHeadEquipment(ItemStack headItem) {
		this.val$headEquipment = headItem;
	}

	@Override
	public void val$setChestEquipment(ItemStack chestItem) {
		this.val$chestEquipment = chestItem;
	}

	@Override
	public void val$setLegEquipment(ItemStack legItem) {
		this.val$legEquipment = legItem;
	}

	@Override
	public void val$setFeetEquipment(ItemStack feetItem) {
		this.val$feetEquipment = feetItem;
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
