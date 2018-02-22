package com.lothrazar.cyclicmagic.component.pump.fluid;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.component.pump.item.TileEntityItemPump.Fields;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;

public class TileEntityFluidPump extends TileEntityBaseMachineFluid implements ITickable, ITileRedstoneToggle {
  private static final int TRANSFER_PER_TICK = 100;
  public static enum Fields {
    REDSTONE;
  }
  private int needsRedstone = 0;
  public TileEntityFluidPump() {
    super(Fluid.BUCKET_VOLUME);
  }
  @Override
  public EnumFacing getCurrentFacing() {
    //TODO: same as item pump so pump base class!?!?
    EnumFacing facingTo = super.getCurrentFacing();
    if (facingTo.getAxis().isVertical()) {
      facingTo = facingTo.getOpposite();
    }
    return facingTo;
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
    if (this.isPowered() == false && this.onlyRunIfPowered()) {
      return;//i am not powered, and i require it
    }
//    BlockPos posSide;
    EnumFacing importFromSide = this.getCurrentFacing();
    EnumFacing exportToSide = importFromSide.getOpposite();
    // IMPORT
 
      //ModCyclic.logger.log("I am pulling liquid out from "+side.name()+" I currently hold "+this.tank.getFluidAmount());
//      posSide = pos.offset(importFromSide);
      UtilFluid.tryFillTankFromPosition(world, pos.offset(importFromSide), importFromSide.getOpposite(), tank, TRANSFER_PER_TICK);
 
    //eXPORT: now try to DEPOSIT fluid next door
    boolean outputSuccess = UtilFluid.tryFillPositionFromTank(world, pos.offset(exportToSide), exportToSide.getOpposite(), tank, TRANSFER_PER_TICK);
    if (outputSuccess && world.getTileEntity(pos.offset(exportToSide)) instanceof TileEntityCableBase) {
      //TODO: not so compatible with other fluid systems. itl do i guess
      TileEntityCableBase cable = (TileEntityCableBase) world.getTileEntity(pos.offset(exportToSide));
      if (cable.isFluidPipe())
        cable.updateIncomingFluidFace(exportToSide.getOpposite());
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    needsRedstone = compound.getInteger(NBT_REDST);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, needsRedstone);
    return super.writeToNBT(compound);
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
