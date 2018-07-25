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
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityPackager extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  public static final int INPUT_SIZE = 2 * 3;
  public static final int OUTPUT_SIZE = INPUT_SIZE;
  public final static int TIMER_FULL = 40;
  private int needsRedstone = 1;

  public static enum Fields {
    REDSTONE, TIMER, FUEL;
  }

  public InventoryCrafting crafting = new InventoryCrafting(new ContainerDummyPackager(), 1, 1);
  private RecipePackage lastRecipe = null;

  public TileEntityPackager() {
    super(OUTPUT_SIZE + INPUT_SIZE);// in, out 

    this.setSlotsForInsert(1, INPUT_SIZE);
    this.setSlotsForExtract(INPUT_SIZE + 1, INPUT_SIZE + OUTPUT_SIZE);
    this.initEnergy(BlockPackager.FUEL_COST);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getFieldCount() {
    return getFieldOrdinals().length;
  }

  @Override
  public void update() {

    if (this.isRunning() == false) {
      return;
    }
    if (this.updateEnergyIsBurning() == false) {
      return;
    }

    //ignore timer when filling up water
    if (this.updateTimerIsZero() && this.hasEnoughEnergy()) { // time to burn!
      if (this.lastRecipe != null && tryProcessRecipe(lastRecipe)) {
        this.timer = TIMER_FULL;
        this.consumeEnergy();
      }
      else {
        //try to look for a new one
        for (RecipePackage irecipe : RecipePackage.recipes) {
          if (tryProcessRecipe(irecipe)) {
            //if we have found a recipe that can be processed. save reference to it for next loop
            this.consumeEnergy();
            this.timer = TIMER_FULL;
            lastRecipe = irecipe;
          }
        }
      }
    }
  }

  public boolean tryProcessRecipe(RecipePackage recipe) {
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
        if (input.isItemEqual(this.getStackInSlot(i)) == false) {
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
    if (process) {
      for (Map.Entry<Integer, Integer> entry : mapSlotToCost.entrySet()) {
        //go 
        this.decrStackSize(entry.getKey(), entry.getValue());
      }
      this.sendOutputItem(recipe.getRecipeOutput());
    }
    return process;
  }

  public void sendOutputItem(ItemStack itemstack) {
    for (int i = INPUT_SIZE + 1; i < this.getSizeInventory(); i++) {
      if (!itemstack.isEmpty() && itemstack.getMaxStackSize() != 0) {
        itemstack = tryMergeStackIntoSlot(itemstack, i);
      }
    }
    if (!itemstack.isEmpty() && itemstack.getMaxStackSize() != 0) { //FULL
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
      case FUEL:
        return this.getEnergyCurrent();
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case FUEL:
        this.setEnergyCurrent(value);
      break;
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
