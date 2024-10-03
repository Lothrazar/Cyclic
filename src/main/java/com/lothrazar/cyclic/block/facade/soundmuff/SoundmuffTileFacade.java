package com.lothrazar.cyclic.block.facade.soundmuff;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.facade.ITileFacade;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SoundmuffTileFacade extends TileBlockEntityCyclic implements ITileFacade {

  public SoundmuffTileFacade(BlockPos pos, BlockState state) {
    super(TileRegistry.SOUNDPROOFING_GHOST.get(), pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    this.loadFacade(tag);
    super.load(tag);
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
