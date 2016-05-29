package com.lothrazar.cyclicmagic.registry;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.BaseItem;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.ItemEmeraldArmor;
import com.lothrazar.cyclicmagic.item.ItemEmeraldAxe;
import com.lothrazar.cyclicmagic.item.ItemEmeraldHoe;
import com.lothrazar.cyclicmagic.item.ItemEmeraldPickaxe;
import com.lothrazar.cyclicmagic.item.ItemEmeraldSpade;
import com.lothrazar.cyclicmagic.item.ItemEmeraldSword;
import com.lothrazar.cyclicmagic.item.ItemEnderBook;
import com.lothrazar.cyclicmagic.item.ItemFlintTool;
import com.lothrazar.cyclicmagic.item.ItemToolPearlReuse;
import com.lothrazar.cyclicmagic.item.ItemFoodAppleMagic;
import com.lothrazar.cyclicmagic.item.ItemFoodCorruptedChorus;
import com.lothrazar.cyclicmagic.item.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.item.ItemFoodHeart;
import com.lothrazar.cyclicmagic.item.ItemFoodHorse;
import com.lothrazar.cyclicmagic.item.ItemFoodInventory;
import com.lothrazar.cyclicmagic.item.ItemInventoryStorage;
import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import com.lothrazar.cyclicmagic.item.ItemPotionCustom;
import com.lothrazar.cyclicmagic.item.ItemSleepingBag;
import com.lothrazar.cyclicmagic.item.ItemToolHarvest;
import com.lothrazar.cyclicmagic.item.ItemToolPull;
import com.lothrazar.cyclicmagic.item.ItemToolPush;
import com.lothrazar.cyclicmagic.item.ItemToolRotate;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileBlaze;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileDungeon;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileFishing;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileHarvest;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileLightning;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileSnow;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTNT;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTorch;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileWater;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileWool;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {

	public static Map<String,Item> itemMap	= new HashMap<String,Item>();

	public static Item corrupted_chorus;

	public static final ItemPotionCustom potion_viscous = new ItemPotionCustom();
	public static final ItemPotionCustom potion_levitation = new ItemPotionCustom(MobEffects.LEVITATION, 30);
	public static final ItemPotionCustom potion_luck = new ItemPotionCustom(MobEffects.LUCK, 60*3);
//	public static final ItemPotionCustom potion_ender = new ItemPotionCustom(false,PotionRegistry.ender, 60*3);
//	public static final ItemPotionCustom magnet_potion = new ItemPotionCustom(false,PotionRegistry.magnet, 60*3);
//	public static final ItemPotionCustom waterwalk_potion = new ItemPotionCustom(false,PotionRegistry.waterwalk, 60*3);
//	public static final ItemPotionCustom slowfall_potion = new ItemPotionCustom(false,PotionRegistry.slowfall, 60*3);
	public static final ItemPotionCustom potion_ender = new ItemPotionCustom();
	public static final ItemPotionCustom potion_magnet = new ItemPotionCustom();
	public static final ItemPotionCustom potion_waterwalk = new ItemPotionCustom();
	public static final ItemPotionCustom potion_slowfall = new ItemPotionCustom();
	public static final ItemPotionCustom potion_glowing = new ItemPotionCustom(MobEffects.GLOWING, 60*3);
	public static final ItemPotionCustom potion_resistance = new ItemPotionCustom(MobEffects.RESISTANCE, 60*3);
	public static final ItemPotionCustom potion_boost = new ItemPotionCustom(MobEffects.HEALTH_BOOST, 60*3, PotionRegistry.V);
	public static final ItemPotionCustom potion_haste = new ItemPotionCustom(MobEffects.HASTE, 60*3);
	
	public static void construct(){
		//TODO: maybe constructor. MUST be done before config

		addItem(new ItemToolPearlReuse(),"ender_pearl_reuse"); 
		addItem(new ItemPaperCarbon(),"carbon_paper"); 
		addItem(new ItemToolHarvest(),"tool_harvest");
		addItem(new ItemToolPull(),"tool_pull");
		addItem(new ItemToolPush(),"tool_push");
		addItem(new ItemToolRotate(),"tool_rotate");
		addItem(new ItemInventoryStorage(),"storage_bag"); 
		addItem(new ItemChestSack().setHidden(),"chest_sack");
		addItem(new ItemChestSackEmpty(),"chest_sack_empty");
		addItem(new ItemProjectileBlaze(),"ender_blaze");
		addItem(new ItemProjectileDungeon(),"ender_dungeon");
		addItem(new ItemProjectileFishing(),"ender_fishing");
		addItem(new ItemProjectileWool(),"ender_wool");
		addItem(new ItemProjectileTorch(),"ender_torch");
		addItem(new ItemProjectileWater(),"ender_water");
		addItem(new ItemProjectileSnow(),"ender_snow");
		addItem(new ItemProjectileHarvest(),"ender_harvest");
		addItem(new ItemProjectileLightning(),"ender_lightning");
		addItem(new ItemProjectileTNT(1),"ender_tnt_1");
		addItem(new ItemProjectileTNT(2),"ender_tnt_2");
		addItem(new ItemProjectileTNT(3),"ender_tnt_4");
		addItem(new ItemProjectileTNT(4),"ender_tnt_6");
		addItem(new ItemFoodHorse(new ItemStack(Items.EMERALD)),"horse_upgrade_type");
		addItem(new ItemFoodHorse(new ItemStack(Items.DYE, 1, Const.dye_lapis)),"horse_upgrade_variant");
		addItem(new ItemFoodHorse(new ItemStack(Items.DIAMOND)),"horse_upgrade_health");
		addItem(new ItemFoodHorse(new ItemStack(Items.REDSTONE)),"horse_upgrade_speed");
		addItem(new ItemFoodHorse(new ItemStack(Items.ENDER_EYE)),"horse_upgrade_jump");
		addItem(new ItemEnderBook(),"book_ender"); 
		corrupted_chorus = addItem(new ItemFoodCorruptedChorus(),"corrupted_chorus");
		addItem(new ItemFoodHeart(),"heart_food"); 
		addItem(new ItemFoodCrafting(),"crafting_food");
		addItem(new ItemFoodInventory(),"inventory_food");
		addItem(new ItemSleepingBag(),"sleeping_mat");
		addItem(potion_haste, "potion_haste");
		addItem(potion_ender, "potion_ender");
		addItem(potion_luck, "potion_luck");	
		addItem(potion_levitation, "potion_levitation");
		addItem(potion_viscous, "potion_viscous");
		addItem(potion_boost, "potion_boost");
		addItem(potion_resistance, "potion_resistance");
		addItem(potion_waterwalk, "potion_waterwalk");
		addItem(potion_slowfall, "potion_slowfall");
		addItem(potion_glowing, "potion_glowing");
		addItem(potion_magnet, "potion_magnet");
	}

	private static Item addItem(Item i, String key){ 
		i.setUnlocalizedName(key);
		itemMap.put(key, i);
		return i;
	}
	
	private static boolean emeraldGearEnabled ; 
	public static ToolMaterial		MATERIAL_EMERALD;
	public static ArmorMaterial		ARMOR_MATERIAL_EMERALD;
	public final static Item		REPAIR_EMERALD = Items.EMERALD;

	// only because theyre private, with no getters
//	private static final int		diamondDurability				= 33;
	//private static final int[]	diamondreductionAmounts	= new int[] { 3, 6, 8, 3 };
 
	public static void syncConfig(Configuration config) {
		Property prop;
		Item item;
		for (String key : itemMap.keySet()) {
			item = itemMap.get(key);
			if (item instanceof IHasConfig) {
				((IHasConfig) item).syncConfig(config);
			}
		}
		
		ItemCyclicWand.syncConfig(config);

		String category = Const.ConfigCategory.items;
		prop = config.get(category, "emeraldGear", true, "Full set of emerald gear with similar properties as diamond");
		prop.setRequiresMcRestart(true);
		emeraldGearEnabled  =   prop.getBoolean();

		ItemFoodHorse.syncConfig(config);
		
		ItemFoodAppleMagic.syncConfig(config);
	}
	
	private static void registerRecipes() {

		Item item;
		for (String key : itemMap.keySet()) {
			item = itemMap.get(key);
			if (item instanceof IHasRecipe) {
				((IHasRecipe) item).addRecipe();
			}
		}
	}

	public static void register() {
		registerMaterials();

		addItem(new ItemFlintTool(),"flint_tool");

		potion_ender.addEffect(PotionRegistry.ender, 60*3,PotionRegistry.I);
		potion_magnet.addEffect(PotionRegistry.magnet, 60*3,PotionRegistry.I);
		potion_waterwalk.addEffect(PotionRegistry.waterwalk, 60*3,PotionRegistry.I);
		potion_slowfall.addEffect(PotionRegistry.slowfall, 60*3,PotionRegistry.I);
		
		if (ItemCyclicWand.sceptersEnabled) {

			ItemCyclicWand cyclic_wand_build = new ItemCyclicWand();
			addItem(cyclic_wand_build, "cyclic_wand_build");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_build), "sds", " o ", "gog", 'd', new ItemStack(Blocks.DIAMOND_BLOCK), 'g', Items.GHAST_TEAR, 'o', Blocks.OBSIDIAN, 's', Items.NETHER_STAR);

			ItemCyclicWand cyclic_wand_range = new ItemCyclicWand();
			addItem(cyclic_wand_range, "cyclic_wand_range");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_range), "sds", " o ", "gog", 'd', new ItemStack(Blocks.EMERALD_BLOCK), 'g', Items.GHAST_TEAR, 'o', Blocks.OBSIDIAN, 's', Items.NETHER_STAR);

			ItemCyclicWand cyclic_wand_fly = new ItemCyclicWand();
			addItem(cyclic_wand_fly, "cyclic_wand_fly");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_fly), "sds", " o ", "gog", 'd', new ItemStack(Blocks.REDSTONE_BLOCK), 'g', Items.GHAST_TEAR, 'o', Blocks.OBSIDIAN, 's', Items.NETHER_STAR);
		}

		// thanks for help:
		// http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/

		
		
		if (emeraldGearEnabled) {

			Item emerald_sword = new ItemEmeraldSword();
			addItem(emerald_sword, ItemEmeraldSword.name);

			Item emerald_pickaxe = new ItemEmeraldPickaxe();
			addItem(emerald_pickaxe, ItemEmeraldPickaxe.name);

			Item emerald_axe = new ItemEmeraldAxe();
			addItem(emerald_axe, ItemEmeraldAxe.name);

			Item emerald_spade = new ItemEmeraldSpade();
			addItem(emerald_spade, ItemEmeraldSpade.name);

			Item emerald_hoe = new ItemEmeraldHoe();
			addItem(emerald_hoe, ItemEmeraldHoe.name);

			// ..yeah.. kind of breaks the pattern. i could make one class for each i
			// guess.
			Item emerald_helmet = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
			addItem(emerald_helmet, "emerald_helmet");

			Item emerald_chestplate = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
			addItem(emerald_chestplate, "emerald_chestplate");

			Item emerald_leggings = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
			addItem(emerald_leggings, "emerald_leggings");

			Item emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
			addItem(emerald_boots, "emerald_boots");

		}
 

		int I = PotionRegistry.I;
		int II = PotionRegistry.II;
	////	int III = PotionRegistry.III;
	//	int IV = PotionRegistry.IV;
		int V = PotionRegistry.V;
 
		final int hunger		= 4;
		final int time			= 8 * 60; // 8:00

		
		
		ItemFoodAppleMagic apple_chorus = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.CHORUS_FRUIT)
				,MobEffects.LEVITATION, 30, I);
		addItem(apple_chorus, "apple_chorus");
	  
		ItemFoodAppleMagic apple_ender = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.ENDER_PEARL)
				,PotionRegistry.ender, time, I);
		addItem(apple_ender, "apple_ender");
 
		ItemFoodAppleMagic apple_emerald = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.EMERALD)
				,MobEffects.HEALTH_BOOST, time, V);
	 
		addItem(apple_emerald, "apple_emerald"); 
 
		ItemFoodAppleMagic apple_chocolate = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.DYE, 1, Const.dye_cocoa)
				,MobEffects.LUCK, time, II); 
		addItem(apple_chocolate, "apple_chocolate"); 
 
		ItemFoodAppleMagic apple_lapis = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.DYE, 1, Const.dye_lapis),MobEffects.HASTE, time, II);
	 
		addItem(apple_lapis, "apple_lapis"); 
 
		ItemFoodAppleMagic apple_diamond = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.DIAMOND),MobEffects.RESISTANCE, time, I);
		addItem(apple_diamond, "apple_diamond"); 
 
		ItemFoodAppleMagic apple_bone = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.BONE)
				,MobEffects.GLOWING, time, I);
		addItem(apple_bone, "apple_bone"); 
 
		ItemFoodAppleMagic apple_netherwart = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.NETHER_WART),PotionRegistry.magnet, time, I);
		addItem(apple_netherwart, "apple_netherwart"); 
	
		ItemFoodAppleMagic apple_prismarine = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.PRISMARINE_SHARD),PotionRegistry.waterwalk, time, I);
		addItem(apple_prismarine, "apple_prismarine"); 
 
		ItemFoodAppleMagic apple_slowfall = new ItemFoodAppleMagic(hunger, false, 
				new ItemStack(Items.FISH, 1, Const.fish_puffer),PotionRegistry.slowfall, time, I);
		 
		addItem(apple_slowfall, "apple_slowfall");
 
		//maybe one day it will be all base items
		Item item;
		for (String key : itemMap.keySet()) {
			item = itemMap.get(key);
			
			if (item instanceof BaseItem) {
				((BaseItem) item).register(key);
			}
			else{
				registerItem(item,key);
			}
		}


		registerRecipes();
	}

	private static void registerMaterials() {
		
		ARMOR_MATERIAL_EMERALD = 
				EnumHelper.addArmorMaterial("emerald", Const.MODID + ":emerald",  
						33,//same as diamond   
						new int[]{
							 ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
							,ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.CHEST)
							,ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.LEGS)
							,ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.FEET)
						}, 
						ArmorMaterial.DIAMOND.getEnchantability(), 
						ArmorMaterial.DIAMOND.getSoundEvent(),
						ArmorMaterial.DIAMOND.getToughness());

		//enum helper is broken
		//https://github.com/MinecraftForge/MinecraftForge/issues/2870
		//https://github.com/MinecraftForge/MinecraftForge/pull/2874
		// TODO: addToolMat causes a bug/crash, not sure if forge will fix.
		//	EnumHelper.addToolMaterial("emerald", harvestLevel, maxUses, efficiency, damage, enchantability)
		//ToolMaterial.DIAMOND;
		MATERIAL_EMERALD = //ToolMaterial.DIAMOND;
		
			EnumHelper.addToolMaterial("emerald", 
				ToolMaterial.DIAMOND.getHarvestLevel(), ToolMaterial.DIAMOND.getMaxUses(), 
				ToolMaterial.DIAMOND.getEfficiencyOnProperMaterial(), 
				ToolMaterial.DIAMOND.getDamageVsEntity(), 
				ToolMaterial.DIAMOND.getEnchantability());
		
		// EnumHelper.addToolMaterial("emerald", 3, harvestLevel 3 same as diamond
		// 1600,3.5F, 5+25 );
	}
	 
	public static void registerItem(Item item, String name) {
		registerItem(item, name, false);// default is not hidden
	}

	public static void registerItem(Item item, String name, boolean isHidden) {

		//item.setUnlocalizedName(name);

		GameRegistry.register(item, new ResourceLocation(Const.MODID, name));

		if (isHidden == false) {
			item.setCreativeTab(ModMain.TAB);
		} 
	}
}
