package com.lothrazar.cyclicmagic.component.fluidtransfer;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidTransfer extends TileEntityBaseMachineFluid implements ITickable {
  private static final int TRANSFER_PER_TICK = 100;
  public TileEntityFluidTransfer(int invoSize) {
    super(invoSize);
  }
  public TileEntityFluidTransfer() {
    super(Fluid.BUCKET_VOLUME);
  }
  /**
   * for every side connected to me pull fluid in from it UNLESS its my current facing direction. for THAT side, i push fluid out from me pull first then push
   *
   * TODO: UtilFluid that does a position, a facing, and tries to move fluid across
   *
   *
   */
  @Override
  public void update() {
    BlockPos posSide;
    EnumFacing facingTo = this.getCurrentFacing().getOpposite();
 
    for (EnumFacing side : EnumFacing.values()) {
      if (side == facingTo) {
        continue;
      }
      EnumFacing sideOpp = side.getOpposite();
      //ModCyclic.logger.log("I am pulling liquid out from "+side.name()+" I currently hold "+this.tank.getFluidAmount());
      posSide = pos.offset(side);
      UtilFluid.tryFillTankFromPosition(world, posSide, sideOpp, tank, TRANSFER_PER_TICK);
    }
    //looping is over. now try to DEPOSIT fluid next door
    boolean outputSuccess = UtilFluid.tryFillPositionFromTank(world, pos.offset(facingTo), facingTo.getOpposite(), tank, TRANSFER_PER_TICK);
    if (outputSuccess && world.getTileEntity(pos.offset(facingTo)) instanceof TileEntityFluidCable) {
      //TODO: not so compatible with other fluid systems. itl do i guess
      TileEntityFluidCable cable = (TileEntityFluidCable) world.getTileEntity(pos.offset(facingTo));
      cable.updateIncomingFace(facingTo.getOpposite());
    }
  }
}
