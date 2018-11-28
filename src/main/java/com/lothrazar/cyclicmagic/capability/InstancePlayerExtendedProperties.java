package com.lothrazar.cyclicmagic.capability;

import com.lothrazar.cyclicmagic.playerupgrade.ItemAppleStep;
import com.lothrazar.cyclicmagic.playerupgrade.ItemHeartContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class InstancePlayerExtendedProperties implements IPlayerExtendedProperties {

  private static final String MHEALTH = "mhealth";
  private static final String NBT_TODO = "todo";
  private static final String HAS_INVENTORY_EXTENDED = "hasInventoryExtended";
  private static final String HAS_INVENTORY_CRAFTING = "hasInventoryCrafting";
  private static final String IS_SLEEPING = "isSleeping";
  private static final String KEY_BOOLEAN = "ghost_on";
  private static final String KEY_TIMER = "ghost_timer";
  private static final String KEY_EATLOC = "ghost_location";
  private static final String KEY_EATDIM = "ghost_dim";
  private static final String KEY_STEP = Const.MODID + "_step";
  private static final String KEY_STEPFORCE = Const.MODID + "_stepforced";
  private static final String KEY_FLYING = Const.MODID + "_flying";
  private boolean foreStepHeightOff = false;
  private boolean isStepOn = ItemAppleStep.defaultPlayerStepUp;
  private boolean isSleeping = false;
  private boolean hasInventoryCrafting = false;
  private boolean hasInventoryExtended = false;
  private String todo = "";
  private int health = ItemHeartContainer.defaultHearts * 2;//two health per heart. magic number alert!
  private boolean isChorusSpectator = false;
  private BlockPos chorusStart = null;
  private int chorusDim = 0;
  private int chorusSeconds = 0;
  private int flyingSeconds = 0;

  @Override
  public boolean isSleeping() {
    return isSleeping;
  }

  @Override
  public void setSleeping(boolean value) {
    this.isSleeping = value;
  }

  @Override
  public boolean hasInventoryCrafting() {
    return hasInventoryCrafting;
  }

  @Override
  public void setInventoryCrafting(boolean value) {
    hasInventoryCrafting = value;
  }

  @Override
  public boolean hasInventoryExtended() {
    return hasInventoryExtended;
  }

  @Override
  public void setInventoryExtended(boolean value) {
    hasInventoryExtended = value;
  }

  @Override
  public NBTTagCompound getDataAsNBT() {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setByte(IS_SLEEPING, (byte) (this.isSleeping() ? 1 : 0));
    tags.setByte(HAS_INVENTORY_CRAFTING, (byte) (this.hasInventoryCrafting() ? 1 : 0));
    tags.setByte(HAS_INVENTORY_EXTENDED, (byte) (this.hasInventoryExtended() ? 1 : 0));
    tags.setString(NBT_TODO, this.getTODO());
    tags.setInteger(MHEALTH, this.getMaxHealth());
    tags.setBoolean(KEY_BOOLEAN, this.isChorusSpectator);
    tags.setString(KEY_EATLOC, UtilNBT.posToStringCSV(this.chorusStart));
    tags.setInteger(KEY_EATDIM, this.chorusDim);
    tags.setInteger(KEY_TIMER, this.chorusSeconds);
    tags.setBoolean(KEY_STEP, this.isStepOn);
    tags.setBoolean(KEY_STEPFORCE, this.foreStepHeightOff);
    tags.setInteger(KEY_FLYING, this.flyingSeconds);
    return tags;
  }

  @Override
  public void setDataFromNBT(NBTTagCompound nbt) {
    NBTTagCompound tags;
    if (nbt instanceof NBTTagCompound == false) {
      tags = new NBTTagCompound();
    }
    else {
      tags = (NBTTagCompound) nbt;
    }
    this.setFlyingTimer(tags.getInteger(KEY_FLYING));
    this.setSleeping(tags.getByte(IS_SLEEPING) == 1);
    this.setInventoryCrafting(tags.getByte(HAS_INVENTORY_CRAFTING) == 1);
    this.setInventoryExtended(tags.getByte(HAS_INVENTORY_EXTENDED) == 1);
    this.setTODO(tags.getString(NBT_TODO));
    this.setMaxHealth(tags.getInteger(MHEALTH));
    this.setChorusDim(tags.getInteger(KEY_EATDIM));
    this.setChorusTimer(tags.getInteger(KEY_TIMER));
    this.setChorusOn(tags.getBoolean(KEY_BOOLEAN));
    this.setStepHeightOn(tags.getBoolean(KEY_STEP));
    this.setForceStepOff(tags.getBoolean(KEY_STEPFORCE));
    String posCSV = tags.getString(KEY_EATLOC);
    if (posCSV != null && posCSV.length() > 0) {
      String[] p = posCSV.split(",");
      if (p != null && p.length == 3)
        this.setChorusStart(new BlockPos(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2])));
    }
  }

  @Override
  public String getTODO() {
    return todo;
  }

  @Override
  public void setTODO(String value) {
    todo = value;
  }

  @Override
  public int getMaxHealth() {
    return health;
  }

  @Override
  public void setMaxHealth(int value) {
    health = value;
  }

  @Override
  public boolean getChorusOn() {
    return this.isChorusSpectator;
  }

  @Override
  public void setChorusOn(boolean f) {
    this.isChorusSpectator = f;
  }

  @Override
  public BlockPos getChorusStart() {
    return this.chorusStart;
  }

  @Override
  public void setChorusStart(BlockPos s) {
    this.chorusStart = s;
  }

  @Override
  public int getChorusDim() {
    return this.chorusDim;
  }

  @Override
  public void setChorusDim(int d) {
    this.chorusDim = d;
  }

  @Override
  public int getChorusTimer() {
    return this.chorusSeconds;
  }

  @Override
  public void setChorusTimer(int d) {
    this.chorusSeconds = d;
  }

  @Override
  public boolean isStepHeightOn() {
    return this.isStepOn;
  }

  @Override
  public void setStepHeightOn(boolean b) {
    this.isStepOn = b;
  }

  @Override
  public boolean doForceStepOff() {
    return this.foreStepHeightOff;
  }

  @Override
  public void setForceStepOff(boolean b) {
    this.foreStepHeightOff = b;
  }

  @Override
  public int getFlyingTimer() {
    return this.flyingSeconds;
  }

  @Override
  public void setFlyingTimer(int d) {
    this.flyingSeconds = d;
  }
}