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
package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.PeatFuelBlock;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePeatFarm extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    REDSTONE, RENDER;
  }

  public static IntValue POWERCONF;
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  static final int MAX = 64000;
  public static final int TIMER_FULL = 1 * 10;
  private static final int PER_TICK = 1;
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> tankWrapper = LazyOptional.of(() -> tank);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(6) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return Block.getBlockFromItem(stack.getItem()) == BlockRegistry.peat_unbaked;
    }
  };
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int blockPointer = 0;

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerPeatFarm(i, world, pos, playerInventory, playerEntity);
  }

  private void init() {
    if (baked == null) {
      baked = BlockRegistry.peat_baked;
    }
    if (unbaked == null) {
      unbaked = BlockRegistry.peat_unbaked;
    }
    if (outer == null) {
      outer = getShape();
      List<BlockPos> waterShape = UtilShape.squareHorizontalHollow(this.pos, 6);
      outer.addAll(waterShape);
    }
  }

  @Override
  public void tick() {
    this.syncEnergy();
    this.init();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      blockPointer = 0;
      return;
    }
    setLitProperty(true);
    if (this.timer > 0) {
      this.timer--;
      return;
    }
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      return;
    }
    for (int i = 0; i < PER_TICK; i++) {
      if (blockPointer < outer.size()) {
        BlockPos target = outer.get(blockPointer);
        boolean placeWater = (target.getX() - pos.getX()) % 3 == 0
            && (target.getZ() - pos.getZ()) % 3 == 0;
        if (placeWater) {
          if (tryPlaceWater(target)) {
            energy.extractEnergy(cost, false);
          }
        }
        else if (tryPlacePeat(target)) {
          energy.extractEnergy(cost, false);
        }
        blockPointer++;
      }
      else {
        blockPointer = 0;
      }
    }
    this.timer = TIMER_FULL;
  }

  @Override
  public void setField(int field, int value) {
    switch (TilePeatFarm.Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }

  @Override
  public int getField(int id) {
    switch (TilePeatFarm.Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
    }
    return 0;
  }

  public TilePeatFarm() {
    super(TileRegistry.peat_farm);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  }

  Block baked = null;
  Block unbaked = null;
  List<BlockPos> outer = null;

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) == unbaked;
  }

  List<BlockPos> getShape() {
    List<BlockPos> outer = UtilShape.squareHorizontalHollow(this.pos, 7);
    outer.addAll(UtilShape.squareHorizontalHollow(this.pos, 5));
    return outer;
  }

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  public float getCapacity() {
    return CAPACITY;
  }

  private boolean tryPlacePeat(BlockPos target) {
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack itemStack = inventory.getStackInSlot(i);
      BlockState state = Block.getBlockFromItem(itemStack.getItem()).getDefaultState();
      if (itemStack.getCount() == 0) {
        continue;
      }
      if (world.getBlockState(target).getBlock() instanceof PeatFuelBlock) {
        world.destroyBlock(target, true);
      }
      if ((world.isAirBlock(target)
          || world.getFluidState(target).getFluid() == Fluids.WATER
          || world.getFluidState(target).getFluid() == Fluids.FLOWING_WATER)
          && world.setBlockState(target, state)) {
        itemStack.shrink(1);
        return true;
      }
    }
    return false;
  }

  private boolean tryPlaceWater(BlockPos target) {
    if (world.getBlockState(target).isReplaceable(Fluids.WATER)
        && world.getBlockState(target).getBlock() != Blocks.WATER
        && tank.getFluidAmount() >= FluidAttributes.BUCKET_VOLUME
        && tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE) != null) {
      world.setBlockState(target, Blocks.WATER.getDefaultState());
      return true;
    }
    return false;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return tankWrapper.cast();
    }
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }
}
