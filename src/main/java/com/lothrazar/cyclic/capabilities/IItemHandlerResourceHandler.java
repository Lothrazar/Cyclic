package com.lothrazar.cyclic.capabilities;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// Bridges the old deprecated-but-functional IItemHandler to the new ResourceHandler<ItemResource>
// capability registration API. Not transaction-aware (neither was IItemHandler), executes immediately.
public class IItemHandlerResourceHandler implements ResourceHandler<ItemResource> {

  private final IItemHandler wrapped;

  public IItemHandlerResourceHandler(IItemHandler wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public int size() {
    return wrapped.getSlots();
  }

  @Override
  public ItemResource getResource(int index) {
    return ItemResource.of(wrapped.getStackInSlot(index));
  }

  @Override
  public long getAmountAsLong(int index) {
    return wrapped.getStackInSlot(index).getCount();
  }

  @Override
  public long getCapacityAsLong(int index, ItemResource resource) {
    return wrapped.getSlotLimit(index);
  }

  @Override
  public boolean isValid(int index, ItemResource resource) {
    return wrapped.isItemValid(index, resource.toStack(1));
  }

  @Override
  public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
    ItemStack toInsert = resource.toStack(amount);
    ItemStack remaining = wrapped.insertItem(index, toInsert, false);
    return amount - remaining.getCount();
  }

  @Override
  public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
    ItemStack extracted = wrapped.extractItem(index, amount, false);
    return extracted.getCount();
  }
}
