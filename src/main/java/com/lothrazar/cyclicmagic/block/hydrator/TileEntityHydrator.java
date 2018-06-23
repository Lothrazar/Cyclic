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

import java.util.Arrays;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.liquid.FluidTankBase;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidRegistry;

public class TileEntityHydrator extends TileEntityBaseMachineFluid implements ITileRedstoneToggle, ITickable {

  public static final int RECIPE_SIZE = 4;
  public static final int TANK_FULL = 10000;
  public final static int TIMER_FULL = 40;

  private int needsRedstone = 1;
  public static enum Fields {
    REDSTONE, TIMER, RECIPELOCKED;
  }

  private int recipeIsLocked = 0;
  private InventoryCrafting crafting = new InventoryCrafting(new ContainerDummy(), RECIPE_SIZE / 2, RECIPE_SIZE / 2);

  public TileEntityHydrator() {
    super(2 * RECIPE_SIZE);// in, out 
    tank = new FluidTankBase(TANK_FULL);
    timer = TIMER_FULL;
    tank.setTileEntity(this);
    tank.setFluidAllowed(FluidRegistry.WATER);
    this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3));
    this.setSlotsForExtract(Arrays.asList(4, 5, 6, 7));
    this.initEnergy(BlockHydrator.FUEL_COST);

  }


  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (this.recipeIsLocked == 0) {
      return true;
    }
    return true;
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
    if (this.getCurrentFluidStackAmount() == 0) {
      return;
    }
    if (this.updateTimerIsZero()) { // time to burn!
      this.spawnParticlesAbove();
      if (tryProcessRecipe()) {
        this.timer = TIMER_FULL;
      }
    }
  }

  /**
   * try to match a shaped or shapeless recipe
   * 
   * @return
   */
  private RecipeHydrate findMatchingRecipe() {
    boolean allAir = true;
    for (int i = 0; i < RECIPE_SIZE; i++) {
      //if ANY slot is non empty, we will get an && false which makes false
      allAir = allAir && this.getStackInSlot(i).isEmpty();
      this.crafting.setInventorySlotContents(i, this.getStackInSlot(i).copy());
    }
    if (allAir) {
      return null;
    }
    for (RecipeHydrate irecipe : RecipeHydrate.recipesShaped) {
      if (irecipe.matches(this.crafting, world)) {
        return irecipe;
      }
    }
    return null;
  }

  public boolean tryProcessRecipe() {
    RecipeHydrate rec = findMatchingRecipe();
    if (rec != null && this.getCurrentFluidStackAmount() >= rec.getFluidCost()) {
      if (rec.tryPayCost(this, this.tank, this.recipeIsLocked == 1)) {
        //only create the output if cost was successfully paid
        this.sendOutputItem(rec.getRecipeOutput());
      }
      return true;
    }
    return false;
  }

  public void sendOutputItem(ItemStack itemstack) {
    for (int i = 3 + 1; i < 8; i++) {
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

  public float getFillRatio() {
    return tank.getFluidAmount() / tank.getCapacity();
  }

  /**
   * For the crafting inventory, since its never in GUI and is just used for auto processing
   * 
   * @author Sam
   */
  public static class ContainerDummy extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
  }
}
