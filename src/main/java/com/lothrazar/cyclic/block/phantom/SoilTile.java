package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.ticket.AABBTicket;

public class SoilTile extends TileEntityBase {

  public static final int HEIGHT = 1;
  public static final int RANGE = 4;
  private AABBTicket farmWater;

  public SoilTile() {
    super(TileRegistry.SOIL.get());
  }

  @Override
  public void onLoad() {
    if (!level.isClientSide) {
      AABB box = new AABB(worldPosition);
      farmWater = FarmlandWaterManager.addAABBTicket(level, box.inflate(RANGE, HEIGHT, RANGE));
      farmWater.validate();
    }
  }

  @Override
  public void onChunkUnloaded() {
    if (!level.isClientSide && farmWater != null) {
      farmWater.invalidate();
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
