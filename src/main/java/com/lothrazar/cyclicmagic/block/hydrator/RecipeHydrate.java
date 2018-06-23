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
package com.lothrazar.cyclicmagic.block.hydrator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHydrate extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

  public static ArrayList<RecipeHydrate> recipesShaped = new ArrayList<RecipeHydrate>();
  private NonNullList<ItemStack> recipeInput = NonNullList.withSize(4, ItemStack.EMPTY);// new ItemStack[4];
  private ItemStack resultItem = ItemStack.EMPTY;
  private int fluidCost = 25;


  public RecipeHydrate(ItemStack in, ItemStack out) {
    this(new ItemStack[] { in }, out, 25);
  }

  public RecipeHydrate(ItemStack[] in, ItemStack out) {
    this(in, out, 25);
  }

  public RecipeHydrate(ItemStack[] in, ItemStack out, int w) {
    if (in.length > 4 || in.length == 0) {
      throw new IllegalArgumentException("Input array must be length 4 or less");
    }
    ModCyclic.logger.info("Hydrator recipe for " + out.getDisplayName() + " is size? " + in.length);
    for (int i = 0; i < in.length; i++) {
      if (in[i] != null && in[i].isEmpty() == false)
        recipeInput.set(i, in[i]);
    }
    this.fluidCost = w;
    this.resultItem = out;
    this.setRegistryName(new ResourceLocation(Const.MODID, "hydrator_" + UUID.randomUUID().toString() + out.getUnlocalizedName()));
  }

  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {

 
    return recipeSlotMatches(inv.getStackInSlot(0), recipeInput.get(0)) &&
        recipeSlotMatches(inv.getStackInSlot(1), recipeInput.get(1)) &&
        recipeSlotMatches(inv.getStackInSlot(2), recipeInput.get(2)) &&
        recipeSlotMatches(inv.getStackInSlot(3), recipeInput.get(3));
  }

  private boolean recipeSlotMatches(ItemStack sInvo, ItemStack sRecipe) {

    if (sInvo.isEmpty() != sRecipe.isEmpty()) {
      return false;//empty matching empty
    }
    //if item matches, then we are fine. check ore dict as well after that if item doesnt match 
    return sInvo.getCount() >= sRecipe.getCount() &&
        (sInvo.getItem() == sRecipe.getItem() || OreDictionary.itemMatches(sInvo, sRecipe, false));
  }

  public boolean tryPayCost(IInventory invoSource, FluidTank tank, boolean keepOneMinimum) {
    if (tank.getFluidAmount() < this.getFluidCost()) {
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

  // static init
  public static void initAllRecipes() {
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.FARMLAND)));
    addRecipe(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.TALLGRASS, 1, 1), new ItemStack(Blocks.DIRT), new ItemStack(Blocks.TALLGRASS, 1, 1), new ItemStack(Blocks.DIRT) },
        new ItemStack(Blocks.GRASS, 2)));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.GRASS), new ItemStack(Blocks.GRASS_PATH)));
    addRecipe(new RecipeHydrate(new ItemStack(Items.BRICK), new ItemStack(Items.CLAY_BALL)));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.STONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0)));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.COBBLESTONE, 1, 0), new ItemStack(Blocks.MOSSY_COBBLESTONE, 1, 0)));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.COBBLESTONE_WALL, 1, 0), new ItemStack(Blocks.COBBLESTONE_WALL, 1, 1)));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.STONEBRICK, 1, 0), new ItemStack(Blocks.STONEBRICK, 1, 1)));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.HARDENED_CLAY), new ItemStack(Blocks.CLAY)));
    //GRAVEL JUST FOR FUN EH
    //IDEAS: bones, rotten flesh, mushrooms, leather??
    addRecipe(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Items.FLINT) },
        new ItemStack(Blocks.GRAVEL)));
    addRecipe(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.DIRT, 1, 1), new ItemStack(Blocks.RED_MUSHROOM_BLOCK), new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK), new ItemStack(Blocks.GRASS_PATH) },
        new ItemStack(Blocks.MYCELIUM)));
    addRecipe(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW) },
        new ItemStack(Blocks.ICE)));
    addRecipe(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.ICE), new ItemStack(Blocks.ICE), new ItemStack(Blocks.ICE), new ItemStack(Blocks.ICE) },
        new ItemStack(Blocks.PACKED_ICE)));
    for (EnumDyeColor col : EnumDyeColor.values()) {
      addRecipe(new RecipeHydrate(new ItemStack(Blocks.CONCRETE_POWDER, 1, col.getMetadata()), new ItemStack(Blocks.CONCRETE, 1, col.getMetadata())));
    }
    for (EnumDyeColor col : EnumDyeColor.values()) {
      if (col.getMetadata() != EnumDyeColor.WHITE.getMetadata())
        addRecipe(new RecipeHydrate(new ItemStack(Blocks.WOOL, 1, col.getMetadata()), new ItemStack(Blocks.WOOL, 1, EnumDyeColor.WHITE.getMetadata())));
    }
    //they didnt use metadata for glazed because of facing direction i guess
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.BLACK_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLACK.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.BLUE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLUE.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BROWN.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.CYAN.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.GREEN.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIGHT_BLUE.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.LIME_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIME.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.MAGENTA_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.MAGENTA.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.ORANGE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.ORANGE.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.PINK_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PINK.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.PURPLE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PURPLE.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.RED_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.RED.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.SILVER_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.SILVER.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.WHITE.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack(Blocks.YELLOW_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.YELLOW.getMetadata())));
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Blocks.WOOL, 1, EnumDyeColor.YELLOW.getMetadata()), new ItemStack(Items.SLIME_BALL), new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Blocks.SOUL_SAND)
    }, new ItemStack(Blocks.SPONGE)));
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Blocks.WEB), new ItemStack(Items.STRING), new ItemStack(Items.STRING), new ItemStack(Items.BONE)
    }, new ItemStack(Blocks.WEB, 4)));
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.IRON_NUGGET), new ItemStack(Items.NETHERBRICK), new ItemStack(Items.CLAY_BALL)
    }, new ItemStack(Items.PRISMARINE_SHARD)));
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Items.PRISMARINE_SHARD)
    }, new ItemStack(Items.PRISMARINE_CRYSTALS)));
    // lava fabricator
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.IRON_INGOT, 3), new ItemStack(Items.NETHERBRICK), new ItemStack(Items.BLAZE_POWDER)
    }, new ItemStack(Items.LAVA_BUCKET)));
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Blocks.CACTUS), new ItemStack(Blocks.VINE), new ItemStack(Blocks.TALLGRASS, 1, 1), new ItemStack(Items.WHEAT_SEEDS)
    }, new ItemStack(Blocks.WATERLILY, 2)));
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.BROWN_MUSHROOM)
    }, new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK)));
    addRecipe(new RecipeHydrate(new ItemStack[] {
        new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM)
    }, new ItemStack(Blocks.RED_MUSHROOM_BLOCK)));
  }

  public static void addRecipe(RecipeHydrate rec) {
    recipesShaped.add(rec);
  }
}
