package com.lothrazar.cyclic.block.placerfluid;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TilePlacerFluid extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static final int CAPACITY = 8 * FluidAttributes.BUCKET_VOLUME;
  protected final FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> tank);

  static enum Fields {
    REDSTONE, RENDER;
  }

  public TilePlacerFluid() {
    super(TileRegistry.placer_fluid);
    this.needsRedstone = 1;
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);

    final int fluidAmount = tank.getFluidAmount();
    if (fluidAmount <= FluidAttributes.BUCKET_VOLUME) {
      return;
    }
    final FluidStack fluidStack = new FluidStack(tank.getFluid(), FluidAttributes.BUCKET_VOLUME);
    final Direction side = this.getBlockState().get(BlockStateProperties.FACING);
    FluidUtil.tryPlaceFluid(null, world, Hand.MAIN_HAND, pos.offset(side), tank, fluidStack);
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public FluidStack getFluid() {
    return tank.getFluid();
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerPlacerFluid(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      //      return tankWrapper.cast();
      return fluidHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    fluidHandlerLazyOptional.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    return super.write(tag);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }
}
