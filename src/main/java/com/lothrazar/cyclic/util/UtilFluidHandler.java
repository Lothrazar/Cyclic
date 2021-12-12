package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.capability.wrappers.SpongeBlockWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.SpongeBlock;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BucketPickupHandlerWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

public final class UtilFluidHandler {
  private UtilFluidHandler() {
  }

  public static IFluidHandler get(final World world, final BlockPos blockPos, final Direction side) {
    return getOptCap(world, blockPos, side).resolve().orElse(null);
  }

  public static LazyOptional<IFluidHandler> getOptCap(final World world, final BlockPos blockPos, final Direction side) {
    final TileEntity tileEntity = world.getTileEntity(blockPos);
    if (tileEntity != null) {
      return tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
    }
    return LazyOptional.empty();
  }

  public static IFluidHandler getFromBlock(final World world, final BlockPos blockPos) {
    final BlockState blockState = world.getBlockState(blockPos);
    final Block block = blockState.getBlock();
    if (block instanceof IBucketPickupHandler) {
      return new BucketPickupHandlerWrapper((IBucketPickupHandler) block, world, blockPos);
    }
    if (block instanceof IFluidBlock) {
      return new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
    }
    if (block instanceof SpongeBlock || block instanceof WetSpongeBlock) {
      return new SpongeBlockWrapper(block, world, blockPos);
    }
    return null;
  }
}
