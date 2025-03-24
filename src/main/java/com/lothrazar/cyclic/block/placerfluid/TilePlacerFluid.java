package com.lothrazar.cyclic.block.placerfluid;

import java.util.function.Predicate;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers.FluidAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TilePlacerFluid extends TileBlockEntityCyclic implements MenuProvider {

  public static final int CAPACITY = 8 * FluidType.BUCKET_VOLUME;
  FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());;

  static enum Fields {
    REDSTONE, RENDER;
  }

  public TilePlacerFluid(BlockPos pos, BlockState state) {
    super(TileRegistry.PLACER_FLUID.get(), pos, state);
    this.needsRedstone = 1;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TilePlacerFluid e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TilePlacerFluid e) {
    e.tick();
  }

  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    FluidStack test = tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
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
        tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
      }
    }
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
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.PLACER_FLUID.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerPlacerFluid(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tank.readFromNBT(registries,tag.getCompound(NBTFLUID));
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries,fluid);
    tag.put(NBTFLUID, fluid);
    super.saveAdditional(tag,registries);
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
        this.render = value % PreviewOutlineType.values().length;
      break;
    }
  }
}
