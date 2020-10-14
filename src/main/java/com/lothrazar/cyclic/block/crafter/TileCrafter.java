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
package com.lothrazar.cyclic.block.crafter;


import com.lothrazar.cyclic.base.SidedTileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileCrafter extends SidedTileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static final int MAX = 64000;
  public static IntValue POWERCONF;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  public static final int IO_NUM_ROWS = 5;
  public static final int IO_NUM_COLS = 2;
  public static final int GRID_SIZE = 3;
  public static final int PREVIEW_SLOT = IO_NUM_ROWS * IO_NUM_COLS * 2 + GRID_SIZE * GRID_SIZE;
  public static final int OUTPUT_SLOT_START = IO_NUM_ROWS * IO_NUM_COLS + GRID_SIZE * GRID_SIZE;
  public static final int OUTPUT_SLOT_STOP = OUTPUT_SLOT_START + IO_NUM_ROWS * IO_NUM_COLS - 1;
  public static final int GRID_START = IO_NUM_ROWS * IO_NUM_COLS;
  public static final int GRID_STOP = GRID_START + GRID_SIZE * GRID_SIZE - 1;
  private boolean hasValidRecipe = false;
  private ArrayList<ItemStack> lastValidRecipeGrid = new ArrayList<>();
  private IRecipe<?> lastValidRecipe = null;
  private ItemStack recipeOutput = ItemStack.EMPTY;

  @Override
  public int[] getSlotsForFace(Direction side) {
    int[] inputSlotIds = {IO_NUM_ROWS * IO_NUM_COLS};
    int[] outputSlotIds = {IO_NUM_ROWS * IO_NUM_COLS};
    for (int i = 0; i < IO_NUM_ROWS * IO_NUM_COLS; i++) {
      inputSlotIds[i] = i;
    }
    for (int i = OUTPUT_SLOT_START; i < OUTPUT_SLOT_STOP; i++) {
      outputSlotIds[i - OUTPUT_SLOT_START] = i;
    }

    switch (side) {
      case UP:
      case NORTH:
      case SOUTH:
      case EAST:
      case WEST:
        return ArrayUtils.addAll(inputSlotIds, outputSlotIds);
      case DOWN:
      default:
        return outputSlotIds;
    }
  }

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
    return index < IO_NUM_ROWS * IO_NUM_COLS;
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
    return index >= OUTPUT_SLOT_START && index <= OUTPUT_SLOT_STOP;
  }

  static enum Fields {
    TIMER, REDSTONE, RENDER;
  }

  public TileCrafter() {
    super(TileRegistry.crafter);
    timer = 40;
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index != PREVIEW_SLOT && (index < OUTPUT_SLOT_START
            || index > OUTPUT_SLOT_STOP)) {
      super.isItemValidForSlot(index, stack);
    }
    return false;
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      reset();
      return;
    }
    setLitProperty(true);
    IEnergyStorage cap = this.energy.orElse(null);
    if (cap == null) {
      return;
    }
    final int cost = POWERCONF.get();
    if (cap.getEnergyStored() < cost && cost > 0) {
      return;//broke
    }

    IEnergyStorage en = this.energy.orElse(null);
    IItemHandler inv = this.inventory.orElse(null);
    int gridStartSlot = TileCrafter.IO_NUM_COLS * TileCrafter.IO_NUM_ROWS;
    int gridStopSlot = gridStartSlot + TileCrafter.GRID_SIZE * TileCrafter.GRID_SIZE;
    ArrayList<ItemStack> itemStacksInGrid = new ArrayList<>();
    for (int i = gridStartSlot; i < gridStopSlot; i++) {
      itemStacksInGrid.add(inv.getStackInSlot(i));
    }
    if (world == null || world.getServer() == null)
      return;
    Collection<IRecipe<?>> recipes = world.getServer().getRecipeManager().getRecipes();
    if (!hasValidRecipe) {
      if (countNonEmptyStacks(itemStacksInGrid) == 0)
        return;
      for (IRecipe<?> recipe : recipes) {
        if (recipe instanceof ShapelessRecipe) {
          recipe = (ShapelessRecipe) recipe;
          //TODO: Process Shapeless Recipe
        }
        else if (recipe instanceof ShapedRecipe) {
          ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
          if (!doSizesMatch(shapedRecipe, itemStacksInGrid)) {
            continue;
          }
          hasValidRecipe = tryMatchShapedRecipe(itemStacksInGrid, shapedRecipe);
          if (hasValidRecipe) {
            inv.extractItem(PREVIEW_SLOT, 64, false);
            inv.insertItem(PREVIEW_SLOT, recipe.getRecipeOutput(), false);
            lastValidRecipe = shapedRecipe;
            lastValidRecipeGrid = itemStacksInGrid;
            recipeOutput = shapedRecipe.getRecipeOutput();
            break;
          }
        }
      }
    }
    else {
      if (!itemStacksInGrid.equals(lastValidRecipeGrid)) {
        hasValidRecipe = false;
        lastValidRecipeGrid = null;
        lastValidRecipe = null;
        inv.extractItem(PREVIEW_SLOT, 64, false);
        inv.insertItem(PREVIEW_SLOT, ItemStack.EMPTY, false);
        return;
      }
      //TODO: Process crafting
      if (timer <= 0 && hasFreeSpace(inv, recipeOutput)) {
        en.extractEnergy(cost, false);
        ItemStack output = recipeOutput.copy();
        for (int slotId = OUTPUT_SLOT_START; slotId <= OUTPUT_SLOT_STOP; slotId++) {
          output = inv.insertItem(slotId, output, false);
          if (output == ItemStack.EMPTY || output.getCount() == 0)
            break;
        }
      }
    }
    if (this.timer <= 0) {
      this.timer = 40;
    }
    timer--;
  }

  private boolean hasFreeSpace(IItemHandler inv, ItemStack output) {
    for (int slotId = OUTPUT_SLOT_START; slotId <= OUTPUT_SLOT_STOP; slotId++) {
      if (inv.getStackInSlot(slotId) == ItemStack.EMPTY
          || (inv.getStackInSlot(slotId).isItemEqual(output)
              && inv.getStackInSlot(slotId).getCount() + output.getCount() <= output.getMaxStackSize()))
        return true;
      if (output == ItemStack.EMPTY || output.getCount() == 0)
        return true;
    }
    return false;
  }

  private ArrayList<ItemStack> getItemStacksForCrafting() {
    return null;
  }

  private boolean tryMatchShapedRecipe(ArrayList<ItemStack> itemStacks, ShapedRecipe recipe) {
    for(int i = 0; i <= 3 - recipe.getWidth(); ++i) {
      for(int j = 0; j <= 3 - recipe.getHeight(); ++j) {
        if (this.tryMatchShapedRecipeRegion(itemStacks, recipe, i, j)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean tryMatchShapedRecipeRegion(ArrayList<ItemStack> itemStacks, ShapedRecipe recipe, int offsetX, int offsetY) {
    int recipeSize = recipe.getWidth() * recipe.getHeight();
    int itemStacksSize = itemStacks.size();
    Ingredient ingredient;
    if (itemStacksSize < recipeSize || offsetX + recipe.getWidth() > 3 || offsetY + recipe.getHeight() > 3)
      return false;
    int indexInRecipe = 0;
    for (int recipeYPos = 0; recipeYPos < recipe.getHeight(); recipeYPos++) {
      for (int recipeXPos = 0; recipeXPos < recipe.getWidth(); recipeXPos++) {
        int indexInArray = offsetX * 3 + offsetY + recipeXPos * 3 + recipeYPos;
        ItemStack itemStack = itemStacks.get(indexInArray);
        ingredient = recipe.getIngredients().get(indexInRecipe);
        if (!ingredient.test(itemStack)) {
          return false;
        }
        indexInRecipe++;
      }
    }
    return true;
  }

  public void reset() {
    this.hasValidRecipe = false;
    this.lastValidRecipeGrid = null;
    this.lastValidRecipe = null;
  }

  private int countNonEmptyStacks(ArrayList<ItemStack> itemStacks) {
    int count = 0;
    for (ItemStack itemStack : itemStacks) {
      if (itemStack != ItemStack.EMPTY)
        count++;
    }
    return count;
  }

  private boolean doSizesMatch(ShapedRecipe recipe, ArrayList<ItemStack> itemStacks) {
    int ingredientCount = 0;
    int itemStackCount = 0;
    for (Ingredient ingredient : recipe.getIngredients()) {
      if (!ingredient.test(ItemStack.EMPTY))
        ingredientCount++;
    }
    for (ItemStack itemStack : itemStacks) {
      if (itemStack != ItemStack.EMPTY)
        itemStackCount++;
    }
    return ingredientCount == itemStackCount;
  }


  private IEnergyStorage createEnergy() { return new CustomEnergyStorage(MAX, MAX); }

  private IItemHandler createHandler() { return new ItemStackHandler(10 + 10 + 10); }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerCrafter(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public int getField(int id) {
    switch (TileCrafter.Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (TileCrafter.Fields.values()[id]) {
      case TIMER:
        this.timer = value;
        break;
      case REDSTONE:
        this.needsRedstone = value % 2;
        break;
      case RENDER:
        this.render = value % 2;
        break;
    }
  }
}
