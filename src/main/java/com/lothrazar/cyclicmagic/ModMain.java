package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.command.*;
import com.lothrazar.cyclicmagic.config.ModConfig;
import com.lothrazar.cyclicmagic.net.MessageKeyCast;
import com.lothrazar.cyclicmagic.net.MessageKeyLeft;
import com.lothrazar.cyclicmagic.net.MessageKeyRight;
import com.lothrazar.cyclicmagic.net.MessageOpenSpellbook;
import com.lothrazar.cyclicmagic.net.MessageParticle;
import com.lothrazar.cyclicmagic.net.MessageRecharge;
import com.lothrazar.cyclicmagic.net.MessageSpellFromServer;
import com.lothrazar.cyclicmagic.net.MessageSpellPull;
import com.lothrazar.cyclicmagic.net.MessageSpellPush;
import com.lothrazar.cyclicmagic.net.MessageSpellReplacer;
import com.lothrazar.cyclicmagic.net.MessageSpellRotate;
import com.lothrazar.cyclicmagic.net.MessageToggleBuild;
import com.lothrazar.cyclicmagic.net.MessageToggleSpell;
import com.lothrazar.cyclicmagic.net.MessageUpgrade;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", guiFactory = "com.lothrazar." + Const.MODID + ".config.IngameConfigHandler")
public class ModMain{

	@Instance(value = Const.MODID)
	public static ModMain instance;
	@SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
	public static CommonProxy proxy;
	public static Logger logger;
	public static Configuration config;
	public static SimpleNetworkWrapper network;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event){

		logger = event.getModLog();

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
		
		
		
		ModConfig.syncConfig();
	 
		registerPackets();
		
		EventRegistry.register();
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event){

		proxy.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiRegistry());
		
		
		ItemRegistry.register();
		BlockRegistry.register();
		SpellRegistry.register();
		PotionRegistry.register();
		RecipeRegistry.register();
		MobSpawningRegistry.register();
		
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandEnderChest("enderchest",true));
		event.registerServerCommand(new CommandGetHome("gethome",true));
		event.registerServerCommand(new CommandHearts("sethearts",true));
		event.registerServerCommand(new CommandHome("home",false));
		event.registerServerCommand(new CommandKit("kit",false)); 
		event.registerServerCommand(new CommandPing("ping",false));
		event.registerServerCommand(new CommandPlaceBlocks("place",false));
		event.registerServerCommand(new CommandRecipe("searchrecipe",false));
		event.registerServerCommand(new CommandSearchItem("searchitem",false)); 
		event.registerServerCommand(new CommandSearchSpawner("searchspawner",true)); 
		event.registerServerCommand(new CommandSearchTrades("searchtrade",false)); 
		event.registerServerCommand(new CommandSimpleWaypoints("waypoint",false)); 
		event.registerServerCommand(new CommandTodoList("todo",false));  
		event.registerServerCommand(new CommandUses("searchuses",false));
		event.registerServerCommand(new CommandVillageInfo("villageinfo",false));
		event.registerServerCommand(new CommandWorldHome("worldhome",false)); 
	}
	
	
	private void registerPackets(){

		ModMain.network.registerMessage(MessageKeyCast.class, MessageKeyCast.class, MessageKeyCast.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageKeyLeft.class, MessageKeyLeft.class, MessageKeyLeft.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageKeyRight.class, MessageKeyRight.class, MessageKeyRight.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageToggleSpell.class, MessageToggleSpell.class, MessageKeyRight.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageParticle.class, MessageParticle.class, MessageParticle.ID, Side.CLIENT);
		ModMain.network.registerMessage(MessageOpenSpellbook.class, MessageOpenSpellbook.class, MessageOpenSpellbook.ID, Side.CLIENT);
		ModMain.network.registerMessage(MessageSpellFromServer.class, MessageSpellFromServer.class, MessageSpellFromServer.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageToggleBuild.class, MessageToggleBuild.class, MessageToggleBuild.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellRotate.class, MessageSpellRotate.class, MessageSpellRotate.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellPush.class, MessageSpellPush.class, MessageSpellPush.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellPull.class, MessageSpellPull.class, MessageSpellPull.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellReplacer.class, MessageSpellReplacer.class, MessageSpellReplacer.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageRecharge.class, MessageRecharge.class, MessageRecharge.ID, Side.SERVER);
		ModMain.network.registerMessage(MessageUpgrade.class, MessageUpgrade.class, MessageUpgrade.ID, Side.SERVER);
		
		
		

		//ModMain.network.registerMessage(MessageTogglePassive.class, MessageTogglePassive.class, packetID++, Side.SERVER);
	//	ModMain.network.registerMessage(MessageToggleVariant.class, MessageToggleVariant.class, packetID++, Side.SERVER);
		//ModMain.network.registerMessage(MessageTogglePlace.class, MessageTogglePlace.class, packetID++, Side.SERVER);
		
	}

/*131 (unreleased)

-fix typo in config file
-add max and mins to wand energy in config	
-fix cooldown and mana cost of waypoint spell
-change manabar render apperaance to match quantity

 * --- COST of each spell in config !!! 
 * 
1. text message if we use a build spell but invo is empty
- max and regen in nbt, not config
2. fix phasing spell, or get rid of it!
3. maybe add some levitation thing
4. chest give failure message text (only useable on a container)

regen space in text
set maximum function/button

//TODO: waterwalk/slowfall/frost potions are unused

*/
}
