package com.lothrazar.cyclicmagic;

import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.BrewingRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.CommandRegistry;
import com.lothrazar.cyclicmagic.registry.DispenserBehaviorRegistry;
import com.lothrazar.cyclicmagic.registry.EventRegistry;
import com.lothrazar.cyclicmagic.registry.FuelRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.MobSpawningRegistry;
import com.lothrazar.cyclicmagic.registry.PacketRegistry;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.registry.ProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeAlterRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeNewRegistry;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.registry.StackSizeRegistry;
import com.lothrazar.cyclicmagic.registry.TileEntityRegistry;
import com.lothrazar.cyclicmagic.registry.VillageTradeRegistry;
import com.lothrazar.cyclicmagic.registry.WorldGenRegistry;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", guiFactory = "com.lothrazar." + Const.MODID + ".gui.IngameConfigFactory")
public class ModMain {

	@Instance(value = Const.MODID)
	public static ModMain instance;
	@SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
	public static CommonProxy proxy;
	public static ModLogger logger;
	private static Configuration config;
	private EventRegistry events;
	public static SimpleNetworkWrapper network;
	public final static CreativeTabs TAB = new CreativeTabs(Const.MODID) {
		@Override
		public Item getTabIconItem() {
			Item tab = ItemRegistry.itemMap.get("apple_diamond");
			if (tab == null) {
				tab = Items.DIAMOND;
			}
			return tab;
		}
	};

	// thank you for the examples forge. player data storage based on API source code example:
	// https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
	@CapabilityInject(IPlayerExtendedProperties.class)
	public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {

		logger = new ModLogger(event.getModLog());
		config = new Configuration(event.getSuggestedConfigurationFile());

		events = new EventRegistry();
		ItemRegistry.construct();// MAYBE it should be a constructed, not static
		BlockRegistry.construct();

		config.load();
		syncConfig();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);

		events.register();

		CapabilityRegistry.register();
		// MinecraftForge.EVENT_BUS.register(new EventPlayerData());

		ReflectionRegistry.register();
		PacketRegistry.register(network);
		VillageTradeRegistry.register();
		SoundRegistry.register();
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {

		PotionRegistry.register();
		ItemRegistry.register();
		BrewingRegistry.register();
		BlockRegistry.register();
		SpellRegistry.register();
		MobSpawningRegistry.register();
		WorldGenRegistry.register();
		FuelRegistry.register();
		StackSizeRegistry.register();
		RecipeAlterRegistry.register();
		RecipeNewRegistry.register();

		proxy.register();

		TileEntityRegistry.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

		ProjectileRegistry.register(event);
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {

		// registers all plantable crops. the plan is to work with non vanilla
		// data
		DispenserBehaviorRegistry.register();
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		CommandRegistry.register(event);
	}

	public static Configuration getConfig() {
		return config;
	}

	public void syncConfig() {
		// hit on startup and on change event from
		// we cant make this a list/loop because the order does matter
		Configuration c = getConfig();
		WorldGenRegistry.syncConfig(c);
		PotionRegistry.syncConfig(c);
		events.syncConfig(c);
		BlockRegistry.syncConfig(c);
		ItemRegistry.syncConfig(c);
		FuelRegistry.syncConfig(c);
		MobSpawningRegistry.syncConfig(c);
		RecipeAlterRegistry.syncConfig(c);
		RecipeNewRegistry.syncConfig(c);
		DispenserBehaviorRegistry.syncConfig(c);
		StackSizeRegistry.syncConfig(c);
		SpellRegistry.syncConfig(c);
		CommandRegistry.syncConfig(c);
		VillageTradeRegistry.syncConfig(c);

		c.save();

	}
	/*
	 * 
	 * TODO LIST
	 * 
	 * REMOVE ? button and textbox from build scepter inventory
	 * 
	 * INVESTIGATE different uses/keybindings/ways of launching. maybe new equipment screen.
	 * 
	 * crafting table hotkeys - numpad?
	 *  
	 * [disabled] building spells: make phantom/ghost/outline/particle blocks
	 *
	 * pets live longer and/or respawn
	 *
	 * ROTATE: STAIRS: allow switch frop top to bottom
	 * 
	 * Trading Tool // gui
	 * 
	 * rebalance emerald armor ?? diamond strength + gold ench?
	 * 
	 * achievemnets give exp 
	 * 
	 */
}
