package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
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
import com.lothrazar.cyclicmagic.item.ItemEnderPearlReuse;
import com.lothrazar.cyclicmagic.item.ItemFoodAppleMagic;
import com.lothrazar.cyclicmagic.item.ItemFoodHorse;
import com.lothrazar.cyclicmagic.item.ItemInventoryStorage;
import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
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
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
	public static ArrayList<Item> items	= new ArrayList<Item>();
	public static Map<String,Item> itemMap	= new HashMap<String,Item>();
	
	private static boolean emeraldGearEnabled ; 
	public static ToolMaterial		MATERIAL_EMERALD;
	public static ArmorMaterial		ARMOR_MATERIAL_EMERALD;
	public final static Item		REPAIR_EMERALD = Items.emerald;

	// only because theyre private, with no getters
	private static final int		diamondDurability				= 33;
	private static final int[]	diamondreductionAmounts	= new int[] { 3, 6, 8, 3 };
 
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

		ItemFoodHorse.syncConfig(config);
		
		ItemFoodAppleMagic.syncConfig(config);
	}
	private static void registerRecipes() {

		 
		for (Item item : items) {
			if (item instanceof IHasRecipe) {
				((IHasRecipe) item).addRecipe();
			}
		}
	}

	public static void register() {
		registerMaterials();
		
		addItem(new ItemEnderPearlReuse().setRawName("ender_pearl_reuse")); 
		addItem(new ItemPaperCarbon().setRawName("carbon_paper")); 
		addItem(new ItemToolHarvest().setRawName("tool_harvest"));
		addItem(new ItemToolPull().setRawName("tool_pull"));
		addItem(new ItemToolPush().setRawName("tool_push"));
		addItem(new ItemToolRotate().setRawName("tool_rotate"));
		addItem(new ItemInventoryStorage().setRawName("storage_bag"));
		addItem(new ItemChestSack().setRawName("chest_sack").setHidden());
		addItem(new ItemChestSackEmpty().setRawName("chest_sack_empty"));
		addItem(new ItemProjectileBlaze().setRawName("ender_blaze"));
		addItem(new ItemProjectileDungeon().setRawName("ender_dungeon"));
		addItem(new ItemProjectileFishing().setRawName("ender_fishing"));
		addItem(new ItemProjectileWool().setRawName("ender_wool"));
		addItem(new ItemProjectileTorch().setRawName("ender_torch"));
		addItem(new ItemProjectileWater().setRawName("ender_water"));
		addItem(new ItemProjectileSnow().setRawName("ender_snow"));
		addItem(new ItemProjectileHarvest().setRawName("ender_harvest"));
		addItem(new ItemProjectileLightning().setRawName("ender_lightning"));
		addItem(new ItemProjectileTNT(1).setRawName("ender_tnt_1"));
		addItem(new ItemProjectileTNT(2).setRawName("ender_tnt_2"));
		addItem(new ItemProjectileTNT(3).setRawName("ender_tnt_4"));
		addItem(new ItemProjectileTNT(4).setRawName("ender_tnt_6"));
		addItem(new ItemFoodHorse(new ItemStack(Items.emerald)).setRawName("horse_upgrade_type"));
		addItem(new ItemFoodHorse(new ItemStack(Items.dye, 1, Const.dye_lapis)).setRawName("horse_upgrade_variant"));
		addItem(new ItemFoodHorse(new ItemStack(Items.diamond)).setRawName("horse_upgrade_health"));
		addItem(new ItemFoodHorse(new ItemStack(Items.redstone)).setRawName("horse_upgrade_speed"));
		addItem(new ItemFoodHorse(new ItemStack(Items.ender_eye)).setRawName("horse_upgrade_jump"));
  
		//only some need static references
		addItem(new ItemEnderBook().setRawName("book_ender")); 
 
		//maybe one day it will be all base items
		for (Item item : items) { 
			if (item instanceof BaseItem) {
				((BaseItem) item).register();
			}
//			else{
//				System.out.println("WARN: unregistered item"+item.getClass()); 
//				//registerItem(item);//NAME??
//			}
		}


		if (ItemCyclicWand.sceptersEnabled) {

			ItemCyclicWand cyclic_wand_build = new ItemCyclicWand();
			registerItem(cyclic_wand_build, "cyclic_wand_build");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_build), "sds", " o ", "gog", 'd', new ItemStack(Blocks.diamond_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

			ItemCyclicWand cyclic_wand_range = new ItemCyclicWand();
			registerItem(cyclic_wand_range, "cyclic_wand_range");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_range), "sds", " o ", "gog", 'd', new ItemStack(Blocks.emerald_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

			ItemCyclicWand cyclic_wand_fly = new ItemCyclicWand();
			registerItem(cyclic_wand_fly, "cyclic_wand_fly");
			GameRegistry.addRecipe(new ItemStack(cyclic_wand_fly), "sds", " o ", "gog", 'd', new ItemStack(Blocks.redstone_block), 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);
		}

		// thanks for help:
		// http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/

		
		
		if (emeraldGearEnabled) {

			Item emerald_sword = new ItemEmeraldSword();
			registerItem(emerald_sword, ItemEmeraldSword.name);

			Item emerald_pickaxe = new ItemEmeraldPickaxe();
			registerItem(emerald_pickaxe, ItemEmeraldPickaxe.name);

			Item emerald_axe = new ItemEmeraldAxe();
			registerItem(emerald_axe, ItemEmeraldAxe.name);

			Item emerald_spade = new ItemEmeraldSpade();
			registerItem(emerald_spade, ItemEmeraldSpade.name);

			Item emerald_hoe = new ItemEmeraldHoe();
			registerItem(emerald_hoe, ItemEmeraldHoe.name);

			// ..yeah.. kind of breaks the pattern. i could make one class for each i
			// guess.
			Item emerald_helmet = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
			registerItem(emerald_helmet, "emerald_helmet");

			Item emerald_chestplate = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
			registerItem(emerald_chestplate, "emerald_chestplate");

			Item emerald_leggings = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
			registerItem(emerald_leggings, "emerald_leggings");

			Item emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
			registerItem(emerald_boots, "emerald_boots");

		}
 

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
			ItemFoodAppleMagic apple_diamond = new ItemFoodAppleMagic(hunger, false, new ItemStack(Items.diamond), ItemFoodAppleMagic.apple_diamond_expensive,true);
			ItemRegistry.registerItem(apple_diamond, "apple_diamond");
			apple_diamond.addEffect(MobEffects.resistance, time, I);
			apple_diamond.addEffect(MobEffects.saturation, time, I);
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

	private static void registerMaterials() {

		ARMOR_MATERIAL_EMERALD = EnumHelper.addArmorMaterial("emerald", Const.MODID + ":emerald", diamondDurability, diamondreductionAmounts, ArmorMaterial.DIAMOND.getEnchantability(), ArmorMaterial.DIAMOND.getSoundEvent());

		MATERIAL_EMERALD = ToolMaterial.DIAMOND;
		// TODO: addToolMat causes a bug/crash, not sure if forge will fix.

		// EnumHelper.addToolMaterial("emerald", 3, harvestLevel 3 same as diamond
		// 1600,3.5F, 5+25 );

	}

	private static void addItem(BaseItem i){
		items.add(i);
		itemMap.put(i.getRawName(), i);
	}
	 
	public static void registerItem(Item item, String name) {
		registerItem(item, name, false);// default is not hidden
	}

	public static void registerItem(Item item, String name, boolean isHidden) {

		item.setUnlocalizedName(name);

		GameRegistry.register(item, new ResourceLocation(Const.MODID, name));

		if (isHidden == false) {
			item.setCreativeTab(ModMain.TAB);
		}
		items.add(item);
		itemMap.put(name, item);
	}
}
