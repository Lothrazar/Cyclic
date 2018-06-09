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
package com.lothrazar.cyclicmagic.core.item;

import java.util.List;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true)
public abstract class BaseCharm extends BaseItem implements IHasClickToggle, baubles.api.IBauble {

  private final static String NBT_STATUS = "onoff";
  public ItemStack repairedBy = ItemStack.EMPTY;

  public BaseCharm(int durability) {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }

  public void toggle(EntityPlayer player, ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.setInteger(NBT_STATUS, vnew);
  }

  public boolean isOn(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    if (tags.hasKey(NBT_STATUS) == false) {
      return true;//default for newlycrafted//legacy items
    }
    return tags.getInteger(NBT_STATUS) == 1;
  }

  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return canTick(stack);
  }

  public boolean canTick(ItemStack stack) {
    return isOn(stack) && (stack.getItemDamage() < stack.getMaxDamage());
  }

  public void damageCharm(EntityPlayer living, ItemStack stack) {
    UtilItemStack.damageItem(living, stack);
  }

  public IRecipe addRecipeAndRepair(Item craftItem) {
    return this.addRecipeAndRepair(new ItemStack(craftItem));
  }

  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }

  public IRecipe addRecipeAndRepair(ItemStack craftItem) {
    this.repairedBy = craftItem.copy();
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "r x",
        "id ",
        "iir",
        'x', craftItem,
        'd', "gemDiamond",
        'r', "cropNetherWart",
        'i', "ingotIron");
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack ingredient) {
    return ingredient.isItemEqual(repairedBy);
  }

  /**
   * Fires while in inventory OR while in bauble slot
   * 
   * @param arg0
   * @param arg1
   */
  public abstract void onTick(ItemStack arg0, EntityPlayer arg1);

  @Optional.Method(modid = "baubles")
  public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
    return true;
  }

  @Optional.Method(modid = "baubles")
  public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1) {
    return true;
  }

  @Optional.Method(modid = "baubles")
  public baubles.api.BaubleType getBaubleType(ItemStack arg0) {
    try {
      if (baubles.api.BaubleType.values().length >= 4) { //length is 4 if trinket
        return baubles.api.BaubleType.TRINKET;
      }
      else {
        return baubles.api.BaubleType.RING;
      }
    }
    catch (Exception e) {
      return baubles.api.BaubleType.RING;
    }
  }

  @Optional.Method(modid = "baubles")
  public void onEquipped(ItemStack arg0, EntityLivingBase arg1) {}

  @Optional.Method(modid = "baubles")
  public void onUnequipped(ItemStack arg0, EntityLivingBase arg1) {}

  @Optional.Method(modid = "baubles")
  public void onWornTick(ItemStack stack, EntityLivingBase arg1) {
    if (!this.canTick(stack)) {
      return;
    }
    if (arg1 instanceof EntityPlayer && stack != null && stack.getCount() > 0) {
      this.onTick(stack, (EntityPlayer) arg1);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack held, World player, List<String> list, net.minecraft.client.util.ITooltipFlag par4) {
    super.addInformation(held, player, list, par4);
    String onoff = this.isOn(held) ? "on" : "off";
    list.add(UtilChat.lang("item.cantoggle.tooltip.info") + UtilChat.lang("item.cantoggle.tooltip." + onoff));
  }
}
