package com.lothrazar.cyclic.capabilities.player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

  public static Capability<PlayerCapabilityStorage> CYCLIC_PLAYER = CapabilityManager.get(new CapabilityToken<>() {});
  private PlayerCapabilityStorage playerMana = null;
  private final LazyOptional<PlayerCapabilityStorage> opt = LazyOptional.of(this::createMe);

  @Nonnull
  private PlayerCapabilityStorage createMe() {
    if (playerMana == null) {
      playerMana = new PlayerCapabilityStorage();
    }
    return playerMana;
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
    if (cap == CYCLIC_PLAYER) {
      return opt.cast();
    }
    return LazyOptional.empty();
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    return getCapability(cap);
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    createMe().saveNBTData(nbt);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    createMe().loadNBTData(nbt);
  }
}
