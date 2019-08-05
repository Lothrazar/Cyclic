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
package com.lothrazar.cyclicmagic.block.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import com.lothrazar.cyclicmagic.data.InvWrapperRestricted;
import com.lothrazar.cyclicmagic.data.StackWrapper;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityBaseMachineInvo extends TileEntityBaseMachine implements IInventory, ISidedInventory {

  protected static final int SPEED_FUELED = 8;
  private static final int MAX_SPEED = 10;
  public static final int MENERGY = 64 * 1000;
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  public static final String NBT_TIMER = "Timer";
  public static final String NBT_REDST = "redstone";
  public static final String NBT_SIZE = "size";
  public static final String NBTPLAYERID = "uuid";
  public static final String NBT_SPEED = "speed";
  public static final String NBT_RENDER = "render";
  public static final String NBT_TANK = "tankwater";
  private static final String NBT_ENERGY = "ENERGY";
  public static final String NBT_UHASH = "uhash";
  public static final String NBT_UNAME = "uname";
  protected NonNullList<ItemStack> inv;
  private int energyCost = 0;
  /**
   * speed > 0
   */
  protected int speed = 1;
  protected int timer;
  protected int renderParticles = 0;
  protected int needsRedstone = 0;
  //Vanilla Furnace has this -> makes it works with some modded pipes such as EXU2
  InvWrapperRestricted invHandler;
  protected EnergyStore energyStorage;
  private boolean setRenderGlobally;
  private boolean hasEnergy;

  public TileEntityBaseMachineInvo(int invoSize) {
    super();
    inv = NonNullList.withSize(invoSize, ItemStack.EMPTY);
    invHandler = new InvWrapperRestricted(this);
  }

  protected void setSlotsForExtract(int slot) {
    this.setSlotsForExtract(Arrays.asList(slot));
  }

  protected void setSlotsForInsert(int slot) {
    this.setSlotsForInsert(Arrays.asList(slot));
  }

  protected void setSlotsForExtract(List<Integer> slots) {
    invHandler.setSlotsExtract(slots);
  }

  protected void setSlotsForExtract(int startInclusive, int endInclusive) {
    setSlotsForExtract(
        IntStream.rangeClosed(
            startInclusive,
            endInclusive).boxed().collect(Collectors.toList()));
  }

  protected void setSlotsForInsert(int startInclusive, int endInclusive) {
    setSlotsForInsert(
        IntStream.rangeClosed(
            startInclusive,
            endInclusive).boxed().collect(Collectors.toList()));
  }

  protected void setSlotsForInsert(List<Integer> slots) {
    invHandler.setSlotsInsert(slots);
  }

  protected void setSlotsForBoth(List<Integer> slots) {
    invHandler.setSlotsInsert(slots);
    invHandler.setSlotsExtract(slots);
  }

  /**
   * no input means all slots
   */
  protected void setSlotsForBoth() {
    this.setSlotsForBoth(IntStream.rangeClosed(0, this.getSizeInventory()).boxed().collect(Collectors.toList()));
  }

  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return this.isItemValidForSlot(index, itemStackIn)
        && this.invHandler.canInsert(index);
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    return index != this.getEnergyCost() && //override to inv handler: do not extract fuel
        this.invHandler.canExtract(index);
  }

  protected void initEnergy(EnergyStore store, int energyCost) {
    this.energyStorage = store;
    this.hasEnergy = true;
    this.setEnergyCost(energyCost);
  }

  public int getEnergyMax() {
    if (energyStorage == null) {
      return 0;
    }
    return this.energyStorage.getMaxEnergyStored();
  }

  public int getEnergyCurrent() {
    if (this.energyStorage == null) {
      return 0;
    }
    return this.energyStorage.getEnergyStored();
  }

  public void setEnergyCurrent(int f) {
    this.energyStorage.setEnergyStored(f);
  }

  public int getEnergyCost() {
    return this.energyCost;
  }

  public void consumeEnergy() {
    //only drain on server, if we have enough and if not free
    if (!world.isRemote && this.getEnergyCost() > 0 &&
        this.getEnergyCurrent() >= this.getEnergyCost()) {
      this.energyStorage.extractEnergy(this.getEnergyCost(), false);
      //it drained, notify client 
      this.markDirty();
    }
  }

  public int[] getFieldArray(int length) {
    return IntStream.rangeClosed(0, length - 1).toArray();
  }

  public boolean isDoingWork() {
    return super.isRunning() && this.hasEnoughEnergy();
  }

  public boolean updateEnergyIsBurning() {
    if (this.getEnergyCost() > 0) {
      if (this.hasEnoughEnergy()) {
        this.consumeEnergy();
      }
      else {
        // dont run, dont count down, just stop now 
        return false;
      }
    }
    return true;
  }

  /**
   * much energy code helped out and referenced and inspired by @Ellpeck and then I tweaked it to fit my needs
   * https://github.com/Ellpeck/ActuallyAdditions/blob/9bed6f7ea59e8aa23fa3ba540d92cd61a04dfb2f/src/main/java/de/ellpeck/actuallyadditions/mod/util/WorldUtil.java#L151
   */
  public boolean hasEnoughEnergy() {
    if (this.getEnergyCost() == 0) {
      return true;
    }
    return this.getEnergyCurrent() >= this.getEnergyCost();
  }

  protected boolean updateTimerIsZero() {
    timer -= this.getSpeed();
    if (timer < 0) {
      timer = 0;
    }
    return timer == 0;
  }

  @Override
  public String getName() {
    if (this.getBlockType() == null) {
      return "";
    }
    return this.getBlockType().getTranslationKey() + ".name";
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }

  @Override
  public int getField(int id) {
    return 0;
  }

  @Override
  public void setField(int id, int value) {}

  @Override
  public int getFieldCount() {
    return 0;
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Override
  public ITextComponent getDisplayName() {
    return null;
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public void openInventory(EntityPlayer player) {}

  @Override
  public void closeInventory(EntityPlayer player) {}

  @Override
  public void clear() {
    for (int i = 0; i < this.inv.size(); ++i) {
      inv.set(i, ItemStack.EMPTY);
    }
  }

  protected void shiftAllUp() {
    shiftAllUp(0);
  }

  /**
   * pass in how many slots on the end ( right ) to skip
   * 
   * @param endOffset
   */
  protected void shiftAllUp(int endOffset) {
    if (this.world.isRemote == false) {
      for (int i = 0; i < this.getSizeInventory() - endOffset - 1; i++) {
        shiftPairUp(i, i + 1);
      }
    }
  }

  protected void shiftPairUp(int low, int high) {
    ItemStack main = getStackInSlot(low);
    ItemStack second = getStackInSlot(high);
    if (main.isEmpty() && !second.isEmpty()) {
      this.setInventorySlotContents(high, ItemStack.EMPTY);
      this.setInventorySlotContents(low, second);
    }
    else if (!main.isEmpty() && !second.isEmpty()) { // if the one below this is not
      if (ItemStack.areItemsEqual(main, second)
          && UtilNBT.stacksTagsEqual(main, second)) {
        //temSt        main.stack 
        //         this.tryMergeStackIntoSlot(held, furnaceSlot)
        ItemStack test = main.copy();
        test.setCount(second.getCount() + main.getCount());
        if (this.isItemValidForSlot(low, test)
            && main.getCount() + second.getCount() < 64) {
          main.setCount(second.getCount() + main.getCount());
          second.setCount(0);
        }
      }
      // empty, move it up
    }
  }

  @Override
  public int getSizeInventory() {
    return inv.size();
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    if (index < 0 || index >= getSizeInventory()) {
      return ItemStack.EMPTY;
    }
    return inv.get(index);
  }

  public ItemStack decrStackSize(int index) {
    return this.decrStackSize(index, 1);
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (!stack.isEmpty()) {
      if (stack.getMaxStackSize() <= count) {
        setInventorySlotContents(index, ItemStack.EMPTY);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.getMaxStackSize() == 0) {
          setInventorySlotContents(index, ItemStack.EMPTY);
        }
      }
    }
    return stack;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    setInventorySlotContents(index, ItemStack.EMPTY);
    return stack;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    if (stack == null) {
      stack = ItemStack.EMPTY;
    }
    if (!stack.isEmpty() && stack.getMaxStackSize() > getInventoryStackLimit()) {
      stack.setCount(getInventoryStackLimit());
    }
    inv.set(index, stack);
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return IntStream.range(0, this.getSizeInventory()).toArray();
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean isUsableByPlayer(EntityPlayer player) {
    return true;
  }

  private void readInvoFromNBT(NBTTagCompound tagCompound) {
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.size()) {
        inv.set(slot, UtilNBT.itemFromNBT(tag));
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.readInvoFromNBT(compound);
    renderParticles = compound.getInteger(NBT_RENDER);
    speed = compound.getInteger(NBT_SPEED);
    needsRedstone = compound.getInteger(NBT_REDST);
    timer = compound.getInteger(NBT_TIMER);
    if (this.hasEnergy && compound.hasKey(NBT_ENERGY)) {
      CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.getTag(NBT_ENERGY));
    }
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    this.writeInvoToNBT(compound);
    compound.setInteger(NBT_RENDER, renderParticles);
    compound.setInteger(NBT_SPEED, speed);
    compound.setInteger(NBT_TIMER, timer);
    compound.setInteger(NBT_REDST, needsRedstone);
    if (hasEnergy && energyStorage != null) {
      compound.setTag(NBT_ENERGY, CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
    }
    return super.writeToNBT(compound);
  }

  private void writeInvoToNBT(NBTTagCompound compound) {
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.size(); i++) {
      ItemStack stack = inv.get(i);
      if (!stack.isEmpty()) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    compound.setTag(NBT_INV, itemList);
  }

  @Override
  public boolean receiveClientEvent(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      this.setField(id, value);
      return true;
    }
    else {
      return super.receiveClientEvent(id, value);
    }
  }

  public ItemStack tryMergeStackIntoSlot(ItemStack held, int furnaceSlot) {
    ItemStack current = this.getStackInSlot(furnaceSlot);
    boolean success = false;
    if (current.isEmpty()) {
      this.setInventorySlotContents(furnaceSlot, held);
      held = ItemStack.EMPTY;
      success = true;
    }
    else if (held.isItemEqual(current)) {
      success = true;
      UtilItemStack.mergeItemsBetweenStacks(held, current);
    }
    if (success) {
      if (held != ItemStack.EMPTY && held.getMaxStackSize() == 0) {// so now we just fix if something is size zero
        held = ItemStack.EMPTY;
      }
      this.markDirty();
    }
    return held;
  }

  public int[] getFieldOrdinals() {
    return new int[0];
  }

  public int getSpeed() {
    if (this.getEnergyCost() == 0) {
      return this.speed;// does not use fuel. use NBT saved speed value
    }
    else {
      if (this.getEnergyCurrent() == 0) {
        return 0; // do not run without fuel
      }
      else {
        return Math.max(this.speed, 1);// i have fuel, use what it says eh
      }
    }
  }

  public void setSpeed(int value) {
    if (value < 0) {
      value = 0;
    }
    speed = Math.min(value, MAX_SPEED);
  }

  @Override
  public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        && this.getSizeInventory() > 0) {
      return true;
    }
    if (capability == CapabilityEnergy.ENERGY
        && this.energyStorage != null) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
    if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return (T) invHandler;
    }
    if (this.hasEnergy && capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(energyStorage);
    }
    return super.getCapability(capability, facing);
  }

  protected boolean isInventoryFull() {
    return isInventoryFull(0);
  }

  protected boolean isInventoryFull(int start) {
    for (int i = start; i < this.inv.size(); i++) {
      //if its empty or it is below max count, then it has room -> not full
      if (this.inv.get(i).isEmpty()
          || this.inv.get(i).getCount() < this.inv.get(i).getMaxStackSize()) {
        return false;
      }
    }
    //no empty stacks found
    return true;
  }

  protected boolean inventoryHasRoom(int start, final ItemStack wouldInsert) {
    int emptySlots = 0;
    for (int i = start; i < this.inv.size(); i++) {
      //if its empty or it is below max count, then it has room -> not full
      ItemStack invStack = this.inv.get(i);
      if (invStack.isEmpty()) {
        return true;
      }
      //      || this.inv.get(i).getCount() < this.inv.get(i).getMaxStackSize()
      //if the stack matches, AND theres n
      if (ItemStack.areItemsEqual(invStack, wouldInsert)) {
        emptySlots += (invStack.getMaxStackSize() - invStack.getCount());
      }
    }
    //no empty stacks found 
    return emptySlots >= wouldInsert.getCount();
  }

  /**
   * Returns true only if every slot is empty
   * 
   * ignores fuelSlot
   * 
   * @return boolean
   */
  protected boolean isInventoryEmpty() {
    for (int i = 0; i < this.inv.size(); i++) {
      // something is non-empty: false right away
      if (this.inv.get(i).isEmpty() == false) {
        return false;
      }
    }
    //every stack we tested was empty
    return true;
  }

  public boolean isSetRenderGlobally() {
    return setRenderGlobally;
  }

  public void setSetRenderGlobally(boolean setRenderGlobally) {
    this.setRenderGlobally = setRenderGlobally;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public double getMaxRenderDistanceSquared() {
    if (this.isSetRenderGlobally())
      return 65536.0D;
    else
      return super.getMaxRenderDistanceSquared();
  }

  /**
   * https://shadowfacts.net/tutorials/forge-modding-1112/dynamic-tileentity-rendering/
   */
  @Override
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    if (this.isSetRenderGlobally())
      return TileEntity.INFINITE_EXTENT_AABB;
    else
      return super.getRenderBoundingBox();
  }

  public void readStackWrappers(NonNullList<StackWrapper> stacksWrapped, NBTTagCompound compound) {
    NBTTagList invList = compound.getTagList("ghostSlots", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < invList.tagCount(); i++) {
      NBTTagCompound stackTag = invList.getCompoundTagAt(i);
      int slot = stackTag.getByte("Slot");
      stacksWrapped.set(slot, StackWrapper.loadStackWrapperFromNBT(stackTag));
    }
  }

  public void writeStackWrappers(NonNullList<StackWrapper> stacksWrapped, NBTTagCompound compound) {
    NBTTagList invList = new NBTTagList();
    for (int i = 0; i < stacksWrapped.size(); i++) {
      NBTTagCompound stackTag = new NBTTagCompound();
      stackTag.setByte("Slot", (byte) i);
      stacksWrapped.get(i).writeToNBT(stackTag);
      invList.appendTag(stackTag);
    }
    compound.setTag("ghostSlots", invList);
  }

  protected void tryOutputPower(int TRANSFER_ENERGY_PER_TICK) {
    // TODO share code in base class somehow? with CableBase maybe?
    List<EnumFacing> targetFaces = Arrays.asList(EnumFacing.values());
    Collections.shuffle(targetFaces);
    for (EnumFacing myFacingDir : targetFaces) {
      BlockPos posTarget = pos.offset(myFacingDir);
      if (this.hasCapability(CapabilityEnergy.ENERGY, myFacingDir) == false) {
        continue;
      }
      IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, myFacingDir);
      TileEntity tileTarget = world.getTileEntity(posTarget);
      if (tileTarget == null) {
        continue;
      }
      IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, myFacingDir.getOpposite());
      if (handlerHere != null && handlerOutput != null
          && handlerHere.canExtract() && handlerOutput.canReceive()) {
        //first simulate
        int drain = handlerHere.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
        if (drain > 0) {
          //now push it into output, but find out what was ACTUALLY taken
          int filled = handlerOutput.receiveEnergy(drain, false);
          //now actually drain that much from here
          handlerHere.extractEnergy(filled, false);
          if (tileTarget instanceof TileEntityCableBase) {
            //TODO: not so compatible with other fluid systems. itl do i guess
            TileEntityCableBase cable = (TileEntityCableBase) tileTarget;
            if (cable.isEnergyPipe())
              cable.updateIncomingEnergyFace(myFacingDir.getOpposite());
          }
          //              return;// stop now because only pull from one side at a time
        }
      }
    }
  }

  protected List<EnumFacing> getSidesNotFacing() {
    EnumFacing in = this.getCurrentFacing();
    List<EnumFacing> sidesOut = new ArrayList<>();
    for (EnumFacing s : EnumFacing.values())
      if (s != in)
        sidesOut.add(s);
    Collections.shuffle(sidesOut);
    return sidesOut;
  }

  public void setEnergyCost(int energyCost) {
    this.energyCost = energyCost;
  }

  @Override
  public boolean isRunning() {
    this.getEnergyCost();
    this.hasEnoughEnergy();
    return super.isRunning();
  }
}
