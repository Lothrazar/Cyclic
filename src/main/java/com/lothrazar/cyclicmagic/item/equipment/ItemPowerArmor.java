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
package com.lothrazar.cyclicmagic.item.equipment;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

@SuppressWarnings("incomplete-switch")
public class ItemPowerArmor extends ItemArmor implements IHasRecipe {

  public ItemPowerArmor(EntityEquipmentSlot armorType) {
    super(MaterialRegistry.powerArmorMaterial, 0, armorType);
  }

  @Override
  public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    // bonus easter egg for anyone who does not shift click. not documented
    if (this.armorType == EntityEquipmentSlot.CHEST && EnchantRegistry.reach != null) {
      stack.addEnchantment(EnchantRegistry.reach, EnchantRegistry.reach.getMaxLevel());
    }
  }

  private ItemStack addEnchantment(ItemStack stack) {
    switch (this.armorType) {
      case CHEST:
        stack.addEnchantment(Enchantments.PROTECTION, Enchantments.PROTECTION.getMaxLevel());
      break;
      case FEET:
        stack.addEnchantment(Enchantments.FEATHER_FALLING, Enchantments.FEATHER_FALLING.getMaxLevel());
        stack.addEnchantment(Enchantments.DEPTH_STRIDER, Enchantments.DEPTH_STRIDER.getMaxLevel());
      break;
      case HEAD:
        stack.addEnchantment(Enchantments.AQUA_AFFINITY, Enchantments.AQUA_AFFINITY.getMaxLevel());
        stack.addEnchantment(Enchantments.RESPIRATION, Enchantments.RESPIRATION.getMaxLevel());
      break;
      case LEGS:
        stack.addEnchantment(Enchantments.MENDING, Enchantments.MENDING.getMaxLevel());
      break;
    }
    return stack;
  }

  @Override
  public IRecipe addRecipe() {
    switch (this.armorType) {
      case CHEST:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "p p",
            "odo",
            "ooo",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
      case FEET:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "p p",
            "odo",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
      case HEAD:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "odo",
            "p p",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
      case LEGS:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "odo",
            "p p",
            "o o",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
    }
    return null;
  }
}
