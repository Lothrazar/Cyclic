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
package com.lothrazar.cyclicmagic.block.crafter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

public class TileEntityCrafter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  public static final int TIMER_FULL = 20;
  public static final int ROWS = 5;
  public static final int COLS = 2;
  public static final int SIZE_INPUT = ROWS * COLS;//10
  public static final int SIZE_GRID = 3 * 3;//19
  public static final int SIZE_OUTPUT = ROWS * COLS;//20 to 30
  public static final int FILTER_SIZE = 9;
  private Map<Integer, StackWrapper> filter = new HashMap<Integer, StackWrapper>();

  public static enum Fields {
    REDSTONE, TIMER;
  }

  private Container fakeContainer;
  private IRecipe recipe;
  private int needsRedstone = 1;
  private InventoryCrafting crafter;

  public TileEntityCrafter() {
    super(SIZE_INPUT + SIZE_GRID + SIZE_OUTPUT);// left and right side both have a tall rectangle. then 3x3 crafting 
    fakeContainer = new Container() {

      @Override
      public boolean canInteractWith(@Nonnull final EntityPlayer playerIn) {
        return false;
      }
    };
    crafter = new InventoryCrafting(fakeContainer, 3, 3);
    this.initEnergyWithCost(BlockCrafter.FUEL_COST);
    this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    this.setSlotsForExtract(Arrays.asList(19, 20, 21, 22, 23, 24, 25, 26, 27, 28));
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    this.spawnParticlesAbove();
    if (this.updateTimerIsZero() == false) {
      return;
    }
    
    //so now we do not burn fuel if timer is stuck at zero with no craft action
    if (this.getEnergyCurrent() >= this.getEnergyCost()) {
      findRecipe();
      if (recipe != null && tryPayCost()) {
        // pay the cost  
        final ItemStack craftingResult = recipe.getCraftingResult(this.crafter);
        //confirmed this test does actually et the outut: 4x planks 
        sendOutput(craftingResult);
        timer = TIMER_FULL;
        this.consumeEnergy();
      }
    }
  }

  private boolean tryPayCost() {
    ItemStack fromRecipe;
    ItemStack fromInput;
    boolean thisPaid = false;
    //map is <slotNumber, amtToPay>
    Map<Integer, Integer> slotsToPay = new HashMap<Integer, Integer>();//can have dupes
    for (int i = 0; i < this.crafter.getSizeInventory(); i++) {
      //for ever i val, we must pay the cost
      thisPaid = false;
      fromRecipe = this.crafter.getStackInSlot(i);
      if (fromRecipe.isEmpty()) {
        continue;
      }
      //try to pay its cost
      for (int j = 0; j < SIZE_INPUT; j++) {
        fromInput = this.getStackInSlot(j);
        if (fromRecipe.isItemEqual(fromInput)) {
          //now set the key 'j' slot to need one more extra item
          if (!slotsToPay.containsKey(j)) {
            slotsToPay.put(j, 0);
          }
          //if what we are going to be pulling from this slot not more than what it contains
          if (slotsToPay.get(j) + 1 <= fromInput.getCount()) {
            slotsToPay.put(j, slotsToPay.get(j) + 1);
            thisPaid = true;
            break;//break only the j loop
          }
        }
      }
      if (thisPaid == false) {//required input not even fond
        return false;
      }
    }
    //TODO: in retroscpect this 2econd pass might be redndant since we already validated in above loop. but keeping it doesnt hurt
    //we need to do 2 passes. one pass to make sure we haven enough and another to cost
    //otherwise we could start spending and halfway thru run out and we would havce to rollback ransacions
    //and couldnt have done it above because of the slot spread
    //EX: stairs need 6 wood planks. This could be 6 all from one stack, or split over a few
    for (Map.Entry<Integer, Integer> entry : slotsToPay.entrySet()) {
      // if there isnt enough, in any one of these spots, stop now
      if (entry.getValue() > this.getStackInSlot(entry.getKey()).getCount()) {
        return false;
      }
    }
    //now we know there is enough everywhere. we validated
    for (Map.Entry<Integer, Integer> entry : slotsToPay.entrySet()) {
      //      ModCyclic.logger.info(" PAY cost at  = " + entry);
      Item bucketThing = this.getStackInSlot(entry.getKey()).getItem().getContainerItem();
      if (bucketThing != null && this.getStackInSlot(entry.getKey()).getCount() == 1) {
        //example: making cake, dump out empty bucket
        this.sendOutput(new ItemStack(bucketThing));
      }
      this.getStackInSlot(entry.getKey()).shrink(entry.getValue());
    }
    return true;
  }

  private void sendOutput(ItemStack craftingResult) {
    //bit o a hack since util method assmes takes a list, and we have only one, so just wrap it eh
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(Arrays.asList(craftingResult), this, SIZE_INPUT + SIZE_GRID);
    //if something is given back, it didnt fit so we have to spew
    if (!toDrop.isEmpty()) {
      for (ItemStack s : toDrop) {
        UtilItemStack.dropItemStackInWorld(this.getWorld(), this.getPos().up(), s);
      }
    }
  }

  private void findRecipe() {
    setRecipeInput();//make sure the 3x3 inventory is linked o the crater
    if (this.recipe != null && recipe.matches(crafter, world)) {
      //recipe exists and it matches whats currently in the gui so stop now
      return;
    }
    recipe = null;//doesnt match
    //    final List<IRecipe> recipes = CraftingManager.field_193380_a();//.getInstance().getRecipeList();
    for (final IRecipe rec : CraftingManager.REGISTRY) {
      try {
        // rec.getRecipeSize() <= 9 && 
        if (rec.matches(this.crafter, this.world)) {
          this.recipe = rec;
          return;
        }
      }
      catch (Exception err) {
        throw new RuntimeException("Caught exception while querying recipe ", err);
      }
    }
  }

  private void setRecipeInput() {
    int gridStart = SIZE_INPUT, craftSlot;
    for (int i = gridStart; i < gridStart + SIZE_GRID; i++) {
      craftSlot = i - gridStart;
      //      ModCyclic.logger.info("Crafter set "+craftSlot+"_"+ this.getStackInSlot(i ));
      this.crafter.setInventorySlotContents(craftSlot, this.getStackInSlot(i));
    }
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
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public void toggleNeedsRedstone() {
    this.setField(Fields.REDSTONE.ordinal(), (this.needsRedstone + 1) % 2);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    needsRedstone = compound.getInteger(NBT_REDST);
    timer = compound.getInteger(NBT_TIMER);
    setFilter(new HashMap<Integer, StackWrapper>());
    NBTTagList invList = compound.getTagList("crunchTE", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < invList.tagCount(); i++) {
      NBTTagCompound stackTag = invList.getCompoundTagAt(i);
      int slot = stackTag.getByte("Slot");
      getFilter().put(slot, StackWrapper.loadStackWrapperFromNBT(stackTag));
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagList invList = new NBTTagList();
    for (int i = 0; i < FILTER_SIZE; i++) {
      if (getFilter().get(i) != null) {
        NBTTagCompound stackTag = new NBTTagCompound();
        stackTag.setByte("Slot", (byte) i);
        getFilter().get(i).writeToNBT(stackTag);
        invList.appendTag(stackTag);
      }
    }
    compound.setInteger(NBT_TIMER, timer);
    compound.setInteger(NBT_REDST, needsRedstone);
    return super.writeToNBT(compound);
  }

  public Map<Integer, StackWrapper> getFilter() {
    return filter;
  }

  public void setFilter(Map<Integer, StackWrapper> filter) {
    this.filter = filter;
  }
}
