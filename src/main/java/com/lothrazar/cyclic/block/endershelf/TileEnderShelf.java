package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEnderShelf extends TileEntityBase {

  final EnderShelfItemHandler inventory = new EnderShelfItemHandler(this);
  private final LazyOptional<EnderShelfItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private BlockPos controllerLocation;

  public TileEnderShelf() {
    super(TileRegistry.ender_shelf);
    this.controllerLocation = null;
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  public void setControllerLocation(BlockPos pos) {
    this.controllerLocation = pos;
  }

  public BlockPos getControllerLocation() {
    return this.controllerLocation;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }
}
