package com.lothrazar.cyclicmagic.component.controlledminer;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.SlotSingleStack;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMinerSmart extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = Const.PAD;
  public static final int SLOTY = 92;
  public static final int SLOTID_EQUIP = TileEntityControlledMiner.TOOLSLOT_INDEX;
  public static final int SLOTEQUIP_X = SLOTX_START + (SLOTID_EQUIP + 2) * Const.SQ;
  public static final int SLOTEQUIP_Y = SLOTY;
  public ContainerMinerSmart(InventoryPlayer inventoryPlayer, TileEntityControlledMiner te) {
    tile = te;
    this.screenSize = ScreenSize.LARGE;
    this.setTile(te);
    for (int i = 0; i < SLOTID_EQUIP; i++) {
      addSlotToContainer(new SlotSingleStack(tile, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
    addSlotToContainer(new SlotSingleStack(tile, SLOTID_EQUIP, SLOTEQUIP_X, SLOTEQUIP_Y));
    addSlotToContainer(new SlotFurnaceFuel(tile, 5, SLOTX_FUEL, SLOTY_FUEL));
    
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
      if (slot < tile.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tile.getSizeInventory(), 36 + tile.getSizeInventory(), true)) { return ItemStack.EMPTY; }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory(), false)) { return ItemStack.EMPTY; }
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
    this.tile.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tile);
  }
}
