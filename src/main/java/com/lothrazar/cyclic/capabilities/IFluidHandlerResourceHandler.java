package com.lothrazar.cyclic.capabilities;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// Bridges the old deprecated-but-functional IFluidHandler to the new ResourceHandler<FluidResource>
// capability registration API. Executes immediately (not transaction-aware) since IFluidHandler
// never supported rollback either.
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
    return wrapped.fill(resource.toStack(amount), FluidAction.EXECUTE);
  }

  @Override
  public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
    FluidStack drained = wrapped.drain(resource.toStack(amount), FluidAction.EXECUTE);
    return drained.getAmount();
  }
}
