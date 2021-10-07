package com.lothrazar.cyclic.block.placerfluid;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TilePlacerFluid extends TileEntityBase implements MenuProvider, TickableBlockEntity {

  public static final int CAPACITY = 8 * FluidAttributes.BUCKET_VOLUME;
  FluidTankBase tank;

  static enum Fields {
    REDSTONE, RENDER;
  }

  public TilePlacerFluid() {
    super(TileRegistry.placer_fluid);
    tank = new FluidTankBase(this, CAPACITY, isFluidValid());
    this.needsRedstone = 1;
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    FluidStack test = tank.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.SIMULATE);
    if (test.getAmount() == FluidAttributes.BUCKET_VOLUME
        && test.getFluid().defaultFluidState() != null &&
        test.getFluid().defaultFluidState().createLegacyBlock() != null) {
      //we got enough
      Direction dir = this.getBlockState().getValue(BlockStateProperties.FACING);
      BlockPos offset = worldPosition.relative(dir);
      BlockState state = test.getFluid().defaultFluidState().createLegacyBlock();
      if (level.isEmptyBlock(offset) &&
          level.setBlockAndUpdate(offset, state)) {
        //pay
        tank.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.EXECUTE);
      }
    }
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerPlacerFluid(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      //      return tankWrapper.cast();
      return LazyOptional.of(() -> tank).cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    super.load(bs, tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    return super.save(tag);
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
