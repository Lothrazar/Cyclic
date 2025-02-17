package com.lothrazar.cyclic.fixers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.minecraft.world.level.Level;

public class CapabilityFixer {


  public static IFluidHandler fluid(net.minecraft.world.level.Level level, BlockPos pos, BlockHitResult dir) {
    return fluid(level,pos,dir.getDirection());
  }

  public static IFluidHandler fluid(net.minecraft.world.level.Level level, BlockPos pos, Direction dir) {
    return  level.getCapability(Capabilities.FluidHandler.BLOCK, pos, dir);
  }



}
