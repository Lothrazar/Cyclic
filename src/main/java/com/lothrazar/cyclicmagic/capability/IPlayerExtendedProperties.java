package com.lothrazar.cyclicmagic.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface IPlayerExtendedProperties {

  boolean isSleeping();

  void setSleeping(boolean value);

  boolean hasInventoryCrafting();

  void setInventoryCrafting(boolean value);

  boolean hasInventoryExtended();

  void setInventoryExtended(boolean value);

  int getMaxHealth();

  void setMaxHealth(int value);

  NBTTagCompound getDataAsNBT();

  void setDataFromNBT(NBTTagCompound nbt);

  String getTODO();

  void setTODO(String value);

  //for ItemFoodChorusCorrupted; save persistently here so entityData doesnt forget
  boolean getChorusOn();

  void setChorusOn(boolean f);

  BlockPos getChorusStart();

  void setChorusStart(BlockPos s);

  int getChorusDim();

  void setChorusDim(int d);

  int getChorusTimer();

  void setChorusTimer(int d);

  boolean isStepHeightOn();

  void setStepHeightOn(boolean b);

  boolean doForceStepOff();

  void setForceStepOff(boolean b);

  int getFlyingTimer();

  void setFlyingTimer(int d);
}