package com.lothrazar.cyclic.block.cable.fluid;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilFluid;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileCableFluid extends TileEntityBase implements ITickableTileEntity {

  public static final int CAPACITY = 16 * FluidAttributes.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidAttributes.BUCKET_VOLUME / 2;
  private Map<Direction, LazyOptional<IFluidHandler>> flow = Maps.newHashMap();

  public TileCableFluid() {
    super(BlockRegistry.TileRegistry.fluid_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(this::createHandler));
    }
  }

  private IFluidHandler createHandler() {
    FluidTankBase h = new FluidTankBase(this, CAPACITY, isFluidValid());
    return h;
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  List<Integer> rawList = IntStream.rangeClosed(
      0,
      5).boxed().collect(Collectors.toList());

  @Override
  public void tick() {
    tryExtract();
    normalFlow();
  }

  private void tryExtract() {
    Direction extractSide = this.getBlockState().get(BlockCableFluid.EXTR).direction();
    if (extractSide == null) {
      return;
    }
    //
    BlockPos target = this.pos.offset(extractSide);
    UtilFluid.tryFillPositionFromTank(world, pos, extractSide,
        UtilFluid.getTank(world, target, extractSide.getOpposite()), CAPACITY);
  }

  private void normalFlow() {
    IFluidHandler sideHandler;
    Direction outgoingSide;
    Direction importFromSide = this.getBlockState().get(BlockCableFluid.EXTR).direction();
    for (Direction incomingSide : Direction.values()) {
      sideHandler = flow.get(incomingSide).orElse(null);
      //thise items came from that
      Collections.shuffle(rawList);
      for (Integer i : rawList) {
        outgoingSide = Direction.values()[i];
        if (outgoingSide == incomingSide) {
          continue;
        }
        if (importFromSide != null && importFromSide == outgoingSide) {
          continue;
        }
        this.moveFluids(outgoingSide, CAPACITY, sideHandler);
      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (side != null && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      if (!CableBase.isCableBlocked(this.getBlockState(), side))
        return flow.get(side).cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    FluidTankBase fluidh;
    for (Direction dir : Direction.values()) {
      fluidh = (FluidTankBase) flow.get(dir).orElse(null);
      if (tag.contains("fluid" + dir.toString())) {
        fluidh.readFromNBT(tag.getCompound("fluid" + dir.toString()));
      }
    }
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    FluidTankBase fluidh;
    for (Direction dir : Direction.values()) {
      fluidh = (FluidTankBase) flow.get(dir).orElse(null);
      CompoundNBT fluidtag = new CompoundNBT();
      if (fluidh != null) {
        fluidh.writeToNBT(fluidtag);
      }
      tag.put("fluid" + dir.toString(), fluidtag);
    }
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {}
}
