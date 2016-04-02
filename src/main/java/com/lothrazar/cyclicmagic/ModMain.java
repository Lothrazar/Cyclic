package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.command.*;
import com.lothrazar.cyclicmagic.config.ModConfig;
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
		
		EventRegistry.register();
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event){

		ItemRegistry.register();
		BlockRegistry.register();
		SpellRegistry.register();
		PotionRegistry.register();
		RecipeRegistry.register();

		(new MobSpawningRegistry()).register();
		
		proxy.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiRegistry());
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
