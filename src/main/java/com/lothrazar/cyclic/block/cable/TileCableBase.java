package com.lothrazar.cyclic.block.cable;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.library.core.ITileFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;

import java.util.Map;

public abstract class TileCableBase extends TileBlockEntityCyclic implements ITileFacade {

  protected final Map<Direction, Integer> mapIncomingEnergy = Maps.newHashMap();
  protected int energyLastSynced = -1; //fluid tanks have 'onchanged', energy caps do not
  public TileCableBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
    super(tileEntityTypeIn, pos, state);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    this.loadFacade(tag);
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    this.saveFacade(tag);
    super.saveAdditional(tag, registries);
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
