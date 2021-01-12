package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.TileRegistry;
import javax.annotation.Nonnull;
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

  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlot = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlot);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

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
    ItemStack stackHere = inputSlots.getStackInSlot(0);
    ItemStack stackBackup = outputSlot.getStackInSlot(0);
    if (!stackHere.isEmpty() && stackBackup.isEmpty()) {
      //copy here to backup. backup never gets drained its always a fresh copy
      outputSlot.insertItem(0, stackHere.copy(), false);
      return;
    }
    //take the backup, and overwrite whats here. if here is empty
    //    if (stackHere.isEmpty()) {
    //      stackBackup.setCount(64);
    //      inputSlots.extractItem(0, 64, false);
    //      inputSlots.insertItem(0, stackBackup.copy(), false);
    //    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
