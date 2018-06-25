/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class MaterialRegistry { // thanks for help:

  private static final int diamondDurability = 33;
  private static final int ironDurability = 15;
  // http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
  private static final String emeraldName = "emerald";
  private static final String MATERIALNAME = "power";
  private static final String GLOWING = "glowing";
  // thanks for help: http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
  public static ToolMaterial netherToolMaterial;
  public static ToolMaterial sandstoneToolMaterial;
  public static ToolMaterial emeraldToolMaterial;
  public static ArmorMaterial emeraldArmorMaterial;
  public static ArmorMaterial powerArmorMaterial;
  public static ToolMaterial powerToolMaterial;
  public static ArmorMaterial glowingArmorMaterial;

  public static void register() {
    registerPurpleMaterial();
    registerEmeraldMaterial();
    registerSandstoneMaterials();
    registerGlowingMaterials();
    registerNetherMaterials();
  }

  private static void registerGlowingMaterials() {
    ArmorMaterial mimicArmor = ArmorMaterial.IRON;
    MaterialRegistry.glowingArmorMaterial = EnumHelper.addArmorMaterial(GLOWING, Const.MODRES + GLOWING,
        ironDurability, // affects DURABILITY  
        new int[] {
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.FEET),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.LEGS),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.CHEST),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        mimicArmor.getEnchantability() + 1,
        mimicArmor.getSoundEvent(),
        mimicArmor.getToughness() + 1);
    MaterialRegistry.glowingArmorMaterial.repairMaterial = new ItemStack(Blocks.GLOWSTONE);
  }

  private static void registerPurpleMaterial() {
    ArmorMaterial mimicArmor = ArmorMaterial.DIAMOND;
    MaterialRegistry.powerArmorMaterial = EnumHelper.addArmorMaterial(MATERIALNAME, Const.MODRES + MATERIALNAME,
        diamondDurability * 2, // affects DURABILITY . 15 is the same as iron
        new int[] {
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.FEET) + 1,
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.LEGS) + 1,
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.CHEST) + 1,
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.HEAD) + 2
        },
        mimicArmor.getEnchantability() / 4,
        mimicArmor.getSoundEvent(),
        mimicArmor.getToughness() + 2);
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
        diamondDurability + 30, //was -2 affects DURABILITY 
        new int[] {
            ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.FEET),
            ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.LEGS),
            ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.CHEST),
            ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        ArmorMaterial.GOLD.getEnchantability(),
        ArmorMaterial.DIAMOND.getSoundEvent(),
        ArmorMaterial.DIAMOND.getToughness() + 1);//was  / 2
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

  /**
   * Sandstone is between wood and stone, with stone harvest level
   */
  private static void registerSandstoneMaterials() {
    MaterialRegistry.sandstoneToolMaterial = EnumHelper.addToolMaterial("sandstone",
        ToolMaterial.STONE.getHarvestLevel(),
        (ToolMaterial.STONE.getMaxUses() + ToolMaterial.WOOD.getMaxUses()) / 2,
        (ToolMaterial.STONE.getEfficiencyOnProperMaterial() + ToolMaterial.STONE.getEfficiencyOnProperMaterial()) / 2,
        (ToolMaterial.STONE.getDamageVsEntity() + ToolMaterial.WOOD.getDamageVsEntity()) / 2.0F,
        ToolMaterial.GOLD.getEnchantability());
    MaterialRegistry.sandstoneToolMaterial.setRepairItem(new ItemStack(Blocks.SANDSTONE));
  }

  /**
   * Netherbrick is between stone and iron, still with stone harvest level
   */
  private static void registerNetherMaterials() {
    MaterialRegistry.netherToolMaterial = EnumHelper.addToolMaterial("nether",
        ToolMaterial.STONE.getHarvestLevel(),
        (ToolMaterial.STONE.getMaxUses() + ToolMaterial.IRON.getMaxUses()) / 2,
        (ToolMaterial.STONE.getEfficiencyOnProperMaterial() + //halfway in between
            ToolMaterial.IRON.getEfficiencyOnProperMaterial()) / 2,
        ToolMaterial.IRON.getDamageVsEntity(),
        ToolMaterial.GOLD.getEnchantability());
    MaterialRegistry.netherToolMaterial.setRepairItem(new ItemStack(Items.NETHERBRICK));
  }
}
