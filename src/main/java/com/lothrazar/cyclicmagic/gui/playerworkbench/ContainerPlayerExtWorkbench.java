package com.lothrazar.cyclicmagic.gui.playerworkbench;

import com.lothrazar.cyclicmagic.gui.ContainerBase;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPlayerExtWorkbench extends ContainerBase {
  public InventoryPlayerExtWorkbench craftMatrix;

  private final EntityPlayer thePlayer;
//  private static final EntityEquipmentSlot[] ARMOR = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
  public static final int SLOT_SHIELD = 40;
  public static final int SQ = 18;
  public static final int VROW = 3;
  public static final int VCOL = 9;
  public static final int HOTBAR_SIZE = 9;
  public IInventory craftResult = new InventoryCraftResult();
  final int pad = 8;
  public ContainerPlayerExtWorkbench(InventoryPlayer playerInv, EntityPlayer player) {

    this.thePlayer = player;
    craftMatrix = new InventoryPlayerExtWorkbench(this, player);
  
    if (!player.worldObj.isRemote) {
     // UtilPlayerInventoryFilestorage.putDataIntoInventory(inventory, player);
      //      inventory.stackList = UtilPlayerInventoryFilestorage.getPlayerInventory(player).stackList;
    }
//    for (int k = 0; k < ARMOR.length; k++) {
//      final EntityEquipmentSlot slot = ARMOR[k];
//      this.addSlotToContainer(new Slot(playerInv, 4 * VCOL + (VROW - k), pad, pad + k * SQ) {
//        @Override
//        public int getSlotStackLimit() {
//          return 1;
//        }
//        @Override
//        public boolean isItemValid(ItemStack stack) {
//          if (stack == null) {
//            return false;
//          }
//          else {
//            return stack.getItem().isValidArmor(stack, slot, thePlayer);
//          }
//        }
//        @Override
//        @SideOnly(Side.CLIENT)
//        public String getSlotTexture() {
//          return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
//        }
//      });
//    }
    //the output
//    this.addSlotToContainer(new Slot(player, craftingMatrix, craftingResult, 0, 136, 35));
    this.addSlotToContainer(new SlotCrafting(player,craftMatrix,craftResult, 0,136,35));

    
    int xPos, yPos, sl;
    for (int i = 0; i < InventoryPlayerExtWorkbench.IROW; ++i) {
      for (int j = 0; j < InventoryPlayerExtWorkbench.ICOL; ++j) {
        xPos = pad + (j + 1) * SQ;
        yPos = pad + i * SQ;
        sl = j + (i ) * InventoryPlayerExtWorkbench.ICOL;
        this.addSlotToContainer(new Slot(craftMatrix, sl, xPos, yPos));
      }
    }
    for (int i = 0; i < VROW; ++i) {
      for (int j = 0; j < VCOL; ++j) {
        xPos = pad + j * SQ;
        yPos = 84 + i * SQ;
        sl = j + (i + 1) * HOTBAR_SIZE;
        this.addSlotToContainer(new Slot(playerInv, sl, xPos, yPos));
      }
    }
    yPos = 142;
    for (int i = 0; i < HOTBAR_SIZE; ++i) {
      xPos = pad + i * SQ;
      sl = i;
      this.addSlotToContainer(new Slot(playerInv, sl, xPos, yPos));
    }

    this.onCraftMatrixChanged(craftMatrix);
  }
  @Override
  public void onCraftMatrixChanged(IInventory inventory) {
      craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, this.thePlayer.worldObj));
  }

  /**
   * Called when the container is closed.
   */
  @Override
  public void onContainerClosed(EntityPlayer player) {
    super.onContainerClosed(player);
    if (!player.worldObj.isRemote) {
//      UtilPlayerInventoryFilestorage.setPlayerInventory(player, inventory);
    }
  }
  /**
   * Called when a player shift-clicks on a slot. You must override this or you
   * will crash when someone does that.
   */
  @Override
  public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
      ItemStack itemStack = null;
      Slot fromSlot = (Slot) this.inventorySlots.get(slotIndex);

      // getHasStack - has stuff and things in it
      if (fromSlot != null && fromSlot.getHasStack()) {
          ItemStack itemStack1 = fromSlot.getStack();
          itemStack = itemStack1.copy();

          // If we are taking the crafting result...
          if (slotIndex == 0) {
              // I guess this is true if there isnt space in the players inventory for the result
              if (!this.mergeItemStack(itemStack1, 10, 46, false)) {
                  return null;
              }
              // I dont really understand what this is doing, at this point the stacks should be identical
              fromSlot.onSlotChange(itemStack1, itemStack);
          }

          // Move from the matrix into the inventory
          else if (slotIndex >= 1 && slotIndex <= 9) {
              if (!this.mergeItemStack(itemStack1, 10, 46, false)) {
                  fromSlot.onSlotChanged();
                  return null;
              }
          }

          // Now we should try to move from the inventory to the crafting matrix
          else if (slotIndex >= 10 && slotIndex < 46) {
              if (!this.mergeItemStack(itemStack1, 1, 10, false)) {
                  return null;
              }
          }

          // So at this point, itemstack1 only contains items which could not fit when moved
          if (itemStack1.stackSize == 0) {
              // Empty the from slot if all the items were moved out
              fromSlot.putStack((ItemStack) null);
          } else {
              // Still some items in the stack, let the slot know it has changed
              fromSlot.onSlotChanged();
          }

          // If stack1 and stack are the same size, then nothing could be moved
          if (itemStack.stackSize == itemStack1.stackSize) {
              return null;
          }

          fromSlot.onPickupFromSlot(this.thePlayer, itemStack1);
      }
      return itemStack;
  }

//  @Override
//  public void putStacksInSlots(ItemStack[] s) {
//    craftMatrix.blockEvents = true;
//    super.putStacksInSlots(s);
//  }
  protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, Slot ss) {
    boolean flag1 = false;
    int k = par2;
    if (par4) {
      k = par3 - 1;
    }
    Slot slot;
    ItemStack itemstack1;
    if (par1ItemStack.isStackable()) {
      while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
        slot = (Slot) this.inventorySlots.get(k);
        itemstack1 = slot.getStack();
        if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1)) {
          int l = itemstack1.stackSize + par1ItemStack.stackSize;
          if (l <= par1ItemStack.getMaxStackSize()) {
            // if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
            par1ItemStack.stackSize = 0;
            itemstack1.stackSize = l;
            slot.onSlotChanged();
            flag1 = true;
          }
          else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize()) {
            // if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
            par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
            itemstack1.stackSize = par1ItemStack.getMaxStackSize();
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
    if (par1ItemStack.stackSize > 0) {
      if (par4) {
        k = par3 - 1;
      }
      else {
        k = par2;
      }
      while (!par4 && k < par3 || par4 && k >= par2) {
        slot = (Slot) this.inventorySlots.get(k);
        itemstack1 = slot.getStack();
        if (itemstack1 == null) {
          // if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
          slot.putStack(par1ItemStack.copy());
          slot.onSlotChanged();
          par1ItemStack.stackSize = 0;
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
  /*
   * @Override public boolean canMergeSlot(ItemStack par1ItemStack, Slot
   * par2Slot) { return par2Slot.inventory != this.craftResult &&
   * super.canMergeSlot(par1ItemStack, par2Slot); }
   */
}
