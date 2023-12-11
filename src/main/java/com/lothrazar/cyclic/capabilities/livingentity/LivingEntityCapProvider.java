package com.lothrazar.cyclic.capabilities.livingentity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LivingEntityCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

  public static Capability<LivingEntityCapabilityStorage> CYCLIC_LIVING_ENTITY = CapabilityManager.get(new CapabilityToken<>() {});
  private LivingEntityCapabilityStorage livingEntityAntiBeaconPosition = null;
  private final LazyOptional<LivingEntityCapabilityStorage> opt = LazyOptional.of(this::createMe);

  @Nonnull
  private LivingEntityCapabilityStorage createMe() {
    if (livingEntityAntiBeaconPosition == null) {
      livingEntityAntiBeaconPosition = new LivingEntityCapabilityStorage();
    }
    return livingEntityAntiBeaconPosition;
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
    if (cap == CYCLIC_LIVING_ENTITY) {
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
