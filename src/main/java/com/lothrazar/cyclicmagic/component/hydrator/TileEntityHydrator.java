package com.lothrazar.cyclicmagic.component.hydrator;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityHydrator extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable, IFluidHandler {
  private static final int SLOT_PROCESSING = 0;
  private static final int SLOT_INFLUID = 1;
  public FluidTank tank = new FluidTank(4000);
  public final static int TIMER_FULL = 120;
  public static enum Fields {
    REDSTONE
  }
  private InventoryCrafting crafting = new InventoryCrafting(new ContainerDummy(), 1, 1);
  public TileEntityHydrator() {
    super(9);
    timer = TIMER_FULL;
  }
  private int needsRedstone = 1;
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (!isRunning()) { return; }
    if (this.updateTimerIsZero()) { // time to burn!
      this.timer = TIMER_FULL;
      ItemStack maybeBucket = this.getStackInSlot(SLOT_INFLUID);
      FluidStack f = FluidUtil.getFluidContained(maybeBucket);
      IFluidHandlerItem bucketHandler = FluidUtil.getFluidHandler(maybeBucket);
      if (f != null && bucketHandler != null && f.getFluid().equals(FluidRegistry.WATER)) {
        //https://github.com/BluSunrize/ImmersiveEngineering/blob/fc022675bb550318cbadc879b3f28dde511e29c3/src/main/java/blusunrize/immersiveengineering/common/blocks/wooden/TileEntityWoodenBarrel.java
        if (this.tank.getFluid() != null) {
          ModCyclic.logger.info("BEFORE tank =  " + this.tank.getFluid().amount
              + "/" + this.tank.getInfo().capacity
              + " " + this.tank.getFluid().getFluid().getName());
        }
        
        
        FluidActionResult r=   FluidUtil.tryEmptyContainer(maybeBucket, tank, Fluid.BUCKET_VOLUME, null, true);
        
        ModCyclic.logger.info("  FluidActionResult  "+r.success);
        ModCyclic.logger.info("  FluidActionResult  "+r.result);
        
        if(r.success){
             this.setInventorySlotContents(SLOT_INFLUID, r.result);
        }
        
        
        
        
        
//        ModCyclic.logger.info(" try input water  ");//ffftryEmptyContainer
//        //        FluidStack reallyDrained = FluidUtil.getFluidHandler(maybeBucket).drain(1000, true);
//        //source, dest, amt, doIt 
//        FluidStack afterTransferBucketToTank = FluidUtil.tryFluidTransfer(tank, FluidUtil.getFluidHandler(maybeBucket), new FluidStack(FluidRegistry.WATER,  Fluid.BUCKET_VOLUME), true);
//        //        if (reallyDrained != null) {}
//        ModCyclic.logger.info(" maybeBucket is now  " + maybeBucket.getDisplayName());
//        if (afterTransferBucketToTank != null) {//now REALLLLLLY empty the bucket. k? k.
//          ModCyclic.logger.info(" !afterTransferBucketToTank  " + afterTransferBucketToTank.amount);//this has 1000.
//          
//
////          ModCyclic.logger.info(" bucketHandler has flud   " + bucketHandler.get);//this has 1000.
//          
//          bucketHandler = FluidUtil.getFluidHandler(maybeBucket);
//          ModCyclic.logger.info("maybeBucket   getItem " + maybeBucket.getItem());//this has 1000.
//          ModCyclic.logger.info("IS EQUAL WATER?" + (maybeBucket.getItem() == Items.WATER_BUCKET));//this has 1000.
//          
//          
//          FluidStack drainBucketresult = bucketHandler.drain(afterTransferBucketToTank.amount, true);
//          //          f.amount -= afterTransferBucketToTank.amount;
//
//          
//          
//         
//          
//          ModCyclic.logger.info(" !f bucket SHOULD HAVE   " + f.amount);//this has 1000.
//          
//         
//            ModCyclic.logger.info(" BUCKET DRAIN HAS FAILED ???   " +drainBucketresult);//this has 1000.
//         this.setInventorySlotContents(SLOT_INFLUID, maybeBucket);
//          
//          //          NBTTagCompound tag = new NBTTagCompound();
//          //          afterTransferBucketToTank.writeToNBT(tag);
//          //          maybeBucket.setTagCompound(tag);
//          //          ModCyclic.logger.info(" maybeBucket is now 2 " + maybeBucket.getDisplayName());
//          //this.setStackInSlot(SLOT_INFLUID,maybeBucket);
//        }
        //        int fillResult = this.fill(FluidUtil.getFluidContained(maybeBucket), true);
        //        if (fillResult > 0) {}
        //        ModCyclic.logger.info(" fillResult  " + fillResult);
        //WAT https://github.com/BluSunrize/ImmersiveEngineering/search?utf8=%E2%9C%93&q=FluidUtil&type=
        //    ModCyclic.logger.info("fluidstack " + this.tank.getFluid());
        if (this.tank.getFluid() != null) {
          ModCyclic.logger.info("AFTERtank =  " + this.tank.getFluid().amount
              + "/" + this.tank.getInfo().capacity
              + " " + this.tank.getFluid().getFluid().getName());
        }
      }
      this.spawnParticlesAbove();
      //    this.shiftAllUp();
      ItemStack s = this.getStackInSlot(SLOT_PROCESSING);
      this.crafting.setInventorySlotContents(SLOT_PROCESSING, s);
      IRecipe rec = CraftingManager.findMatchingRecipe(crafting, this.world);
      if (rec != null) {
        this.sendOutput(rec.getRecipeOutput());
        s.shrink(1);
      }
    }
  }
  public void sendOutput(ItemStack out) {
    UtilItemStack.dropItemStackInWorld(this.world, this.getPos(), out);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    tank.readFromNBT(tagCompound.getCompoundTag("tank"));
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  /******************************
   * fluid properties here
   ******************************/
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
      return true;
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
      return (T) tank;
    return super.getCapability(capability, facing);
  }
  public static class ContainerDummy extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
  }
  @Override
  public IFluidTankProperties[] getTankProperties() {
    FluidTankInfo info = tank.getInfo();
    return new IFluidTankProperties[] { new FluidTankProperties(info.fluid, info.capacity, true, true) };
  }
  @Override
  public int fill(FluidStack resource, boolean doFill) {
    return tank.fill(resource, doFill);
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    return tank.drain(resource, doDrain);
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    return tank.drain(maxDrain, doDrain);
  }
}
