package com.lothrazar.cyclicmagic.block.cablewireless.item;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.TileCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.locationgps.ItemLocationGps;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileCableContentWireless extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  public static final int SLOT_TRANSFER = 0;
  public static final int MAX_TRANSFER = 2;
  public static final int SLOT_COUNT = 9 + 1;
  List<Integer> slotList = IntStream.rangeClosed(
      0, TileCableEnergyWireless.SLOT_COUNT).boxed().collect(Collectors.toList());
  private int transferRate = MAX_TRANSFER / 2;

  public static enum Fields {
    REDSTONE, TRANSFER_RATE, RENDERPARTICLES;
  }

  public TileCableContentWireless() {
    super(SLOT_COUNT);
    this.setSlotsForInsert(0);
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
      default:
      break;
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
      default:
      break;
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return index == SLOT_TRANSFER ? true : stack.getItem() instanceof ItemLocationGps;
  }

  private BlockPosDim getTarget(int slot) {
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
      outputItems(slot);
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

  private void outputItems(int slot) {
    BlockPosDim dim = this.getTarget(slot);
    if (!this.isTargetValid(dim)) {
      return;
    }
    BlockPos target = dim.toBlockPos();
    ItemStack stackToExport;
    stackToExport = this.getStackInSlot(SLOT_TRANSFER).copy();
    stackToExport.setCount(1);
    if (stackToExport.isEmpty() == false) {
      EnumFacing rando = UtilWorld.getRandFacing();
      ItemStack leftAfterDeposit = UtilItemStack.tryDepositToHandler(world, target, rando, stackToExport);
      if (leftAfterDeposit.getCount() < stackToExport.getCount()) { //something moved!
        //then save result
        this.decrStackSize(SLOT_TRANSFER);
      }
    }
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
