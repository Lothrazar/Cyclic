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

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.container.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class ContainerMerchantAlmanac extends ContainerBaseMachine {

  final static int HOTBAR_START = 27;
  final static int HOTBAR_END = 35;
  final static int INV_START = 0;
  final static int INV_END = 26;
  public final EntityVillager merchant;
  private MerchantRecipeList trades;
  private final InventoryMerchantAlmanac merchantInventory;
  private EntityPlayer player;

  public ContainerMerchantAlmanac(InventoryPlayer playerInventory, EntityVillager m, InventoryMerchantAlmanac im, World worldIn) {
    this.setScreenSize(ScreenSize.LARGEWIDE);
    this.merchant = m;
    this.merchantInventory = im;
    player = playerInventory.player;
    trades = merchant.getRecipes(player);
    bindPlayerInventory(playerInventory);
    this.detectAndSendChanges();
  }

  public void setCareer(int c) {
    UtilEntity.setVillagerCareer(merchant, c);
  }

  public InventoryMerchantAlmanac getMerchantInventory() {
    return this.merchantInventory;
  }

  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.merchantInventory);
  }

  @Override
  public void detectAndSendChanges() {
    merchantInventory.markDirty();
    super.detectAndSendChanges();
    if (player instanceof EntityPlayerMP && player.openContainer instanceof ContainerMerchantAlmanac) {
      MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(player);
      EntityPlayerMP mp = (EntityPlayerMP) player;
      ModCyclic.network.sendTo(new PacketSyncVillagerToClient(this.getCareer(), merchantrecipelist), mp);
    }
  }

  private int getCareer() {
    return UtilEntity.getVillagerCareer(merchant);
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventoryIn) {
    this.merchantInventory.resetRecipeAndSlots();
    super.onCraftMatrixChanged(inventoryIn);
  }

  public void setCurrentRecipeIndex(int currentRecipeIndex) {
    this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.merchant.getCustomer() == playerIn;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
    super.onContainerClosed(playerIn);
    this.merchant.setCustomer((EntityPlayer) null);
    super.onContainerClosed(playerIn);
  }

  public void setTrades(MerchantRecipeList t) {
    this.trades = t;
    this.merchant.setRecipes(t);
    this.merchantInventory.setRecipes(t);
  }

  public MerchantRecipeList getTrades() {
    return trades;
  }

  public void doTrade(EntityPlayer player, int selectedMerchantRecipe) {
    MerchantRecipe trade = getTrades().get(selectedMerchantRecipe);
    if (trade.isRecipeDisabled()) {
      // trade has red x
      return;
    }
    ItemStack itemToBuy = trade.getItemToBuy().copy();
    ItemStack itemSecondBuy = (trade.getSecondItemToBuy().isEmpty()) ? ItemStack.EMPTY : trade.getSecondItemToBuy().copy();
    ItemStack firstItem = ItemStack.EMPTY;
    ItemStack secondItem = ItemStack.EMPTY;
    int firstSlot = -1, secondSlot = -1;
    ItemStack iStack = ItemStack.EMPTY;
    boolean canTrade = false;
    for (int i = 0; i <= player.inventory.getSizeInventory(); i++) {
      iStack = player.inventory.getStackInSlot(i);
      if (iStack.isEmpty()) {
        continue;
      }
      if (firstItem.isEmpty() &&
          iStack.getItem() == itemToBuy.getItem() && iStack.getCount() >= itemToBuy.getCount()) {
        firstItem = iStack;
        firstSlot = i;
      }
      if (secondItem.isEmpty() && !itemSecondBuy.isEmpty()) {
        if (itemSecondBuy.getItem() == iStack.getItem() && iStack.getCount() >= itemSecondBuy.getCount()) {
          secondItem = iStack;
          secondSlot = i;
        }
      }
      canTrade = (!firstItem.isEmpty() && (itemSecondBuy.isEmpty() || !secondItem.isEmpty()));
      if (canTrade) {
        break;
      }
    }
    boolean tradeSuccess = false;
    if (canTrade) {
      //swap the items by counts
      if (!secondItem.isEmpty()) {
        firstItem.shrink(itemToBuy.getCount());
        secondItem.shrink(itemSecondBuy.getCount());
        tradeSuccess = true;
      }
      if (itemSecondBuy.isEmpty() && secondItem.isEmpty()) {
        firstItem.shrink(itemToBuy.getCount());
        tradeSuccess = true;
      }
    }
    if (tradeSuccess) {
      //give purchase 
      ItemStack purchased = trade.getItemToSell().copy();
      player.entityDropItem(purchased, 1);
      this.merchant.useRecipe(trade);
      player.addStat(StatList.TRADED_WITH_VILLAGER);
      if (firstItem.getCount() == 0) {
        player.inventory.setInventorySlotContents(firstSlot, ItemStack.EMPTY);
      }
      if (!secondItem.isEmpty() && secondItem.getCount() == 0) {
        player.inventory.setInventorySlotContents(secondSlot, ItemStack.EMPTY);
      }
    }
  }
}
