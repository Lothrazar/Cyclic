package com.lothrazar.cyclic.capabilities.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

public class PlayerCapabilityStorage {

  public static final Codec<PlayerCapabilityStorage> CODEC = RecordCodecBuilder.create(inst -> inst.group(
      Codec.INT.fieldOf("mana").forGetter(PlayerCapabilityStorage::getMana)
  ).apply(inst, PlayerCapabilityStorage::new));

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