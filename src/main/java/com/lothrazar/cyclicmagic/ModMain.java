package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.proxy.MessageKeyCast;
import com.lothrazar.cyclicmagic.proxy.MessageKeyLeft;
import com.lothrazar.cyclicmagic.proxy.MessageKeyRight;
import com.lothrazar.cyclicmagic.proxy.MessageKeyToggle;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
	public static CreativeTabs tabSamsContent = new CreativeTabs("tabScepter") {
		@Override
		public Item getTabIconItem() {
			return Items.stick;// TODO placeholder
		}
	};

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		cfg = new ModConfig(new Configuration(event.getSuggestedConfigurationFile()));

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);

		network.registerMessage(MessageKeyCast.class, MessageKeyCast.class, MessageKeyCast.ID, Side.SERVER);
		network.registerMessage(MessageKeyLeft.class, MessageKeyLeft.class, MessageKeyLeft.ID, Side.SERVER);
		network.registerMessage(MessageKeyRight.class, MessageKeyRight.class, MessageKeyRight.ID, Side.SERVER);
		network.registerMessage(MessageKeyToggle.class, MessageKeyToggle.class, MessageKeyToggle.ID, Side.SERVER);

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
}
