package com.lothrazar.cyclic.block.facade.light;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.core.ITileFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TileLightFacade extends TileBlockEntityCyclic implements ITileFacade {

  public TileLightFacade(BlockPos pos, BlockState state) {
    super(TileRegistry.LIGHT_CAMO.get(), pos, state);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag syncData = super.getUpdateTag(registries);
    return syncData;
  }

  @Override
  public void loadAdditional(ValueInput input) {
    CompoundTag facadeTag = new CompoundTag();
    input.read(NBT_FACADE, CompoundTag.CODEC).ifPresent(t -> facadeTag.put(NBT_FACADE, t));
    this.loadFacade(facadeTag);
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    CompoundTag facadeTag = new CompoundTag();
    this.saveFacade(facadeTag);
    if (facadeTag.contains(NBT_FACADE)) {
      output.store(NBT_FACADE, CompoundTag.CODEC, facadeTag.getCompoundOrEmpty(NBT_FACADE));
    }
    super.saveAdditional(output);
  }

  @Override
  public void setField(int field, int value) {
  }

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
  public void setFacade(CompoundTag facadeStateIn) {
    this.facadeState = facadeStateIn;
  }
}
