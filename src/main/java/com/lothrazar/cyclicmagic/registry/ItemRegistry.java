package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
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
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry{

	public static ArrayList<Item> items = new ArrayList<Item>();

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

		ItemEnderPearlReuse ender_pearl_reuse = new ItemEnderPearlReuse();
		registerItem(ender_pearl_reuse, "ender_pearl_reuse");

		cyclic_wand = new ItemCyclicWand();
		registerItem(cyclic_wand, "cyclic_wand");

		Item multitool = new ItemMultiTool();
		registerItem(multitool, "multitool");

		Item carbon_paper = new ItemPaperCarbon();
		registerItem(carbon_paper, "carbon_paper");

		chest_sack = new ItemChestSack();
		registerItem(chest_sack, "chest_sack",true);//true for ishidden

		chest_sack_empty = new ItemChestSackEmpty();
		registerItem(chest_sack_empty, "chest_sack_empty");

		// thanks for help:
		// http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/

		ItemSword emerald_sword = new ItemEmeraldSword();
		registerItem(emerald_sword, "emerald_sword");

		ItemEmeraldPickaxe emerald_pickaxe = new ItemEmeraldPickaxe();
		registerItem(emerald_pickaxe, "emerald_pickaxe");

		ItemEmeraldAxe emerald_axe = new ItemEmeraldAxe();
		registerItem(emerald_axe, "emerald_axe");

		ItemSpade emerald_spade = new ItemEmeraldSpade();
		registerItem(emerald_spade, "emerald_spade");

		ItemHoe emerald_hoe = new ItemEmeraldHoe();
		registerItem(emerald_hoe, "emerald_hoe");

		emerald_helmet = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
		registerItem(emerald_helmet, "emerald_helmet");

		emerald_chestplate = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
		registerItem(emerald_chestplate, "emerald_chestplate");

		emerald_leggings = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
		registerItem(emerald_leggings, "emerald_leggings");

		emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
		registerItem(emerald_boots, "emerald_boots");

		registerRecipes();
	}
}
