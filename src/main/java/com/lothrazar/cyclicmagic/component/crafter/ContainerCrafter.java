package com.lothrazar.cyclicmagic.component.crafter;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotOutputOnly;
import com.lothrazar.cyclicmagic.gui.slot.SlotSingleStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCrafter extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  protected TileEntityCrafter tileEntity;
  public ContainerCrafter(InventoryPlayer inventoryPlayer, TileEntityCrafter te) {
    tileEntity = te;
    this.setTile(te);
    screenSize = ScreenSize.LARGE;
    int slot = 0;
    //inpt on left
    int xPrefix = Const.PAD, yPrefix = 27;
    int rows = TileEntityCrafter.ROWS;
    int cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new Slot(tileEntity, slot,
            xPrefix + j * Const.SQ,
            yPrefix + i * Const.SQ));
        slot++;
      }
    }
    //crafting in the middle
    rows = cols = 3;
    xPrefix = (screenSize.width() / 2 - (Const.SQ * 3) / 2);
    yPrefix = 40;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new SlotSingleStack(tileEntity, slot,
            xPrefix + j * Const.SQ,
            yPrefix + i * Const.SQ));
        slot++;
      }
    }
    //output on right
    xPrefix = 134;
    yPrefix = 27;
    rows = TileEntityCrafter.ROWS;
    cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new SlotOutputOnly(tileEntity, slot,
            xPrefix + j * Const.SQ,
            yPrefix + i * Const.SQ));
        slot++;
      }
    }
    super.addFurnaceFuelSlot(SLOTX_FUEL, SLOTY_FUEL);
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity
      if (slot < tileEntity.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return ItemStack.EMPTY; }
      }
      else if (TileEntityFurnace.isItemFuel(stack)) {
        //fuel slot
        if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory(), true)) { return ItemStack.EMPTY; }
        //        else if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory()-1, false)) { return ItemStack.EMPTY; }
      }
      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), true)) { return ItemStack.EMPTY; }
      if (stackInSlot.getCount() == 0) {
        slotObject.putStack(ItemStack.EMPTY);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.getCount() == stack.getCount()) { return ItemStack.EMPTY; }
      slotObject.onTake(player, stackInSlot);
    }
    return stack;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tileEntity.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tileEntity);
  }
}
