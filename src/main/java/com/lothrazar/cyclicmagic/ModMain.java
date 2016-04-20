 package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", guiFactory = "com.lothrazar." + Const.MODID + ".gui.IngameConfigHandler")
public class ModMain {

	@SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
	public static CommonProxy						proxy;
	@Instance(value = Const.MODID)
	public static ModMain								instance;
	public static Logger								logger;
	private static Configuration				config;
	public static SimpleNetworkWrapper	network;
	public final static CreativeTabs		TAB	= new CreativeTabs(Const.MODID) {
		                                        @Override
		                                        public Item getTabIconItem() {
			                                        return ItemRegistry.chest_sack;
		                                        }
	                                        };

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {

		logger = event.getModLog();

		config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		syncConfig();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
		
		EventRegistry.register();

		ReflectionRegistry.register();

		ExtraButtonRegistry.register();

		PacketRegistry.register(network);
		
		VillageTradeRegistry.register();
	}


	@EventHandler
	public void onInit(FMLInitializationEvent event) {

		PotionRegistry.register();
		ItemRegistry.register();
		BlockRegistry.register();
		SpellRegistry.register();
		MobSpawningRegistry.register();
		WorldGenRegistry.register();
		FuelRegistry.register();
		SoundRegistry.register();

		if (StackSizeRegistry.enabled) {
			StackSizeRegistry.register();
		}
		if (RecipeAlterRegistry.enabled) {
			RecipeAlterRegistry.register();
		}
		if (RecipeNewRegistry.enabled) {
			RecipeNewRegistry.register();
		}

		proxy.register();
		proxy.registerEvents();

		TileEntityRegistry.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

		ProjectileRegistry.register(event);
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {

		// registers all plantable crops. the plan is to work with non vanilla data
		// also
		DispenserBehaviorRegistry.register();

	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		CommandRegistry.register(event);
	}

	public static Configuration getConfig() {
		return config;
	}

	public static void syncConfig() {
		// hit on startup and on change event from
		Configuration c = getConfig();
		WorldGenRegistry.syncConfig(c);
		PotionRegistry.syncConfig(c);
		EventRegistry.syncConfig(c);
		BlockRegistry.syncConfig(c);
		ItemRegistry.syncConfig(c);
		FuelRegistry.syncConfig(c);
		MobSpawningRegistry.syncConfig(c);
		RecipeAlterRegistry.syncConfig(c);
		RecipeNewRegistry.syncConfig(c);
		DispenserBehaviorRegistry.syncConfig(c);
		StackSizeRegistry.syncConfig(c);
		SpellRegistry.syncConfig(c);
		ExtraButtonRegistry.syncConfig(c);
		CommandRegistry.syncConfig(c);
		VillageTradeRegistry.syncConfig(c);

		c.save();
	}

	/* TODO LIST
	 * 
	 * reach scepter starts on inventory spell for a second
	 * 
	 * make sure fragile scaffolding can be placed in midair
	 * 
	 * more plants that give monster loot ie pearls/ blaze powder/ gunpowder?
	 * 
	 * a supertorch? gives off bigger light radius?
	 * 
	 * simple redstone additions? timers/ block update detectors?
	 * 
	 * some sort of tool that displays trade of every nearby villager?
	 * 
	 * WAND: turn particle beam on / off
	 * 
	 * reachplace.name = Place Block
	 * 
	 * SPELLS that use numbers, put in their name 'name' +" ["+ number + "]";
	 * 
	 * 
	 * rebalance emerald armor numbers plan.
	 * 
	 *  
	 * -> instead add a transforming tool? changes between
	 * all the types for emerald
	 * 
	 * all block spells use block sound.
	 * 
	 * All spells that do not use wand inventory moved to other
	 * item.:
	 * : push pull rotate 
	 * 
	 * 
	 * ROTATE: STAIRS: allow switch frop top to bottom
	 * 
	 * PLAY BLOCCK SOUND on rotate spell...+others
	 * 
	 * achievemnets give exp
	 * 
	 * more achieves - inspire by consoles and also my own
	 * 
	 * 
	 * 
	 * add potion brewing!! work with the real brew stands to make new custom potions
	 * 
	 * 
	 * 
	 * BUG: enderman drop block: does it make doubel?
	 * 
	 *UNCRAFITNG:  test/fix hopper interaction
	 * 
	 * config to logspam every enabled feature on startup
	 * 
	 * refactor seed logic into util
	 * 
	 * refactor noteblock/sign into util
	 * 
	 * try to auto detect home biomes of saplings for modded compat
	 * 
	 * 
	 * 
	 * SPELL: bring back ghost - let it put you in new location but only if air
	 * blocks
	 *  
	 * 
	 * 1. text message if we use a build spell but invo is empty
	 * - max and regen in nbt, not config
	 * 
	 * 4. chest give failure message text (only useable on a container)
	 * 
	 * 
	 * //IDEA: make boats float
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4d4ob1/
	 * make_boats_float_again/
	 * 
	 * 
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4chlpo/
	 * add_a_control_option_for_elytra_automatically/
	 * 
	 * 
	 * CONCURRENT : 
	 * 
	 * 
	 */
	
	/*There was a critical exception handling a packet on channel cyclicmagic
java.util.ConcurrentModificationException
	at java.util.WeakHashMap$HashIterator.nextEntry(Unknown Source) ~[?:1.8.0_77]
	at java.util.WeakHashMap$KeyIterator.next(Unknown Source) ~[?:1.8.0_77]
	at java.util.AbstractCollection.toArray(Unknown Source) ~[?:1.8.0_77]
	at net.minecraft.pathfinding.PathWorldListener.notifyBlockUpdate(PathWorldListener.java:28) ~[PathWorldListener.class:?]
	at net.minecraft.world.World.notifyBlockUpdate(World.java:465) ~[World.class:?]
	at net.minecraft.world.World.markAndNotifyBlock(World.java:407) ~[World.class:?]
	at net.minecraft.world.World.setBlockState(World.java:393) ~[World.class:?]
	at net.minecraft.world.World.destroyBlock(World.java:449) ~[World.class:?]
	at com.lothrazar.cyclicmagic.util.UtilMoveBlock.moveBlockTo(UtilMoveBlock.java:52) ~[UtilMoveBlock.class:?]
	at com.lothrazar.cyclicmagic.util.UtilMoveBlock.pushBlock(UtilMoveBlock.java:85) ~[UtilMoveBlock.class:?]
	at com.lothrazar.cyclicmagic.spell.SpellRangePush.castFromServer(SpellRangePush.java:40) ~[SpellRangePush.class:?]
	at com.lothrazar.cyclicmagic.net.MessageSpellPush.onMessage(MessageSpellPush.java:67) ~[MessageSpellPush.class:?]
	at com.lothrazar.cyclicmagic.net.MessageSpellPush.onMessage(MessageSpellPush.java:1) ~[MessageSpellPush.class:?]

	 
	*/
}
