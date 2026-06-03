package com.lothrazar.cyclic.capabilities.item;

import com.lothrazar.cyclic.util.RegistryHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * Item-level fluid capability backed by the host stack's CUSTOM_DATA component.
 * Reads/writes a single FluidStack under the "fluid" key so the contents persist
 * across place/break cycles.
 */
public class ItemFluidCap implements IFluidHandlerItem {

  private static final String KEY = "fluid";
  private final ItemStack host;
  private final int capacity;

  public ItemFluidCap(ItemStack host, int capacity) {
    this.host = host;
    this.capacity = capacity;
  }

  private FluidStack read() {
    var registries = RegistryHolder.get();
    if (registries == null) {
      return FluidStack.EMPTY;
    }
    CompoundTag data = host.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (!data.contains(KEY)) {
      return FluidStack.EMPTY;
    }
    return FluidStack.parseOptional(registries, data.getCompound(KEY));
  }

  private void write(FluidStack fs) {
    var registries = RegistryHolder.get();
    if (registries == null) {
      return;
    }
    CompoundTag data = host.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (fs.isEmpty()) {
      data.remove(KEY);
    }
    else {
      Tag saved = fs.save(registries);
      if (saved instanceof CompoundTag c) {
        data.put(KEY, c);
      }
    }
    host.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
  }

  @Override
  public ItemStack getContainer() {
    return host;
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return read();
  }

  @Override
  public int getTankCapacity(int tank) {
    return capacity;
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return true;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (resource.isEmpty()) {
      return 0;
    }
    FluidStack current = read();
    if (current.isEmpty()) {
      int amount = Math.min(resource.getAmount(), capacity);
      if (action.execute()) {
        FluidStack copy = resource.copy();
        copy.setAmount(amount);
        write(copy);
      }
      return amount;
    }
    if (!FluidStack.isSameFluidSameComponents(current, resource)) {
      return 0;
    }
    int free = capacity - current.getAmount();
    int amount = Math.min(free, resource.getAmount());
    if (amount > 0 && action.execute()) {
      current.grow(amount);
      write(current);
    }
    return amount;
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    FluidStack current = read();
    if (current.isEmpty() || !FluidStack.isSameFluidSameComponents(current, resource)) {
      return FluidStack.EMPTY;
    }
    return drain(resource.getAmount(), action);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    FluidStack current = read();
    if (current.isEmpty() || maxDrain <= 0) {
      return FluidStack.EMPTY;
    }
    int drained = Math.min(current.getAmount(), maxDrain);
    FluidStack result = current.copy();
    result.setAmount(drained);
    if (action.execute()) {
      current.shrink(drained);
      write(current);
    }
    return result;
  }
}
