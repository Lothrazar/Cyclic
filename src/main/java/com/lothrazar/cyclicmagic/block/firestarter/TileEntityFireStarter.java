package com.lothrazar.cyclicmagic.block.firestarter;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityFireStarter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  enum FireType {
    NORMAL, DARK, FROST;
  }

  private int yOffset = 0;
  private int fireType = 0;
  private int needsRedstone = 1;
  private int hOffset = 0;
  private int delay = 11;

  public TileEntityFireStarter() {
    super(1);
    timer = delay;
    this.setSlotsForInsert(0);
    this.initEnergy(BlockFireStarter.FUEL_COST, 8000);
  }

  public static enum Fields {
    TIMER, REDSTONE, DELAY, OFFSET, FIRETYPE, Y_OFFSET;
  }

  @GameRegistry.ObjectHolder(Const.MODRES + "fire_dark")
  public static final Block fireDark = null;
  @GameRegistry.ObjectHolder(Const.MODRES + "fire_frost")
  public static final Block fireFrost = null;

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    BlockPos target = this.getCurrentFacingPos().offset(this.getCurrentFacing(), hOffset).offset(EnumFacing.UP, yOffset);
    if (!world.isAirBlock(target)) {
      return; //dont drain power or tick down if blocked
    }
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    if (this.updateTimerIsZero()) {
      timer = delay;
      Block fire = null;
      switch (FireType.values()[this.fireType]) {
        case DARK:
          fire = fireDark;
        break;
        case FROST:
          fire = fireFrost;
        break;
        case NORMAL:
          fire = Blocks.FIRE;
        break;
      }
      world.setBlockState(target, fire.getDefaultState());
    }
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case DELAY:
        return delay;
      case OFFSET:
        return hOffset;
      case FIRETYPE:
        return fireType;
      case Y_OFFSET:
        return yOffset;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case DELAY:
        delay = value;
      break;
      case OFFSET:
        hOffset = value;
      break;
      case FIRETYPE:
        fireType = value % FireType.values().length;
      break;
      case Y_OFFSET:
        if (value > 1) {
          value = -1;
        }
        this.yOffset = value;
      break;
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.needsRedstone = compound.getInteger(NBT_REDST);
    this.delay = compound.getInteger("delay");
    this.hOffset = compound.getInteger("hOffset");
    this.fireType = compound.getInteger("fireType");
    yOffset = compound.getInteger("yoff");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, this.needsRedstone);
    compound.setInteger("delay", this.delay);
    compound.setInteger("hOffset", this.hOffset);
    compound.setInteger("fireType", this.fireType);
    compound.setInteger("yoff", yOffset);
    return super.writeToNBT(compound);
  }
}
