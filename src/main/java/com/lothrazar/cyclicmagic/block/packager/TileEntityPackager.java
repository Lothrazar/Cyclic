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
package com.lothrazar.cyclicmagic.block.packager;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityPackager extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  public static final int INPUT_SIZE = 2 * 3;
  public static final int OUTPUT_SIZE = INPUT_SIZE;

  public static enum Fields {
    REDSTONE, TIMER;
  }

  public InventoryCrafting crafter = new InventoryCrafting(new ContainerDummyPackager(), 3, 2);
  private RecipePackager lastRecipe = null;

  public TileEntityPackager() {
    super(OUTPUT_SIZE + INPUT_SIZE);// in, out 
    this.setSlotsForInsert(0, INPUT_SIZE - 1);
    this.setSlotsForExtract(INPUT_SIZE, INPUT_SIZE + OUTPUT_SIZE - 1);
    this.initEnergy(new EnergyStore(MENERGY, MENERGY, MENERGY), BlockPackager.FUEL_COST);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getFieldCount() {
    return getFieldOrdinals().length;
  }


  private int lastInvHash = 0;

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    this.shiftAllUp(INPUT_SIZE);
    //ignore timer when filling up water
    if (this.hasEnoughEnergy()) { // time to burn!
      if (this.lastRecipe != null
          && lastRecipe.matches(this.crafter, this.world)
          && tryProcessRecipe(lastRecipe)) {
        // are we empty? if empty dont consume
        this.consumeEnergy();
      }
      else if (!world.isRemote) {
        //no matching recipe found, OR could not process (ingredients not found)
        this.lastRecipe = null;
        int currHash = calculateInventoryHash(0, INPUT_SIZE);
        // before we go hunting for recipes
        //use hash to say "has inventory contents changed since last time"
        //if they have changed, and if not empty, 
        // THEN go recipe hunting (expensive operation)
        if (currHash != lastInvHash &&
            !this.isInventoryEmpty(0, INPUT_SIZE)) {
          //inventory changed since last check, and its not empty
          lastInvHash = currHash;
         // ModCyclic.logger.log("packager find recipe new lastInvHash=" + lastInvHash);
          findRecipe();
        } 
      }
    }
  }

  private void findRecipe() {
    setRecipeInput();//make sure the 3x3 inventory is linked o the crater
    //   ArrayList<RecipePackager> shuffled = RecipePackager.recipes;
    // so we dont get stuck on the same one
    //also it does not get called too frequently
    //Collections.shuffle(shuffled);
    for (RecipePackager irecipe : RecipePackager.recipes) {
      if (irecipe.matches(this.crafter, this.world)) {
        this.lastRecipe = irecipe;
        break;
      }
    }
  }

  private void setRecipeInput() {
    for (int slot = 0; slot < INPUT_SIZE; slot++) {
      ItemStack stack = this.getStackInSlot(slot);
      if (stack == null) {
        stack = ItemStack.EMPTY;
      }
      this.crafter.setInventorySlotContents(slot, stack.copy());
    }
  }

  public boolean tryProcessRecipe(RecipePackager recipe) {
    boolean process = false;
    //check inventory to see if we can pay costs of the recipe
    //loop over recipe ingredients
    //for each ingredient. track down all requirements
    // if all are met, pay then => true
    Map<Integer, Integer> mapSlotToCost = new HashMap<>();
    for (ItemStack input : recipe.getInput()) {
      process = false;
      int needed = input.getCount();
      //now find this many of them
      int neededRemaining = needed;
      for (int i = 0; i < INPUT_SIZE; i++) {
        if (UtilItemStack.isItemStackEqualIgnoreCount(input, this.getStackInSlot(i)) == false) {
          continue;
        }
        //   ModCyclic.logger.info("matched ! " + irecipe.getRecipeOutput());
        //we found AN item with a matching recipe
        int payHere = Math.min(neededRemaining, this.getStackInSlot(i).getCount());
        mapSlotToCost.put(i, payHere);
        //   ModCyclic.logger.info(i + " => " + payHere);
        neededRemaining = neededRemaining - payHere;
        if (neededRemaining == 0) {
          process = true;
          break;//break inv loop for current recipe
        }
        //else keep looking for the rest
      }
      if (!process) {
        //inventory did not have enough if "input" to craft so abort
        //and ignore the rest of the recipe
        break;
      }
    }
    // done looping inventory but not recipe
    if (process && inventoryHasRoom(INPUT_SIZE, recipe.getRecipeOutput())) {
      for (Map.Entry<Integer, Integer> entry : mapSlotToCost.entrySet()) {
        //go 
        this.decrStackSize(entry.getKey(), entry.getValue());
      }
      this.sendOutputItem(recipe.getRecipeOutput());
    }
    return process;
  }

  public void sendOutputItem(ItemStack itemstack) {
    for (int i = INPUT_SIZE; i < this.getSizeInventory(); i++) {
      if (!itemstack.isEmpty()) {
        itemstack = tryMergeStackIntoSlot(itemstack, i);
      }
    }
    if (!itemstack.isEmpty()) {
      UtilItemStack.dropItemStackInWorld(this.getWorld(), this.pos.up(), itemstack);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.needsRedstone = compound.getInteger(NBT_REDST);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case TIMER:
        this.timer = value;
      break;
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  /**
   * For the crafting inventory, since its never in GUI and is just used for auto processing
   * 
   * @author Sam
   */
  public static class ContainerDummyPackager extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
  }
}
