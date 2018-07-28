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
package com.lothrazar.cyclicmagic.block.dehydrator;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.liquid.FluidTankFixDesync;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidRegistry;

public class TileEntityDeHydrator extends TileEntityBaseMachineFluid implements ITileRedstoneToggle, ITickable {

  static final int SLOT_RECIPE = 0;
  public static final int STASH_SIZE = 4;
  public static final int TANK_FULL = 10000;
  private int needsRedstone = 1;
  private int timerMax = 1;
  private RecipeDeHydrate lastRecipe = null;

  public static enum Fields {
    REDSTONE, TIMER, TIMERMAX, FUEL;
  }

  private InventoryCrafting crafting = new InventoryCrafting(new ContainerDummyHydrator(), 1, 1);

  public TileEntityDeHydrator() {
    super(1 + 2 * STASH_SIZE);// in, out 
    tank = new FluidTankFixDesync(TANK_FULL, this);
    tank.setTileEntity(this);
    tank.setFluidAllowed(FluidRegistry.LAVA);
    this.setSlotsForInsert(1, 4);
    this.setSlotsForExtract(5, 8);
    this.initEnergy(BlockDeHydrator.FUEL_COST);
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
    this.tryShiftInput();
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    if (this.getStackInSlot(SLOT_RECIPE).isEmpty()) {
      lastRecipe = null;//all gone 
    }
    if (this.lastRecipe == null) {
      lastRecipe = findMatchingRecipe();
    }
    if (this.lastRecipe != null && this.updateTimerIsZero() && !this.getStackInSlot(SLOT_RECIPE).isEmpty()) { // time to burn!
      if (tryProcessRecipe()) {
        if (this.getStackInSlot(SLOT_RECIPE).isEmpty()) {
          lastRecipe = null;
        }
        else {//reset timer; if on same recipe
          this.timer = lastRecipe.getTime();
        }
      //else recipe became null
      }
    }
  }

  private void tryShiftInput() {
    for (int i = 1; i <= STASH_SIZE; i++) {
      ItemStack result = this.tryMergeStackIntoSlot(this.getStackInSlot(i), SLOT_RECIPE);
      this.setInventorySlotContents(i, result);
    }
  }

  public boolean tryProcessRecipe() {
    //last minute check in case item swapped
    if (!recipeMatches(lastRecipe)) {
      lastRecipe = null;
      return false;
    }
    if (lastRecipe.tryPayCost(this)) {
      //only create the output if cost was successfully paid
      this.sendOutputItem(lastRecipe.getRecipeOutput());
      return true;
    }
    return false;
  }

  private boolean recipeMatches(RecipeDeHydrate irecipe) {
    this.crafting.setInventorySlotContents(SLOT_RECIPE, this.getStackInSlot(SLOT_RECIPE).copy());
    return irecipe.matches(this.crafting, world);
  }

  /**
   * try to match a shaped or shapeless recipe
   * 
   * @return
   */
  private RecipeDeHydrate findMatchingRecipe() {
    //    this.crafting.setInventorySlotContents(SLOT_RECIPE, this.getStackInSlot(SLOT_RECIPE).copy());
    for (RecipeDeHydrate irecipe : RecipeDeHydrate.recipes) {
      if (recipeMatches(irecipe)) {
        timerMax = timer = irecipe.getTime();
        return irecipe;
      }
    }
    return null;
  }

  public void sendOutputItem(ItemStack itemstack) {
    for (int i = STASH_SIZE + 1; i < this.getSizeInventory(); i++) {
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
      case FUEL:
        return this.getEnergyCurrent();
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case TIMERMAX:
        return timerMax;
      default:
      break;
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
      case TIMERMAX:
        timerMax = value;
      break;
      default:
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

  public float getFillRatio() {
    return tank.getFluidAmount() / tank.getCapacity();
  }

  /**
   * For the crafting inventory, since its never in GUI and is just used for auto processing
   * 
   * @author Sam
   */
  public static class ContainerDummyHydrator extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
  }
}
