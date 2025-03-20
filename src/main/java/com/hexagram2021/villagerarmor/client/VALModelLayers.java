package com.hexagram2021.villagerarmor.client;

import com.hexagram2021.villagerarmor.client.models.IllagerArmorModel;
import com.hexagram2021.villagerarmor.client.models.IronGolemArmorModel;
import com.hexagram2021.villagerarmor.client.models.VillagerArmorModel;
import com.hexagram2021.villagerarmor.client.models.WitchArmorModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.hexagram2021.villagerarmor.VillagerArmor.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VALModelLayers {
	public static final ModelLayerLocation VILLAGER_INNER_ARMOR = registerInnerArmor("villager");
	public static final ModelLayerLocation VILLAGER_OUTER_ARMOR = registerOuterArmor("villager");
	public static final ModelLayerLocation ILLAGER_INNER_ARMOR = registerInnerArmor("illager");
	public static final ModelLayerLocation ILLAGER_OUTER_ARMOR = registerOuterArmor("illager");
	public static final ModelLayerLocation WITCH_INNER_ARMOR = registerInnerArmor("witch");
	public static final ModelLayerLocation WITCH_OUTER_ARMOR = registerOuterArmor("witch");
	public static final ModelLayerLocation IRON_GOLEM_INNER_ARMOR = registerInnerArmor("iron_golem");
	public static final ModelLayerLocation IRON_GOLEM_OUTER_ARMOR = registerOuterArmor("iron_golem");
	
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
		event.registerLayerDefinition(VILLAGER_INNER_ARMOR, () -> VillagerArmorModel.createBodyLayer(new CubeDeformation(0.0F), 0.0F, 0.25F));
		event.registerLayerDefinition(VILLAGER_OUTER_ARMOR, () -> VillagerArmorModel.createBodyLayer(new CubeDeformation(1.0F), 0.0F, -0.5F));
		event.registerLayerDefinition(ILLAGER_INNER_ARMOR, () -> IllagerArmorModel.createBodyLayer(new CubeDeformation(0.0F), 0.0F, 0.25F));
		event.registerLayerDefinition(ILLAGER_OUTER_ARMOR, () -> IllagerArmorModel.createBodyLayer(new CubeDeformation(1.0F), 0.0F, -0.5F));
		event.registerLayerDefinition(WITCH_INNER_ARMOR, () -> WitchArmorModel.createBodyLayer(new CubeDeformation(0.0F), 0.0F, 0.25F));
		event.registerLayerDefinition(WITCH_OUTER_ARMOR, () -> WitchArmorModel.createBodyLayer(new CubeDeformation(1.0F), 0.0F, -0.5F));
		event.registerLayerDefinition(IRON_GOLEM_INNER_ARMOR, () -> IronGolemArmorModel.createBodyLayer(new CubeDeformation(0.0F)));
		event.registerLayerDefinition(IRON_GOLEM_OUTER_ARMOR, () -> IronGolemArmorModel.createBodyLayer(new CubeDeformation(1.0F)));
	}
}
