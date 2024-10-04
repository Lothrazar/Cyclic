package com.lothrazar.cyclic.block.cable;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.facade.ITileFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CableTileBase extends TileBlockEntityCyclic implements ITileFacade {

  public CableTileBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
    super(tileEntityTypeIn, pos, state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.loadFacade(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    this.saveFacade(tag);
    super.saveAdditional(tag);
  }

  private CompoundTag facadeState = null;

  @Override
  public CompoundTag getFacade() {
    return facadeState;
  }

  @Override
  public void setFacade(CompoundTag facadeState) {
    this.facadeState = facadeState;
  }
}
