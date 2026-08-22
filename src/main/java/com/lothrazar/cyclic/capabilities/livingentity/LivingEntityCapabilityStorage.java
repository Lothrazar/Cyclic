package com.lothrazar.cyclic.capabilities.livingentity;

import net.minecraft.core.BlockPos;

public class LivingEntityCapabilityStorage {

  BlockPos closestAntiBeaconPosition = null;

  public LivingEntityCapabilityStorage() {
  }

  public BlockPos getClosestAntiBeaconPosition() {
    return closestAntiBeaconPosition;
  }

  public void setClosestAntiBeaconPosition(BlockPos closestAntiBeaconPosition) {
    this.closestAntiBeaconPosition = closestAntiBeaconPosition;
  }
}