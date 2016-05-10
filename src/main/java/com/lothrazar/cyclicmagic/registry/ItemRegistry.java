package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.item.*;
import com.lothrazar.cyclicmagic.spell.BaseSpellRange;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
	public static ArrayList<Item> items					= new ArrayList<Item>();
	private static boolean emeraldGearEnabled ; 
	
	public static class ModItems{
		
		static ItemEnderPearlReuse ender_pearl_reuse;
		static ItemPaperCarbon carbon_paper;
		public static ItemChestSack	chest_sack;
		static ItemEnderBook book_ender;
	}
	
	public ItemRegistry(){

		ModItems.ender_pearl_reuse = new ItemEnderPearlReuse();
		items.add(ModItems.ender_pearl_reuse);
		ModItems.carbon_paper = new ItemPaperCarbon();
		items.add(ModItems.carbon_paper);
		ModItems.book_ender = new ItemEnderBook();
		items.add(ModItems.book_ender);
	}

	public static void syncConfig(Configuration config) {
		Property prop;
		for (Item item : items) {
			if (item instanceof IHasConfig) {
				((IHasConfig) item).syncConfig(config);
			}
		}
		
		ItemCyclicWand.syncConfig(config);

		String category = Const.ConfigCategory.items;
		  
		prop = config.get(category, "emeraldGear", true, "Full set of emerald gear with similar properties as diamond");
		prop.setRequiresMcRestart(true);
		emeraldGearEnabled  =   prop.getBoolean();


		ItemHorseFood.syncConfig(config);
		ItemFoodAppleMagic.syncConfig(config);
		
		
		
		
		category = Const.ConfigCategory.items_projectiles; 
		//TODO: need to set ItemProjectile to have both interfaces
		//each can do "name" + crafted for recipe config
		
		config.addCustomCategoryComment(category, "For each item, you can decide how many the recipe produces. Set to zero to disable the crafting recipe.");
		torch_recipe = config.getInt("torch.crafted", category, 6, 0, 64, "");
		lightning_recipe = config.getInt("lightning.crafted", category, 1, 0, 64, "");
		snow_recipe = config.getInt("snow.crafted", category, 4, 0, 64, "");
		water_recipe = config.getInt("water.crafted", category, 4, 0, 64, "");
		harvest_recipe = config.getInt("harvest.crafted", category, 4, 0, 64, "");
		wool_recipe = config.getInt("wool.crafted", category, 32, 0, 64, "");
		fishing_recipe = config.getInt("fishing.recipe", category, 10, 0, 64, "");
		bed_recipe = config.getInt("bed.recipe", category, 4, 0, 64, "");
		dungeon_recipe = config.getInt("dungeon.recipe", category, 4, 0, 64, "");
		tnt_recipe = config.getInt("tnt.recipe", category, 6, 0, 64, "");
		blaze_recipe = config.getInt("blaze.recipe", category, 3, 0, 64, "");

		ItemProjectile.DUNGEONRADIUS = config.getInt("dungeon.radius", category, 64, 8, 128, "Search distance");

		EntityShearingBolt.doesKnockback = config.getBoolean("wool.does_knockback", category, true, "Does appear to damage sheep on contact");
		EntityShearingBolt.doesShearChild = config.getBoolean("wool.does_child", category, true, "Does shear child sheep as well.");

		EntityBlazeBolt.fireSeconds = config.getInt("blaze.fire_seconds", category, 3, 0, 64, "Seconds of fire to put on entity when hit");
		EntityBlazeBolt.damageEntityOnHit = config.getBoolean("blaze.does_knockback", category, true, "Does it damage entity or not on hit (0 damage to blaze, 1 to others)");
		EntitySnowballBolt.damageEntityOnHit = config.getBoolean("snow.does_knockback", category, true, "Does it damage entity or not on hit (1 damage to blaze, 0 to others)");
		EntityTorchBolt.damageEntityOnHit = config.getBoolean("torch.does_knockback", category, true, "Does it damage entity or not on hit (0 dmg like a snowball)");

		EntityHarvestBolt.range_main = config.getInt("harvest.range_main", category, 6, 1, 32, "Horizontal range on level of hit to harvest");
		EntityHarvestBolt.range_offset = config.getInt("harvest.range_offset", category, 4, 1, 32, "Horizontal range on further heights to harvest");
		//EntityHarvestBolt.doesHarvestStem = config.getBoolean("harvest.does_harvest_stem", category, false, "Does it harvest stems (pumkin/melon)");
		//EntityHarvestBolt.doesHarvestSapling = config.getBoolean("harvest.does_harvest_sapling", category, false, "Does it harvest sapling");
		//EntityHarvestBolt.doesHarvestTallgrass = config.getBoolean("harvest.does_harvest_tallgrass", category, false, "Does it harvest tallgrass/doubleplants");
		//EntityHarvestBolt.doesHarvestMushroom = config.getBoolean("harvest.does_harvest_mushroom", category, true, "Does it harvest mushrooms");
		//EntityHarvestBolt.doesMelonBlocks = config.getBoolean("harvest.does_harvest_melonblock", category, true, "Does it harvest pumpkin block");
		//EntityHarvestBolt.doesPumpkinBlocks = config.getBoolean("harvest.does_harvest_pumpkinblock", category, true, "Does it harvest melon block");

	}


	public static int									fishing_recipe;
	public static int									wool_recipe;
	public static int									torch_recipe;
	public static int									lightning_recipe;
	public static int									snow_recipe;
	public static int									water_recipe;
	public static int									harvest_recipe;
	public static int									bed_recipe;
	public static int									dungeon_recipe;
	public static int									tnt_recipe;
	public static int									blaze_recipe;
	 
	public final static int						I					= 0;
	public final static int						II				= 1;
	public final static int						III				= 2;
	public final static int						IV				= 3;
	public final static int						V					= 4;

	public static int									hunger		= 4;
	public static int									time			= 8 * 60;					// 8:00

	public static boolean							apple_emerald_enabled;
	public static boolean							apple_diamond_enabled;
	public static boolean							apple_ender_enabled;
	public static boolean							apple_bone_enabled;
	public static boolean							apple_lapis_enabled;
	public static boolean							apple_chocolate_enabled;
	public static boolean							apple_netherwart_enabled;
	public static boolean							apple_prismarine_enabled;
	public static boolean							apple_clownfish_enabled;

	public static boolean							apple_emerald_expensive;
	public static boolean							apple_diamond_expensive;
	public static boolean							apple_ender_expensive;
	public static boolean							apple_bone_expensive;
	public static boolean							apple_lapis_expensive;
	public static boolean							apple_chocolate_expensive;
	public static boolean							apple_netherwart_expensive;
	public static boolean							apple_prismarine_expensive;
	public static boolean							apple_clownfish_expensive;

	public static boolean							apple_chorus_expensive;
	public static boolean							apple_chorus_enabled;

	public static Item								emerald_helmet;
	public static Item								emerald_chestplate;
	public static Item								emerald_leggings;
	public static Item								emerald_boots;
	public static ToolMaterial				MATERIAL_EMERALD;
	public static ArmorMaterial				ARMOR_MATERIAL_EMERALD;

	public static ItemHorseFood				emeraldCarrot;
	public static ItemHorseFood				lapisCarrot;
	public static ItemHorseFood				diamondCarrot;
	public static ItemHorseFood				horse_upgrade_jump;
	public static ItemHorseFood				horse_upgrade_speed;
	public static ItemProjectile			ender_water;
	public static ItemProjectile			ender_snow;
	public static ItemProjectile			ender_harvest;
	public static ItemProjectile			ender_lightning;
	public static ItemProjectile			ender_torch;
	public static ItemProjectile			ender_wool;
	public static ItemProjectile			ender_fishing;
	public static ItemProjectile			ender_dungeon;
	public static ItemProjectile			ender_blaze;
	public static ItemProjectile			ender_bed;
	public static ItemProjectile			ender_tnt_1;																										// creeper
	public static ItemProjectile			ender_tnt_2;																										// chcr
	public static ItemProjectile			ender_tnt_4;																										// tnt
	public static ItemProjectile			ender_tnt_6;																										// ender

	public static ItemCyclicWand			cyclic_wand_build;       																		// crystal
	public static ItemCyclicWand			cyclic_wand_range;
	public static ItemCyclicWand			cyclic_wand_fly;
	public static ItemFoodAppleMagic apple_diamond;
	public static ItemEmeraldPickaxe emerald_pickaxe;
	public static ItemEmeraldAxe emerald_axe;
	public static ItemEmeraldSpade emerald_spade;
	public static ItemEmeraldHoe emerald_hoe;
	public static ItemEmeraldSword emerald_sword;

	public static void registerItem(Item item, String name) {
		registerItem(item, name, false);// default is not hidden
	}

	public static void registerItem(Item item, String name, boolean isHidden) {

		item.setUnlocalizedName(name);

		// GameRegistry.registerItem(item, name);

		GameRegistry.register(item, new ResourceLocation(Const.MODID, name));

		if (isHidden == false) {
			item.setCreativeTab(ModMain.TAB);
		}
		items.add(item);
	}

	public final static Item		REPAIR_EMERALD					= Items.emerald;

	// only because theyre private, with no getters
	private static final int		diamondDurability				= 33;
	private static final int[]	diamondreductionAmounts	= new int[] { 3, 6, 8, 3 };

	private static void registerMaterials() {

		ARMOR_MATERIAL_EMERALD = EnumHelper.addArmorMaterial("emerald", Const.MODID + ":emerald", diamondDurability, diamondreductionAmounts, ArmorMaterial.DIAMOND.getEnchantability(), ArmorMaterial.DIAMOND.getSoundEvent());

		MATERIAL_EMERALD = ToolMaterial.DIAMOND;
		// TODO: addToolMat causes a bug/crash, not sure if forge will fix.

		// EnumHelper.addToolMaterial("emerald", 3, harvestLevel 3 same as diamond
		// 1600,3.5F, 5+25 );

	}

	private static void registerRecipes() {

		ItemHorseFood.addRecipes();// TODO:
		
		for (Item item : items) {
			if (item instanceof IHasRecipe) {
				((IHasRecipe) item).addRecipe();
			}
		}
	}

	public static void register() {

		registerMaterials();

		if (ItemCyclicWand.sceptersEnabled) {

			cyclic_wand_build = new ItemCyclicWand();
			registerItem(cyclic_wand_build, "cyclic_wand_build");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_build), "sds", " o ", "gog", 'd', new ItemStack(Blocks.diamond_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

			cyclic_wand_range = new ItemCyclicWand();
			registerItem(cyclic_wand_range, "cyclic_wand_range");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_range), "sds", " o ", "gog", 'd', new ItemStack(Blocks.emerald_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

			cyclic_wand_fly = new ItemCyclicWand();
			registerItem(cyclic_wand_fly, "cyclic_wand_fly");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_fly), "sds", " o ", "gog", 'd', new ItemStack(Blocks.redstone_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);
		}

		// TODO: retexture and/or decide if we are even keeping this
		//if (configToggle.get(ItemEnderPearlReuse.name)) {
		//	Item multitool = new ItemMultiTool();
		//	registerItem(multitool, ItemMultiTool.name);
		//}

		//TODO: CONFIG
		ItemToolHarvest tool_harvest = new ItemToolHarvest();
		registerItem(tool_harvest, "tool_harvest");
		
		ItemToolPull tool_pull = new ItemToolPull();
		registerItem(tool_pull, "tool_pull");

		ItemToolPush tool_push = new ItemToolPush();
		registerItem(tool_push, "tool_push");
		
		ItemToolRotate tool_rotate = new ItemToolRotate();
		registerItem(tool_rotate, "tool_rotate");

		//TODO: CONFIG STORAGE
		ItemInventoryStorage storage_bag = new ItemInventoryStorage();
		registerItem(storage_bag, "storage_bag");
		
		if (ItemEnderPearlReuse.enabled) {

			registerItem(ModItems.ender_pearl_reuse, ItemEnderPearlReuse.name);
		}

		if (ItemPaperCarbon.enabled) {

			registerItem(ModItems.carbon_paper, ItemPaperCarbon.name);
		}

		if (ItemChestSack.enabled) {

			ModItems.chest_sack = new ItemChestSack();
			registerItem(ModItems.chest_sack, ItemChestSack.name, true);// true for ishidden

			ItemChestSackEmpty chest_sack_empty = new ItemChestSackEmpty();
			registerItem(chest_sack_empty, ItemChestSackEmpty.name);
		}
		
		if(ItemEnderBook.enabled){
			registerItem(ModItems.book_ender, ItemEnderBook.name);
			
		}

		// thanks for help:
		// http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/

		if (emeraldGearEnabled) {

			emerald_sword = new ItemEmeraldSword();
			registerItem(emerald_sword, ItemEmeraldSword.name);

			emerald_pickaxe = new ItemEmeraldPickaxe();
			registerItem(emerald_pickaxe, ItemEmeraldPickaxe.name);

			emerald_axe = new ItemEmeraldAxe();
			registerItem(emerald_axe, ItemEmeraldAxe.name);

			emerald_spade = new ItemEmeraldSpade();
			registerItem(emerald_spade, ItemEmeraldSpade.name);

			emerald_hoe = new ItemEmeraldHoe();
			registerItem(emerald_hoe, ItemEmeraldHoe.name);

			// ..yeah.. kind of breaks the pattern. i could make one class for each i
			// guess.
			emerald_helmet = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
			registerItem(emerald_helmet, "emerald_helmet");

			emerald_chestplate = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
			registerItem(emerald_chestplate, "emerald_chestplate");

			emerald_leggings = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
			registerItem(emerald_leggings, "emerald_leggings");

			emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
			registerItem(emerald_boots, "emerald_boots");

		}
		if (ItemHorseFood.horseFoodEnabled) {

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

		}

		ender_tnt_1 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_1, "ender_tnt_1");
		ender_tnt_2 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_2, "ender_tnt_2");
		ender_tnt_4 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_4, "ender_tnt_4");
		ender_tnt_6 = new ItemProjectile();
		ItemRegistry.registerItem(ender_tnt_6, "ender_tnt_6");

		if (tnt_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_1, tnt_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.tnt), new ItemStack(Items.clay_ball));

			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_2, 1), new ItemStack(ender_tnt_1), new ItemStack(Items.gunpowder));

			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4, 1), new ItemStack(ender_tnt_2), new ItemStack(Items.gunpowder));

			GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6, 1), new ItemStack(ender_tnt_4), new ItemStack(Items.gunpowder));

		}

		ender_blaze = new ItemProjectile();
		ItemRegistry.registerItem(ender_blaze, "ender_blaze");

		if (blaze_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_blaze, blaze_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.blaze_powder), new ItemStack(Items.flint));
		}

		ender_dungeon = new ItemProjectile();
		ItemRegistry.registerItem(ender_dungeon, "ender_dungeon");

		if (dungeon_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_dungeon, dungeon_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.mossy_cobblestone), new ItemStack(Items.nether_wart));// Blocks.iron_bars
		}

		ender_bed = new ItemProjectile();
		ItemRegistry.registerItem(ender_bed, "ender_bed");

		if (bed_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_bed, bed_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.redstone), new ItemStack(Items.bed));
		}

		ender_fishing = new ItemProjectile();
		ItemRegistry.registerItem(ender_fishing, "ender_fishing");

		if (fishing_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_fishing, fishing_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.gunpowder), new ItemStack(Items.string));
		}

		ender_wool = new ItemProjectile();
		ItemRegistry.registerItem(ender_wool, "ender_wool");

		if (wool_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_wool, wool_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.wool), new ItemStack(Items.shears));
		}

		ender_torch = new ItemProjectile();
		ItemRegistry.registerItem(ender_torch, "ender_torch");
		if (torch_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_torch, torch_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.stick), new ItemStack(Items.coal));
			GameRegistry.addShapelessRecipe(new ItemStack(ender_torch, torch_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.stick), new ItemStack(Items.coal, 1, 1));// charcoal
		}

		ender_water = new ItemProjectile();
		ItemRegistry.registerItem(ender_water, "ender_water");
		if (water_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_water, water_recipe), new ItemStack(Items.blaze_rod), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.ice));
		}

		ender_snow = new ItemProjectile();
		ItemRegistry.registerItem(ender_snow, "ender_snow");
		if (snow_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_snow, snow_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.ice), new ItemStack(Items.snowball));
		}

		ender_harvest = new ItemProjectile();
		ItemRegistry.registerItem(ender_harvest, "ender_harvest");
		if (harvest_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_harvest, harvest_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.wheat), new ItemStack(Items.wheat_seeds));
		}

		ender_lightning = new ItemProjectile();
		ItemRegistry.registerItem(ender_lightning, "ender_lightning");
		if (lightning_recipe > 0) {
			GameRegistry.addShapelessRecipe(new ItemStack(ender_lightning, lightning_recipe), new ItemStack(Items.ender_pearl), new ItemStack(Items.quartz), new ItemStack(Items.ghast_tear));
		}

		if (apple_chorus_enabled) {
			ItemFoodAppleMagic apple_chorus = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.chorus_fruit), apple_chorus_expensive);

			apple_chorus.addEffect(MobEffects.levitation, 30, I);
			ItemRegistry.registerItem(apple_chorus, "apple_chorus");
 
		}

		if (apple_chorus_enabled) {
			ItemFoodAppleMagic apple_ender = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.ender_pearl), apple_ender_expensive);

			apple_ender.addEffect(PotionRegistry.ender, time, I);
			ItemRegistry.registerItem(apple_ender, "apple_ender");
 
		}

		if (apple_emerald_enabled) {
			ItemFoodAppleMagic apple_emerald = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.emerald), apple_emerald_expensive);
			apple_emerald.addEffect(MobEffects.moveSpeed, time, II);
			apple_emerald.addEffect(MobEffects.absorption, time, I);
			// ItemRegistry.apple_emerald.addEffect(MobEffects.saturation, time, I);
			apple_emerald.addEffect(MobEffects.healthBoost, time, V);
			ItemRegistry.registerItem(apple_emerald, "apple_emerald"); 
		}

		if (apple_chocolate_enabled) {
			ItemFoodAppleMagic apple_chocolate = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.dye, 1, Const.dye_cocoa), apple_chocolate_expensive);
			apple_chocolate.addEffect(MobEffects.saturation, time, I);
			apple_chocolate.addEffect(MobEffects.moveSpeed, time, I);
			ItemRegistry.registerItem(apple_chocolate, "apple_chocolate"); 
		}

		if (apple_lapis_enabled) {
			ItemFoodAppleMagic apple_lapis = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.dye, 1, Const.dye_lapis), apple_lapis_expensive);
			apple_lapis.addEffect(MobEffects.digSpeed, time, II); // Haste
			apple_lapis.addEffect(MobEffects.saturation, time, I);
			ItemRegistry.registerItem(apple_lapis, "apple_lapis"); 
		}
		if (apple_diamond_enabled) {
			apple_diamond = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.diamond), apple_diamond_expensive);
			ItemRegistry.registerItem(apple_diamond, "apple_diamond");
			apple_diamond.addEffect(MobEffects.resistance, time, I);
			apple_diamond.addEffect(MobEffects.saturation, time, I);
 

		}

		if (apple_bone_enabled) {
			ItemFoodAppleMagic apple_bone = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.bone), apple_bone_expensive);
			ItemRegistry.registerItem(apple_bone, "apple_bone");
			apple_bone.addEffect(MobEffects.luck, time, I);
			apple_bone.addEffect(MobEffects.saturation, time, I);
		}

		if (apple_netherwart_enabled) {
			ItemFoodAppleMagic apple_netherwart = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.nether_wart), apple_netherwart_expensive);
			ItemRegistry.registerItem(apple_netherwart, "apple_netherwart");
			apple_netherwart.addEffect(PotionRegistry.magnet, time, I);
			apple_netherwart.addEffect(MobEffects.saturation, time, I);
		}

		if (apple_prismarine_enabled) {
			ItemFoodAppleMagic apple_prismarine = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.prismarine_shard), apple_prismarine_expensive);
			ItemRegistry.registerItem(apple_prismarine, "apple_prismarine");
			apple_prismarine.addEffect(PotionRegistry.waterwalk, time, I);
			apple_prismarine.addEffect(MobEffects.saturation, time, I);
		}

		if (apple_clownfish_enabled) {
			ItemFoodAppleMagic apple_slowfall = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.fish, 1, Const.clownfish), apple_clownfish_expensive);
			ItemRegistry.registerItem(apple_slowfall, "apple_slowfall");
			apple_slowfall.addEffect(PotionRegistry.slowfall, time, I);
			apple_slowfall.addEffect(MobEffects.saturation, time, I);

		}

		registerRecipes();
	}

}
