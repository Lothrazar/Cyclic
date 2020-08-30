package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemInfinite extends TileEntityBase implements ITickableTileEntity {

  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  public TileItemInfinite() {
    super(TileRegistry.item_infinite);
  }

  int here = 0;
  int backup = 1;

  private IItemHandler createHandler() {
    return new ItemStackHandler(2) {

      @Override
      public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == backup)//no extracting here allowed
          return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
      }
    };
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    super.read(bs, tag);
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  //  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public void tick() {
    inventory.ifPresent(h -> {
      ItemStack stackHere = h.getStackInSlot(here);
      //      ItemStack stackBackup = h.getStackInSlot(backup);
      //      if (stackBackup.isEmpty()) {
      //        //copy here to backup. backup never gets drained its always a fresh copy
      //        h.insertItem(backup, stackHere, false);
      //        return;
      //      }
      //      //take the backup, and overwrite whats here. if here is empty
      //      if (stackHere.isEmpty()) {
      //        stackBackup.setCount(64);
      //        h.extractItem(here, 64, false);
      //        h.insertItem(here, stackBackup.copy(), false);
      //      }
    });
  }

  @Override
  public void setField(int field, int value) {}
}
