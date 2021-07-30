/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeSolidifier extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

  private static final int FLUID_DEFAULT = 25;
  public static ArrayList<RecipeSolidifier> recipes = new ArrayList<RecipeSolidifier>();
  private NonNullList<ItemStack> recipeInput = NonNullList.withSize(TileSolidifier.RECIPE_SIZE, ItemStack.EMPTY);// new ItemStack[4];
  private ItemStack resultItem = ItemStack.EMPTY;
  private int fluidCost = FLUID_DEFAULT;
  private String fluidString;
  Fluid fluidResult;
  private int size = 0;

  public RecipeSolidifier(ItemStack[] in, ItemStack out, String fluidName, int fluid) {
    fluidString = fluidName;
    this.setFluidResult(FluidRegistry.getFluid(fluidName));
    if (in.length > TileSolidifier.RECIPE_SIZE || in.length == 0) {
      throw new IllegalArgumentException("Input array must be length 4 or less");
    }
    //how many do we have 
    for (ItemStack itemStack : in) {
      if (itemStack != null && itemStack.isEmpty() == false) {
        recipeInput.set(size, itemStack);
        size++;
      }
    }
    this.fluidCost = fluid;
    this.resultItem = out;
    this.setRegistryName(new ResourceLocation(Const.MODID, "solidifier_" + UUID.randomUUID().toString() + out.getTranslationKey()));
  }

  public FluidStack getFluidIngredient() {
    return new FluidStack(this.fluidResult, this.fluidCost);
  }

  public void setFluidResult(Fluid fluidResult) {
    this.fluidResult = fluidResult;
  }

  public int getSize() {
    return size;
  }

  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    this.sanityCheckInput();
    int countFull = 0;
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      if (inv.getStackInSlot(i).isEmpty() == false) {
        countFull++;
      }
    }
    if (countFull != this.size) {
      //      ModCyclic.logger.info("hydrate recipe shortcut " + countNonEmpty + "_" + size);
      return false;
    }
    boolean match0 = recipeSlotMatches(inv.getStackInSlot(0), recipeInput.get(0));
    boolean match1 = recipeSlotMatches(inv.getStackInSlot(1), recipeInput.get(1));
    boolean match2 = recipeSlotMatches(inv.getStackInSlot(2), recipeInput.get(2));
    boolean match3 = recipeSlotMatches(inv.getStackInSlot(3), recipeInput.get(3));
    boolean all = match0 && match1 && match2 && match3;
    //    if (all) {
    //      ModCyclic.logger.info("MATCH" + this.getRecipeOutput() + this.size + "___" + countFull);
    //    }
    return all;
  }

  /**
   * Clean out errors/bad data. Example: Oak sapling that has metadata zero gets converted to 32767
   */
  private void sanityCheckInput() {
    for (int i = 0; i < recipeInput.size(); i++) {
      ItemStack s = recipeInput.get(i);
      if (s.isEmpty()) {
        continue;
      }
      if (s.getMetadata() == 32767) {
        ItemStack snew = new ItemStack(s.getItem(), 1, 0);
        snew.setTagCompound(s.getTagCompound());
        recipeInput.set(i, snew);
      }
    }
  }

  public static boolean recipeSlotMatches(ItemStack sInvo, ItemStack sRecipe) {
    if (sInvo.isEmpty() != sRecipe.isEmpty()) {
      return false;//empty matching empty
    }
    //if item matches, then we are fine. check ore dict as well after that if item doesnt match 
    return sInvo.getCount() >= sRecipe.getCount() &&
        (sInvo.isItemEqual(sRecipe)
            || OreDictionary.itemMatches(sInvo, sRecipe, false));
  }

  public boolean tryPayCost(IInventory invoSource, FluidTank tank, boolean keepOneMinimum) {
    if (tank.getFluidAmount() < this.getFluidCost()
        || tank.getFluid() == null
        || tank.getFluid().getFluid() != this.fluidResult) {
      return false;//not enough fluid, so stop now
    }
    //if minimum is 2, then the recipe slots always stay locked with at least 1 in each spot
    //otherwise its allowed to drain empty
    int minimum = (keepOneMinimum) ? 2 : 1;
    //TODO: merge/codeshare between shaped and shapeless!
    //  int inputStackToPay = -1, inputCountToPay = -1;
    //first test before we try to pay
    for (int i = 0; i < recipeInput.size(); i++) {
      if (recipeInput.get(i).isEmpty()) {
        continue; //pay zero
      }
      if (invoSource.getStackInSlot(i).getCount() < recipeInput.get(i).getCount() + (minimum - 1)) {
        return false;//at least one of the stacks cannot pay
      }
    }
    //now actually pay, since they all can
    for (int i = 0; i < recipeInput.size(); i++) {
      if (recipeInput.get(i).isEmpty()) {
        continue; //pay zero
      }
      invoSource.decrStackSize(i, recipeInput.get(i).getCount());
    }
    //    }
    //pay fluid last. same for shaped and shapeless
    tank.drain(this.getFluidCost(), true);
    return true;
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

  public List<ItemStack> getRecipeInput() {
    return recipeInput;
  }

  public int getFluidCost() {
    return fluidCost;
  }

  public void setFluidCost(int fluidCost) {
    this.fluidCost = fluidCost;
  }

  public static void initAllRecipes() {
    addRecipe(new RecipeSolidifier(new ItemStack[] {
        new ItemStack(Items.STICK)
    }, new ItemStack(Blocks.ICE),
        "water", 1000));
    addRecipe(new RecipeSolidifier(new ItemStack[] {
        new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK)
    }, new ItemStack(Blocks.OBSIDIAN),
        "lava", 1000));
    addRecipe(new RecipeSolidifier(new ItemStack[] {
        new ItemStack(Items.ARROW), new ItemStack(Items.ARROW), new ItemStack(Items.ARROW), new ItemStack(Items.FLINT)
    }, PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW, 3), PotionTypes.STRONG_HARMING),
        "lava", 500));
    addRecipe(new RecipeSolidifier(new ItemStack[] {
        new ItemStack(Items.BUCKET)
    }, new ItemStack(Items.MILK_BUCKET),
        "milk", 1000));
    if (FluidRegistry.isFluidRegistered("poison")) {
      addRecipe(new RecipeSolidifier(new ItemStack[] {
          new ItemStack(Items.ARROW)
      }, PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), PotionTypes.STRONG_POISON),
          "poison", 100));
      // addRecipe(new RecipeSolidifier(new ItemStack[] {
      //     new ItemStack(Items.ARROW)
      // }, PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), ItemPotionContent.potionTypeButterII),
      //     "poison", 100));
    }
    if (FluidRegistry.isFluidRegistered("amber")) {
      Item amber = Item.getByNameOrId(Const.MODRES + "crystallized_amber");
      if (amber != null) {
        addRecipe(new RecipeSolidifier(new ItemStack[] {
            new ItemStack(Items.GOLD_NUGGET)
        }, new ItemStack(amber), 
            "amber", 1000));
      }
    }
    if (FluidRegistry.isFluidRegistered("crystal")) {
      Item crystal = Item.getByNameOrId(Const.MODRES + "crystallized_obsidian");
      if (crystal != null) {
        addRecipe(new RecipeSolidifier(new ItemStack[] {
            new ItemStack(Items.IRON_NUGGET)
        }, new ItemStack(crystal),
            "crystal", 1000));
      }
    }
    if (FluidRegistry.isFluidRegistered("biomass")) {
      Item biomass = Item.getByNameOrId(Const.MODRES + "peat_biomass");
      if (biomass != null) {
        addRecipe(new RecipeSolidifier(new ItemStack[] {
            new ItemStack(Items.WHEAT_SEEDS)
        }, new ItemStack(biomass),
            "biomass", 1000));
        addRecipe(new RecipeSolidifier(new ItemStack[] {
            new ItemStack(Items.APPLE)
        }, new ItemStack(biomass),
            "biomass", 800));
      }
      Item peat = Item.getByNameOrId(Const.MODRES + "peat_unbaked");
      if (peat != null) {
        addRecipe(new RecipeSolidifier(new ItemStack[] {
            new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT)
        }, new ItemStack(peat, 2),
            "biomass", 100));
      }
    }
  }

  public static void addRecipe(RecipeSolidifier rec) {
    recipes.add(rec);
  }

  public String getFluidString() {
    return fluidString;
  }

  public void setFluidString(String fluidString) {
    this.fluidString = fluidString;
  }
}
