package com.lothrazar.cyclic.capabilities.livingentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class LivingEntityCapabilityStorage {

  BlockPos closestAntiBeaconPosition = null;

  public LivingEntityCapabilityStorage() {}

  public BlockPos getClosestAntiBeaconPosition() {
    return closestAntiBeaconPosition;
  }

  public void setClosestAntiBeaconPosition(BlockPos closestAntiBeaconPosition) {
    this.closestAntiBeaconPosition = closestAntiBeaconPosition;
  }

  public void saveNBTData(CompoundTag compound) {
    if (closestAntiBeaconPosition != null) {
      compound.put("closestAntiBeaconPosition", NbtUtils.writeBlockPos(closestAntiBeaconPosition));
    }
  }

  public void loadNBTData(CompoundTag compound) {
    if (compound.contains("closestAntiBeaconPosition")) {
      closestAntiBeaconPosition = NbtUtils.readBlockPos(compound.getCompound("closestAntiBeaconPosition"));
    }
  }
}