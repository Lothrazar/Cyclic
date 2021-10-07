package com.lothrazar.cyclic.item.datacard;

import net.minecraft.world.level.block.state.BlockState;

public class BlockStateMatcher {

  private BlockState state;
  private boolean exactProperties = true;

  public boolean doesMatch(BlockState other) {
    return false;
  }

  public BlockState getState() {
    return state;
  }

  public void setState(BlockState state) {
    this.state = state;
  }

  public boolean isExactProperties() {
    return exactProperties;
  }

  public void setExactProperties(boolean exactProperties) {
    this.exactProperties = exactProperties;
  }
}
