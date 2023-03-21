package com.hexagram2021.villagerarmor;

import com.hexagram2021.villagerarmor.utils.VALLogger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod(VillagerArmor.MODID)
public class VillagerArmor {
	public static final String MODID = "villagerarmor";
	public static final String MODNAME = "Villager Armor Layer";
	public static final String VERSION = "${version}";

	public static <T>
	Supplier<T> bootstrapErrorToXCPInDev(Supplier<T> in) {
		if(FMLLoader.isProduction()) {
			return in;
		}
		return () -> {
			try {
				return in.get();
			} catch(BootstrapMethodError e) {
				throw new RuntimeException(e);
			}
		};
	}

	public VillagerArmor() {
		VALLogger.logger = LogManager.getLogger(MODID);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		MinecraftForge.EVENT_BUS.register(this);
	}


}
