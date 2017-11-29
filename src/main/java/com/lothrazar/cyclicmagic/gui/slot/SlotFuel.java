package com.lothrazar.cyclicmagic.gui.slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

public class SlotFuel extends SlotFurnaceFuel {
  public SlotFuel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
    super(inventoryIn, slotIndex, xPosition, yPosition);
  }
  @Override
  public boolean isItemValid(ItemStack stack) {
    return stack.hasCapability(CapabilityEnergy.ENERGY, null) || super.isItemValid(stack);
  }
}
