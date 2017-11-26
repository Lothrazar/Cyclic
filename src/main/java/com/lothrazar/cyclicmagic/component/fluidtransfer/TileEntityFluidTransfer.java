package com.lothrazar.cyclicmagic.component.fluidtransfer;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.fluid.FluidTankFixDesync;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFluidTransfer extends TileEntityBaseMachineInvo implements IFluidHandler, ITickable {
  private static final int TRANSFER_PER_TICK = 50;
  public TileEntityFluidTransfer(int invoSize) {
    super(invoSize);
    // TODO Auto-generated constructor stub
  }
  public static final String NBT_ID = "buckets";
  public static final int TANK_FULL = Fluid.BUCKET_VOLUME;
  private FluidTank tank;
  public TileEntityFluidTransfer() {
    super(0);
    tank = new FluidTankFixDesync(TANK_FULL, this);
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
    }
    this.world.markChunkDirty(pos, this);
    return super.getCapability(capability, facing);
  }
  public static class ContainerDummy extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
  }
  public FluidStack getCurrentFluidStack() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return null;
    }
    return fluidHandler.getTankProperties()[0].getContents();
  }
  @Override
  public IFluidTankProperties[] getTankProperties() {
    FluidTankInfo info = tank.getInfo();
    return new IFluidTankProperties[] { new FluidTankProperties(info.fluid, info.capacity, true, true) };
  }
  private boolean doesFluidMatchTank(FluidStack incoming) {
    if (tank.getFluid() == null) {
      return true;
    }
    return tank.getFluid().getFluid() == incoming.getFluid();
  }
  @Override
  public int fill(FluidStack resource, boolean doFill) {
    if (resource == null || doesFluidMatchTank(resource) == false) {
      return 0;
    }
    if (resource.amount + tank.getFluidAmount() > TANK_FULL) {//enForce limit
      resource.amount = TANK_FULL - tank.getFluidAmount();
    }
    int result = tank.fill(resource, doFill);
    tank.setFluid(resource);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    if (doesFluidMatchTank(resource) == false) {
      return resource;
    }
    FluidStack result = tank.drain(resource, doDrain);
    tank.setFluid(resource);
    return result;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    tank.setFluid(result);
    return result;
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    tank.readFromNBT(tagCompound.getCompoundTag(NBT_TANK));
  }
  /**
   * fix fluid rendering breaks because pipes and pumps update my fluid level only client side
   * 
   * @param fluid
   */
  @SideOnly(Side.CLIENT)
  public void updateFluidTo(FluidStack fluid) {
    this.tank.setFluid(fluid);
  }
  /**
   * for every side connected to me pull fluid in from it UNLESS its my current facing direction. for THAT side, i push fluid out from me pull first then push
   *
   *TODO: UtilFluid that does a position, a facing, and tries to move fluid across
   *
   *
   */
  @Override
  public void update() {
    BlockPos posSide;
    EnumFacing facingTo = this.getCurrentFacing();
    ModCyclic.logger.log("I am facing"+facingTo.name());
    for (EnumFacing side : EnumFacing.values()) {
      if (side == facingTo) {
        continue;
      }
      EnumFacing sideOpp = side.getOpposite();
     ModCyclic.logger.log("I am pulling liquid out from "+side.name()+" I currently hold "+this.tank.getFluidAmount());

   posSide = pos.offset(side);
     UtilFluid.tryFillTankFromPosition(world, posSide, sideOpp, tank, TRANSFER_PER_TICK);
     
     
 
    }
    //looping is over. now try to DEPOSIT fluid next door
    
    

    IFluidHandler fluidTo = FluidUtil.getFluidHandler(world, pos, facingTo);
    
    
    
  }
}
