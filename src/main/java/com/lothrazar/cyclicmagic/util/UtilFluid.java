package com.lothrazar.cyclicmagic.util;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class UtilFluid {
  //  public static ItemStack dispenseStack(World world, BlockPos pos, ItemStack stack, EnumFacing facing) {
  //    if (FluidUtil.getFluidContained(stack) != null) {
  //      return dumpContainer(world, pos, stack);
  //    }
  //    else {
  //      return fillContainer(world, pos, stack, facing);
  //    }
  //  }
  /**
   * Picks up fluid fills a container with it.
   */
  public static FluidActionResult fillContainer(World world, BlockPos pos, ItemStack stackIn, EnumFacing facing) {
    //    ItemStack result = stackIn.copy();
    return FluidUtil.tryPickUpFluid(stackIn, null, world, pos, facing);
    //  if (--stackIn.stackSize == 0) {
    //    stackIn.deserializeNBT(result.serializeNBT());
    //  }
    //    if (res == FluidActionResult.FAILURE) { return stackIn; }
    //    return res.getResult();
  }
  /**
   * Drains a filled container and places the fluid.
   * 
   * RETURN new item stack that has been drained after placing in world if it
   * works null otherwise
   */
  public static ItemStack dumpContainer(World world, BlockPos pos, ItemStack stackIn) {
    //    BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, pos);
    ItemStack dispensedStack = stackIn.copy();
    dispensedStack.setCount(1);
    IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(dispensedStack);
    if (fluidHandler == null) {
      return null;
    }
    FluidStack fluidStack = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);
    if (fluidStack != null && fluidStack.amount >= Fluid.BUCKET_VOLUME) {
      //      FluidActionResult placeResult = FluidUtil.tryPlaceFluid(null, world, pos, dispensedStack, fluidStack);
      if (FluidUtil.tryPlaceFluid(null, world, pos, dispensedStack, fluidStack).isSuccess()) {
        //http://www.minecraftforge.net/forum/topic/56265-1112-fluidhandler-capability-on-buckets/
        fluidHandler.drain(Fluid.BUCKET_VOLUME, true);
        ItemStack returnMe = fluidHandler.getContainer();
        //        stackIn.deserializeNBT(returnMe.serializeNBT());
        return returnMe;
      }
    }
    return null;
  }
  public static ItemStack drainOneBucket(ItemStack d) {
    IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(d);
    if (fluidHandler == null) {
      return d;
    } //its empty, ok no problem
    fluidHandler.drain(Fluid.BUCKET_VOLUME, true);
    return fluidHandler.getContainer();
  }
  //  public static boolean isFullOfFluid(ItemStack returnMe) {
  //    IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(returnMe);
  //    fluidHandler.fill(resource, doFill)
  //    return FluidUtil.getFluidContained(returnMe) == null;
  //  }
  public static boolean isEmptyOfFluid(ItemStack returnMe) {
    return FluidUtil.getFluidContained(returnMe) == null;
  }
  public static FluidStack getFluidContained(ItemStack returnMe) {
    return FluidUtil.getFluidContained(returnMe);
  }
  public static Fluid getFluidType(ItemStack returnMe) {
    FluidStack f = FluidUtil.getFluidContained(returnMe);
    return (f == null) ? null : f.getFluid();
  }
  public static boolean stackHasFluidHandler(ItemStack stackIn) {
    return FluidUtil.getFluidHandler(stackIn) != null;
  }
  public static boolean hasFluidHandler(TileEntity tile, EnumFacing side) {
    return tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
  }
  public static IFluidHandler getFluidHandler(TileEntity tile, EnumFacing side) {
    return (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) ? tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side) : null;
  }
  public static boolean interactWithFluidHandler(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nullable EnumFacing side) {
    return FluidUtil.interactWithFluidHandler(player, EnumHand.MAIN_HAND, world, pos, side);
  }
  
  /**
   * Look for a fluid handler with gien position and direction
   * try to extract from that pos and fill the tank
   * 
   * 
   * @param world
   * @param posSide
   * @param sideOpp
   * @param tank
   * @param amount
   * @return
   */
 public static boolean tryFillTankFromPosition(World world,
     BlockPos posSide
     , EnumFacing sideOpp
     , FluidTank tank
     ,int amount){
   
   

   IFluidHandler fluidFrom = FluidUtil.getFluidHandler(world, posSide, sideOpp);
   if (fluidFrom != null) {
     //its not my facing dir
     // SO: pull fluid from that into myself
     FluidStack wasDrained = fluidFrom.drain(100, false);
     int filled = tank.fill(wasDrained, false);
     if (wasDrained != null && wasDrained.amount > 0
         && filled > 0) {
       

//       ModCyclic.logger.log(" wasDrained  "+wasDrained.amount);
//       ModCyclic.logger.log(" filled  "+  filled);
       int realAmt = Math.min(filled, wasDrained.amount);
       wasDrained = fluidFrom.drain(realAmt, true);
     return  tank.fill(wasDrained, true) > 0;
       
       
       
     }
     
   }
   
   
   return false;
 }
 
 
  
  
  
}
