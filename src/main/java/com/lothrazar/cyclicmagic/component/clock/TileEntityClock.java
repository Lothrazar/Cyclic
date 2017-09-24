package com.lothrazar.cyclicmagic.component.clock;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileFacingToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityClock extends TileEntityBaseMachineInvo implements ITickable, ITileFacingToggle {
  public static enum Fields {
    TIMER, TOFF, TON, POWER;
  }
  private int timeOff;//dont let these times be zero !!!
  private int timeOn;
  private int power;
  private Map<EnumFacing, Boolean> poweredSides = new HashMap<EnumFacing, Boolean>();
  public TileEntityClock() {
    super(0);
    timer = 0;
    timeOff = 60;
    timeOn = 60;
    power = 15;
    this.facingResetAllOn();
  }
  public int getPower() {
    return this.power;
  }
  public int getPowerForSide(EnumFacing side) {
    if (this.getSideHasPower(side))
      return this.power;
    else
      return 0;
  }
  public boolean getSideHasPower(EnumFacing side) {
    return this.poweredSides.get(side);
  }
  @Override
  public void toggleSide(EnumFacing side) {
    this.poweredSides.put(side, !this.poweredSides.get(side));
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    //oldState.getBlock() instanceof BlockRedstoneClock &&
    return !(newSate.getBlock() instanceof BlockRedstoneClock);// : oldState != newSate;
  }
  @Override
  public void update() {
    if (world.isRemote == false) {
      this.timer++;
      boolean powered;
      boolean prevPowered = world.getBlockState(pos).getValue(BlockRedstoneClock.POWERED);
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
      if (prevPowered != powered)
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
        if (value < 1) {
          value = 1;
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
        timeOff = Math.max(value, 1);
      break;
      case TON:
        timeOn = Math.max(value, 1);
      break;
      default:
      break;
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("off", timeOff);
    compound.setInteger("on", timeOn);
    compound.setInteger("power", power);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setBoolean(f.getName(), poweredSides.get(f));
    }
    return super.writeToNBT(compound);
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    timeOff = compound.getInteger("off");
    timeOn = compound.getInteger("on");
    power = compound.getInteger("power");
    for (EnumFacing f : EnumFacing.values()) {
      poweredSides.put(f, compound.getBoolean(f.getName()));
    }
    if (this.detectAllOff()) {
      this.facingResetAllOn();//fix legacy data for one
    }
  }
  private boolean detectAllOff() {
    boolean areAnyOn = false;
    for (EnumFacing f : EnumFacing.values()) {
      areAnyOn = areAnyOn || poweredSides.get(f);
    }
    return !areAnyOn;
  }
  private void facingResetAllOn() {
    for (EnumFacing f : EnumFacing.values()) {
      poweredSides.put(f, true);
    }
  }
}
