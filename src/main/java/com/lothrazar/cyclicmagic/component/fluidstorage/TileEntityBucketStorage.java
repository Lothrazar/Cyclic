package com.lothrazar.cyclicmagic.component.fluidstorage;
import com.lothrazar.cyclicmagic.component.fluidtransfer.TileEntityFluidTransfer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityBucketStorage extends TileEntityFluidTransfer implements IFluidHandler {
  public TileEntityBucketStorage() {
    super(Fluid.BUCKET_VOLUME * 64);
  }
}
