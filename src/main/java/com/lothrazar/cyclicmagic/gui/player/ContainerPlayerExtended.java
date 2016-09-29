package com.lothrazar.cyclicmagic.gui.player;
import com.lothrazar.cyclicmagic.gui.ContainerBase;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPlayerExtended extends ContainerBase {
  public InventoryPlayerExtended inventory;

  private final EntityPlayer thePlayer;
  private static final EntityEquipmentSlot[] ARMOR = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
  public static final int SLOT_SHIELD = 40;
  public static final int SQ = 18;
  public static final int VROW = 3;
  public static final int VCOL = 9;
  public static final int HOTBAR_SIZE = 9;
  final int pad = 8;
  public ContainerPlayerExtended(InventoryPlayer playerInv,  EntityPlayer player) {

    this.thePlayer = player;
    inventory = new InventoryPlayerExtended(player);
    inventory.setEventHandler(this);
    if (!player.worldObj.isRemote) {
      UtilPlayerInventoryFilestorage.putDataIntoInventory(inventory, player);
      //      inventory.stackList = UtilPlayerInventoryFilestorage.getPlayerInventory(player).stackList;
    }
    for (int k = 0; k < ARMOR.length; k++) {
      final EntityEquipmentSlot slot = ARMOR[k];
      this.addSlotToContainer(new Slot(playerInv, 4 * VCOL + (VROW - k), pad, pad + k * SQ) {
        @Override
        public int getSlotStackLimit() {
          return 1;
        }
        @Override
        public boolean isItemValid(ItemStack stack) {
          if (stack == null) {
            return false;
          }
          else {
            return stack.getItem().isValidArmor(stack, slot, thePlayer);
          }
        }
        @Override
        @SideOnly(Side.CLIENT)
        public String getSlotTexture() {
          return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
        }
      });
    }
    int xPos, yPos, sl;
    for (int i = 0; i < InventoryPlayerExtended.IROW; ++i) {
      for (int j = 0; j < InventoryPlayerExtended.ICOL; ++j) {
        xPos = pad + (j + 1) * SQ;
        yPos = pad + i * SQ;
        sl = j + (i + 1) * InventoryPlayerExtended.ICOL;
        this.addSlotToContainer(new Slot(inventory, sl, xPos, yPos));
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
  }
  /**
   * Called when the container is closed.
   */
  @Override
  public void onContainerClosed(EntityPlayer player) {
    super.onContainerClosed(player);
    if (!player.worldObj.isRemote) {
      UtilPlayerInventoryFilestorage.setPlayerInventory(player, inventory);
    }
  }
  /**
   * Called when a player shift-clicks on a slot. You must override this or you
   * will crash when someone does that.
   */
  @Override
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int iSlot) {
    ItemStack itemstack = null;
    Slot slot = (Slot) this.inventorySlots.get(iSlot);
    int playerStart = 36, playerEnd = 63, topStart = 4, topEnd = 36, hotbarStart = 63, hotbarEnd = 72, armorStart = 0, armorEnd = 4;
    //36 to 62 is lower
    //4 to 40 is bottom
    if (slot != null && slot.getHasStack()) {
      ItemStack copy = slot.getStack();
      itemstack = copy.copy();
      if (itemstack.getItem() instanceof ItemArmor) {
        //ItemArmor armor = (ItemArmor) copy.getItem();
        //int armorSlot = 8 - armor.armorType.getIndex();
        //if (!this.mergeItemStack(copy, armorSlot, armorSlot + 1, false)) { return null; }
        if (armorStart <= iSlot && iSlot < armorEnd) {
          if (!this.mergeItemStack(copy, playerStart, playerEnd, false)) { return null; }
        }
        else {
          if (!this.mergeItemStack(copy, 0, 4, false)) { return null; }
        }
      }
      else if (playerStart <= iSlot && iSlot < playerEnd) {
        if (!this.mergeItemStack(copy, topStart, topEnd, false)) { return null; }
      }
      else if (topStart <= iSlot && iSlot < topEnd) {
        if (!this.mergeItemStack(copy, playerStart, playerEnd, false)) { return null; }
      }
      else if (hotbarStart <= iSlot && iSlot < hotbarEnd) {
        if (!this.mergeItemStack(copy, topStart, topEnd, false)) { return null; }
      }
      if (copy.stackSize == 0) {
        slot.putStack((ItemStack) null);
      }
      else {
        slot.onSlotChanged();
      }
      if (copy.stackSize == itemstack.stackSize) { return null; }
      slot.onPickupFromSlot(par1EntityPlayer, copy);
    }
    return itemstack;
  }
  @Override
  public void putStacksInSlots(ItemStack[] s) {
    inventory.blockEvents = true;
    super.putStacksInSlots(s);
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
