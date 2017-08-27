package com.lothrazar.cyclicmagic.component.clock;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityClock extends TileEntityBaseMachineInvo implements ITickable {
  public static enum Fields {
    TIMER, TOFF, TON, POWER;
  }
  public TileEntityClock() {
    super(0);
    timer = 0;
  }
  private int timeOff = 60;
  private int timeOn = 60;
  private int power = 15;
  public int getPower() {
    return this.power;
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (world.isRemote == false) {
      this.timer++;
      boolean powered;
      if (timer < timeOff) {
        powered = false;
      }
      else if (timer < timeOff + timeOn) {
        //we are in the ON section
        powered = true;
      }
      else {
        timer = 0;
        powered = false;
      }
      this.power = 3;
      world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockRedstoneClock.POWERED, powered));
    }
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case POWER:
        return power;
      case TIMER:
        return timer;
      case TOFF:
        return timeOff;
      case TON:
        return timeOn;
      default:
      break;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case POWER:
        if (value < 0) {
          value = 0;
        }
        if (value > 15) {
          value = 15;
        }
        power = value;
      break;
      case TIMER:
        timer = value;
      break;
      case TOFF:
        timeOff = Math.max(value, 0);
      break;
      case TON:
        timeOn =  Math.max(value, 0);
      break;
      default:
      break;
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    compound.setInteger("off", timeOff);
    compound.setInteger("on", timeOn);
    compound.setInteger("power", power);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    timeOff = compound.getInteger("off");
    timeOn = compound.getInteger("on");
    power = compound.getInteger("power");
    return super.writeToNBT(compound);
  }
}
