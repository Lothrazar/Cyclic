package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.config.ModConfig;
import com.lothrazar.cyclicmagic.net.*;
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
import net.minecraftforge.fml.relauncher.Side;

/**
 * Split out into a standalone mod from my old abandoned one
 * https://github.com/PrinceOfAmber/SamsPowerups
 * 
 * @author Sam Bassett (Lothrazar)
 */
@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, 
updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json",
guiFactory = "com.lothrazar." + Const.MODID + ".config.IngameConfigHandler"
)
public class ModMain {

	@Instance(value = Const.MODID)
	public static ModMain instance;
	@SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
	public static CommonProxy proxy;
	public static Logger logger;
	public static ModConfig cfg;
	public static SimpleNetworkWrapper network;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		cfg = new ModConfig(new Configuration(event.getSuggestedConfigurationFile()));

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);

		int packetID = 11;
		network.registerMessage(MessageKeyCast.class, MessageKeyCast.class, packetID++, Side.SERVER);
		network.registerMessage(MessageKeyLeft.class, MessageKeyLeft.class, packetID++, Side.SERVER);
		network.registerMessage(MessageKeyRight.class, MessageKeyRight.class, packetID++, Side.SERVER);
		network.registerMessage(MessageToggleSpell.class, MessageToggleSpell.class, packetID++, Side.SERVER);
		network.registerMessage(MessageParticle.class, MessageParticle.class, packetID++, Side.CLIENT);
		network.registerMessage(MessageOpenSpellbook.class, MessageOpenSpellbook.class, packetID++, Side.CLIENT);
		network.registerMessage(MessagePlaceBlock.class, MessagePlaceBlock.class, packetID++, Side.SERVER);
		network.registerMessage(MessageToggleBuild.class, MessageToggleBuild.class, packetID++, Side.SERVER);
		

		MinecraftForge.EVENT_BUS.register(new EventRegistry());
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {
		ItemRegistry.register();
		BlockRegistry.register();
		ProjectileRegistry.register();
		PotionRegistry.register();
		SpellRegistry.register();

		proxy.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiRegistry());
	}
}
