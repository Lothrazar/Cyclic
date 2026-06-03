package com.lothrazar.cyclic.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public class CapabilityUtil {

  public static boolean isItem( Direction facing, Level level, BlockPos facingPos) {
    if (facing == null) {
      return false;
    }
    return  CapabilityUtil.item(level, facingPos, facing.getOpposite()) != null ;
  }

  public static boolean isFluid(Direction facing, Level level, BlockPos facingPos){
    if (facing == null) {
      return false;
    }
    return  CapabilityUtil.fluid(level, facingPos, facing.getOpposite()) != null ;
  }

  public static boolean isEnergy(Direction facing, Level level, BlockPos facingPos) {
    if (facing == null) {
      return false;
    }
    return  CapabilityUtil.energy(level, facingPos, facing.getOpposite()) != null ;
  }

  public static IEnergyStorage energy(ItemStack stack) {
    return stack.getCapability(Capabilities.EnergyStorage.ITEM);
  }

  public static IEnergyStorage energy(Level level, BlockPos pos) {
    return energy(level,pos,null);
  }

  public static int energyStored(Level level, BlockPos pos) {
    var handler = energy(level,pos,null);
    return handler == null ? 0 : handler.getEnergyStored();
  }
  public static IEnergyStorage energy(Level level, BlockPos pos, Direction dir) {
    return  level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, dir);
  }

  public static IFluidHandler fluid(Level level, BlockPos pos ) {
    return fluid(level,pos, (Direction)null);
  }

  public static IFluidHandler fluid(Level level, BlockPos pos, BlockHitResult dir) {
    return fluid(level,pos,dir.getDirection());
  }

  public static IFluidHandler fluid(ItemStack stack) {
    return stack.getCapability(Capabilities.FluidHandler.ITEM);
  }

  public static IFluidHandler fluid(Level level, BlockPos pos, Direction dir) {
    return  level.getCapability(Capabilities.FluidHandler.BLOCK, pos, dir);
  }

  public static IItemHandler item(Level level, BlockPos pos) {
    return  item(level, pos, null);
  }

  public static IItemHandler item(Level level, BlockPos pos, Direction dir) {
    return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, dir);
  }

  public static IItemHandler item(ItemStack stack) {
    return stack.getCapability(Capabilities.ItemHandler.ITEM);
  }
}
