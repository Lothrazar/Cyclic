package com.lothrazar.cyclicmagic.component.playerext.storage;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBase;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPlayerExtended extends ContainerBase {
  public InventoryPlayerExtended inventory;
  public static final int SQ = Const.SQ;
  public static final int HOTBAR_SIZE = Const.HOTBAR_SIZE;
  final int pad = Const.PAD;
  public ContainerPlayerExtended(InventoryPlayer playerInv, InventoryPlayerExtended eInvo, EntityPlayer player) {
    inventory = eInvo;
    inventory.setEventHandler(this);
    if (!player.getEntityWorld().isRemote) {
      UtilPlayerInventoryFilestorage.putDataIntoInventory(inventory, player);
    }
    int xPos, yPos, sl;
    for (int i = 0; i < InventoryPlayerExtended.IROW; ++i) {
      for (int j = 0; j < InventoryPlayerExtended.ICOL; ++j) {
        xPos = pad + j * SQ;
        yPos = pad + i * SQ;
        sl = j + (i + 1) * InventoryPlayerExtended.ICOL;
        this.addSlotToContainer(new Slot(inventory, sl, xPos, yPos));
      }
    }
    for (int i = 0; i < Const.ROWS_VANILLA; ++i) {
      for (int j = 0; j < Const.COLS_VANILLA; ++j) {
        xPos = pad + j * SQ;
        yPos = 84 + i * SQ;
        sl = j + (i + 1) * Const.COLS_VANILLA;
        this.addSlotToContainer(new Slot(playerInv, sl, xPos, yPos));
      }
    }
    yPos = 142;
    for (int i = 0; i < Const.HOTBAR_SIZE; ++i) {
      xPos = pad + i * SQ;
      sl = i;
      this.addSlotToContainer(new Slot(playerInv, sl, xPos, yPos));
    }
  }
  /**
   * Called when the container is closed.
   */
  @Override
  public void onContainerClosed(EntityPlayer player) {
    super.onContainerClosed(player);
    if (!player.getEntityWorld().isRemote) {
      UtilPlayerInventoryFilestorage.setPlayerInventory(player, inventory);
    }
  }
  /**
   * Called when a player shift-clicks on a slot. You must override this or you
   * will crash when someone does that.
   */
  @Override
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int iSlot) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slot = (Slot) this.inventorySlots.get(iSlot);
    int playerStart = 36, playerEnd = 63, topStart = 0, topEnd = 35, hotbarStart = 63, hotbarEnd = 72;
    if (slot != null && slot.getHasStack()) {
      ItemStack stackInSlot = slot.getStack();
      stack = stackInSlot.copy();
      if (playerStart <= iSlot && iSlot < playerEnd) {
        if (!this.mergeItemStack(stackInSlot, topStart, topEnd, false)) {
          return ItemStack.EMPTY;
        }
      }
      else if (topStart <= iSlot && iSlot < topEnd) {
        if (!this.mergeItemStack(stackInSlot, playerStart, playerEnd, false)) {
          return ItemStack.EMPTY;
        }
      }
      else if (hotbarStart <= iSlot && iSlot < hotbarEnd) {
        if (!this.mergeItemStack(stackInSlot, topStart, topEnd, false)) {
          return ItemStack.EMPTY;
        }
      }
      if (stackInSlot.getCount() == 0) {
        slot.putStack(ItemStack.EMPTY);
      }
      else {
        slot.onSlotChanged();
      }
      if (stackInSlot.getCount() == stack.getCount()) {
        return ItemStack.EMPTY;
      }
      slot.onTake(par1EntityPlayer, stackInSlot);
    }
    return stack;
  }
  protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, Slot ss) {
    boolean flag1 = false;
    int k = par2;
    if (par4) {
      k = par3 - 1;
    }
    Slot slot;
    ItemStack itemstack1;
    if (par1ItemStack.isStackable()) {
      while (par1ItemStack.getCount() > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
        slot = (Slot) this.inventorySlots.get(k);
        itemstack1 = slot.getStack();
        if (!itemstack1.isEmpty() && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1)) {
          int l = itemstack1.getCount() + par1ItemStack.getCount();
          if (l <= par1ItemStack.getMaxStackSize()) {
            // if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
            par1ItemStack.setCount(0);
            itemstack1.setCount(l);
            slot.onSlotChanged();
            flag1 = true;
          }
          else if (itemstack1.getCount() < par1ItemStack.getMaxStackSize()) {
            // if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
            par1ItemStack.setCount(par1ItemStack.getCount() - par1ItemStack.getMaxStackSize() - itemstack1.getCount());
            itemstack1.setCount(par1ItemStack.getMaxStackSize());
            slot.onSlotChanged();
            flag1 = true;
          }
        }
        if (par4) {
          --k;
        }
        else {
          ++k;
        }
      }
    }
    if (par1ItemStack.getCount() > 0) {
      if (par4) {
        k = par3 - 1;
      }
      else {
        k = par2;
      }
      while (!par4 && k < par3 || par4 && k >= par2) {
        slot = (Slot) this.inventorySlots.get(k);
        itemstack1 = slot.getStack();
        if (itemstack1 == null || itemstack1.isEmpty()) {
          // if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
          slot.putStack(par1ItemStack.copy());
          slot.onSlotChanged();
          par1ItemStack.setCount(0);
          flag1 = true;
          break;
        }
        if (par4) {
          --k;
        }
        else {
          ++k;
        }
      }
    }
    return flag1;
  }
}
