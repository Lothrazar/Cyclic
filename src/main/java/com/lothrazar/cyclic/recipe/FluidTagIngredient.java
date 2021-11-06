package com.lothrazar.cyclic.recipe;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraftforge.fluids.FluidStack;

public class FluidTagIngredient {

  private final FluidStack fluid;
  private final String tag;
  private final int amount; // copy of fluidstack count used for tags

  public FluidTagIngredient(FluidStack fs, String str, int cnt) {
    fluid = (fs == null) ? FluidStack.EMPTY : fs;
    tag = (str == null) ? "" : str;
    amount = cnt;
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

  public List<Fluid> list() {
    if (!hasTag()) {
      return null;
    }
    for (INamedTag<Fluid> fluidTag : FluidTags.getAllTags()) {
      if (fluidTag.getName().toString().equalsIgnoreCase(tag)) {
        //add all fluids for tag?
        return fluidTag.getAllElements();
      }
    }
    return null;
  }

  /**
   * create fluidstacks for all fluids matching the tag. if hastag
   * 
   * @return
   */
  public List<FluidStack> getMatchingFluids() {
    List<Fluid> fluids = list();
    if (fluids == null) {
      return null;
    }
    List<FluidStack> me = new ArrayList<>();
    for (Fluid f : fluids) {
      me.add(new FluidStack(f, this.amount));
    }
    return me;
  }

  public static FluidTagIngredient readFromPacket(PacketBuffer buffer) {
    return new FluidTagIngredient(FluidStack.readFromPacket(buffer), buffer.readString(), buffer.readInt());
  }

  public void writeToPacket(PacketBuffer buffer) {
    fluid.writeToPacket(buffer);
    buffer.writeString(tag);
    buffer.writeInt(amount);
  }

  public int getAmount() {
    return amount;
  }
}
