package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileWirelessRec extends TileEntityBase implements TickableBlockEntity {

  public TileWirelessRec() {
    super(TileRegistry.wireless_receiver);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    super.load(bs, tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    return super.save(tag);
  }

  @Override
  public void tick() {}

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
