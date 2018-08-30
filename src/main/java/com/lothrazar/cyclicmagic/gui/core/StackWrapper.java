package com.lothrazar.cyclicmagic.gui.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StackWrapper {

  private ItemStack stack = ItemStack.EMPTY;
  private int x;
  private int y;

  public StackWrapper(ItemStack stack) {
    this.stack = stack;
  }

  public StackWrapper() {
    this(ItemStack.EMPTY);
  }

  public void readFromNBT(NBTTagCompound compound) {
    NBTTagCompound c = compound.getCompoundTag("stack");
    stack = new ItemStack(c);
  }

  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagCompound c = new NBTTagCompound();
    ItemStack copy = stack.copy();
    copy.writeToNBT(c);
    compound.setTag("stack", c);
    return compound;
  }

  @Override
  public String toString() {
    return "StackWrapper: stack=" + stack.getDisplayName()
        + ";x=" + getX() + ";y=" + getY();
  }

  //  @Override
  //  public boolean equals(Object obj) {
  //    if (!(obj instanceof StackWrapper)) {
  //      return false;
  //    }
  //    StackWrapper o = (StackWrapper) obj;
  //    return o.stack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(o.stack, stack);
  //  }
  public ItemStack getStack() {
    return stack;
  }

  public void setStack(ItemStack stack) {
    this.stack = stack;
    this.setCount(stack.getCount());
    if (stack.isEmpty()) {
      this.setCount(0);//non zero stacks can be empty IE null or Air 
    }
  }

  public int getCount() {
    return stack.getCount();
  }

  public void setCount(int size) {
    this.stack.setCount(size);
  }

  //  public StackWrapper copy() {
  //    return new StackWrapper(stack.copy());
  //  }
  public static StackWrapper loadStackWrapperFromNBT(NBTTagCompound nbt) {
    StackWrapper wrap = new StackWrapper();
    wrap.readFromNBT(nbt);
    if (wrap.getStack() == null || wrap.getStack().getItem() == null) {
      return null;
    }
    return wrap;
  }

  public boolean isEmpty() {
    return stack.isEmpty();
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
