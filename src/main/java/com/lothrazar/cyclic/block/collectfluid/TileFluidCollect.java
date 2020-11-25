package com.lothrazar.cyclic.block.collectfluid;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileFluidCollect extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  public static IntValue POWERCONF;
  static final int MAX_HEIGHT = 64;
  public static final int MAX_SIZE = 12;//radius 7 translates to 15x15 area (center block + 7 each side)
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> tankWrapper = LazyOptional.of(() -> tank);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private int shapeIndex = 0;// current index of shape array
  private int size = 4 * 2;
  private int height = 4;
  BlockPos targetPos = null;
  static final int MAX = 64000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT;
  }

  public TileFluidCollect() {
    super(TileRegistry.collector_fluid);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      this.setLitProperty(false);
      return;
    }
    IEnergyStorage en = this.energy.orElse(null);
    if (en == null) {
      return;
    }
    Integer cost = POWERCONF.get();
    if (en.getEnergyStored() < cost && cost > 0) {
      return;//broke
    }
    inventory.ifPresent(inv -> {
      ItemStack stack = inv.getStackInSlot(0);
      if (stack.isEmpty() || Block.getBlockFromItem(stack.getItem()) == Blocks.AIR) {
        return;
      }
      this.setLitProperty(true);
      List<BlockPos> shape = this.getShapeFilled();
      if (shape.size() == 0) {
        return;
      }
      //iterate around shape
      incrementShapePtr(shape);
      targetPos = shape.get(shapeIndex);
      //ok on this target get fluid check it out
      FluidState fluidState = world.getFluidState(targetPos);
      //      for (int ff = 0; ff < 20; ff++) {
      //        if (!fluidState.isSource()) {
      //          break;
      //        } //fast forward ten spots at a time in case big chunkso f nothin
      //        incrementShapePtr(shape);
      //        targetPos = shape.get(shapeIndex);
      //        fluidState = world.getFluidState(targetPos);
      //      }
      if (fluidState.isSource()) {
        //        ModCyclic.LOGGER.info("source " + targetPos);
        FluidStack fstack = new FluidStack(fluidState.getFluid(), FluidAttributes.BUCKET_VOLUME);
        int result = tank.fill(fstack, FluidAction.SIMULATE);
        if (result == FluidAttributes.BUCKET_VOLUME) {
          //we got enough  
          if (world.setBlockState(targetPos, Block.getBlockFromItem(stack.getItem()).getDefaultState())) {
            //build the block, shrink the item
            stack.shrink(1);
            //drink fluid
            tank.fill(fstack, FluidAction.EXECUTE);
            en.extractEnergy(cost, false);
          }
        }
      }
    });
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem()) != Blocks.AIR;
      }
    };
  }

  private BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getCurrentFacingPos(size + 1);//this.getPos().offset(this.getCurrentFacing(), size + 1);
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    BlockPos ctr = getTargetCenter();
    List<BlockPos> shape = UtilShape.squareHorizontalHollow(ctr.down(height), this.size);
    shape = UtilShape.repeatShapeByHeight(shape, height);
    if (targetPos != null) {
      shape.add(targetPos);
    }
    return shape;
  }

  //for harvest
  public List<BlockPos> getShapeFilled() {
    BlockPos ctr = getTargetCenter();
    List<BlockPos> shape = UtilShape.squareHorizontalFull(ctr.down(height), this.size);
    shape = UtilShape.repeatShapeByHeight(shape, height - 1);
    return shape;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerFluidCollect(i, world, pos, playerInventory, playerEntity);
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

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    shapeIndex = tag.getInt("shapeIndex");
    tank.readFromNBT(tag.getCompound("fluid"));
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    tag.putInt("shapeIndex", shapeIndex);
    return super.write(tag);
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
        this.render = value % 2;
      break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
      break;
      case SIZE:
        size = Math.min(value, MAX_SIZE);
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
        return size;
    }
    return 0;
  }
}
