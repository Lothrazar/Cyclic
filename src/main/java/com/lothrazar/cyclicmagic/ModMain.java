package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.command.*;
import com.lothrazar.cyclicmagic.net.*;
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
	private static Configuration config;
	public static SimpleNetworkWrapper network;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event){

		logger = event.getModLog();

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
		
		syncConfig();
		
		registerPackets();
		
		EventRegistry.register();
	}

	public static void syncConfig(){

		SpellRegistry.syncConfig(getConfig());
		PotionRegistry.syncConfig(getConfig());
		EventRegistry.syncConfig(getConfig());
		BlockRegistry.syncConfig(getConfig());
		ItemRegistry.syncConfig(getConfig());
		MobSpawningRegistry.syncConfig(getConfig());
		RecipeAlterRegistry.syncConfig(getConfig());
		RecipeNewRegistry.syncConfig(getConfig());
		WorldGenRegistry.syncConfig(getConfig());
		
		config.save();
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event){

		ItemRegistry.register();
		BlockRegistry.register();
		SpellRegistry.register();
		PotionRegistry.register();
		MobSpawningRegistry.register();
		WorldGenRegistry.register();
		
		if(RecipeAlterRegistry.enabled){
			RecipeAlterRegistry.register();
		}
		if(RecipeNewRegistry.enabled){
			RecipeNewRegistry.register();
		}

		proxy.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiRegistry());
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event){
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

		
		//
		network.registerMessage(MessageKeyCast.class, MessageKeyCast.class, MessageKeyCast.ID, Side.SERVER);
		
		//merge into key shift packet?
		network.registerMessage(MessageKeyLeft.class, MessageKeyLeft.class, MessageKeyLeft.ID, Side.SERVER);
		network.registerMessage(MessageKeyRight.class, MessageKeyRight.class, MessageKeyRight.ID, Side.SERVER);
		
		network.registerMessage(MessageToggleSpell.class, MessageToggleSpell.class, MessageToggleSpell.ID, Side.SERVER);
		network.registerMessage(MessageParticle.class, MessageParticle.class, MessageParticle.ID, Side.CLIENT);
		network.registerMessage(MessageOpenSpellbook.class, MessageOpenSpellbook.class, MessageOpenSpellbook.ID, Side.CLIENT);
		network.registerMessage(MessageSpellFromServer.class, MessageSpellFromServer.class, MessageSpellFromServer.ID, Side.SERVER);
		network.registerMessage(MessageToggleBuild.class, MessageToggleBuild.class, MessageToggleBuild.ID, Side.SERVER);
		network.registerMessage(MessageSpellRotate.class, MessageSpellRotate.class, MessageSpellRotate.ID, Side.SERVER);
		network.registerMessage(MessageSpellPush.class, MessageSpellPush.class, MessageSpellPush.ID, Side.SERVER);
		network.registerMessage(MessageSpellPull.class, MessageSpellPull.class, MessageSpellPull.ID, Side.SERVER);
		network.registerMessage(MessageSpellReplacer.class, MessageSpellReplacer.class, MessageSpellReplacer.ID, Side.SERVER);
		network.registerMessage(MessageRecharge.class, MessageRecharge.class, MessageRecharge.ID, Side.SERVER);
		network.registerMessage(MessageUpgrade.class, MessageUpgrade.class, MessageUpgrade.ID, Side.SERVER);
	
    	network.registerMessage(MessageSlotMove.class, MessageSlotMove.class, MessageSlotMove.ID, Side.SERVER);
    	network.registerMessage(MessageBarMove.class, MessageBarMove.class, MessageBarMove.ID, Side.SERVER);
	
	}

	public static Configuration getConfig(){

		return config;
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

//IDEA: nether iron and gold

//IDEA: make boats float
 * https://www.reddit.com/r/minecraftsuggestions/comments/4d4ob1/make_boats_float_again/
 
 https://www.reddit.com/r/minecraftsuggestions/?count=100&after=t3_4ciqqv
 
 https://www.reddit.com/r/minecraftsuggestions/comments/4chlpo/add_a_control_option_for_elytra_automatically/
 
 * 
 // https://www.reddit.com/r/minecraftsuggestions/comments/4cr84c/blocks_of_quartz_can_be_crafted_back_into_4_quartz/
 
 https://www.reddit.com/r/minecraftsuggestions/comments/4d20g5/bring_back_the_notch_apple_crafting_recipe/
 * 
 * 
 //stardew valley style furnace
  * https://www.reddit.com/r/minecraftsuggestions/comments/4d4eqy/leftclick_on_furnaces_holding_an_item_to_be/
 
 
 
 //do we need custom ItemBlocks for these?
		//top logs recipe

		//smoothstone block
		 //mushroomies?
 
 
 idea: make ladders faster
 
 
*/
}
