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

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
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

@SuppressWarnings("unchecked")
public class TileCrafter extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static final int MAX = 64000;
  public static final int TIMER_FULL = 40;
  public static IntValue POWERCONF;
  private CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputHandler = new ItemStackHandler(IO_SIZE);
  ItemStackHandler outHandler = new ItemStackHandler(IO_SIZE);
  private final LazyOptional<IItemHandler> input = LazyOptional.of(() -> inputHandler);
  private final LazyOptional<IItemHandler> output = LazyOptional.of(() -> outHandler);
  private final LazyOptional<IItemHandler> gridCap = LazyOptional.of(() -> new ItemStackHandler(GRID_SIZE));
  private final LazyOptional<IItemHandler> preview = LazyOptional.of(() -> new ItemStackHandler(1));
  private ItemStackHandlerWrapper inventoryWrapper = new ItemStackHandlerWrapper(inputHandler, outHandler);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventoryWrapper);
  //
  public static final int IO_NUM_ROWS = 5;
  public static final int IO_NUM_COLS = 2;
  public static final int GRID_NUM_ROWS = 3;
  public static final int GRID_NUM_COLS = 3;
  public static final int IO_SIZE = IO_NUM_ROWS * IO_NUM_COLS;
  public static final int GRID_SIZE = GRID_NUM_ROWS * GRID_NUM_COLS;
  public static final int PREVIEW_SLOT = IO_SIZE * 2 + GRID_SIZE;
  public static final int OUTPUT_SLOT_START = IO_SIZE + GRID_SIZE;
  public static final int OUTPUT_SLOT_STOP = OUTPUT_SLOT_START + IO_SIZE - 1;
  public static final int GRID_SLOT_START = IO_SIZE;
  public static final int GRID_SLOT_STOP = GRID_SLOT_START + GRID_SIZE - 1;
  private boolean hasValidRecipe = false;
  public boolean shouldSearch = true;
  private ArrayList<ItemStack> lastRecipeGrid = null;
  private IRecipe<?> lastValidRecipe = null;
  private ItemStack recipeOutput = ItemStack.EMPTY;

  public enum ItemHandlers {
    INPUT, OUTPUT, GRID, PREVIEW
  };

  public enum Fields {
    TIMER, REDSTONE, RENDER;
  }

  public TileCrafter() {
    super(TileRegistry.crafter);
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index != PREVIEW_SLOT &&
        (index < OUTPUT_SLOT_START || index > OUTPUT_SLOT_STOP)) {
      super.isItemValidForSlot(index, stack);
    }
    return false;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (world == null || world.getServer() == null) {
      return;
    }
    if (world.isRemote) {
      return;
    }
    IItemHandler previewHandler = this.preview.orElse(null);
    ArrayList<ItemStack> itemStacksInGrid = getItemsInCraftingGrid();
    if (lastRecipeGrid == null) {
      lastRecipeGrid = itemStacksInGrid;
    }
    if (itemStacksInGrid == null || countNonEmptyStacks(itemStacksInGrid) == 0) {
      //Nothing in Crafting grid, so don't do anything
      setPreviewSlot(previewHandler, ItemStack.EMPTY);
      this.timer = TIMER_FULL;
      return;
    }
    else if (!itemStacksInGrid.equals(lastRecipeGrid)) {
      //Crafting grid is updated, search for new recipe
      reset();
      lastRecipeGrid = itemStacksInGrid;
      shouldSearch = true;
    }
    if (!hasValidRecipe && shouldSearch) {
      IRecipe<?> recipe = tryRecipes(itemStacksInGrid);
      if (recipe != null) {
        hasValidRecipe = true;
        lastValidRecipe = recipe;
        lastRecipeGrid = itemStacksInGrid;
        recipeOutput = lastValidRecipe.getRecipeOutput();
        shouldSearch = false;
        setPreviewSlot(previewHandler, lastValidRecipe.getRecipeOutput());
        this.timer = TIMER_FULL;
      }
      else {
        reset();
        shouldSearch = false; //Went through all recipes, didn't find match. Don't search again (until crafting grid changes)
      }
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    IEnergyStorage cap = this.energyCap.orElse(null);
    if (cap == null) {
      return;
    }
    final int cost = POWERCONF.get();
    if (cap.getEnergyStored() < cost && cost > 0) {
      return;
    }
    if (hasValidRecipe) {
      if (--timer > 0) {
        return;
      }
      timer = TIMER_FULL;
      //      ModCyclic.LOGGER.info("tick down valid recipe output " + recipeOutput + " readyToCraft = " + readyToCraft);
      if (doCraft(inputHandler, true)) {
        // docraft in simulate mode
        ItemStack output = recipeOutput.copy();
        if (hasFreeSpace(outHandler, recipeOutput) && doCraft(inputHandler, false)) {
          if (lastValidRecipe != null && lastValidRecipe instanceof ShapedRecipe) {
            ShapedRecipe r = (ShapedRecipe) lastValidRecipe;
            r.getRemainingItems(craftMatrix);
          } //docraft simulate false is done, and we have space for output 
          energy.extractEnergy(cost, false);
          //compare to what it was
          ArrayList<ItemStack> itemStacksInGridBackup = getItemsInCraftingGrid();
          for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
            ItemStack recipeLeftover = this.craftMatrix.getStackInSlot(i);
            if (!recipeLeftover.isEmpty()) {
              if (recipeLeftover.getContainerItem().isEmpty() == false) {
                // TODO: shared code refactor
                ModCyclic.LOGGER.info(i + " recipe leftovers " + recipeLeftover + " ||| itemStacksInGridBackup " + itemStacksInGridBackup.get(i));
                boolean leftoverEqual = (recipeLeftover.getItem() == recipeLeftover.getContainerItem().getItem());
                if (leftoverEqual) {
                  ModCyclic.LOGGER.info(i + "leftoverEqual TRUE  " + recipeLeftover.getContainerItem());
                  ItemStack result = recipeLeftover.getContainerItem().copy();
                  //                  if (!result.isEmpty() && result.isDamageable() && result.isDamaged()) {
                  //                    // push damage forward to next cycle
                  //                    ModCyclic.LOGGER.info(i + " !craft matrix " + result);
                  //                    craftMatrix.setInventorySlotContents(i, result);
                  //                  }
                  for (int j = 0; j < inputHandler.getSlots(); j++) {
                    //test it
                    result = inputHandler.insertItem(j, result, false);
                    if (result.isEmpty()) {
                      break;
                    }
                  }
                }
                else {
                  ItemStack result = recipeLeftover.getContainerItem().copy();
                  for (int j = 0; j < outHandler.getSlots(); j++) {
                    //test it
                    result = outHandler.insertItem(j, result, false);
                    if (result.isEmpty()) {
                      break;
                    }
                  }
                }
              }
              //its not empty
              //              ItemStack actualLeftovers = itemStacksInGridBackup.get(i);
              //              if (!recipeLeftover.equals(actualLeftovers, true)) {
              //                ModCyclic.LOGGER.info("mismatch post craft:"
              //                    + " recipeLeftover = " + recipeLeftover + " " + recipeLeftover.getTag()
              //                    + " ; itemStacksInGridBackup.get(i) = " + actualLeftovers + " " + actualLeftovers.getTag());
              //              }
            }
          }
          for (int slotId = 0; slotId < IO_SIZE; slotId++) {
            output = outHandler.insertItem(slotId, output, false);
            if (output == ItemStack.EMPTY || output.getCount() == 0) {
              break;
            }
          }
        }
      }
    }
  }

  private ArrayList<ItemStack> getItemsInCraftingGrid() {
    ArrayList<ItemStack> itemStacks = new ArrayList<>();
    IItemHandler gridHandler = this.gridCap.orElse(null);
    if (gridHandler == null) {
      return null;
    }
    for (int i = 0; i < GRID_SIZE; i++) {
      itemStacks.add(gridHandler.getStackInSlot(i));
    }
    return itemStacks;
  }

  private void setPreviewSlot(IItemHandler previewHandler, ItemStack itemStack) {
    previewHandler.extractItem(0, 64, false);
    previewHandler.insertItem(0, itemStack, false);
  }

  private boolean hasFreeSpace(IItemHandler inv, ItemStack output) {
    for (int slotId = 0; slotId < IO_SIZE; slotId++) {
      if (inv.getStackInSlot(slotId) == ItemStack.EMPTY
          || (inv.getStackInSlot(slotId).isItemEqual(output)
              && inv.getStackInSlot(slotId).getCount() + output.getCount() <= output.getMaxStackSize())) {
        return true;
      }
      if (output == ItemStack.EMPTY || output.getCount() == 0) {
        return true;
      }
    }
    return false;
  }

  private boolean doCraft(IItemHandler input, boolean simulate) {
    HashMap<Integer, List<ItemStack>> putbackStacks = new HashMap<>();
    for (Ingredient ingredient : lastValidRecipe.getIngredients()) {
      if (ingredient == Ingredient.EMPTY) {
        continue;
      }
      boolean matched = false;
      for (int index = 0; index < input.getSlots(); index++) {
        ItemStack itemStack = input.getStackInSlot(index);
        if (ingredient.test(itemStack)) {
          if (putbackStacks.containsKey(index)) {
            putbackStacks.get(index).add(new ItemStack(input.getStackInSlot(index).getItem(), 1));
          }
          else {
            List<ItemStack> list = new ArrayList<>();
            list.add(new ItemStack(input.getStackInSlot(index).getItem(), 1));
            putbackStacks.put(index, list);
          }
          matched = true;
          input.extractItem(index, 1, false);
          break;
        }
      }
      if (!matched) {
        putbackStacks(putbackStacks, input);
        return false;
      }
    }
    if (simulate) {
      putbackStacks(putbackStacks, input);
    }
    return true;
  }

  private void putbackStacks(HashMap<Integer, List<ItemStack>> putbackStacks, IItemHandler itemHandler) {
    for (HashMap.Entry<Integer, List<ItemStack>> entry : putbackStacks.entrySet()) {
      for (ItemStack stack : entry.getValue()) {
        itemHandler.insertItem(entry.getKey(), stack, false);
      }
    }
  }

  private IRecipe<?> tryRecipes(ArrayList<ItemStack> itemStacksInGrid) {
    if (world == null || world.getServer() == null) {
      return null;
    }
    Collection<IRecipe<?>> recipes = world.getServer().getRecipeManager().getRecipes();
    for (IRecipe<?> recipe : recipes) {
      if (recipe instanceof ShapelessRecipe) {
        ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
        if (tryMatchShapelessRecipe(itemStacksInGrid, shapelessRecipe)) {
          return shapelessRecipe;
        }
      }
      else if (recipe instanceof ShapedRecipe) {
        ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
        if (!doSizesMatch(shapedRecipe, itemStacksInGrid)) {
          continue;
        }
        if (tryMatchShapedRecipe(itemStacksInGrid, shapedRecipe)) {
          return shapedRecipe;
        }
      }
    }
    return null;
  }

  private boolean tryMatchShapedRecipe(ArrayList<ItemStack> itemStacks, ShapedRecipe recipe) {
    for (int i = 0; i <= 3 - recipe.getWidth(); ++i) {
      for (int j = 0; j <= 3 - recipe.getHeight(); ++j) {
        if (this.tryMatchShapedRecipeRegion(itemStacks, recipe, i, j)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean tryMatchShapelessRecipe(ArrayList<ItemStack> itemStacks, ShapelessRecipe recipe) {
    ArrayList<ItemStack> itemStacksCopy = (ArrayList<ItemStack>) itemStacks.clone();
    for (Ingredient ingredient : recipe.getIngredients()) {
      Iterator<ItemStack> iter = itemStacksCopy.iterator();
      boolean matched = false;
      while (iter.hasNext()) {
        ItemStack itemStack = iter.next();
        if (ingredient.test(itemStack)) {
          iter.remove();
          matched = true;
          break;
        }
      }
      if (!matched) {
        return false;
      }
    }
    return countNonEmptyStacks(itemStacksCopy) == 0;
  }

  public static class FakeContainer extends Container {

    protected FakeContainer(ContainerType<?> type, int id) {
      super(type, id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
      return true;
    }
  }

  private final CraftingInventory craftMatrix = new CraftingInventory(new FakeContainer(ContainerType.CRAFTING, 18291238), 3, 3);

  private boolean tryMatchShapedRecipeRegion(ArrayList<ItemStack> itemStacks, ShapedRecipe recipe, int offsetX, int offsetY) {
    for (int i = 0; i < recipe.getWidth(); i++) {
      for (int j = 0; j < recipe.getHeight(); j++) {
        try {
          int indexInArray = i + j * 3;
          ItemStack itemStack = itemStacks.get(indexInArray);
          craftMatrix.setInventorySlotContents(indexInArray, itemStack.copy());
        }
        catch (Exception e) {
          //breakpoint 
          return false;
        }
      }
    }
    boolean matched = recipe.matches(craftMatrix, world);
    //    ModCyclic.LOGGER.info(recipe.getRecipeOutput() + " -0 matched recipe and getRemainingItems " + matched);
    return matched;
  }

  public void reset() {
    this.hasValidRecipe = false;
    this.lastRecipeGrid = null;
    this.lastValidRecipe = null;
    this.shouldSearch = true;
    this.timer = 0;
    IItemHandler previewHandler = this.preview.orElse(null);
    if (previewHandler != null) {
      setPreviewSlot(previewHandler, ItemStack.EMPTY);
    }
  }

  private int countNonEmptyStacks(ArrayList<ItemStack> itemStacks) {
    int count = 0;
    for (ItemStack itemStack : itemStacks) {
      if (itemStack != ItemStack.EMPTY) {
        count++;
      }
    }
    return count;
  }

  private boolean doSizesMatch(ShapedRecipe recipe, ArrayList<ItemStack> itemStacks) {
    int ingredientCount = 0;
    int itemStackCount = 0;
    for (Ingredient ingredient : recipe.getIngredients()) {
      if (!ingredient.test(ItemStack.EMPTY)) {
        ingredientCount++;
      }
    }
    for (ItemStack itemStack : itemStacks) {
      if (itemStack != ItemStack.EMPTY) {
        itemStackCount++;
      }
    }
    return ingredientCount == itemStackCount;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerCrafter(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  public <T> LazyOptional<T> getCapability(Capability<T> cap, ItemHandlers type) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      switch (type) {
        case INPUT:
          return input.cast();
        case OUTPUT:
          return output.cast();
        case GRID:
          return gridCap.cast();
        case PREVIEW:
          return preview.cast();
      }
    }
    return null;
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energyCap.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    input.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("input")));
    output.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("output")));
    gridCap.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("grid")));
    preview.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("preview")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    energyCap.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    input.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("input", compound);
    });
    output.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("output", compound);
    });
    gridCap.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("grid", compound);
    });
    preview.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("preview", compound);
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
