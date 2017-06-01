package com.lothrazar.cyclicmagic.component.merchant;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class ContainerMerchantBetter extends ContainerBaseMachine {
  final static int HOTBAR_START = 27;
  final static int HOTBAR_END = 35;
  final static int INV_START = 0;
  final static int INV_END = 26;
  public final EntityVillager merchant;
  private MerchantRecipeList trades;
  private final InventoryMerchantBetter merchantInventory;
  private EntityPlayer player;
  public ContainerMerchantBetter(InventoryPlayer playerInventory, EntityVillager m, InventoryMerchantBetter im, World worldIn) {
    this.screenSize = ScreenSize.LARGEWIDE;
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
  public InventoryMerchantBetter getMerchantInventory() {
    return this.merchantInventory;
  }
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.merchantInventory);
  }
  public void detectAndSendChanges() {
    merchantInventory.markDirty();
    super.detectAndSendChanges();
    if (player instanceof EntityPlayerMP && player.openContainer instanceof ContainerMerchantBetter) {
      MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(player);
      EntityPlayerMP mp = (EntityPlayerMP) player;
      ModCyclic.network.sendTo(new PacketSyncVillagerToClient(this.getCareer(), merchantrecipelist), mp);
    }
  }
  private int getCareer() {
    return UtilEntity.getVillagerCareer(merchant);
  }
  public void onCraftMatrixChanged(IInventory inventoryIn) {
    this.merchantInventory.resetRecipeAndSlots();
    super.onCraftMatrixChanged(inventoryIn);
  }
  public void setCurrentRecipeIndex(int currentRecipeIndex) {
    this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
  }
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.merchant.getCustomer() == playerIn;
  }
  @Nullable
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = (Slot) this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      //      if (index == SLOT_OUTPUT) {
      //        if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true)) { return null; }
      //        slot.onSlotChange(itemstack1, itemstack);
      //      }
      //      else if (index != SLOT_INPUT && index != SLOT_INPUTX) { //so it must be a player slot
      //        if (!this.mergeItemStack(itemstack1, SLOT_INPUT, SLOT_INPUTX + 1, false)) { return null; }
      //      }
      //      else {//so it is 0,1
      if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, false)) { return ItemStack.EMPTY; }
      //      }
      //cleanup steps
      if (itemstack1.getCount() == 0) {
        slot.putStack(ItemStack.EMPTY);
      }
      else {
        slot.onSlotChanged();
      }
      if (itemstack1.getCount() == itemstack.getCount()) { return ItemStack.EMPTY; }
      slot.onTake(playerIn, itemstack1);
    }
    return itemstack;
  }
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
    if (trade.isRecipeDisabled()) { return; }
    ItemStack itemToBuy = trade.getItemToBuy().copy();
    ItemStack itemSecondBuy = (trade.getSecondItemToBuy() == ItemStack.EMPTY) ? ItemStack.EMPTY : trade.getSecondItemToBuy().copy();
    ItemStack firstItem = ItemStack.EMPTY;
    ItemStack secondItem = ItemStack.EMPTY;
    int firstSlot = -1, secondSlot = -1;
    ItemStack iStack = ItemStack.EMPTY;
    boolean canTrade = false;
    for (int i = 0; i <= 3 * 9; i++) {
      iStack = player.inventory.getStackInSlot(i);
      if (iStack == ItemStack.EMPTY) {
        continue;
      }
      if (firstItem == ItemStack.EMPTY &&
          iStack.getItem() == itemToBuy.getItem() && iStack.getCount() >= itemToBuy.getCount()) {
        firstItem = iStack;
        firstSlot = i;
      }
      if (secondItem == ItemStack.EMPTY && itemSecondBuy != ItemStack.EMPTY) {
        if (itemSecondBuy.getItem() == iStack.getItem() && iStack.getCount() >= itemSecondBuy.getCount()) {
          secondItem = iStack;
          secondSlot = i;
        }
      }
      canTrade = (firstItem != ItemStack.EMPTY && (itemSecondBuy == ItemStack.EMPTY || secondItem != ItemStack.EMPTY));
      if (canTrade) {
        break;
      }
    }
    boolean tradeSuccess = false;
    if (canTrade) {
      if (!secondItem.isEmpty()) {
        //        firstItem.stackSize -= itemToBuy.stackSize;
        //        secondItem.stackSize -= itemSecondBuy.stackSize;
        firstItem.shrink(itemToBuy.getCount());
        secondItem.shrink(itemSecondBuy.getCount());
        tradeSuccess = true;
      }
      if (itemSecondBuy.isEmpty() && secondItem.isEmpty()) {
        //        firstItem.stackSize -= itemToBuy.stackSize;
        firstItem.shrink(itemToBuy.getCount());
        tradeSuccess = true;
      }
    }
    if (tradeSuccess) {
      ItemStack purchased = trade.getItemToSell().copy();
      player.entityDropItem(purchased, 0);
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
