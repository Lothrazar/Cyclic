package com.lothrazar.cyclic.block.bedrock;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;

public class UnbreakablePoweredTile extends TileEntityBase  {

  public UnbreakablePoweredTile() {
    super(TileRegistry.unbreakable_reactive);
  }

//  @Override
  public void tick() {
    boolean isBreakable = !this.isPowered();
    UnbreakablePoweredBlock.setBreakable(level, worldPosition, isBreakable);
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
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
