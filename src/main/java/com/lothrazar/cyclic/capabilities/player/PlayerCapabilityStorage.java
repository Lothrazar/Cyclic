package com.lothrazar.cyclic.capabilities.player;

import net.minecraft.nbt.CompoundTag;

public class PlayerCapabilityStorage {

  int mana;

  public PlayerCapabilityStorage(int readInt) {
    mana = readInt;
  }

  public PlayerCapabilityStorage() {}

  public int getMana() {
    return mana;
  }

  public void setMana(int mana) {
    this.mana = mana;
  }

  public void addMana(int mana) {
    this.mana += mana;
  }

  public void copyFrom(PlayerCapabilityStorage source) {
    mana = source.mana;
  }

  public void saveNBTData(CompoundTag compound) {
    compound.putInt("mana", mana);
  }

  public void loadNBTData(CompoundTag compound) {
    mana = compound.getInt("mana");
  }
}