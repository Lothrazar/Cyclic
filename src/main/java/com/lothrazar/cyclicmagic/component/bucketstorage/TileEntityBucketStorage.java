package com.lothrazar.cyclicmagic.component.bucketstorage;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBucketStorage extends TileEntityBaseMachineInvo implements IFluidHandler {
  public static final String NBT_ID = "buckets";
  public static final int TANK_FULL = Fluid.BUCKET_VOLUME * 64;//yep 64  
  private FluidTank tank;
  public TileEntityBucketStorage() {
    super(0);
    tank = new FluidTankFixDesync(TANK_FULL, this);
  }
  //  @Override
  //  public int getBlockMetadata() {
  //    return getBuckets();
  //  } 
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) { return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank); }
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
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) { return null; }
    return fluidHandler.getTankProperties()[0].getContents();
  }
  @Override
  public IFluidTankProperties[] getTankProperties() {
    FluidTankInfo info = tank.getInfo();
    return new IFluidTankProperties[] { new FluidTankProperties(info.fluid, info.capacity, true, true) };
  }
  private boolean doesFluidMatchTank(FluidStack incoming) {
    if (tank.getFluid() == null) { return true; }
    return tank.getFluid().getFluid() == incoming.getFluid();
  }
  @Override
  public int fill(FluidStack resource, boolean doFill) {
    if (doesFluidMatchTank(resource) == false) { return 0; }
    if (resource.amount + tank.getFluidAmount() > TANK_FULL) {//enForce limit
      resource.amount = TANK_FULL - tank.getFluidAmount();
    }
    int result = tank.fill(resource, doFill);
    // this.world.markChunkDirty(pos, this);
    tank.setFluid(resource);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    if (doesFluidMatchTank(resource) == false) { return resource; }
    FluidStack result = tank.drain(resource, doDrain);
    //   this.world.markChunkDirty(pos, this);
    tank.setFluid(resource);
    return result;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    //  this.world.markChunkDirty(pos, this);
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
   * fix fluid rendering breaks because pipes and pumps update my fluid level
   * only client side
   * 
   * @param fluid
   */
  @SideOnly(Side.CLIENT)
  public void updateFluidTo(FluidStack fluid) {
    this.tank.setFluid(fluid);
  }
}
