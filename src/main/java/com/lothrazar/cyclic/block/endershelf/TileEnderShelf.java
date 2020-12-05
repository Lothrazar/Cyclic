package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class TileEnderShelf extends TileEntityBase {

  private LazyOptional<EnderShelfItemHandler> inventory = LazyOptional.of(() -> new EnderShelfItemHandler(this));
  private LazyOptional<EnderShelfItemHandler> fakeInventory = LazyOptional.of(() -> new EnderShelfItemHandler(this, true));

  public TileEnderShelf() {
    super(TileRegistry.ender_shelf);
  }

  @Override
  public void setField(int field, int value) {

  }

  @Override
  public int getField(int field) {
    return 0;
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (side == Direction.DOWN)
        return fakeInventory.cast();
      return inventory.cast();
    }

    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }
}
