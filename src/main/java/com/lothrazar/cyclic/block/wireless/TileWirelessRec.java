package com.lothrazar.cyclic.block.wireless;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class TileWirelessRec extends TileEntityBase implements ITickableTileEntity {

  public TileWirelessRec() {
    super(BlockRegistry.Tiles.wireless_receiver);
  }

  @Override
  public void read(CompoundNBT tag) {
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    return super.write(tag);
  }

  @Override
  public void tick() {}

  @Override
  public void setField(int field, int value) {}
}
