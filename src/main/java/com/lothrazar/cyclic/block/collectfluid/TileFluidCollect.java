package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.item.datacard.fluid.FluidFilterCardItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers.FluidAttributes;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;


public class TileFluidCollect extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT;
  }

  static final int MAX_HEIGHT = 64;
  public static final int MAX_SIZE = 12; //radius 7 translates to 15x15 area (center block + 7 each side)
  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static ModConfigSpec.IntValue POWERCONF;
  FluidTankBase tank;
  // //  private final LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);
  private int shapeIndex = 0; // current index of shape array
  private int radius = 4 * 2;
  private int height = 4;
  BlockPos targetPos = null;
  static final int MAX = 64000;
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return Block.byItem(stack.getItem()) != Blocks.AIR;
    }
  };
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (stack.isEmpty()) {
        return true;
      }
      return stack.getItem() == ItemRegistry.FILTER_FLUID.get();
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);

  public TileFluidCollect(BlockPos pos, BlockState state) {
    super(TileRegistry.COLLECTOR_FLUID.get(), pos, state);
    tank = new FluidTankBase(this, CAPACITY, p -> true);
  }

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileFluidCollect e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      this.setLitProperty(false);
      return;
    }
    Integer cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      return;
    }
    ItemStack stack = inventory.getStackInSlot(0);
    //use air if its empty
    BlockState newState = Block.byItem(stack.getItem()).defaultBlockState();
    this.setLitProperty(true);
    List<BlockPos> shape = this.getShapeFilled();
    if (shape.size() == 0) {
      return;
    }
    //iterate around shape
    incrementShapePtr(shape);
    targetPos = shape.get(shapeIndex);
    //ok on this target get fluid check it out
    FluidState fluidState = level.getFluidState(targetPos);
    if (fluidState.isSource()) {
      FluidStack fstack = new FluidStack(fluidState.getType(), FluidAttributes.BUCKET_VOLUME);
      //if a filter card is set, gate pickup through it
      ItemStack filterSta = filter.getStackInSlot(0);
      if (!filterSta.isEmpty() && !FluidFilterCardItem.filterAllowsExtract(filterSta, fstack)) {
        return;
      }
      int result = tank.fill(fstack, IFluidHandler.FluidAction.SIMULATE);
      if (result == FluidAttributes.BUCKET_VOLUME) {
        //we got enough   
        if (level.setBlockAndUpdate(targetPos, newState)) {
          //build the block, shrink the item
          stack.shrink(1);
          //drink fluid
          tank.fill(fstack, IFluidHandler.FluidAction.EXECUTE);
          energy.extractEnergy(cost, false);
        }
      }
    }
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  private int heightWithDirection() {
    Direction blockFacing = this.getBlockState().getValue(BlockStateProperties.FACING);
    int diff = 1; // directionIsUp ? 1 : -1;
    if (blockFacing.getAxis().isVertical()) {
      diff = (blockFacing == Direction.UP) ? 1 : -1;
    }
    return diff * height;
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    BlockPos center = getFacingShapeCenter(radius);
    List<BlockPos> shape = ShapeUtil.squareHorizontalHollow(center, this.radius);
    //
    int heightWithDirection = heightWithDirection();
    if (heightWithDirection != 0) {
      shape = ShapeUtil.repeatShapeByHeight(shape, heightWithDirection);
    }
    if (targetPos != null) {
      shape.add(targetPos);
    }
    return shape;
  }

  //for harvest
  public List<BlockPos> getShapeFilled() {
    BlockPos center = getFacingShapeCenter(radius);
    int heightWithDirection = heightWithDirection();
    List<BlockPos> shape = ShapeUtil.squareHorizontalFull(center, this.radius);
    if (heightWithDirection != 0) {
      shape = ShapeUtil.repeatShapeByHeight(shape, heightWithDirection);
    }
    return shape;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.COLLECTOR_FLUID.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerFluidCollect(i, level, worldPosition, playerInventory, playerEntity);
  }


  @Override
  public void loadAdditional(ValueInput input) {
    // For backwards-compatibility: these weren't always stored, so keep the default
    radius = input.getIntOr("size", 0);
    height = input.getIntOr("height", 0);
    shapeIndex = input.getIntOr("shapeIndex", 0);
    tank.deserialize(input.childOrEmpty(NBTFLUID));
    energy.deserialize(input.childOrEmpty(NBTENERGY));
    inventory.deserialize(input.childOrEmpty(NBTINV));
    filter.deserialize(input.childOrEmpty("filter"));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    output.putInt("size", radius);
    output.putInt("height", height);
    energy.serialize(output.child(NBTENERGY));
    inventory.serialize(output.child(NBTINV));
    filter.serialize(output.child("filter"));
    tank.serialize(output.child(NBTFLUID));
    output.putInt("shapeIndex", shapeIndex);
    super.saveAdditional(output);
  }

  private void incrementShapePtr(List<BlockPos> shape) {
    shapeIndex++;
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
        break;
      case RENDER:
        this.render = value % PreviewOutlineType.values().length;
        break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
        break;
      case SIZE:
        radius = Math.min(value, MAX_SIZE);
        break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return this.render;
      case HEIGHT:
        return height;
      case SIZE:
        return radius;
    }
    return 0;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
