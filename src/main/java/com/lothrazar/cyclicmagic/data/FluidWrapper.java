package com.lothrazar.cyclicmagic.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class FluidWrapper {

  private FluidStack stack;
  private int x;
  private int y;

  public FluidWrapper(FluidStack stack) {
    this.stack = stack;
  }

  public FluidWrapper() {
    this(null);
  }

  public void readFromNBT(NBTTagCompound compound) {
    NBTTagCompound c = compound.getCompoundTag("stack");
    stack = FluidStack.loadFluidStackFromNBT(c);
  }

  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagCompound c = new NBTTagCompound();
    if (stack != null) {
      FluidStack copy = stack.copy();
      copy.writeToNBT(c);
    }
    compound.setTag("stack", c);
    return compound;
  }

  @Override
  public String toString() {
    return "StackWrapper: stack=" + stack.toString()
        + ";x=" + getX() + ";y=" + getY();
  }

  public FluidStack getStack() {
    return stack;
  }

  public void setStack(FluidStack stack) {
    this.stack = stack;
    //    this.setCount(stack.getCount());
    //    if (stack.isEmpty()) {
    //      this.setCount(0);//non zero stacks can be empty IE null or Air 
    //    }
  }

  public static FluidWrapper loadStackWrapperFromNBT(NBTTagCompound nbt) {
    FluidWrapper wrap = new FluidWrapper();
    wrap.readFromNBT(nbt);
    if (wrap.getStack() == null) {
      return null;
    }
    return wrap;
  }

  public boolean isEmpty() {
    return stack == null;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
