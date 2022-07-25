package com.lothrazar.cyclic.capabilities.player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.capabilities.ManaManager;
import com.lothrazar.cyclic.capabilities.ManaManager.PlayerCapabilityStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerManaProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

  public static Capability<PlayerCapabilityStorage> PLAYER_MANA = CapabilityManager.get(new CapabilityToken<>() {});
  private PlayerCapabilityStorage playerMana = null;
  private final LazyOptional<ManaManager.PlayerCapabilityStorage> opt = LazyOptional.of(this::createPlayerMana);

  @Nonnull
  private PlayerCapabilityStorage createPlayerMana() {
    if (playerMana == null) {
      playerMana = new ManaManager.PlayerCapabilityStorage();
    }
    return playerMana;
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
    if (cap == PLAYER_MANA) {
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
    createPlayerMana().saveNBTData(nbt);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    createPlayerMana().loadNBTData(nbt);
  }
}
