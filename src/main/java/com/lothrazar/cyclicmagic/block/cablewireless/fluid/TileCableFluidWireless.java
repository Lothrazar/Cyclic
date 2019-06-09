package com.lothrazar.cyclicmagic.block.cablewireless.fluid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.lothrazar.cyclicmagic.block.cablewireless.ILaserTarget;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.TileCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.locationgps.ItemLocationGps;
import com.lothrazar.cyclicmagic.liquid.FluidTankFixDesync;
import com.lothrazar.cyclicmagic.util.RenderUtil.LaserConfig;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileCableFluidWireless extends TileEntityBaseMachineFluid implements ITickable, ILaserTarget, ITileRedstoneToggle {

  public static final int TRANSFER_FLUID_PER_TICK = 500;
  public static final int TANK_FULL = 10000;
  public static final int MAX_TRANSFER = 1000;
  public static final int SLOT_COUNT = 9;
  List<Integer> slotList = IntStream.rangeClosed(
      0, TileCableEnergyWireless.SLOT_COUNT).boxed().collect(Collectors.toList());
  private int transferRate = MAX_TRANSFER / 2;

  public static enum Fields {
    REDSTONE, TRANSFER_RATE, RENDERPARTICLES;
  }

  public TileCableFluidWireless() {
    super(SLOT_COUNT);
    tank = new FluidTankFixDesync(TANK_FULL, this);
    this.setSlotsForBoth();
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TRANSFER_RATE:
        return this.transferRate;
      case RENDERPARTICLES:
        return this.renderParticles;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TRANSFER_RATE:
        transferRate = value;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return stack.getItem() instanceof ItemLocationGps;
  }

  private BlockPosDim getSlotGps(int slot) {
    return ItemLocationGps.getPosition(this.getStackInSlot(slot));
  }

  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    //shuffle into random order
    Collections.shuffle(slotList);
    for (int slot : slotList) {
      outputFluid(slot);
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone == 1) ? 0 : 1;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  private boolean isTargetValid(BlockPosDim target) {
    return target != null &&
        target.getDimension() == this.getDimension() &&
        world.isAreaLoaded(target.toBlockPos(), target.toBlockPos().up());
  }

  private void outputFluid(int slot) {
    BlockPosDim dim = this.getSlotGps(slot);
    if (this.isTargetValid(dim)) {
      EnumFacing sideTarget = dim.getSide();
      if (sideTarget == null) {//legacy from null
        UtilWorld.getRandFacing();
      }
      UtilFluid.tryFillPositionFromTank(world, dim.toBlockPos(), sideTarget, this.tank, TRANSFER_FLUID_PER_TICK);
    }
  }

  @Override
  public boolean isPreviewVisible() {
    return this.renderParticles == 1;
  }

  static final float[] laserColor = new float[] { 0.2F, 0.2F, 0.8F };
  static final double rotationTime = 0;
  static final double beamWidth = 0.02;
  static final float alpha = 0.5F;

  @Override
  public List<LaserConfig> getTarget() {
    //find laser endpoints and go
    BlockPosDim first = new BlockPosDim(this.getPos(), this.getDimension());
    List<LaserConfig> laser = new ArrayList<>();
    for (BlockPos second : this.getShape()) {
      //  && second.getDimension() == first.getDimension()
      if (second != null && first != null) {
        laser.add(new LaserConfig(first.toBlockPos(), second,
            rotationTime, alpha, beamWidth, laserColor));
      }
    }
    return laser;
  }

  @Override
  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<>();
    for (int slot : slotList) {
      if (this.getStackInSlot(slot).isEmpty() == false) {
        BlockPosDim target = this.getSlotGps(slot);
        if (this.isTargetValid(target)) {
          shape.add(target.toBlockPos());
        }
      }
    }
    return shape;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.transferRate = compound.getInteger("transferRate");
    this.needsRedstone = compound.getInteger(NBT_REDST);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("transferRate", transferRate);
    compound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(compound);
  }
}
