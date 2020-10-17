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
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.BlockPeatFuel;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
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
import net.minecraftforge.common.util.INBTSerializable;
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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TilePeatFarm extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  public static IntValue POWERCONF;
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  static final int MAX = 64000;
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> tankWrapper = LazyOptional.of(() -> tank);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  public static final int TIMER_FULL = 1 * 10;
  private static final int PER_TICK = 1;
  private int blockPointer = 0;

  static enum Fields {
    REDSTONE, RENDER;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerPeatFarm(i, world, pos, playerInventory, playerEntity);
  }

  private void init() {
    if (baked == null)
      baked = BlockRegistry.peat_baked;
    if (unbaked == null)
      unbaked = BlockRegistry.peat_unbaked;
    if (outer == null) {
      outer = getShape();
      List<BlockPos> waterShape = UtilShape.squareHorizontalHollow(this.pos, 6);
      outer.addAll(waterShape);
    }
  }

  @Override
  public void tick() {
    this.init();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      blockPointer = 0;
      return;
    }
    setLitProperty(true);
    IEnergyStorage cap = this.energy.orElse(null);
    if (cap == null) {
      return;
    }
    if (this.timer > 0) {
      this.timer--;
      return;
    }
    final int cost = POWERCONF.get();
    if (cap.getEnergyStored() < cost && cost > 0) {
      return;//broke
    }
    for (int i = 0; i < PER_TICK; i++) {
      if (blockPointer < outer.size()) {
        BlockPos target = outer.get(blockPointer);
        boolean placeWater = (target.getX() - pos.getX()) % 3 == 0
            && (target.getZ() - pos.getZ()) % 3 == 0;
        if (placeWater) {
          if (tryPlaceWater(target))
            cap.extractEnergy(cost, false);
        }
        else if (tryPlacePeat(target))
          cap.extractEnergy(cost, false);
        blockPointer++;
      }
      else
        blockPointer = 0;
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

  private List<BlockPos> getShape() {
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
    IItemHandler inventory = this.inventory.orElse(null);
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack itemStack = inventory.getStackInSlot(i);
      BlockState state = Block.getBlockFromItem(itemStack.getItem()).getDefaultState();
      if (itemStack.getCount() == 0)
        continue;
      if (world.getBlockState(target).getBlock() instanceof BlockPeatFuel) {
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
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return tankWrapper.cast();
    }
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(6) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem()) == BlockRegistry.peat_unbaked;
      }
    };
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound("fluid"));
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
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
}
