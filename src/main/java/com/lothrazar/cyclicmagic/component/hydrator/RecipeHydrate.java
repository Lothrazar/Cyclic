package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHydrate extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private static int id = 0;
  private ItemStack[] input = new ItemStack[4];
  private ItemStack resultItem = ItemStack.EMPTY;
  private int fluidCost = 25;
  /**
   * typically, size 1x1 is shapeless, and 2x2 is not shapeless
   */
  private boolean isShapeless;
  public RecipeHydrate(ItemStack in, ItemStack out) {
    this(new ItemStack[] { in, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY }, out, true);
  }
  public RecipeHydrate(ItemStack[] in, ItemStack out) {
    this(in, out, false);
  }
  public RecipeHydrate(ItemStack[] in, ItemStack out, boolean shapeless) {
    if (in.length != 4) {
      throw new IllegalArgumentException("Input array must be length 4");
    }
    this.isShapeless = shapeless;
    this.input = in;
    this.resultItem = out;
    this.setRegistryName(new ResourceLocation(Const.MODID, "hydrate_" + id + out.getUnlocalizedName()));
    id++;
  }
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    ItemStack s0 = inv.getStackInSlot(0);
    ItemStack s1 = inv.getStackInSlot(1);
    ItemStack s2 = inv.getStackInSlot(2);
    ItemStack s3 = inv.getStackInSlot(3);
    if (this.isShapeless) {
      ItemStack theRecipeStack = input[0];
      return OreDictionary.itemMatches(s0, theRecipeStack, false) ||
          OreDictionary.itemMatches(s1, theRecipeStack, false) ||
          OreDictionary.itemMatches(s2, theRecipeStack, false) ||
          OreDictionary.itemMatches(s3, theRecipeStack, false);
    }
    else {
      //hacky lame way but easier to read and debug with all these lines
      return OreDictionary.itemMatches(s0, input[0], false) &&
          OreDictionary.itemMatches(s1, input[1], false) &&
          OreDictionary.itemMatches(s2, input[2], false) &&
          OreDictionary.itemMatches(s3, input[3], false);
    }
  }
  public void payRecipeCost(IInventory invoSource, FluidTank tank) {
    tank.drain(this.getFluidCost(), true);
    if (this.isShapeless) {
      //find the one that has the thing, and decrement THAT ONE only 
      for (int i = 0; i < input.length; i++) {
        //its shapeless so only one thing will have the input
        if (this.isSlotEmpty(i) == false) {
          invoSource.decrStackSize(i, 1);
          break;
        }
      }
    }
    else {
      for (int i = 0; i < input.length; i++) {
        if (this.isSlotEmpty(i) == false) {
          invoSource.decrStackSize(i, 1);
        }
      }
    }
  }
  public boolean isSlotEmpty(int slot) {
    ItemStack inv = input[slot];
    return inv == null || inv.isEmpty();
  }
  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return resultItem.copy();
  }
  @Override
  public boolean canFit(int width, int height) {
    return (width <= 2 && height <= 2);
  }
  @Override
  public ItemStack getRecipeOutput() {
    return resultItem.copy();
  }
  public ItemStack[] getRecipeInput() {
    return input.clone();
  }
  public int getFluidCost() {
    return fluidCost;
  }
  public void setFluidCost(int fluidCost) {
    this.fluidCost = fluidCost;
  }
}
