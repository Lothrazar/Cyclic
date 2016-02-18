package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.config.ModConfig;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", guiFactory = "com.lothrazar." + Const.MODID + ".config.IngameConfigHandler")
public class ModMain{

	@Instance(value = Const.MODID)
	public static ModMain instance;
	@SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
	public static CommonProxy proxy;
	public static Logger logger;
	public static ModConfig cfg;
	public static SimpleNetworkWrapper network;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event){

		logger = event.getModLog();

		cfg = new ModConfig(new Configuration(event.getSuggestedConfigurationFile()));

		PacketRegistry.register();
		
		MinecraftForge.EVENT_BUS.register(new EventRegistry());
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event){

		ItemRegistry.register();
		BlockRegistry.register();
		ProjectileRegistry.register();
		PotionRegistry.register();
		SpellRegistry.register();

		proxy.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiRegistry());
	}
}
