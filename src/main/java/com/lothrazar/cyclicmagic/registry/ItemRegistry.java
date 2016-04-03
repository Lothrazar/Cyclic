package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.item.*;
import com.lothrazar.cyclicmagic.item.tool.*;
import com.lothrazar.cyclicmagic.item.armor.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
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
		registerRecipes();
	}
	public static void syncConfig(Configuration config){

		String category = "items";
		
		config.setCategoryComment(category, "Items added to the game");
		
		configToggle.put(ItemEnderPearlReuse.name, config.get(category,ItemEnderPearlReuse.name, true).getBoolean());
		configToggle.put(ItemPaperCarbon.name, config.get(category,ItemPaperCarbon.name, true).getBoolean());
		configToggle.put(ItemChestSack.name, config.get(category,ItemChestSack.name, true).getBoolean());
		configToggle.put("emerald_gear", config.get(category,"emerald_gear", true).getBoolean());
		
		//configToggle
		
	}
}
