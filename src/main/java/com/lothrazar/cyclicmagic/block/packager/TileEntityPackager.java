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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    REDSTONE, TIMER, RECIPELOCKED;
  }

  private int recipeIsLocked = 0;
  public InventoryCrafting crafting = new InventoryCrafting(new ContainerDummyPackager(), 1, 1);

  public TileEntityPackager() {
    super(OUTPUT_SIZE + INPUT_SIZE);// in, out 
    //tank.setTileEntity(this);
    //tank.setFluidAllowed(FluidRegistry.WATER);
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
    this.updateLockSlots();
    if (this.isRunning() == false) {
      return;
    }
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    //ignore timer when filling up water
    if (this.updateTimerIsZero()) { // time to burn!
      if (tryProcessRecipe()) {
        this.timer = TIMER_FULL;
      }
    }
  }

  private void updateLockSlots() {
    if (this.recipeIsLocked == 1) {
      List<Integer> slotsImport = new ArrayList<Integer>();
      for (int slot = 0; slot < INPUT_SIZE; slot++) {
        if (this.getStackInSlot(slot).isEmpty() == false) {
          slotsImport.add(slot);
        }
      }
      this.setSlotsForInsert(slotsImport);
    }
    else {//all are free game
      this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3));
    }
  }

  public boolean tryProcessRecipe() {
    boolean process = false;
    //loop on all recipes
    // on current recipe, try and find enough to satisfy needs
    //process or continue to next
    Map<Integer, Integer> mapSlotToCost = new HashMap<>();
    for (RecipePackage irecipe : RecipePackage.recipes) {
      int needed = irecipe.getIngredientCount();
      int neededRemaining = needed;
      for (int i = 0; i < INPUT_SIZE; i++) {
        //  ModCyclic.logger.info("contents ! " + this.getStackInSlot(i));
        this.crafting.setInventorySlotContents(0, this.getStackInSlot(i).copy());
        if (irecipe.matches(this.crafting, world)) {
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
      }
      // done looping inventory but not recipe
      if (process) {
        for (Map.Entry<Integer, Integer> entry : mapSlotToCost.entrySet()) {
          //go 
          this.decrStackSize(entry.getKey(), entry.getValue());
        }
        this.sendOutputItem(irecipe.getRecipeOutput());
        break;//break out of recipe loop
      }
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
    compound.setInteger("rlock", recipeIsLocked);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.needsRedstone = compound.getInteger(NBT_REDST);
    this.recipeIsLocked = compound.getInteger("rlock");
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case RECIPELOCKED:
        return this.recipeIsLocked;
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
      case RECIPELOCKED:
        this.recipeIsLocked = value % 2;
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
