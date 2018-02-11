package com.lothrazar.cyclicmagic.component.fluidstorage;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;

public class TileEntityBucketStorage extends TileEntityBaseMachineFluid implements ITickable {
  public TileEntityBucketStorage() {
    super(Fluid.BUCKET_VOLUME * 64);
  }
  @Override
  public void update() {
    //drain below but only to one of myself
    TileEntity below = this.world.getTileEntity(this.pos.down());
    if (below != null && below instanceof TileEntityBucketStorage) {
      UtilFluid.tryFillPositionFromTank(world, this.pos.down(), EnumFacing.UP, tank, TileEntityCableBase.TRANSFER_FLUID_PER_TICK);
    }
  }
}
