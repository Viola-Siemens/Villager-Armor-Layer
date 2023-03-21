package com.hexagram2021.villagerarmor;

import com.hexagram2021.villagerarmor.utils.VALLogger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

@SuppressWarnings("unused")
@Mod(VillagerArmor.MODID)
public class VillagerArmor {
	public static final String MODID = "villagerarmor";
	public static final String MODNAME = "Villager Armor Layer";
	public static final String VERSION = "${version}";

	public VillagerArmor() {
		VALLogger.logger = LogManager.getLogger(MODID);

		MinecraftForge.EVENT_BUS.register(this);
	}
}
