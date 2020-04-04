package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class UtilFluid {

  public static IFluidHandler getTank(World world, BlockPos pos, Direction side) {
    TileEntity tile = world.getTileEntity(pos);
    if (tile == null) {
      return null;
    }
    return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
  }

  public static boolean tryFillPositionFromTank(World world, BlockPos posSide, Direction sideOpp, IFluidHandler tankFrom, int amount) {
    if (tankFrom == null) {
      return false;
    }
    try {
      IFluidHandler fluidTo = FluidUtil.getFluidHandler(world, posSide, sideOpp).orElse(null);
      if (fluidTo != null) {
        //its not my facing dir
        // SO: pull fluid from that into myself
        FluidStack wasDrained = tankFrom.drain(amount, FluidAction.SIMULATE);
        if (wasDrained == null) {
          return false;
        }
        int filled = fluidTo.fill(wasDrained, FluidAction.SIMULATE);
        if (wasDrained != null && wasDrained.getAmount() > 0
            && filled > 0) {
          int realAmt = Math.min(filled, wasDrained.getAmount());
          wasDrained = tankFrom.drain(realAmt, FluidAction.EXECUTE);
          if (wasDrained == null) {
            return false;
          }
          return fluidTo.fill(wasDrained, FluidAction.EXECUTE) > 0;
        }
      }
      return false;
    }
    catch (Exception e) {
      ModCyclic.error("A fluid tank had an issue when we tried to fill", e);
      //charset crashes here i guess
      //https://github.com/PrinceOfAmber/Cyclic/issues/605
      // https://github.com/PrinceOfAmber/Cyclic/issues/605https://pastebin.com/YVtMYsF6
      return false;
    }
  }
}
