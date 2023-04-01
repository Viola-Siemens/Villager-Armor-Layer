package com.hexagram2021.villagerarmor.client;

import com.hexagram2021.villagerarmor.client.models.IllagerArmorModel;
import com.hexagram2021.villagerarmor.client.models.VillagerArmorModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.hexagram2021.villagerarmor.VillagerArmor.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VALModelLayers {
	public static final ModelLayerLocation VILLAGER_INNER_ARMOR = registerInnerArmor("villager");
	public static final ModelLayerLocation VILLAGER_OUTER_ARMOR = registerOuterArmor("villager");
	public static final ModelLayerLocation ILLAGER_INNER_ARMOR = registerInnerArmor("illager");
	public static final ModelLayerLocation ILLAGER_OUTER_ARMOR = registerOuterArmor("illager");
	
	private static ModelLayerLocation registerInnerArmor(String name) {
		return register(name, "inner_armor");
	}
	
	private static ModelLayerLocation registerOuterArmor(String name) {
		return register(name, "outer_armor");
	}
	
	private static ModelLayerLocation register(String name, String layer) {
		return new ModelLayerLocation(new ResourceLocation(MODID, name), layer);
	}
	
	@SubscribeEvent
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(VILLAGER_INNER_ARMOR, () -> VillagerArmorModel.createBodyLayer(new CubeDeformation(0.5F), 0.0F));
		event.registerLayerDefinition(VILLAGER_OUTER_ARMOR, () -> VillagerArmorModel.createBodyLayer(new CubeDeformation(1.0F), 0.0F));
		event.registerLayerDefinition(ILLAGER_INNER_ARMOR, () -> IllagerArmorModel.createBodyLayer(new CubeDeformation(0.5F), 0.0F));
		event.registerLayerDefinition(ILLAGER_INNER_ARMOR, () -> IllagerArmorModel.createBodyLayer(new CubeDeformation(1.0F), 0.0F));
	}
}
