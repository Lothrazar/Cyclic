package com.lothrazar.cyclicmagic.component.fluidstorage;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import net.minecraftforge.fluids.Fluid;

public class TileEntityBucketStorage extends TileEntityBaseMachineFluid {
  public TileEntityBucketStorage() {
    super(Fluid.BUCKET_VOLUME * 64);
  }
}
