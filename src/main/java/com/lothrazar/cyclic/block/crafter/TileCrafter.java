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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("unchecked")
public class TileCrafter extends TileBlockEntityCyclic implements MenuProvider {

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

  public enum ItemHandlers {
    INPUT, OUTPUT, GRID, PREVIEW
  };

  public enum Fields {
    TIMER, REDSTONE, RENDER;
  }

  public TileCrafter(BlockPos pos, BlockState state) {
    super(TileRegistry.CRAFTER.get(), pos, state);
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack stack) {
    if (index != PREVIEW_SLOT &&
        (index < OUTPUT_SLOT_START || index > OUTPUT_SLOT_STOP)) {
      super.canPlaceItem(index, stack);
    }
    return false;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCrafter tile) {
    tile.serverTick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileCrafter tile) {
    tile.serverTick();
  }

  public void serverTick() {
    //redstone phase
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    //i am running
    setLitProperty(true);
    if (this.level.isClientSide) {
      return;
    }
    //do i have enough power phase
    IEnergyStorage cap = this.energyCap.orElse(null);
    if (cap == null) {
      return;
    }
    this.syncEnergy();
    final int cost = POWERCONF.get();
    if (cap.getEnergyStored() < cost && cost > 0) {
      return;
    }
    if (timer < 0) {
      timer = 0;
    }
    //timer phase
    if (--timer > 0) {
      return;
    }
    //timer is out, therefore processing 
    Recipe<CraftingContainer> lastValidRecipe = findMatchingRecipe(null);
    if (lastValidRecipe == null) {
      //reset 
      this.timer = TIMER_FULL;
      setPreviewSlot(ItemStack.EMPTY);
    }
    else {
      //recipes not null and it matches  
      ItemStack recipeOutput = lastValidRecipe.getResultItem(level.registryAccess()).copy();
      setPreviewSlot(recipeOutput);
      //if we have space for the output, then go ahead
      if (hasFreeSpace(outHandler, recipeOutput)) {
        if (doCraft(lastValidRecipe)) {
          //reset the timer
          this.timer = TIMER_FULL;
          //pay energy cost
          this.energy.extractEnergy(cost, false);
          //get the result item
          depositOutput(recipeOutput, outHandler);
          //stuff like empty buckets happen down here
          NonNullList<ItemStack> rem = lastValidRecipe.getRemainingItems(craftMatrix);
          for (int i = 0; i < rem.size(); ++i) {
            ItemStack s = rem.get(i);
            if (!s.isEmpty() && s.getItem() == craftMatrix.getItem(i).getItem()) {
              s = depositOutput(s, this.inputHandler);
            }
            //otherwise its something like bucket->empty bucket, so different item, so right side 
            depositOutput(s, this.outHandler);
          }
        }
      }
    }
  }

  private ItemStack depositOutput(ItemStack recipeOutput, ItemStackHandler dest) {
    if (recipeOutput.isEmpty()) {
      return recipeOutput;
    }
    for (int slotId = 0; slotId < dest.getSlots(); slotId++) {
      recipeOutput = dest.insertItem(slotId, recipeOutput, false);
      if (recipeOutput.isEmpty()) {
        break;
      }
    }
    return recipeOutput;
  }

  private void setPreviewSlot(ItemStack itemStack) {
    IItemHandler previewHandler = this.preview.orElse(null);
    if (previewHandler != null) {
      previewHandler.extractItem(0, 64, false);
      previewHandler.insertItem(0, itemStack, false);
    }
  }

  private boolean hasFreeSpace(IItemHandler inv, ItemStack output) {
    ItemStack test = output.copy();
    for (int slotId = 0; slotId < inv.getSlots(); slotId++) {
      test = inv.insertItem(slotId, test, true); // simulated
    }
    return test.isEmpty(); //empty means all of it was allowed to go in
  }

  //TODO:? re-write this whole thing using ASSEMBLE?
  //big change
  //  for (int i = 0; i < 9; i++) {
  //    Ingredient ingredient = lastValidRecipe.getIngredients().get(i);
  //    String s = ingredient.isEmpty() ? "empty" : "" + ingredient.getItems()[0].getDisplayName().getString();
  //   println(i + " => " + s + "   matrix " + craftMatrix.getItem(i));
  //    //for this ingredient. the mapping and grid lineup is correct
  //    //find ingredients for each and use recipe.assemble(craftMatrixCopy);
  //    //to solve the durability issue?
  //    // https://github.com/Lothrazar/Cyclic/issues/1947
  //  }
  private boolean doCraft(Recipe<CraftingContainer> lastValidRecipe) {
    HashMap<Integer, List<ItemStack>> putbackStacks = new HashMap<>();
    for (Ingredient ingredient : lastValidRecipe.getIngredients()) {
      if (ingredient.isEmpty()) {
        continue;
      }
      boolean matched = false;
      for (int index = 0; index < inputHandler.getSlots(); index++) {
        ItemStack itemStack = inputHandler.getStackInSlot(index);
        if (ingredient.test(itemStack)) {
          if (putbackStacks.containsKey(index)) {
            putbackStacks.get(index).add(inputHandler.getStackInSlot(index).copy());
          }
          else {
            List<ItemStack> list = new ArrayList<>();
            list.add(inputHandler.getStackInSlot(index).copy());
            putbackStacks.put(index, list);
          }
          matched = true;
          inputHandler.extractItem(index, 1, false);
          break;
        }
      }
      if (!matched) {
        putbackStacks(putbackStacks, inputHandler);
        return false;
      }
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

  private Recipe<CraftingContainer> findMatchingRecipe(ArrayList<ItemStack> itemStacksInGrid) {
    IItemHandler gridHandler = this.gridCap.orElse(null);
    for (int i = 0; i < gridHandler.getSlots(); i++) {
      craftMatrix.setItem(i, gridHandler.getStackInSlot(i).copy());//fake items anyway. but also jus do a copy
    }
    List<CraftingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
    for (CraftingRecipe rec : recipes) {
      if (rec.matches(craftMatrix, level)) {
        return rec;
      }
    }
    return null;
  }

  public static class FakeContainer extends AbstractContainerMenu {

    protected FakeContainer(MenuType<?> type, int id) {
      super(type, id);
    }

    @Override
    public boolean stillValid(Player playerIn) {
      return true;
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
      return ItemStack.EMPTY;
    }
  }

  private final CraftingContainer craftMatrix = new CraftingContainer(new FakeContainer(MenuType.CRAFTING, 18291238), 3, 3);

  @Override
  public Component getDisplayName() {
    return BlockRegistry.CRAFTER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCrafter(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  public <T> LazyOptional<T> getCapability(Capability<T> cap, ItemHandlers type) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
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
  public void load(CompoundTag tag) {
    energyCap.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(tag.getCompound("energy")));
    input.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(tag.getCompound("input")));
    output.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(tag.getCompound("output")));
    gridCap.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(tag.getCompound("grid")));
    preview.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(tag.getCompound("preview")));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    energyCap.ifPresent(h -> {
      CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
      tag.put("energy", compound);
    });
    input.ifPresent(h -> {
      CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
      tag.put("input", compound);
    });
    output.ifPresent(h -> {
      CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
      tag.put("output", compound);
    });
    gridCap.ifPresent(h -> {
      CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
      tag.put("grid", compound);
    });
    preview.ifPresent(h -> {
      CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
      tag.put("preview", compound);
    });
    super.saveAdditional(tag);
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
