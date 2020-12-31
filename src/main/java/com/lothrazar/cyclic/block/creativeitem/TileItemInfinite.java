package com.lothrazar.cyclic.block.creativeitem;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemInfinite extends TileEntityBase implements ITickableTileEntity {

  public TileItemInfinite() {
    super(TileRegistry.item_infinite);
  }

  int here = 0;
  int backup = 1;
  ItemStackHandler inventory = new ItemStackHandler(2) {

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if (slot == backup)//no extracting here allowed
        return ItemStack.EMPTY;
      return super.extractItem(slot, amount, simulate);
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    super.read(bs, tag);
    inventory.deserializeNBT(tag.getCompound(NBTINV));
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  //  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public void tick() {
    ItemStack stackHere = inventory.getStackInSlot(here);
    ItemStack stackBackup = inventory.getStackInSlot(backup);
    if (!stackHere.isEmpty() && stackBackup.isEmpty()) {
      //copy here to backup. backup never gets drained its always a fresh copy
      inventory.insertItem(backup, stackHere.copy(), false);
      return;
    }
    //take the backup, and overwrite whats here. if here is empty
    if (stackHere.isEmpty()) {
      stackBackup.setCount(64);
      inventory.extractItem(here, 64, false);
      inventory.insertItem(here, stackBackup.copy(), false);
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
