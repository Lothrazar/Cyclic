package com.lothrazar.cyclic.block.facade.light;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.facade.ITileFacade;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileLightFacade extends TileBlockEntityCyclic implements ITileFacade {

  public TileLightFacade(BlockPos pos, BlockState state) {
    super(TileRegistry.LIGHT_CAMO.get(), pos, state);
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

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
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
