package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.item.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry{

	public static ArrayList<Item> items = new ArrayList<Item>();
	private static Map<String,Boolean> configToggle = new HashMap<String,Boolean>();
	public static Item emerald_helmet;
	public static Item emerald_chestplate;
	public static Item emerald_leggings;
	public static Item emerald_boots;
	public static ItemCyclicWand cyclic_wand;
	public static ItemChestSack chest_sack;
	public static ItemChestSackEmpty chest_sack_empty;
	public static ToolMaterial MATERIAL_EMERALD;
	public static ArmorMaterial ARMOR_MATERIAL_EMERALD;
	public static ItemEnderBook itemEnderBook;
	public static ItemHorseFood emeraldCarrot;
	public static ItemHorseFood lapisCarrot;
	public static ItemHorseFood diamondCarrot;
	public static ItemHorseFood horse_upgrade_jump;
	public static ItemHorseFood horse_upgrade_speed;
	public static ItemProjectile ender_water;
	public static ItemProjectile ender_snow;
	public static ItemProjectile ender_harvest;
	public static ItemProjectile ender_lightning;
	public static ItemProjectile ender_torch;
	public static ItemProjectile ender_wool;
	public static ItemProjectile ender_fishing;
	public static ItemProjectile ender_dungeon;
	public static ItemProjectile ender_blaze;
	public static ItemProjectile ender_bed;
	public static ItemProjectile ender_tnt_1;// creeper
	public static ItemProjectile ender_tnt_2;// chcr
	public static ItemProjectile ender_tnt_4;// tnt
	public static ItemProjectile ender_tnt_6;// ender crystal


	public static void registerItem(Item item, String name){
		registerItem(item,name,false);//default is not hidden
	}
	public static void registerItem(Item item, String name, boolean isHidden){

		item.setUnlocalizedName(name);
		
		
		
		//GameRegistry.registerItem(item, name);

		GameRegistry.register(item, new ResourceLocation(Const.MODID,name));
		
		
		if(isHidden == false){
			item.setCreativeTab(tab);
		}
		items.add(item);
	}

	public final static Item REPAIR_EMERALD = Items.emerald;

	public final static CreativeTabs tab = new CreativeTabs("tab_"+Const.MODID) {

		@Override
		public Item getTabIconItem(){

			return ItemRegistry.chest_sack;
		}
	};
	// only because theyre private, with no getters
	private static final int diamondDurability = 33;
	private static final int[] diamondreductionAmounts = new int[] { 3, 6, 8, 3 };

	private static void registerMaterials(){

		ARMOR_MATERIAL_EMERALD = EnumHelper.addArmorMaterial("emerald", Const.MODID + ":emerald", diamondDurability, diamondreductionAmounts, ArmorMaterial.DIAMOND.getEnchantability(), ArmorMaterial.DIAMOND.getSoundEvent());

		MATERIAL_EMERALD = ToolMaterial.DIAMOND;
		// TODO: addToolMat causes a bug/crash, not sure if forge will fix.
		
		 // EnumHelper.addToolMaterial("emerald", 3, harvestLevel 3 same as diamond 1600,3.5F,  5+25 );
		
	}

	private static void registerRecipes(){

		ItemHorseFood.addRecipes();//TODO:
		for(Item item : items){
			if(item instanceof IHasRecipe){
				((IHasRecipe) item).addRecipe();
			}
		}
	}

	public static void register(){

		registerMaterials();
		//TODO: figure out how to config
		cyclic_wand = new ItemCyclicWand();
		registerItem(cyclic_wand, "cyclic_wand");

		//TODO: retexture and/or decide if we are even keeping this
		Item multitool = new ItemMultiTool();
		registerItem(multitool, "multitool");
		
		if(configToggle.get(ItemEnderPearlReuse.name)){
			
			ItemEnderPearlReuse ender_pearl_reuse = new ItemEnderPearlReuse();
			registerItem(ender_pearl_reuse, ItemEnderPearlReuse.name);
			
		}


		if(configToggle.get(ItemPaperCarbon.name)){

			Item carbon_paper = new ItemPaperCarbon();
			registerItem(carbon_paper, ItemPaperCarbon.name);
		}

		if(configToggle.get(ItemChestSack.name)){

			chest_sack = new ItemChestSack();
			registerItem(chest_sack, ItemChestSack.name ,true);//true for ishidden

			chest_sack_empty = new ItemChestSackEmpty();
			registerItem(chest_sack_empty, ItemChestSackEmpty.name);
		}

		// thanks for help:
		// http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/

		if(configToggle.get("emerald_gear")){
			
			ItemSword emerald_sword = new ItemEmeraldSword();
			registerItem(emerald_sword, ItemEmeraldSword.name);
	
			ItemEmeraldPickaxe emerald_pickaxe = new ItemEmeraldPickaxe();
			registerItem(emerald_pickaxe, ItemEmeraldPickaxe.name);
	
			ItemEmeraldAxe emerald_axe = new ItemEmeraldAxe();
			registerItem(emerald_axe, ItemEmeraldAxe.name);
	
			ItemSpade emerald_spade = new ItemEmeraldSpade();
			registerItem(emerald_spade, ItemEmeraldSpade.name);
	
			ItemHoe emerald_hoe = new ItemEmeraldHoe();
			registerItem(emerald_hoe, ItemEmeraldHoe.name);
	
			//..yeah.. kind of breaks the pattern. i could make one class for each i guess.
			emerald_helmet = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
			registerItem(emerald_helmet, "emerald_helmet");
	
			emerald_chestplate = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
			registerItem(emerald_chestplate, "emerald_chestplate");
	
			emerald_leggings = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
			registerItem(emerald_leggings, "emerald_leggings");
	
			emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
			registerItem(emerald_boots, "emerald_boots");

		}
		
		

		itemEnderBook = new ItemEnderBook();

		String name = "book_ender";
		registerItem(itemEnderBook,name);
		//itemEnderBook.setUnlocalizedName(name);//.setTextureName(ModEnderBook.TEXTURE_LOCATION + name);
		//GameRegistry.registerItem(itemEnderBook,name);
		
		
		
		
		

		emeraldCarrot = new ItemHorseFood();
		ItemRegistry.registerItem(emeraldCarrot, "horse_upgrade_type");

		lapisCarrot = new ItemHorseFood();
		ItemRegistry.registerItem(lapisCarrot, "horse_upgrade_variant");

		diamondCarrot = new ItemHorseFood();
		ItemRegistry.registerItem(diamondCarrot, "horse_upgrade_health");

		horse_upgrade_speed = new ItemHorseFood();
		ItemRegistry.registerItem(horse_upgrade_speed, "horse_upgrade_speed");

		horse_upgrade_jump = new ItemHorseFood();
		ItemRegistry.registerItem(horse_upgrade_jump, "horse_upgrade_jump");

		
		
		

		ender_tnt_1 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_1, "ender_tnt_1");
		ender_tnt_2 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_2, "ender_tnt_2");
		ender_tnt_4 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_4, "ender_tnt_4");
		ender_tnt_6 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_6, "ender_tnt_6");

		if(tnt_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_1, tnt_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.tnt), new ItemStack(Items.clay_ball));

			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_2, 1), new ItemStack(ender_tnt_1), new ItemStack(Items.gunpowder));

			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4, 1), new ItemStack(ender_tnt_2), new ItemStack(Items.gunpowder));

			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6, 1), new ItemStack(ender_tnt_4), new ItemStack(Items.gunpowder));

		}

		ender_blaze = new ItemProjectile();
		ItemRegistry.registerItem(ender_blaze, "ender_blaze");

		if(blaze_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_blaze, blaze_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.blaze_powder), new ItemStack(Items.flint));
		}

		ender_dungeon = new ItemProjectile();
		ItemRegistry.registerItem(ender_dungeon, "ender_dungeon");

		if(dungeon_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_dungeon, dungeon_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.mossy_cobblestone), new ItemStack(Items.nether_wart));// Blocks.iron_bars
		}

		ender_bed = new ItemProjectile();
		ItemRegistry.registerItem(ender_bed, "ender_bed");

		if(bed_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_bed, bed_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.redstone), new ItemStack(Items.bed));
		}

		ender_fishing = new ItemProjectile();
		ItemRegistry.registerItem(ender_fishing, "ender_fishing");

		if(fishing_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_fishing, fishing_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.gunpowder), new ItemStack(Items.string));
		}

		ender_wool = new ItemProjectile();
		ItemRegistry.registerItem(ender_wool, "ender_wool");

		if(wool_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_wool, wool_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.wool), new ItemStack(Items.shears));
		}
		/*
		 * soulstone = new Item(); ItemRegistry.registerItem(soulstone, "soulstone");
		 * GameRegistry.addShapelessRecipe(new ItemStack(soulstone,2) ,new
		 * ItemStack(Items.ender_pearl) ,new ItemStack(Items.nether_wart) ,new
		 * ItemStack(Items.ghast_tear));
		 */
		ender_torch = new ItemProjectile();
		ItemRegistry.registerItem(ender_torch, "ender_torch");
		if(torch_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_torch, torch_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.stick), new ItemStack(Items.coal));
			GameRegistry.addShapelessRecipe(new ItemStack(ender_torch, torch_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.stick), new ItemStack(Items.coal, 1, 1));// charcoal
		}

		ender_water = new ItemProjectile();
		ItemRegistry.registerItem(ender_water, "ender_water");
		if(water_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_water, water_recipe), new ItemStack(Items.blaze_rod), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.ice));
		}

		ender_snow = new ItemProjectile();
		ItemRegistry.registerItem(ender_snow, "ender_snow");
		if(snow_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_snow, snow_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.ice), new ItemStack(Items.snowball));

		}

		ender_harvest = new ItemProjectile();
		ItemRegistry.registerItem(ender_harvest, "ender_harvest");
		if(harvest_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_harvest, harvest_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.wheat), new ItemStack(Items.wheat_seeds));
		}

		ender_lightning = new ItemProjectile();
		ItemRegistry.registerItem(ender_lightning, "ender_lightning");
		if(lightning_recipe > 0){
			GameRegistry.addShapelessRecipe(new ItemStack(ender_lightning, lightning_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.quartz), new ItemStack(Items.ghast_tear));
		}
		
		registerRecipes();
	}
	
	
	
	
	

	public static boolean doesPauseGame;
	public static boolean craftNetherStar;
	public static boolean showCoordTooltips;
	public static int maximumSaved;
	public static int btnsPerColumn;
	public static int expCostPerTeleport;
	public static String category_public;

	public static void syncConfig(Configuration config){

		String category = Const.MODCONF+"items";
		
		config.setCategoryComment(category, "Items added to the game");
		
		Property prop = config.get(category,ItemEnderPearlReuse.name, true,"Reuseable ender pearl");
		prop.setRequiresMcRestart(true);
		configToggle.put(ItemEnderPearlReuse.name, prop.getBoolean());
		
		prop = config.get(category,ItemPaperCarbon.name, true,"Special paper to copy signs and note block data");
		prop.setRequiresMcRestart(true);
		configToggle.put(ItemPaperCarbon.name, prop.getBoolean());
		
		prop = config.get(category,ItemChestSack.name, true,"A bag that transports chests along with its contents");
		prop.setRequiresMcRestart(true);
		configToggle.put(ItemChestSack.name, prop.getBoolean());
		
		prop = config.get(category,"emerald_gear", true,"Full set of emerald gear with similar properties as diamond");
		prop.setRequiresMcRestart(true);
		configToggle.put("emerald_gear", prop.getBoolean());
		
		
		

		category = Const.MODCONF+"items.EnderBook";
		doesPauseGame = config.getBoolean("pause_game_sp", category, false, "The Ender Book GUI will pause the game (single player)");
		
		craftNetherStar = config.getBoolean("needs_nether_star", category, true, "The Ender Book requires a nether star to craft.  REQUIRES RESTART.");
		
		showCoordTooltips = config.getBoolean("show_coordinates_tooltip", category, true, "Waypoint buttons will show the exact coordinates in a hover tooltip.");

		maximumSaved = config.getInt("max_saved", category, 16, 1, 999, "How many waypoints the book can store.");
		
		btnsPerColumn = config.getInt("show_per_column", category, 8, 1, 50, "Number of waypoints per column.  Change this if they are going off the screen for your chosen GUI Scale.");
		
		expCostPerTeleport = config.getInt("exp_per_teleport", category, 10, 0, 9999, "How many experience points are drained from the player on each teleport.  Set to zero for free teleports to your waypoints.");
		
		
		
		

		category = Const.MODCONF+"items.HorseFood";

		ItemHorseFood.HEARTS_MAX = config.getInt("hearts_max", category, 20, 1, 100, "Maximum number of upgraded hearts");
		ItemHorseFood.JUMP_MAX = config.getInt("jump_max", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
		ItemHorseFood.SPEED_MAX = config.getInt("speed_max", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");

		projectileConfig(config);
		
		
		if(config.hasChanged()){config.save();}
	}
	
	public static int fishing_recipe;
	public static int wool_recipe;
	public static int torch_recipe;
	public static int lightning_recipe;
	public static int snow_recipe;
	public static int water_recipe;
	public static int harvest_recipe;
	public static int bed_recipe;
	public static int dungeon_recipe;
	public static int tnt_recipe;
	public static int blaze_recipe;

	private static void projectileConfig(Configuration config){

		//config.load();
		//config.addCustomCategoryComment(MODID, "For each item, you can decide how many the recipe produces.  Set to zero to disable the crafting recipe.");

		String MODID = Const.MODID;
		torch_recipe = config.getInt("torch.crafted", MODID, 6, 0, 64, "");
		lightning_recipe = config.getInt("lightning.crafted", MODID, 1, 0, 64, "");
		snow_recipe = config.getInt("snow.crafted", MODID, 4, 0, 64, "");
		water_recipe = config.getInt("water.crafted", MODID, 4, 0, 64, "");
		harvest_recipe = config.getInt("harvest.crafted", MODID, 4, 0, 64, "");
		wool_recipe = config.getInt("wool.crafted", MODID, 32, 0, 64, "");
		fishing_recipe = config.getInt("fishing.recipe", MODID, 10, 0, 64, "");
		bed_recipe = config.getInt("bed.recipe", MODID, 4, 0, 64, "");
		dungeon_recipe = config.getInt("dungeon.recipe", MODID, 4, 0, 64, "");
		tnt_recipe = config.getInt("tnt.recipe", MODID, 6, 0, 64, "");
		blaze_recipe = config.getInt("blaze.recipe", MODID, 3, 0, 64, "");
		

		ItemProjectile.DUNGEONRADIUS = config.getInt("dungeon.radius", MODID, 64, 8, 128, "Search distance");

		EntityShearingBolt.doesKnockback = config.getBoolean("wool.does_knockback", MODID, true, "Does appear to damage sheep on contact");
		EntityShearingBolt.doesShearChild = config.getBoolean("wool.does_child", MODID, true, "Does shear child sheep as well.");

		EntityBlazeBolt.fireSeconds = config.getInt("blaze.fire_seconds", MODID, 3, 0, 64, "Seconds of fire to put on entity when hit");
		EntityBlazeBolt.damageEntityOnHit = config.getBoolean("blaze.does_knockback", MODID, true, "Does it damage entity or not on hit (0 damage to blaze, 1 to others)");
		EntitySnowballBolt.damageEntityOnHit = config.getBoolean("snow.does_knockback", MODID, true, "Does it damage entity or not on hit (1 damage to blaze, 0 to others)");
		EntityTorchBolt.damageEntityOnHit = config.getBoolean("torch.does_knockback", MODID, true, "Does it damage entity or not on hit (0 dmg like a snowball)");

		EntityHarvestBolt.range_main = config.getInt("harvest.range_main", MODID, 6, 1, 32, "Horizontal range on level of hit to harvest");
		EntityHarvestBolt.range_offset = config.getInt("harvest.range_offset", MODID, 4, 1, 32, "Horizontal range on further heights to harvest");
		EntityHarvestBolt.doesHarvestStem = config.getBoolean("harvest.does_harvest_stem", MODID, false, "Does it harvest stems (pumkin/melon)");
		EntityHarvestBolt.doesHarvestSapling = config.getBoolean("harvest.does_harvest_sapling", MODID, false, "Does it harvest sapling");
		EntityHarvestBolt.doesHarvestTallgrass = config.getBoolean("harvest.does_harvest_tallgrass", MODID, false, "Does it harvest tallgrass/doubleplants");
		EntityHarvestBolt.doesHarvestMushroom = config.getBoolean("harvest.does_harvest_mushroom", MODID, true, "Does it harvest mushrooms");
		EntityHarvestBolt.doesMelonBlocks = config.getBoolean("harvest.does_harvest_melonblock", MODID, true, "Does it harvest pumpkin block");
		EntityHarvestBolt.doesPumpkinBlocks = config.getBoolean("harvest.does_harvest_pumpkinblock", MODID, true, "Does it harvest melon block");

	
	}
}
