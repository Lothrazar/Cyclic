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
package com.lothrazar.cyclicmagic.item.merchant;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchantAlmanac extends InventoryMerchant implements IInventory {

  private final IMerchant theMerchant;
  private final NonNullList<ItemStack> inv = NonNullList.withSize(3, ItemStack.EMPTY);
  private final EntityPlayer thePlayer;
  private MerchantRecipe currentRecipe;
  private int currentRecipeIndex;
  private MerchantRecipeList trades;

  public InventoryMerchantAlmanac(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
    super(thePlayerIn, theMerchantIn);
    this.thePlayer = thePlayerIn;
    this.theMerchant = theMerchantIn;
    trades = this.theMerchant.getRecipes(this.thePlayer);
  }

  /**
   * Returns the number of slots in the inventory.
   */
  @Override
  public int getSizeInventory() {
    return this.inv.size();
  }

  /**
   * Returns the stack in the given slot.
   */
  @Override
  public ItemStack getStackInSlot(int index) {
    return this.inv.get(index);
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack itemstack = this.inv.get(index);
    if (index == 2 && !itemstack.isEmpty()) {
      return ItemStackHelper.getAndSplit(this.inv, index, itemstack.getCount());
    }
    else {
      ItemStack itemstack1 = ItemStackHelper.getAndSplit(this.inv, index, count);
      if (!itemstack1.isEmpty() && this.inventoryResetNeededOnSlotChange(index)) {
        this.resetRecipeAndSlots();
      }
      return itemstack1;
    }
  }

  private boolean inventoryResetNeededOnSlotChange(int slotIn) {
    return slotIn == 0 || slotIn == 1;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    return ItemStackHelper.getAndRemove(this.inv, index);
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    if (index > inv.size()) {
      return;
    }
    this.inv.set(index, stack);
    //    if (stack != null && stack.getCount() > this.getInventoryStackLimit()) {
    //      stack = this.getInventoryStackLimit();
    //    }
    if (this.inventoryResetNeededOnSlotChange(index)) {
      this.resetRecipeAndSlots();
    }
  }

  @Override
  public String getName() {
    return "mob.villager";
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  public boolean isUseableByPlayer(EntityPlayer player) {
    return this.theMerchant.getCustomer() == player;
  }

  @Override
  public void openInventory(EntityPlayer player) {}

  @Override
  public void closeInventory(EntityPlayer player) {}

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }

  @Override
  public void markDirty() {
    this.resetRecipeAndSlots();
  }

  public void setRecipes(MerchantRecipeList t) {
    if (t.size() != trades.size()) {
      trades = t;
    }
  }

  public MerchantRecipeList getRecipes() {
    return trades;
  }

  @Override
  public void resetRecipeAndSlots() {
    this.currentRecipe = null;
    ItemStack itemstack = this.inv.get(0);
    ItemStack itemstack1 = this.inv.get(1);
    if (itemstack.isEmpty()) {
      itemstack = itemstack1;
      itemstack1 = ItemStack.EMPTY;
    }
    if (itemstack.isEmpty()) {
      this.setInventorySlotContents(2, ItemStack.EMPTY);
    }
    else {
      MerchantRecipeList merchantrecipelist = this.getRecipes();
      if (merchantrecipelist != null) {
        MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
        if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
          this.currentRecipe = merchantrecipe;
          this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
        }
        else if (!itemstack1.isEmpty()) {
          merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
          if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
            this.currentRecipe = merchantrecipe;
            this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
          }
          else {
            this.setInventorySlotContents(2, ItemStack.EMPTY);
          }
        }
        else {
          this.setInventorySlotContents(2, ItemStack.EMPTY);
        }
      }
    }
    this.theMerchant.verifySellingItem(this.getStackInSlot(2));
  }

  @Override
  public MerchantRecipe getCurrentRecipe() {
    return this.currentRecipe;
  }

  @Override
  public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
    this.currentRecipeIndex = currentRecipeIndexIn;
    this.resetRecipeAndSlots();
  }

  @Override
  public int getField(int id) {
    return 0;
  }

  @Override
  public void setField(int id, int value) {}

  @Override
  public int getFieldCount() {
    return 0;
  }

  @Override
  public void clear() {
    for (int i = 0; i < this.inv.size(); ++i) {
      this.inv.set(i, ItemStack.EMPTY);
    }
  }
}
