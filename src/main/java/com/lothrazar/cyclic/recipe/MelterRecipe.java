package com.lothrazar.cyclic.recipe;

import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class MelterRecipe<TileMelter> extends CyclicRecipe {

  private ItemStack in;
  private FluidStack out;
  private ItemStack outOptional;

  protected MelterRecipe(ResourceLocation id, ItemStack in, FluidStack out, ItemStack outOptional) {
    super(id);
    this.in = in;
    this.out = out;
    this.outOptional = outOptional;
  }

  @Override
  public boolean matches(TileEntityBase inv, World worldIn) {
    try {
      TileMelter tile = (TileMelter) inv;
      ///
      return true;
    }
    catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public IRecipeType<?> getType() {
    return CyclicRecipeType.MELTER;
  }
}
