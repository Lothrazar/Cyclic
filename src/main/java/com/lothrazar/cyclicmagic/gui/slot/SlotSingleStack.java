package com.lothrazar.cyclicmagic.gui.slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotSingleStack extends Slot {
  public SlotSingleStack(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }
  @Override
  public int getSlotStackLimit() {
    return 1;
  }
}
