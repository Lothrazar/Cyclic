package com.lothrazar.cyclicmagic.block.base;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.wrapper.InvWrapper;

public class InvWrapperRestricted extends InvWrapper {
  private List<Integer> slotsAllowedInsert;
  private List<Integer> slotsAllowedExtract;
  public InvWrapperRestricted(ISidedInventory inv) {
    super(inv);
    slotsAllowedInsert = null;
    slotsAllowedExtract = null;
  }
  public List<Integer> getSlotsExtract() {
    return slotsAllowedExtract;
  }
  public void setSlotsExtract(List<Integer> slotsExport) {
    this.slotsAllowedExtract = slotsExport;
  }
  public List<Integer> getSlotsInsert() {
    return slotsAllowedInsert;
  }
  public void setSlotsInsert(List<Integer> slotsImport) {
    this.slotsAllowedInsert = slotsImport;
  }
  private boolean canInsert(int slot) {
    if (this.getSlotsInsert() == null ||
        this.getSlotsInsert().contains(slot)) {
      return true;
    }
    return false;
  }
  private boolean canExtract(int slot) {
    if (this.getSlotsExtract() == null ||
        this.getSlotsExtract().contains(slot)) {
      return true;
    }
    return false;
  }
  @Override
  @Nonnull
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (canInsert(slot) == false) {
      return stack;
    }
    return super.insertItem(slot, stack, simulate);
  }
  @Override
  @Nonnull
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (canExtract(slot) == false) {
      return ItemStack.EMPTY;
    }
    return super.extractItem(slot, amount, simulate);
  }
}
