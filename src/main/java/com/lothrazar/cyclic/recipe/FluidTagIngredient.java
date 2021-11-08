package com.lothrazar.cyclic.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;
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
 
    for (Map.Entry<ResourceLocation,Tag<Fluid>> fluidTag : FluidTags.getAllTags().getAllTags().entrySet()) {
      if (fluidTag.getKey().toString().equalsIgnoreCase(tag)) {
        //add all fluids for tag?
        return fluidTag.getValue().getValues();
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

  public static FluidTagIngredient readFromPacket(FriendlyByteBuf buffer) { 
    return new FluidTagIngredient(FluidStack.readFromPacket(buffer), buffer.readUtf(), buffer.readInt());
  }

  public void writeToPacket(FriendlyByteBuf buffer) {
    fluid.writeToPacket(buffer);
    buffer.writeUtf(tag);
    buffer.writeInt(amount);
  }

  public int getAmount() {
    return amount;
  }
}
