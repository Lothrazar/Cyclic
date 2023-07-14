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

import java.util.List;
import java.util.function.Predicate;
import com.lothrazar.cyclic.block.PeatFuelBlock;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers.FluidAttributes;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePeatFarm extends TileBlockEntityCyclic implements MenuProvider {

  private static final int SIZE = 6;

  static enum Fields {
    REDSTONE, RENDER;
  }

  public static IntValue POWERCONF;
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  static final int MAX = 64000;
  public static final int TIMER_FULL = 1 * 10;
  private static final int PER_TICK = 1;
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(6) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return Block.byItem(stack.getItem()) == BlockRegistry.PEAT_UNBAKED.get();
    }
  };
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int blockPointer = 0;
  List<BlockPos> outer = null;

  @Override
  public Component getDisplayName() {
    return BlockRegistry.PEAT_FARM.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerPeatFarm(i, level, worldPosition, playerInventory, playerEntity);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TilePeatFarm e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TilePeatFarm e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
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
    if (outer == null) {
      outer = getShape();
      List<BlockPos> waterShape = ShapeUtil.squareHorizontalHollow(this.getBlockPos(), SIZE);
      outer.addAll(waterShape);
    }
    for (int i = 0; i < PER_TICK; i++) {
      if (blockPointer < outer.size()) {
        BlockPos target = outer.get(blockPointer);
        boolean placeWater = (target.getX() - worldPosition.getX()) % 3 == 0
            && (target.getZ() - worldPosition.getZ()) % 3 == 0;
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

  public TilePeatFarm(BlockPos pos, BlockState state) {
    super(TileRegistry.PEAT_FARM.get(), pos, state);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack stack) {
    return Block.byItem(stack.getItem()) == BlockRegistry.PEAT_UNBAKED.get();
  }

  List<BlockPos> getShape() {
    List<BlockPos> outer = ShapeUtil.squareHorizontalHollow(this.worldPosition, 7);
    outer.addAll(ShapeUtil.squareHorizontalHollow(this.worldPosition, 5));
    return outer;
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  public float getCapacity() {
    return CAPACITY;
  }

  private boolean tryPlacePeat(BlockPos target) {
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack itemStack = inventory.getStackInSlot(i);
      BlockState state = Block.byItem(itemStack.getItem()).defaultBlockState();
      if (itemStack.getCount() == 0) {
        continue;
      }
      if (level.getBlockState(target).getBlock() instanceof PeatFuelBlock) {
        level.destroyBlock(target, true);
      }
      if ((level.isEmptyBlock(target)
          || level.getFluidState(target).getType() == Fluids.WATER
          || level.getFluidState(target).getType() == Fluids.FLOWING_WATER)
          && level.setBlockAndUpdate(target, state)) {
        itemStack.shrink(1);
        return true;
      }
    }
    return false;
  }

  private boolean tryPlaceWater(BlockPos target) {
    if (level.getBlockState(target).canBeReplaced(Fluids.WATER)
        && level.getBlockState(target).getBlock() != Blocks.WATER
        && tank.getFluidAmount() >= FluidType.BUCKET_VOLUME
        && tank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE) != null) {
      level.setBlockAndUpdate(target, Blocks.WATER.defaultBlockState());
      return true;
    }
    return false;
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return fluidCap.cast();
    }
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }
}
