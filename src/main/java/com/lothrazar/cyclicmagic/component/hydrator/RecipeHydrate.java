package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.ModCyclic;
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
  private ItemStack[] recipeInput = new ItemStack[4];
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
    ModCyclic.logger.info("Hydrator recipe for " + out.getDisplayName() + " is shapeless? " + this.isShapeless);
    this.recipeInput = in;
    this.resultItem = out;
    this.setRegistryName(new ResourceLocation(Const.MODID, "hydrate_" + id + out.getUnlocalizedName()));
    id++;
  }
  public boolean isShapeless() {
    return this.isShapeless;
  }
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    ItemStack s0 = inv.getStackInSlot(0);
    ItemStack s1 = inv.getStackInSlot(1);
    ItemStack s2 = inv.getStackInSlot(2);
    ItemStack s3 = inv.getStackInSlot(3);
    if (this.isShapeless()) {
      ItemStack theRecipeStack = recipeInput[0];
      return OreDictionary.itemMatches(s0, theRecipeStack, false) ||
          OreDictionary.itemMatches(s1, theRecipeStack, false) ||
          OreDictionary.itemMatches(s2, theRecipeStack, false) ||
          OreDictionary.itemMatches(s3, theRecipeStack, false);
    }
    else {
      //hacky lame way but easier to read and debug with all these lines
      return OreDictionary.itemMatches(s0, recipeInput[0], false) &&
          OreDictionary.itemMatches(s1, recipeInput[1], false) &&
          OreDictionary.itemMatches(s2, recipeInput[2], false) &&
          OreDictionary.itemMatches(s3, recipeInput[3], false);
    }
  }
  public boolean tryPayCost(IInventory invoSource, FluidTank tank, boolean keepOneMinimum) {
    if(tank.getFluidAmount() < this.getFluidCost()){
      return false;//not enough fluid, so stop now
    }
    //if minimum is 2, then the recipe slots always stay locked with at least 1 in each spot
    //otherwise its allowed to drain empty
    int minimum = (keepOneMinimum) ? 2 : 1;
    if (this.isShapeless()) {
      //find the one input slot has the thing, and decrement THAT ONE only 
      int inputStackToPay = -1;
      for (int i = 0; i < recipeInput.length; i++) {
        //its shapeless so only one thing will have the input
        if (invoSource.getStackInSlot(i).getCount() >= minimum) {
          inputStackToPay = i;
          break;
        }
      }
      if (inputStackToPay < 0) {
        return false;
      }
      invoSource.decrStackSize(inputStackToPay, 1);
    }
    else {
      //first test before we try to pay
      for (int i = 0; i < recipeInput.length; i++) {
        if (invoSource.getStackInSlot(i).getCount() < minimum) {
          return false;//at least one of the stacks cannot pay
        }
      }
      //now actually pay, since they all can
      for (int i = 0; i < recipeInput.length; i++) {
        invoSource.decrStackSize(i, 1);
      }
    }
    //pay fluid last. same for shaped and shapeless
    tank.drain(this.getFluidCost(), true);
    return true;
  }
  public boolean isSlotEmpty(int slot) {
    ItemStack inv = recipeInput[slot];
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
    return recipeInput.clone();
  }
  public int getFluidCost() {
    return fluidCost;
  }
  public void setFluidCost(int fluidCost) {
    this.fluidCost = fluidCost;
  }
}
