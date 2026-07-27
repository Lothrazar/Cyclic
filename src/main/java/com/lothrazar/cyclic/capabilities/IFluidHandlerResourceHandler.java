package com.lothrazar.cyclic.capabilities;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.RootCommitJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// Bridges the old deprecated-but-functional IFluidHandler to the new ResourceHandler<FluidResource>
// capability registration API. IFluidHandler has no snapshot/rollback support of its own, so the real
// EXECUTE mutation is deferred via RootCommitJournal until the transaction actually commits; insert/extract
// only ever probe with FluidAction.SIMULATE, otherwise every simulate-only caller (e.g. FluidUtil's
// fill-then-drain protocol used for bucket/tank interaction) would silently mutate the handler for real.
public class IFluidHandlerResourceHandler implements ResourceHandler<FluidResource> {

  private final IFluidHandler wrapped;

  public IFluidHandlerResourceHandler(IFluidHandler wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public int size() {
    return wrapped.getTanks();
  }

  @Override
  public FluidResource getResource(int index) {
    return FluidResource.of(wrapped.getFluidInTank(index));
  }

  @Override
  public long getAmountAsLong(int index) {
    return wrapped.getFluidInTank(index).getAmount();
  }

  @Override
  public long getCapacityAsLong(int index, FluidResource resource) {
    return wrapped.getTankCapacity(index);
  }

  @Override
  public boolean isValid(int index, FluidResource resource) {
    return wrapped.isFluidValid(index, resource.toStack(1));
  }

  @Override
  public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
    FluidStack toFill = resource.toStack(amount);
    int simulated = wrapped.fill(toFill, FluidAction.SIMULATE);
    if (simulated > 0) {
      new RootCommitJournal(() -> wrapped.fill(toFill.copyWithAmount(simulated), FluidAction.EXECUTE)).updateSnapshots(transaction);
    }
    return simulated;
  }

  @Override
  public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
    FluidStack toDrain = resource.toStack(amount);
    FluidStack simulated = wrapped.drain(toDrain, FluidAction.SIMULATE);
    if (!simulated.isEmpty()) {
      FluidStack drainedStack = simulated.copy();
      new RootCommitJournal(() -> wrapped.drain(drainedStack, FluidAction.EXECUTE)).updateSnapshots(transaction);
    }
    return simulated.getAmount();
  }
}
