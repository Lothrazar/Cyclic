package com.lothrazar.cyclic.capabilities;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// Bridges the old deprecated-but-functional IEnergyStorage to the new EnergyHandler
// capability registration API. Not transaction-aware (neither was IEnergyStorage), executes immediately.
public class IEnergyStorageEnergyHandler implements EnergyHandler {

  private final IEnergyStorage wrapped;

  public IEnergyStorageEnergyHandler(IEnergyStorage wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public long getAmountAsLong() {
    return wrapped.getEnergyStored();
  }

  @Override
  public long getCapacityAsLong() {
    return wrapped.getMaxEnergyStored();
  }

  @Override
  public int insert(int amount, TransactionContext transaction) {
    return wrapped.receiveEnergy(amount, false);
  }

  @Override
  public int extract(int amount, TransactionContext transaction) {
    return wrapped.extractEnergy(amount, false);
  }
}
