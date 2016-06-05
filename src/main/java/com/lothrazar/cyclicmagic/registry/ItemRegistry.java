package com.lothrazar.cyclicmagic.registry;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.BaseItem;
import com.lothrazar.cyclicmagic.item.ItemAppleEmerald;
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
import com.lothrazar.cyclicmagic.item.ItemToolPearlReuse;
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
import com.lothrazar.cyclicmagic.item.ItemToolLaunch;
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
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {

	public static Map<String,Item> itemMap	= new HashMap<String,Item>();

	public static final Item corrupted_chorus = new ItemFoodCorruptedChorus();
	public static final ItemToolLaunch corrupted_elytra = new ItemToolLaunch();
	public static final ItemCyclicWand cyclic_wand_build = new ItemCyclicWand();
	public static final ItemPotionCustom potion_viscous = new ItemPotionCustom(false);
	public static final ItemPotionCustom potion_levitation = new ItemPotionCustom(true,MobEffects.LEVITATION, 60*3);
	public static final ItemPotionCustom potion_levitation_long = new ItemPotionCustom(true,MobEffects.LUCK, 60*8);
	public static final ItemPotionCustom potion_luck = new ItemPotionCustom(true,MobEffects.LUCK, 60*3);
	public static final ItemPotionCustom potion_luck_long = new ItemPotionCustom(true,MobEffects.LEVITATION, 60*8);
	public static final ItemPotionCustom potion_ender = new ItemPotionCustom(true);
	public static final ItemPotionCustom potion_ender_long = new ItemPotionCustom(true);
	public static final ItemPotionCustom potion_magnet = new ItemPotionCustom(false);
	public static final ItemPotionCustom potion_magnet_long = new ItemPotionCustom(false);
	public static final ItemPotionCustom potion_waterwalk = new ItemPotionCustom(false);
	public static final ItemPotionCustom potion_waterwalk_long = new ItemPotionCustom(false);
	public static final ItemPotionCustom potion_slowfall = new ItemPotionCustom(true);
	public static final ItemPotionCustom potion_slowfall_long = new ItemPotionCustom(true);
//	public static final ItemPotionCustom potion_glowing = new ItemPotionCustom(MobEffects.GLOWING, 60*3);
//	public static final ItemPotionCustom potion_glowing_long = new ItemPotionCustom(MobEffects.GLOWING, 60*8);
	public static final ItemPotionCustom potion_resistance = new ItemPotionCustom(true,MobEffects.RESISTANCE, 60*3);
	public static final ItemPotionCustom potion_resistance_strong = new ItemPotionCustom(true,MobEffects.RESISTANCE, 90,PotionRegistry.II);
	public static final ItemPotionCustom potion_resistance_long = new ItemPotionCustom(true,MobEffects.RESISTANCE, 60*8);
	public static final ItemPotionCustom potion_boost = new ItemPotionCustom(true,MobEffects.HEALTH_BOOST, 60*3, PotionRegistry.V);
	public static final ItemPotionCustom potion_boost_long = new ItemPotionCustom(true,MobEffects.HEALTH_BOOST, 60*8, PotionRegistry.V);
	public static final ItemPotionCustom potion_haste = new ItemPotionCustom(false,MobEffects.HASTE, 60*3);
	public static final ItemPotionCustom potion_haste_strong = new ItemPotionCustom(false,MobEffects.HASTE, 90, PotionRegistry.II);
	public static final ItemPotionCustom potion_haste_long = new ItemPotionCustom(false,MobEffects.HASTE, 60*8);
	
	public static void construct(){
		//TODO: maybe constructor. MUST be done before config

		addItem(new ItemToolPearlReuse(),"ender_pearl_reuse"); 
		addItem(new ItemPaperCarbon(),"carbon_paper"); 
		addItem(new ItemToolHarvest(),"tool_harvest");
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
		addItem(corrupted_chorus,"corrupted_chorus");
		addItem(new ItemFoodHeart(),"heart_food"); 
		addItem(new ItemFoodCrafting(),"crafting_food");
		addItem(new ItemFoodInventory(),"inventory_food");
		addItem(new ItemSleepingBag(),"sleeping_mat");
		addItem(potion_viscous, "potion_viscous");
		addItem(potion_boost, "potion_boost");
		addItem(potion_boost_long, "potion_boost_long");
		addItem(potion_resistance, "potion_resistance");
		addItem(potion_resistance_long, "potion_resistance_long");
		addItem(potion_resistance_strong, "potion_resistance_strong");
		addItem(potion_waterwalk, "potion_waterwalk");
		addItem(potion_waterwalk_long, "potion_waterwalk_long");
		addItem(potion_slowfall, "potion_slowfall");
		addItem(potion_slowfall_long, "potion_slowfall_long");
//		addItem(potion_glowing, "potion_glowing");
//		addItem(potion_glowing_long, "potion_glowing_long");
		addItem(potion_magnet, "potion_magnet");
		addItem(potion_magnet_long, "potion_magnet_long");
		addItem(potion_haste, "potion_haste");
		addItem(potion_haste_long, "potion_haste_long");
		addItem(potion_haste_strong, "potion_haste_strong");
		addItem(potion_ender, "potion_ender");
		addItem(potion_ender_long, "potion_ender_long");
		addItem(potion_luck, "potion_luck");	
		addItem(potion_luck_long, "potion_luck_long");	
		addItem(potion_levitation, "potion_levitation");
		addItem(potion_levitation_long, "potion_levitation_long");
		addItem(corrupted_elytra ,"corrupted_elytra");
		addItem(cyclic_wand_build, "cyclic_wand_build");

		addItem(new ItemAppleEmerald(), "apple_emerald"); 
	}

	private static Item addItem(Item i, String key){ 
		i.setUnlocalizedName(key);
		itemMap.put(key, i);
		return i;
	}
	
//	private static boolean emeraldGearEnabled ; 
	public static ToolMaterial		MATERIAL_EMERALD;
	public static ArmorMaterial		ARMOR_MATERIAL_EMERALD;
	public final static Item		REPAIR_EMERALD = Items.EMERALD;

	// only because theyre private, with no getters
//	private static final int		diamondDurability				= 33;
	//private static final int[]	diamondreductionAmounts	= new int[] { 3, 6, 8, 3 };
 
	public static void syncConfig(Configuration config) {
//		Property prop;
		Item item;
		for (String key : itemMap.keySet()) {
			item = itemMap.get(key);
			if (item instanceof IHasConfig) {
				((IHasConfig) item).syncConfig(config);
			}
		}
//		
//		String category = Const.ConfigCategory.items;
//		prop = config.get(category, "EmeraldGear", true, "Full set of emerald gear with similar properties as diamond");
//		prop.setRequiresMcRestart(true);
//		emeraldGearEnabled = prop.getBoolean();

		ItemFoodHorse.syncConfig(config);
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

//		addItem(new ItemFlintTool(),"flint_tool");

		potion_ender.addEffect(PotionRegistry.ender, 60*3,PotionRegistry.I);
		potion_magnet.addEffect(PotionRegistry.magnet, 60*3,PotionRegistry.I);
		potion_waterwalk.addEffect(PotionRegistry.waterwalk, 60*3,PotionRegistry.I);
		potion_slowfall.addEffect(PotionRegistry.slowfall, 60*3,PotionRegistry.I);
		
		potion_ender_long.addEffect(PotionRegistry.ender, 60*8,PotionRegistry.I);
		potion_magnet_long.addEffect(PotionRegistry.magnet, 60*8,PotionRegistry.I);
		potion_waterwalk_long.addEffect(PotionRegistry.waterwalk, 60*8,PotionRegistry.I);
		potion_slowfall_long.addEffect(PotionRegistry.slowfall, 60*8,PotionRegistry.I);
	
			


		// thanks for help:
		// http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/

		
		 
 
		addItem(new ItemEmeraldSword(), ItemEmeraldSword.name);
 
		addItem(new ItemEmeraldPickaxe(), ItemEmeraldPickaxe.name);
 
		addItem(new ItemEmeraldAxe(), ItemEmeraldAxe.name);
 
		addItem(new ItemEmeraldSpade(), ItemEmeraldSpade.name);
 
		addItem( new ItemEmeraldHoe(), ItemEmeraldHoe.name);

		// ..yeah.. kind of breaks the pattern. i could make one class for each i
		// guess. 
		addItem(new ItemEmeraldArmor(EntityEquipmentSlot.HEAD), "emerald_helmet");
 
		addItem(new ItemEmeraldArmor(EntityEquipmentSlot.CHEST), "emerald_chestplate");
 
		addItem( new ItemEmeraldArmor(EntityEquipmentSlot.LEGS), "emerald_leggings");
 
		addItem(new ItemEmeraldArmor(EntityEquipmentSlot.FEET), "emerald_boots");

 
  
  
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
