package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
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
@Mod(modid = Const.MODID, useMetadata = true)
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
		network.registerMessage(MessageToggle.class, MessageToggle.class, packetID++, Side.SERVER);
		//TODO: message is never used - it tells client when/where to spawn particles
		network.registerMessage(MessageParticle.class, MessageParticle.class, packetID++, Side.CLIENT);

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
	}
	
	
	/*
	 ideas/plans/bugfixes
	 
	 fix some particles trying to render only server side and failing
	 
	 why is mouse invisible?? -> add gui background
	 
	     TORCH: good speed for projectiles
	-> still drops torches
	
	
	STAIRS: stay up or stay down, they dont change bbetween
	
	
	purple brick_ go through more than one block?
	
	work just in the air
	
	Scaffold X block doesnt work in midair
	
	?? maybe night vision
	
	Regeneration / health boost /absorption
	
	
	they take ? durability in creative mode but shouldnt
	
	potion.frost icon and name missing
	potion.frost missing trail of snow, does not leave behind
	
	make default reparis faster (and only serverside )
	
	wand spell info put also in tooltips
	
	 
	 * */
}
