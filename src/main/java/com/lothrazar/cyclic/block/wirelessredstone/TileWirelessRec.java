package com.lothrazar.cyclic.block.wirelessredstone;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class TileWirelessRec extends TileEntityBase implements ITickableTileEntity {

  public TileWirelessRec() {
    super(TileRegistry.wireless_receiver);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    super.read(bs, tag);
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
