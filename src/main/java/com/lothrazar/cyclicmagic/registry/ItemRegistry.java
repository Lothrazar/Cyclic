package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.item.*;
import com.lothrazar.cyclicmagic.item.projectile.*;
import com.lothrazar.cyclicmagic.registry.ItemRegistry.ModItems;
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
	public static ToolMaterial				MATERIAL_EMERALD;
	public static ArmorMaterial				ARMOR_MATERIAL_EMERALD;
	
	public static class ModItems{
		
		static ItemEnderPearlReuse ender_pearl_reuse;
		static ItemPaperCarbon carbon_paper;
		public static ItemChestSack	chest_sack;
		public static ItemEnderBook book_ender;
		public static ItemHorseFood				emeraldCarrot;
		public static ItemHorseFood				lapisCarrot;
		public static ItemHorseFood				diamondCarrot;
		public static ItemHorseFood				horse_upgrade_jump;
		public static ItemHorseFood				horse_upgrade_speed;
		public static Item						emerald_helmet;
		public static Item						emerald_chestplate;
		public static Item						emerald_leggings;
		public static Item						emerald_boots;
		public static BaseItemProjectile			ender_water;
		public static BaseItemProjectile			ender_snow;
		public static BaseItemProjectile			ender_harvest;
		public static BaseItemProjectile			ender_lightning;
		public static BaseItemProjectile			ender_torch;
		public static BaseItemProjectile			ender_wool;
		public static BaseItemProjectile			ender_fishing;
		public static BaseItemProjectile			ender_dungeon;
		public static BaseItemProjectile			ender_blaze;
		//public static BaseItemProjectile			ender_bed;
		public static BaseItemProjectile			ender_tnt_1;																										// creeper
		public static BaseItemProjectile			ender_tnt_2;																										// chcr
		public static BaseItemProjectile			ender_tnt_4;																										// tnt
		public static BaseItemProjectile			ender_tnt_6;																										// ender

		public static ItemCyclicWand			cyclic_wand_build;       																		// crystal
		public static ItemCyclicWand			cyclic_wand_range;
		public static ItemCyclicWand			cyclic_wand_fly;
		public static ItemFoodAppleMagic apple_diamond;
		public static ItemEmeraldPickaxe emerald_pickaxe;
		public static ItemEmeraldAxe emerald_axe;
		public static ItemEmeraldSpade emerald_spade;
		public static ItemEmeraldHoe emerald_hoe;
		public static ItemEmeraldSword emerald_sword;
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
	}



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

			ModItems.cyclic_wand_build = new ItemCyclicWand();
			registerItem(ModItems.cyclic_wand_build, "cyclic_wand_build");
			GameRegistry.addRecipe(new ItemStack(ModItems.cyclic_wand_build), "sds", " o ", "gog", 'd', new ItemStack(Blocks.diamond_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

			ModItems.cyclic_wand_range = new ItemCyclicWand();
			registerItem(ModItems.cyclic_wand_range, "cyclic_wand_range");
			GameRegistry.addRecipe(new ItemStack(ModItems.cyclic_wand_range), "sds", " o ", "gog", 'd', new ItemStack(Blocks.emerald_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

			ModItems.cyclic_wand_fly = new ItemCyclicWand();
			registerItem(ModItems.cyclic_wand_fly, "cyclic_wand_fly");
			GameRegistry.addRecipe(new ItemStack(ModItems.cyclic_wand_fly), "sds", " o ", "gog", 'd', new ItemStack(Blocks.redstone_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);
		}

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

			ModItems.emerald_sword = new ItemEmeraldSword();
			registerItem(ModItems.emerald_sword, ItemEmeraldSword.name);

			ModItems.emerald_pickaxe = new ItemEmeraldPickaxe();
			registerItem(ModItems.emerald_pickaxe, ItemEmeraldPickaxe.name);

			ModItems.emerald_axe = new ItemEmeraldAxe();
			registerItem(ModItems.emerald_axe, ItemEmeraldAxe.name);

			ModItems.emerald_spade = new ItemEmeraldSpade();
			registerItem(ModItems.emerald_spade, ItemEmeraldSpade.name);

			ModItems.emerald_hoe = new ItemEmeraldHoe();
			registerItem(ModItems.emerald_hoe, ItemEmeraldHoe.name);

			// ..yeah.. kind of breaks the pattern. i could make one class for each i
			// guess.
			ModItems.emerald_helmet = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
			registerItem(ModItems.emerald_helmet, "emerald_helmet");

			ModItems.emerald_chestplate = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
			registerItem(ModItems.emerald_chestplate, "emerald_chestplate");

			ModItems.emerald_leggings = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
			registerItem(ModItems.emerald_leggings, "emerald_leggings");

			ModItems.emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
			registerItem(ModItems.emerald_boots, "emerald_boots");

		}
		if (ItemHorseFood.horseFoodEnabled) {

			ModItems.emeraldCarrot = new ItemHorseFood();
			ItemRegistry.registerItem(ModItems.emeraldCarrot, "horse_upgrade_type");

			ModItems.lapisCarrot = new ItemHorseFood();
			ItemRegistry.registerItem(ModItems.lapisCarrot, "horse_upgrade_variant");

			ModItems.diamondCarrot = new ItemHorseFood();
			ItemRegistry.registerItem(ModItems.diamondCarrot, "horse_upgrade_health");

			ModItems.horse_upgrade_speed = new ItemHorseFood();
			ItemRegistry.registerItem(ModItems.horse_upgrade_speed, "horse_upgrade_speed");

			ModItems.horse_upgrade_jump = new ItemHorseFood();
			ItemRegistry.registerItem(ModItems.horse_upgrade_jump, "horse_upgrade_jump");

		}

		ModItems.ender_tnt_1 = new ItemProjectileTNT(1);
		ItemRegistry.registerItem(ModItems.ender_tnt_1, "ender_tnt_1");
		ModItems.ender_tnt_2 = new ItemProjectileTNT(2);
		ItemRegistry.registerItem(ModItems.ender_tnt_2, "ender_tnt_2");
		ModItems.ender_tnt_4 = new ItemProjectileTNT(3);
		ItemRegistry.registerItem(ModItems.ender_tnt_4, "ender_tnt_4");
		ModItems.ender_tnt_6 = new ItemProjectileTNT(4);
		ItemRegistry.registerItem(ModItems.ender_tnt_6, "ender_tnt_6");

		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ender_tnt_1), new ItemStack(Items.ender_pearl), new ItemStack(Blocks.tnt), new ItemStack(Items.clay_ball));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ender_tnt_2), new ItemStack(ModItems.ender_tnt_1), new ItemStack(Items.gunpowder), new ItemStack(Items.gunpowder));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ender_tnt_4), new ItemStack(ModItems.ender_tnt_2), new ItemStack(Items.gunpowder), new ItemStack(Items.gunpowder));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ender_tnt_6), new ItemStack(ModItems.ender_tnt_4), new ItemStack(Items.gunpowder), new ItemStack(Items.gunpowder));

		

		ModItems.ender_blaze = new ItemProjectileBlaze();
		ItemRegistry.registerItem(ModItems.ender_blaze, "ender_blaze");

		ModItems.ender_dungeon = new ItemProjectileDungeon();
		ItemRegistry.registerItem(ModItems.ender_dungeon, "ender_dungeon");

	 
	

		//ModItems.ender_bed = new BaseItemProjectile();
		//ItemRegistry.registerItem(ModItems.ender_bed, "ender_bed");

		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ender_bed, 4), new ItemStack(Items.ender_pearl), new ItemStack(Items.redstone), new ItemStack(Items.bed));
		

		ModItems.ender_fishing = new ItemProjectileFishing();
		ItemRegistry.registerItem(ModItems.ender_fishing, "ender_fishing");


		ModItems.ender_wool = new ItemProjectileWool();
		ItemRegistry.registerItem(ModItems.ender_wool, "ender_wool");

		

		ModItems.ender_torch = new ItemProjectileTorch();
		ItemRegistry.registerItem(ModItems.ender_torch, "ender_torch");
		
	

		ModItems.ender_water = new ItemProjectileWater();
		ItemRegistry.registerItem(ModItems.ender_water, "ender_water");
	
		

		ModItems.ender_snow = new ItemProjectileSnow();
		ItemRegistry.registerItem(ModItems.ender_snow, "ender_snow");
		
		

		ModItems.ender_harvest = new ItemProjectileHarvest();
		ItemRegistry.registerItem(ModItems.ender_harvest, "ender_harvest");
		
	

		ModItems.ender_lightning = new ItemProjectileLightning();
		ItemRegistry.registerItem(ModItems.ender_lightning, "ender_lightning");
		

		int I = PotionRegistry.I;
		int II = PotionRegistry.II;
	////	int III = PotionRegistry.III;
	//	int IV = PotionRegistry.IV;
		int V = PotionRegistry.V;


		 
		final int hunger		= 4;
		final int time			= 8 * 60;					// 8:00
		if (ItemFoodAppleMagic.apple_chorus_enabled) {
			ItemFoodAppleMagic apple_chorus = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.chorus_fruit), ItemFoodAppleMagic.apple_chorus_expensive);

			apple_chorus.addEffect(MobEffects.levitation, 30, I);
			ItemRegistry.registerItem(apple_chorus, "apple_chorus");
 
		}

		if (ItemFoodAppleMagic.apple_chorus_enabled) {
			ItemFoodAppleMagic apple_ender = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.ender_pearl), ItemFoodAppleMagic.apple_ender_expensive);

			apple_ender.addEffect(PotionRegistry.ender, time, I);
			ItemRegistry.registerItem(apple_ender, "apple_ender");
 
		}

		if (ItemFoodAppleMagic.apple_emerald_enabled) {
			ItemFoodAppleMagic apple_emerald = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.emerald), ItemFoodAppleMagic.apple_emerald_expensive);
			apple_emerald.addEffect(MobEffects.moveSpeed, time, II);
			apple_emerald.addEffect(MobEffects.absorption, time, I);
			// ItemRegistry.apple_emerald.addEffect(MobEffects.saturation, time, I);
			apple_emerald.addEffect(MobEffects.healthBoost, time, V);
			ItemRegistry.registerItem(apple_emerald, "apple_emerald"); 
		}

		if (ItemFoodAppleMagic.apple_chocolate_enabled) {
			ItemFoodAppleMagic apple_chocolate = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.dye, 1, Const.dye_cocoa), ItemFoodAppleMagic.apple_chocolate_expensive);
			apple_chocolate.addEffect(MobEffects.saturation, time, I);
			apple_chocolate.addEffect(MobEffects.moveSpeed, time, I);
			ItemRegistry.registerItem(apple_chocolate, "apple_chocolate"); 
		}

		if (ItemFoodAppleMagic.apple_lapis_enabled) {
			ItemFoodAppleMagic apple_lapis = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.dye, 1, Const.dye_lapis), ItemFoodAppleMagic.apple_lapis_expensive);
			apple_lapis.addEffect(MobEffects.digSpeed, time, II); // Haste
			apple_lapis.addEffect(MobEffects.saturation, time, I);
			ItemRegistry.registerItem(apple_lapis, "apple_lapis"); 
		}
		if (ItemFoodAppleMagic.apple_diamond_enabled) {
			ModItems.apple_diamond = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.diamond), ItemFoodAppleMagic.apple_diamond_expensive);
			ItemRegistry.registerItem(ModItems.apple_diamond, "apple_diamond");
			ModItems.apple_diamond.addEffect(MobEffects.resistance, time, I);
			ModItems.apple_diamond.addEffect(MobEffects.saturation, time, I);
 

		}

		if (ItemFoodAppleMagic.apple_bone_enabled) {
			ItemFoodAppleMagic apple_bone = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.bone), ItemFoodAppleMagic.apple_bone_expensive);
			ItemRegistry.registerItem(apple_bone, "apple_bone");
			apple_bone.addEffect(MobEffects.luck, time, I);
			apple_bone.addEffect(MobEffects.saturation, time, I);
		}

		if (ItemFoodAppleMagic.apple_netherwart_enabled) {
			ItemFoodAppleMagic apple_netherwart = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.nether_wart), ItemFoodAppleMagic.apple_netherwart_expensive);
			ItemRegistry.registerItem(apple_netherwart, "apple_netherwart");
			apple_netherwart.addEffect(PotionRegistry.magnet, time, I);
			apple_netherwart.addEffect(MobEffects.saturation, time, I);
		}

		if (ItemFoodAppleMagic.apple_prismarine_enabled) {
			ItemFoodAppleMagic apple_prismarine = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.prismarine_shard), ItemFoodAppleMagic.apple_prismarine_expensive);
			ItemRegistry.registerItem(apple_prismarine, "apple_prismarine");
			apple_prismarine.addEffect(PotionRegistry.waterwalk, time, I);
			apple_prismarine.addEffect(MobEffects.saturation, time, I);
		}

		if (ItemFoodAppleMagic.apple_clownfish_enabled) {
			ItemFoodAppleMagic apple_slowfall = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.fish, 1, Const.clownfish), ItemFoodAppleMagic.apple_clownfish_expensive);
			ItemRegistry.registerItem(apple_slowfall, "apple_slowfall");
			apple_slowfall.addEffect(PotionRegistry.slowfall, time, I);
			apple_slowfall.addEffect(MobEffects.saturation, time, I);

		}

		registerRecipes();
	}

}
