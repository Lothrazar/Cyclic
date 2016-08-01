package com.lothrazar.cyclicmagic.module;

import com.lothrazar.cyclicmagic.item.ItemEmeraldArmor;
import com.lothrazar.cyclicmagic.item.ItemEmeraldAxe;
import com.lothrazar.cyclicmagic.item.ItemEmeraldHoe;
import com.lothrazar.cyclicmagic.item.ItemEmeraldPickaxe;
import com.lothrazar.cyclicmagic.item.ItemEmeraldSpade;
import com.lothrazar.cyclicmagic.item.ItemEmeraldSword;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

public class EmeraldArmorModule extends BaseModule {

  private boolean enableEmeraldGear ; 

  public static ItemEmeraldArmor emerald_boots;
  // thanks for help:
  // http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
  public static ToolMaterial TOOL_MATERIAL_EMERALD;
  public static ArmorMaterial ARMOR_MATERIAL_EMERALD;
  public final static Item REPAIR_EMERALD = Items.EMERALD;
  private static final int maxDamageFactorDiamond = 33;
  private static final String emeraldName = "emerald";
  public EmeraldArmorModule(){
    super();
    //materials is kind of a SUB-MODULE
    this.registerMaterials();
  }
  //from ArmorMaterial.DIAMOND, second constuctor param
  //used as a ratio for durability

  // only because theyre private, with no getters
  //  private static final int    diamondDurability       = 33;
  //private static final int[]  diamondreductionAmounts = new int[] { 3, 6, 8, 3 };
  private void registerMaterials() {
    ARMOR_MATERIAL_EMERALD = EnumHelper.addArmorMaterial(emeraldName, Const.MODRES + emeraldName,
        maxDamageFactorDiamond - 2, //affects DURABILITY 
        new int[] {
            ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.FEET), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.LEGS), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.CHEST), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        ArmorMaterial.GOLD.getEnchantability(),
        ArmorMaterial.DIAMOND.getSoundEvent(),
        ArmorMaterial.DIAMOND.getToughness() / 2);
    //max uses is durability ex The number of uses this material allows.
    //as of 1.9.4 :  (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
    TOOL_MATERIAL_EMERALD = EnumHelper.addToolMaterial(emeraldName,
        ToolMaterial.DIAMOND.getHarvestLevel(),
        ToolMaterial.DIAMOND.getMaxUses() - 261,
        ToolMaterial.DIAMOND.getEfficiencyOnProperMaterial(),
        ToolMaterial.DIAMOND.getDamageVsEntity() - 0.25F,
        ToolMaterial.GOLD.getEnchantability());
    // EnumHelper.addToolMaterial("emerald", 3, harvestLevel 3 same as diamond
    // 1600,3.5F, 5+25 );
  }
  @Override
  public void onInit() {
    if(enableEmeraldGear){
      ItemRegistry.addItem(new ItemEmeraldArmor(EntityEquipmentSlot.HEAD), "emerald_helmet");
      ItemRegistry.addItem(new ItemEmeraldArmor(EntityEquipmentSlot.CHEST), "emerald_chestplate");
      ItemRegistry.addItem(new ItemEmeraldArmor(EntityEquipmentSlot.LEGS), "emerald_leggings");
      emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
      ItemRegistry.addItem(emerald_boots, "emerald_boots");
  
      ItemRegistry.addItem(new ItemEmeraldSword(), ItemEmeraldSword.name);
      ItemRegistry.addItem(new ItemEmeraldPickaxe(), ItemEmeraldPickaxe.name);
      ItemRegistry.addItem(new ItemEmeraldAxe(), ItemEmeraldAxe.name);
      ItemRegistry.addItem(new ItemEmeraldSpade(), ItemEmeraldSpade.name);
      ItemRegistry.addItem(new ItemEmeraldHoe(), ItemEmeraldHoe.name);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableEmeraldGear = config.getBoolean("Emerald Gear", Const.ConfigCategory.content, true, "Emerald armor and tools that are slightly weaker than diamond. "+Const.ConfigCategory.contentDefaultText);
  }
}
