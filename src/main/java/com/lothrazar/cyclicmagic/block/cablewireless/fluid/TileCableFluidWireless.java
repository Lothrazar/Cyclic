package com.lothrazar.cyclicmagic.block.cablewireless.fluid;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import com.lothrazar.cyclicmagic.liquid.FluidTankBase;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileCableFluidWireless extends TileEntityBaseMachineFluid implements ITickable, ITileRedstoneToggle {

  //ITilePreviewToggle
  public static final int TRANSFER_FLUID_PER_TICK = 500;
  public static final int TANK_FULL = 10000;
  public static final int SLOT_CARD_ITEM = 0;
  public static final int MAX_TRANSFER = 1000;
  private int transferRate = MAX_TRANSFER / 2;
  public static enum Fields {
    REDSTONE, TRANSFER_RATE;
  }

  private int needsRedstone = 0;

  public TileCableFluidWireless() {
    super(1);
    tank = new FluidTankBase(TANK_FULL);
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
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index == SLOT_CARD_ITEM) {
      return true;
    }
    return stack.getItem() instanceof ItemLocation;
  }

  private BlockPosDim getTarget(int slot) {
    return ItemLocation.getPosition(this.getStackInSlot(slot));
  }

  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    outputFluid();
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
        target.dimension == this.getDimension() &&
        world.isAreaLoaded(target.toBlockPos(), target.toBlockPos().up());
  }

  private void outputFluid() {
    BlockPosDim dim = this.getTarget(SLOT_CARD_ITEM);
    if (!this.isTargetValid(dim)) {
      return;
    }
    BlockPos target = dim.toBlockPos();
    UtilFluid.tryFillPositionFromTank(world, target, null, this.tank, TRANSFER_FLUID_PER_TICK);
  }
}
