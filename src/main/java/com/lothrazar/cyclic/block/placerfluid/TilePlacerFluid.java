package com.lothrazar.cyclic.block.placerfluid;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TilePlacerFluid extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static final int CAPACITY = 8 * FluidAttributes.BUCKET_VOLUME;
  FluidTankBase tank;
  private final LazyOptional<FluidTankBase> tankWrapper = LazyOptional.of(() -> tank);

  public static enum Fields {
    REDSTONE;
  }

  public TilePlacerFluid() {
    super(TileRegistry.placer_fluid);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
    this.needsRedstone = 1;
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerPlacerFluid(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return tankWrapper.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound("fluid"));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    FluidStack test = tank.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.SIMULATE);
    if (test.getAmount() == FluidAttributes.BUCKET_VOLUME
        && test.getFluid().getDefaultState() != null &&
        test.getFluid().getDefaultState().getBlockState() != null) {
      //we got enough
      Direction dir = this.getBlockState().get(BlockStateProperties.FACING);
      BlockPos offset = pos.offset(dir);
      BlockState state = test.getFluid().getDefaultState().getBlockState();
      if (world.isAirBlock(offset) &&
          world.setBlockState(offset, state)) {
        //pay
        tank.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.EXECUTE);
      }
    }
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.getNeedsRedstone();
    }
    return 0;
  }
}
