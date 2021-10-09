package com.lothrazar.cyclic.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandlerCapabilityStack implements IFluidHandlerItem, ICapabilityProvider {

  public static final String FLUID_NBT_KEY = FluidHandlerItemStack.FLUID_NBT_KEY;
  private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);
  protected ItemStack container;
  protected int capacity;

  /**
   * @param container
   *          The container itemStack, data is stored on it directly as NBT.
   * @param capacity
   *          The maximum capacity of this fluid tank.
   */
  public FluidHandlerCapabilityStack(ItemStack container, int capacity) {
    this.container = container;
    this.capacity = capacity;
  }

  @Override
  public ItemStack getContainer() {
    return container;
  }

  public FluidStack getFluid() {
    CompoundTag tagCompound = container.getTag();
    if (tagCompound == null || !tagCompound.contains(FLUID_NBT_KEY)) {
      return FluidStack.EMPTY;
    }
    return FluidStack.loadFluidStackFromNBT(tagCompound.getCompound(FLUID_NBT_KEY));
  }

  public void setFluid(FluidStack fluid) {
    if (!container.hasTag()) {
      container.setTag(new CompoundTag());
    }
    CompoundTag fluidTag = new CompoundTag();
    fluid.writeToNBT(fluidTag);
    container.getTag().put(FLUID_NBT_KEY, fluidTag);
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @Override
  public FluidStack getFluidInTank(int tank) {
    return getFluid();
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
  public int fill(FluidStack resource, FluidAction doFill) {
    if (container.getCount() != 1 || resource.isEmpty() || !canFillFluidType(resource)) {
      return 0;
    }
    FluidStack contained = getFluid();
    if (contained.isEmpty()) {
      int fillAmount = Math.min(capacity, resource.getAmount());
      if (doFill.execute()) {
        FluidStack filled = resource.copy();
        filled.setAmount(fillAmount);
        setFluid(filled);
      }
      return fillAmount;
    }
    else {
      if (contained.isFluidEqual(resource)) {
        int fillAmount = Math.min(capacity - contained.getAmount(), resource.getAmount());
        if (doFill.execute() && fillAmount > 0) {
          contained.grow(fillAmount);
          setFluid(contained);
        }
        return fillAmount;
      }
      return 0;
    }
  }

  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    if (container.getCount() != 1 || resource.isEmpty() || !resource.isFluidEqual(getFluid())) {
      return FluidStack.EMPTY;
    }
    return drain(resource.getAmount(), action);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    if (container.getCount() != 1 || maxDrain <= 0) {
      return FluidStack.EMPTY;
    }
    FluidStack contained = getFluid();
    if (contained.isEmpty() || !canDrainFluidType(contained)) {
      return FluidStack.EMPTY;
    }
    final int drainAmount = Math.min(contained.getAmount(), maxDrain);
    FluidStack drained = contained.copy();
    drained.setAmount(drainAmount);
    if (action.execute()) {
      contained.shrink(drainAmount);
      if (contained.isEmpty()) {
        setContainerToEmpty();
      }
      else {
        setFluid(contained);
      }
    }
    return drained;
  }

  public boolean canFillFluidType(FluidStack fluid) {
    return true;
  }

  public boolean canDrainFluidType(FluidStack fluid) {
    return true;
  }

  /**
   * Override this method for special handling. Can be used to swap out or destroy the container.
   */
  protected void setContainerToEmpty() {
    container.removeTagKey(FLUID_NBT_KEY);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
    if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY == capability) {
      return holder.cast();
    }
    return LazyOptional.empty();
  }
}
