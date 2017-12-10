package com.lothrazar.cyclicmagic.component.fluidplacer;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFluidPlacer extends TileEntityBaseMachineFluid implements ITickable {
  public TileEntityFluidPlacer() {
    super(Fluid.BUCKET_VOLUME);
  }
  @Override
  public void update() {
    if (this.isPowered() == false ||
        tank.getFluid() == null ||
        tank.getFluid().getFluid() == null) {
      return;
    }
    EnumFacing facingTo = this.getCurrentFacing().getOpposite();
    BlockPos posTarget = pos.offset(facingTo);
    if (world.isAirBlock(posTarget)==false) {
      return;
    }
    FluidStack maybeDrain = tank.drain(new FluidStack(tank.getFluid().getFluid(), Fluid.BUCKET_VOLUME), false);
    if (maybeDrain != null && maybeDrain.amount == Fluid.BUCKET_VOLUME) {
      Block fluidBlock = tank.getFluid().getFluid().getBlock();
      //if we can drain a full bucket, then do it and place!
      world.setBlockState(posTarget, fluidBlock.getDefaultState());
      tank.drain(new FluidStack(tank.getFluid().getFluid(), Fluid.BUCKET_VOLUME), true);
    }
  }
}
