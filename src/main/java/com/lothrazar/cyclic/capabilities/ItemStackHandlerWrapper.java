package com.lothrazar.cyclic.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Wraps two {@link ItemStackHandler}s: Input and Output. Input's slots come first then the Output's slots come after. Items can only be inserted into Input. Items can only be extracted from Output.
 * Note that the above only applies to operations on the wrapper, the backing handlers are not restricted. For persistence, either the backing {@link ItemStackHandler}s can be saved, or the wrapper
 * itself.
 */
public class ItemStackHandlerWrapper implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {

  public static final String NBT_INPUT = "Input";
  public static final String NBT_OUTPUT = "Output";
  protected final ItemStackHandler input;
  protected final ItemStackHandler output;

  public ItemStackHandlerWrapper(ItemStackHandler input, ItemStackHandler output) {
    this.input = input;
    this.output = output;
  }

  /**
   * Calls with the correct handler, slot for the handler and if it matches the input handler.
   */
  protected <T> T withHandler(int externalSlot, HandlerCallback<T> callback) {
    int numInputSlots = input.getSlots();
    boolean isInput = externalSlot < numInputSlots;
    int internalSlot = isInput ? externalSlot : externalSlot - numInputSlots;
    ItemStackHandler handler = isInput ? input : output;
    return callback.apply(handler, internalSlot, isInput);
  }

  /**
   * For functions that return void.
   *
   * @see ItemStackHandlerWrapper#withHandler(int, HandlerCallback)
   */
  protected void withHandlerV(int slot, HandlerCallbackVoid func) {
    withHandler(slot, (h, s, isInput) -> {
      func.apply(h, s, isInput);
      return false; // Because generics can't be void >.<
    });
  }

  @Override
  public int getSlots() {
    return input.getSlots() + output.getSlots();
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return withHandler(slot, (h, s, isInput) -> h.getStackInSlot(s));
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    return withHandler(slot, (h, s, isInput) -> isInput ? h.insertItem(s, stack, simulate) : stack);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return withHandler(slot, (h, s, isInput) -> isInput ? ItemStack.EMPTY : h.extractItem(s, amount, simulate));
  }

  @Override
  public int getSlotLimit(int slot) {
    return withHandler(slot, (h, s, isInput) -> h.getSlotLimit(s));
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return withHandler(slot, (h, s, isInput) -> isInput && h.isItemValid(s, stack));
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    withHandlerV(slot, (h, s, isInput) -> h.setStackInSlot(s, stack));
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag cmp = new CompoundTag();
    cmp.put(NBT_INPUT, input.serializeNBT());
    cmp.put(NBT_OUTPUT, output.serializeNBT());
    return cmp;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    input.deserializeNBT(nbt.getCompound(NBT_INPUT));
    output.deserializeNBT(nbt.getCompound(NBT_OUTPUT));
  }

  @FunctionalInterface
  protected interface HandlerCallback<T> {

    T apply(ItemStackHandler handler, int slot, boolean isInput);
  }

  @FunctionalInterface
  protected interface HandlerCallbackVoid {

    void apply(ItemStackHandler handler, int slot, boolean isInput);
  }
}
