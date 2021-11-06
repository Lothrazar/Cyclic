package com.lothrazar.cyclic.recipe;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

public class FluidTagIngredient {

  private final FluidStack fluid;
  private final String tag;

  public FluidTagIngredient(FluidStack fs, String str) {
    fluid = (fs == null) ? FluidStack.EMPTY : fs;
    tag = (str == null) ? "" : str;
  }

  public FluidStack getFluidStack() {
    return fluid;
  }

  public String getTag() {
    return tag;
  }

  public boolean hasFluid() {
    return !fluid.isEmpty();
  }

  public boolean hasTag() {
    return !tag.isEmpty();
  }

  public static FluidTagIngredient readFromPacket(PacketBuffer buffer) {
    return new FluidTagIngredient(FluidStack.readFromPacket(buffer), buffer.readString());
  }

  public void writeToPacket(PacketBuffer buffer) {
    fluid.writeToPacket(buffer);
    buffer.writeString(tag);
  }
}
