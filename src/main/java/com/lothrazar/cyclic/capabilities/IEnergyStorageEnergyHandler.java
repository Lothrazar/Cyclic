package com.lothrazar.cyclic.capabilities;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.RootCommitJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// Bridges the old deprecated-but-functional IEnergyStorage to the new EnergyHandler
// capability registration API. IEnergyStorage has no snapshot/rollback support of its own, so the real
// mutation (simulate=false) is deferred via RootCommitJournal until the transaction actually commits;
// insert/extract only ever probe with simulate=true, otherwise every simulate-only caller would silently
// mutate the handler for real (see the identical bug fixed in IFluidHandlerResourceHandler).
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
    int simulated = wrapped.receiveEnergy(amount, true);
    if (simulated > 0) {
      new RootCommitJournal(() -> wrapped.receiveEnergy(simulated, false)).updateSnapshots(transaction);
    }
    return simulated;
  }

  @Override
  public int extract(int amount, TransactionContext transaction) {
    int simulated = wrapped.extractEnergy(amount, true);
    if (simulated > 0) {
      new RootCommitJournal(() -> wrapped.extractEnergy(simulated, false)).updateSnapshots(transaction);
    }
    return simulated;
  }
}
