package com.lothrazar.cyclic.block.facade;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ITileFacade {

  public static final String NBT_FACADE = "facade";

  CompoundTag getFacade();

  void setFacade(CompoundTag facadeState);

  default BlockState getFacadeState(Level level) {
    CompoundTag facadeState = getFacade();
    if (level == null || facadeState == null || facadeState.isEmpty()) {
      return null; // level is null on world load 
    }
    BlockState stateFound = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), facadeState);
    return stateFound;
  }

  default void loadFacade(CompoundTag compound) {
    if (compound.contains(NBT_FACADE)) {
      this.setFacade(compound.getCompound(NBT_FACADE));
    }
    else {
      this.setFacade(null);
    }
  }

  default void saveFacade(CompoundTag compound) {
    var fac = getFacade();
    if (fac != null) {
      compound.put(NBT_FACADE, fac);
    }
  }
}
