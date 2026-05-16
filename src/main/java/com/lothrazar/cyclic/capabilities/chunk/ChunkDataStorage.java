package com.lothrazar.cyclic.capabilities.chunk;

import net.minecraft.nbt.CompoundTag;

public class ChunkDataStorage {

  int mana;

  public ChunkDataStorage(int mana) {
    this.mana = mana;
  }

  public ChunkDataStorage(CompoundTag tag) {
    mana = tag.getInt("mana");
  }

  public void save(CompoundTag manaTag) {
    manaTag.putInt("mana", this.getMana());
  }

  public int getMana() {
    return mana;
  }

  public void setMana(int mana) {
    this.mana = mana;
  }
}