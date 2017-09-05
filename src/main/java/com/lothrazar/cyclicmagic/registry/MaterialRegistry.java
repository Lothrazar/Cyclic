package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class MaterialRegistry { // thanks for help:
  // http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
  private static final int maxDamageFactorDiamond = 33;
  private static final String emeraldName = "emerald";
  private static final String MATERIALNAME = "power";
  // thanks for help: http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
  public static ToolMaterial sandstoneToolMaterial;
  public static ToolMaterial emeraldToolMaterial;
  public static ArmorMaterial emeraldArmorMaterial;
  public static ArmorMaterial powerArmorMaterial;
  public static ToolMaterial powerToolMaterial;
  public static void register() {
    registerPurpleMaterial();
    registerEmeraldMaterial();
    registerSandstoneMaterials();
  }
  //  private static void registerObsidianMaterial() {
  //    ArmorMaterial mimicArmor = ArmorMaterial.DIAMOND;
  //    String name = "obsidian";
  //    MaterialRegistry.obsidianArmorMaterial = EnumHelper.addArmorMaterial(name, Const.MODRES + name,
  //        60, // affects DURABILITY . 15 is the same as iron
  //        new int[] {
  //            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.FEET),
  //            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.LEGS),
  //            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.CHEST),
  //            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
  //        },
  //        mimicArmor.getEnchantability() * 2,
  //        mimicArmor.getSoundEvent(),
  //        mimicArmor.getToughness() * 4);
  //    MaterialRegistry.obsidianArmorMaterial.repairMaterial = new ItemStack(Blocks.OBSIDIAN);
  //    //max uses is durability ex The number of uses this material allows.
  //    //as of 1.9.4 :  (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
  //    MaterialRegistry.obsidianToolMaterial = EnumHelper.addToolMaterial(name,
  //        ToolMaterial.DIAMOND.getHarvestLevel() * 4,
  //        ToolMaterial.DIAMOND.getMaxUses() * 4, //was  - 261
  //        ToolMaterial.DIAMOND.getEfficiencyOnProperMaterial() * 4,
  //        ToolMaterial.DIAMOND.getDamageVsEntity() * 4, //was  - 0.25F
  //        ToolMaterial.GOLD.getEnchantability());
  //    MaterialRegistry.obsidianToolMaterial.setRepairItem(MaterialRegistry.obsidianArmorMaterial.repairMaterial);
  //  }
  private static void registerPurpleMaterial() {
    ArmorMaterial mimicArmor = ArmorMaterial.DIAMOND;
    MaterialRegistry.powerArmorMaterial = EnumHelper.addArmorMaterial(MATERIALNAME, Const.MODRES + MATERIALNAME,
        15, // affects DURABILITY . 15 is the same as iron
        new int[] {
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.FEET),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.LEGS),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.CHEST),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        mimicArmor.getEnchantability(),
        mimicArmor.getSoundEvent(),
        mimicArmor.getToughness());
    MaterialRegistry.powerArmorMaterial.repairMaterial = new ItemStack(Blocks.OBSIDIAN);
    //now the tool material
    MaterialRegistry.powerToolMaterial = EnumHelper.addToolMaterial(MATERIALNAME,
        ToolMaterial.DIAMOND.getHarvestLevel(),
        ToolMaterial.DIAMOND.getMaxUses() * 4, //was  - 261
        ToolMaterial.DIAMOND.getEfficiencyOnProperMaterial(),
        ToolMaterial.DIAMOND.getDamageVsEntity() * 8, //best draconic evolution sword is 35 base, so this is not that crazy
        ToolMaterial.GOLD.getEnchantability() * 2);
    MaterialRegistry.powerToolMaterial.setRepairItem(MaterialRegistry.powerArmorMaterial.repairMaterial);
  }
  private static void registerEmeraldMaterial() {
    MaterialRegistry.emeraldArmorMaterial = EnumHelper.addArmorMaterial(emeraldName, Const.MODRES + emeraldName,
        maxDamageFactorDiamond, //was -2 affects DURABILITY 
        new int[] {
            ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.FEET), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.LEGS), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.CHEST), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        ArmorMaterial.GOLD.getEnchantability(),
        ArmorMaterial.DIAMOND.getSoundEvent(),
        ArmorMaterial.DIAMOND.getToughness());//was  / 2
    MaterialRegistry.emeraldArmorMaterial.repairMaterial = new ItemStack(Items.EMERALD);
    //max uses is durability ex The number of uses this material allows.
    //as of 1.9.4 :  (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
    MaterialRegistry.emeraldToolMaterial = EnumHelper.addToolMaterial(emeraldName,
        ToolMaterial.DIAMOND.getHarvestLevel(),
        ToolMaterial.DIAMOND.getMaxUses(), //was  - 261
        ToolMaterial.DIAMOND.getEfficiencyOnProperMaterial(),
        ToolMaterial.DIAMOND.getDamageVsEntity(), //was  - 0.25F
        ToolMaterial.GOLD.getEnchantability());
    MaterialRegistry.emeraldToolMaterial.setRepairItem(MaterialRegistry.emeraldArmorMaterial.repairMaterial);
  }
  private static void registerSandstoneMaterials() {
    MaterialRegistry.sandstoneToolMaterial = EnumHelper.addToolMaterial("sandstone",
        ToolMaterial.STONE.getHarvestLevel(),
        (ToolMaterial.STONE.getMaxUses() + ToolMaterial.WOOD.getMaxUses()) / 2,
        ToolMaterial.STONE.getEfficiencyOnProperMaterial(),
        (ToolMaterial.STONE.getDamageVsEntity() + ToolMaterial.WOOD.getDamageVsEntity()) / 2,
        ToolMaterial.STONE.getEnchantability());
    MaterialRegistry.sandstoneToolMaterial.setRepairItem(new ItemStack(Blocks.SANDSTONE));
  }
}
