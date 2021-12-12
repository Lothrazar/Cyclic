package com.lothrazar.cyclic.capability.wrappers;

import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SpongeBlockWrapper implements IFluidHandler {
  protected final World world;
  protected final BlockPos blockPos;
  protected Block block;

  public SpongeBlockWrapper(final Block block, final World world, final BlockPos blockPos) {
    this.world = world;
    this.blockPos = blockPos;
    this.block = block;
  }

  protected boolean isWet() {
    return block instanceof WetSpongeBlock;
  }

  protected boolean updateBlock(final Block newBlock) {
    //2 will send the change to clients.
    if (world.setBlockState(blockPos, newBlock.getDefaultState(), 2)) {
      block = newBlock;
      return true;
    }
    return false;
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @Nonnull
  @Override
  public FluidStack getFluidInTank(int tank) {
    if (isWet()) {
      return new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);
    }
    return FluidStack.EMPTY;
  }

  @Override
  public int getTankCapacity(int tank) {
    if (isWet()) {
      return 0;
    }
    return FluidAttributes.BUCKET_VOLUME;
  }

  @Override
  public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
    return stack.getFluid() == Fluids.WATER;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (isWet()) {
      return 0;
    }
    if (action == FluidAction.EXECUTE) {
      return (updateBlock(Blocks.WET_SPONGE))
          ? FluidAttributes.BUCKET_VOLUME
          : 0;
    }
    return FluidAttributes.BUCKET_VOLUME;
  }

  @Nonnull
  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    if (resource.getFluid() != Fluids.WATER || resource.getAmount() < FluidAttributes.BUCKET_VOLUME || !isWet()) {
      return FluidStack.EMPTY;
    }
    if (action == FluidAction.EXECUTE) {
      return (updateBlock(Blocks.SPONGE))
          ? new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME)
          : FluidStack.EMPTY;
    }
    return new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);
  }

  @Nonnull
  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    if (maxDrain < FluidAttributes.BUCKET_VOLUME || !isWet()) {
      return FluidStack.EMPTY;
    }
    if (action == FluidAction.EXECUTE) {
      return (updateBlock(Blocks.SPONGE))
          ? new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME)
          : FluidStack.EMPTY;
    }
    return new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);
  }
}
