package com.lothrazar.cyclicmagic.block.cablewireless.content;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileCableContentWireless extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  public static final int SLOT_CARD_ITEM = 0;
  public static final int SLOT_TRANSFER = 1;

  public static enum Fields {
    REDSTONE;
  }

  private int needsRedstone = 0;

  public TileCableContentWireless() {
    super(2);
    this.setSlotsForInsert(SLOT_TRANSFER);
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
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index == SLOT_TRANSFER) {
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
    outputItems();
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

  private void outputItems() {
    BlockPosDim dim = this.getTarget(SLOT_CARD_ITEM);
    if (!this.isTargetValid(dim)) {
      return;
    }
    BlockPos target = dim.toBlockPos();
    ItemStack stackToExport;
    stackToExport = this.getStackInSlot(SLOT_TRANSFER).copy();
    stackToExport.setCount(1);
    if (stackToExport.isEmpty() == false) {
      ItemStack leftAfterDeposit = UtilItemStack.tryDepositToHandler(world, target, null, stackToExport);
      if (leftAfterDeposit.getCount() < stackToExport.getCount()) { //something moved!
        //then save result
        this.decrStackSize(SLOT_TRANSFER);
      }
    }
  }

}
