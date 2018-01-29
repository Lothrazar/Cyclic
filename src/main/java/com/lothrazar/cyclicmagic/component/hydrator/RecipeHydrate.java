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
  public RecipeHydrate(ItemStack[] in, ItemStack out, int w) {
    this(in, out, false);
    this.fluidCost = w;
  }
  public RecipeHydrate(ItemStack[] in, ItemStack out) {
    this(in, out, false);
  }
  public RecipeHydrate(ItemStack[] in, ItemStack out, boolean shapeless) {
    this.isShapeless = shapeless;
    if (in.length == 1) {
      //redirect to shapeless
      this.isShapeless = true;
      this.recipeInput = new ItemStack[] { in[0], ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY };
    }
    else if (in.length != 4) {
      throw new IllegalArgumentException("Input array must be length 1 or length 4");
    }
    else {
      this.recipeInput = in;
    }
    ModCyclic.logger.info("Hydrator recipe for " + out.getDisplayName() + " is shapeless? " + this.isShapeless);
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
      return recipeSlotMatches(s0, theRecipeStack) ||
          recipeSlotMatches(s1, theRecipeStack) ||
          recipeSlotMatches(s2, theRecipeStack) ||
          recipeSlotMatches(s3, theRecipeStack);
    }
    else {
      //hacky lame way but easier to read and debug with all these lines
      return recipeSlotMatches(s0, recipeInput[0]) &&
          recipeSlotMatches(s1, recipeInput[1]) &&
          recipeSlotMatches(s2, recipeInput[2]) &&
          recipeSlotMatches(s3, recipeInput[3]);
    }
  }
  private boolean recipeSlotMatches(ItemStack sInvo, ItemStack sRecipe) {
    return OreDictionary.itemMatches(sInvo, sRecipe, false)
        && sInvo.getCount() >= sRecipe.getCount();
  }
  public boolean tryPayCost(IInventory invoSource, FluidTank tank, boolean keepOneMinimum) {
    if (tank.getFluidAmount() < this.getFluidCost()) {
      return false;//not enough fluid, so stop now
    }
    //if minimum is 2, then the recipe slots always stay locked with at least 1 in each spot
    //otherwise its allowed to drain empty
    int minimum = (keepOneMinimum) ? 2 : 1;
    //TODO: merge/codeshare between shaped and shapeless!
    int inputStackToPay = -1, inputCountToPay = -1;
    if (this.isShapeless()) {
      //find the one input slot has the thing, and decrement THAT ONE only 
      for (int i = 0; i < recipeInput.length; i++) {
        //its shapeless so only one thing will have the input
        if (invoSource.getStackInSlot(i).getCount() >= minimum
            && invoSource.getStackInSlot(i).getCount() >= recipeInput[i].getCount() + (minimum - 1)) {
          inputStackToPay = i;
          //VALIDATE
          inputCountToPay = recipeInput[i].getCount();
          break;
        }
      }
      if (inputStackToPay < 0) {
        return false;
      }
      invoSource.decrStackSize(inputStackToPay, inputCountToPay);
    }
    else {
      //first test before we try to pay
      for (int i = 0; i < recipeInput.length; i++) {
        if ( invoSource.getStackInSlot(i).getCount() < recipeInput[i].getCount() + (minimum - 1)) {
          return false;//at least one of the stacks cannot pay
        }
      }
      //now actually pay, since they all can
      for (int i = 0; i < recipeInput.length; i++) {
        invoSource.decrStackSize(i, recipeInput[i].getCount());
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
