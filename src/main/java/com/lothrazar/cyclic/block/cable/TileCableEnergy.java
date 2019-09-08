package com.lothrazar.cyclic.block.cable;

import com.lothrazar.cyclic.CyclicRegistry;
import net.minecraft.tileentity.TileEntity;

public class TileCableEnergy extends TileEntity {

  public TileCableEnergy() {
    super(CyclicRegistry.energy_pipeTile);
  }
}
