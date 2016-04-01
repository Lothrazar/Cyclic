package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.item.*;
import com.lothrazar.cyclicmagic.item.tool.*;
import com.lothrazar.cyclicmagic.item.armor.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
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
	
	public final static CreativeTabs tabSamsContent = CreativeTabs.tabMisc;//TODO: own

//we copy diamond 
	/* public static enum ArmorMaterial
    {
        LEATHER("leather", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.item_armor_equip_leather),
        CHAIN("chainmail", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.item_armor_equip_chain),
        IRON("iron", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.item_armor_equip_iron),
        GOLD("gold", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.item_armor_equip_gold),
        DIAMOND("diamond", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.item_armor_equip_diamond);
        
//private ArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionArrayIn, int enchantabilityIn, SoundEvent equipSoundEventIn)
       
       
public static ArmorMaterial addArmorMaterial(String name, String textureName, int durability, int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip)
  
  //TODO: FIX 
   * customCraftingMaterial
*/
	private static void registerMaterials(){

		ARMOR_MATERIAL_EMERALD = 
				//the 33 is private final int maxDamageFactor;, no getter
				//array is damageReductionAmountArray  
		//TODO: how to set repair item
		EnumHelper.addArmorMaterial("emerald",Const.MODID+":emerald",
				33,new int[]{3, 6, 8, 3}, ArmorMaterial.DIAMOND.getEnchantability() , ArmorMaterial.DIAMOND.getSoundEvent());
		 
		MATERIAL_EMERALD = ToolMaterial.DIAMOND;
		
				/*
				//using this causes a forge crash actually
				EnumHelper.addToolMaterial("emerald", 
				3, //harvestLevel 3 same as diamond
				1600, //maxUses more than diamond
				15.0F, //efficiency more than gold
				3.5F, //damage more than diamond
				5+25 );//enchantability more than gold 
		    */

	}

	public static void register(){
		
		registerMaterials();

		cyclic_wand = new ItemCyclicWand();
		registerItem(cyclic_wand, "cyclic_wand");

		GameRegistry.addRecipe(new ItemStack(cyclic_wand), 
				"sds", 
				" o ", 
				"gog", 
				'd', new ItemStack(Blocks.diamond_block), 
				'g', Items.ghast_tear, 
				'o', Blocks.obsidian, 
				's', Items.nether_star);
		
		Item multitool = new ItemMultiTool();
		registerItem(multitool,"multitool");
		
		Item carbon_paper = new ItemPaperCarbon();
		registerItem(carbon_paper,"carbon_paper");
		
		GameRegistry.addRecipe(new ItemStack(carbon_paper,8),
				"ppp",
				"pcp",
				"ppp",
				'c',new ItemStack(Items.coal,1,1), //charcoal
				'p',Items.paper  );
		
		

		chest_sack = new ItemChestSack();
		registerItem(chest_sack,"chest_sack");

		chest_sack_empty = new ItemChestSackEmpty();
		registerItem(chest_sack_empty,"chest_sack_empty"); 
		
		//thanks for help: http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
		 int xp = 1;
		
		ItemSword emerald_sword = new ItemSword(MATERIAL_EMERALD);
		ItemRegistry.registerItem(emerald_sword, "emerald_sword");
		emerald_sword.setCreativeTab(tabSamsContent);
		
		GameRegistry.addShapedRecipe(new ItemStack(emerald_sword)
			, " e "," e "," s "
			,'e',new ItemStack(Blocks.emerald_block)
			,'s',new ItemStack(Items.stick));
			

 // gggg these Constructors are protected./ this is the only reason i have subclasses, need subclasses
		
		ItemSamPickaxe emerald_pickaxe = new ItemSamPickaxe(MATERIAL_EMERALD);
		registerItem(emerald_pickaxe, "emerald_pickaxe");
		emerald_pickaxe.setCreativeTab(tabSamsContent);
		GameRegistry.addShapedRecipe(new ItemStack(emerald_pickaxe)
			, "eee"," s "," s "
			,'e',new ItemStack(Blocks.emerald_block)
			,'s',new ItemStack(Items.stick));

		ItemAxe emerald_axe = new ItemSamAxe(MATERIAL_EMERALD);
		ItemRegistry.registerItem(emerald_axe, "emerald_axe");
		emerald_axe.setCreativeTab(tabSamsContent);
		GameRegistry.addShapedRecipe(new ItemStack(emerald_axe)
			, "ee ","es "," s "
			,'e',new ItemStack(Blocks.emerald_block)
			,'s',new ItemStack(Items.stick));
		GameRegistry.addShapedRecipe(new ItemStack(emerald_axe)
			, " ee"," se"," s "
			,'e',new ItemStack(Blocks.emerald_block)
			,'s',new ItemStack(Items.stick));
		
		ItemSpade emerald_spade = new ItemSpade(MATERIAL_EMERALD);
		ItemRegistry.registerItem(emerald_spade, "emerald_spade");
		emerald_spade.setCreativeTab(tabSamsContent);
		GameRegistry.addShapedRecipe(new ItemStack(emerald_spade)
			, " e "," s "," s "
			,'e',new ItemStack(Blocks.emerald_block)
			,'s',new ItemStack(Items.stick));
		
		ItemHoe emerald_hoe = new ItemHoe(MATERIAL_EMERALD);
		ItemRegistry.registerItem(emerald_hoe, "emerald_hoe");
		emerald_hoe.setCreativeTab(tabSamsContent);
		GameRegistry.addShapedRecipe(new ItemStack(emerald_hoe)
			, "ee "," s "," s "
			,'e',new ItemStack(Blocks.emerald_block)
			,'s',new ItemStack(Items.stick));
		GameRegistry.addShapedRecipe(new ItemStack(emerald_hoe)
			, " ee"," s "," s "
			,'e',new ItemStack(Blocks.emerald_block)
			,'s',new ItemStack(Items.stick));
		

		//if(ModMain.cfg.uncraftGeneral)
		//{
			GameRegistry.addSmelting(emerald_spade, new ItemStack(Blocks.emerald_block,1), xp);
			GameRegistry.addSmelting(emerald_sword, new ItemStack(Blocks.emerald_block,2), xp);
			GameRegistry.addSmelting(emerald_hoe, new ItemStack(Blocks.emerald_block,2), xp);
			GameRegistry.addSmelting(emerald_pickaxe, new ItemStack(Blocks.emerald_block,3), xp);
			GameRegistry.addSmelting(emerald_axe, new ItemStack(Blocks.emerald_block,3), xp);
		//}
						
						
						

			emerald_helmet = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);//TODO: Reference.armor_type_helmet
			ItemRegistry.registerItem(emerald_helmet, "emerald_helmet");
			GameRegistry.addShapedRecipe(new ItemStack(emerald_helmet)
				,"eee","e e","   "
				,'e',new ItemStack(Blocks.emerald_block));
			GameRegistry.addShapedRecipe(new ItemStack(emerald_helmet)
				,"   ","eee","e e"
				,'e',new ItemStack(Blocks.emerald_block));
			//if(ModMain.cfg.uncraftGeneral)
				GameRegistry.addSmelting(emerald_helmet, new ItemStack(Blocks.emerald_block,5), 0);
			
			emerald_chestplate = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
			ItemRegistry.registerItem(emerald_chestplate, "emerald_chestplate");
			GameRegistry.addShapedRecipe(new ItemStack(emerald_chestplate)
				,"e e","eee","eee"
				,'e',new ItemStack(Blocks.emerald_block));
		//	if(ModMain.cfg.uncraftGeneral)
				GameRegistry.addSmelting(emerald_chestplate, new ItemStack(Blocks.emerald_block,8), 0);
			
			emerald_leggings = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
			ItemRegistry.registerItem(emerald_leggings, "emerald_leggings");
			GameRegistry.addShapedRecipe(new ItemStack(emerald_leggings)
				,"eee","e e","e e"
				,'e',new ItemStack(Blocks.emerald_block));
			//if(ModMain.cfg.uncraftGeneral)
				GameRegistry.addSmelting(emerald_leggings, new ItemStack(Blocks.emerald_block,7), 0);
			
			emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
			ItemRegistry.registerItem(emerald_boots, "emerald_boots");
			GameRegistry.addShapedRecipe(new ItemStack(emerald_boots)
				,"e e","e e","   "
				,'e',new ItemStack(Blocks.emerald_block));
			GameRegistry.addShapedRecipe(new ItemStack(emerald_boots)
				,"   ","e e","e e"
				,'e',new ItemStack(Blocks.emerald_block));
			//if(ModMain.cfg.uncraftGeneral)
				GameRegistry.addSmelting(emerald_boots, new ItemStack(Blocks.emerald_block,4), 0);
		 
	}

	public static void registerItem(Item item, String name){

		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		items.add(item);
	}
}
