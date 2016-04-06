package com.lothrazar.cyclicmagic;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Logger;

import com.lothrazar.cyclicmagic.block.BlockUncrafting;
import com.lothrazar.cyclicmagic.block.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.command.*;
import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHomeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;
import com.lothrazar.cyclicmagic.net.*;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", guiFactory = "com.lothrazar." + Const.MODID + ".config.IngameConfigHandler")
public class ModMain{

	public static final int guiId = 0;
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
		
		initReflection();
	}

	public static IAttribute horseJumpStrength = null;
	private void initReflection() {

		// version 1.1.0
		// new item for speed
		// new item for jump

		// TODO: new config for recipe expense - surround or single
		// new config for speed/jump/health the max value of each.
		/*
		 * __ field_110276_bu __ field_110276_bu [18:28:18] [Client thread/INFO]
		 * [STDOUT/samshorsefood]: [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __
		 * field_110271_bv [18:28:18] [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110270_bw [18:28:18]
		 * [Client thread/INFO] [STDERR/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:80]: Severe error, please report
		 * this to the mod author: [18:28:18] [Client thread/INFO] [STDERR/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:81]: java.lang.ClassCastException:
		 * [Ljava.lang.String; cannot be cast to net.minecraft.entity.ai.attributes.IAttribute
		 * [18:28:18] [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110273_bx [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110272_by [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110268_bz [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110269_bA [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110291_bB [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110292_bC [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110289_bD [18:28:18]
		 * [Client thread/INFO] [STDOUT/samshorsefood]:
		 * [com.lothrazar.samshorsefood.ModHorseFood:onPreInit:67]: __ field_110290_bE
		 */
		for(Field f : EntityHorse.class.getDeclaredFields()){
			try{
				// if(f.get(null) instanceof IAttribute)
				// System.out.println("== "+f.getName()+" ** "+f.getType());

				// interface net.minecraft.entity.ai.attributes.IAttribute
			 
				// field_76425_a
				if(f.getName().equals("horseJumpStrength") || f.getName().equals("field_110270_bw") || "interface net.minecraft.entity.ai.attributes.IAttribute".equals(f.getType() + "")) 
				{
					f.setAccessible(true);
					// save pointer to the obj so we can reference it later
					horseJumpStrength = (IAttribute) f.get(null);
					// System.err.println("FOUND FOUND FOUND");
					break;
				}
			}
			catch (Exception e){
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(e);
			}
		}

		if(horseJumpStrength == null){
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
		}

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
		DispenserBehaviorRegistry.syncConfig(getConfig());
		StackSizeRegistry.syncConfig(getConfig());
		
		
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
		FuelRegistry.register();
		
		if(StackSizeRegistry.enabled){
			StackSizeRegistry.register();
		}
		if(RecipeAlterRegistry.enabled){
			RecipeAlterRegistry.register();
		}
		if(RecipeNewRegistry.enabled){
			RecipeNewRegistry.register();
		}
		
		proxy.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiRegistry());
		
		
		initEntities(event);
		initUncrafting(event);
	}

	public void initUncrafting(FMLInitializationEvent event) {
		BlockUncrafting uncrafting_block = new BlockUncrafting();
		uncrafting_block.setUnlocalizedName("uncrafting_block");
		GameRegistry.registerBlock(uncrafting_block, "uncrafting_block");

		GameRegistry.addRecipe(new ItemStack(uncrafting_block), 
				" r ", 
				"fdf", 
				" o ", 'o', Blocks.obsidian, 'f', Blocks.furnace, 'r', Blocks.dropper, 'd', Blocks.diamond_block);

		GameRegistry.registerTileEntity(TileEntityUncrafting.class, "uncrafting_block_te");
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		proxy.register();
	}
	public void initEntities(FMLInitializationEvent event){

		int entityID = 999;

		EntityRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityHarvestBolt.class, "harvestbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityHomeBolt.class, "bedbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityDynamite.class, "tntbolt", entityID++, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityBlazeBolt.class, "tntbolt", entityID++, instance, 64, 1, true);
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event){
	
		//registers all plantable crops. the plan is to work with non vanilla data also
		DispenserBehaviorRegistry.register();
		
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
	
    	int packetID = 26;
		network.registerMessage(PacketWarpButton.class, PacketWarpButton.class, packetID++, Side.SERVER);
		network.registerMessage(PacketNewButton.class, PacketNewButton.class, packetID++, Side.SERVER);
		network.registerMessage(PacketDeleteButton.class, PacketDeleteButton.class, packetID++, Side.SERVER);
	
	}

	public static Configuration getConfig(){

		return config;
	}

/* 
 * 
 * 
 //BUG: spells get casted even if you have zero mana 
 * 
 * 
 *disable entire wand in config
 *OR
 * --- COST of each spell in config !!! 
 * 
1. text message if we use a build spell but invo is empty
- max and regen in nbt, not config
 
4. chest give failure message text (only useable on a container)

regen space in text
set maximum function/button
  
//IDEA: make boats float
 * https://www.reddit.com/r/minecraftsuggestions/comments/4d4ob1/make_boats_float_again/
 
 
 
	public String getTranslatedName();
 
 https://www.reddit.com/r/minecraftsuggestions/comments/4chlpo/add_a_control_option_for_elytra_automatically/
 

 //do we need custom ItemBlocks for these?
		//top logs recipe

		//smoothstone block
		 //mushroomies?
 
 
 idea: make ladders faster
 
 */
}
