package com.lothrazar.cyclic.block.cable.fluid;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.item.datacard.fluid.FluidFilterCardItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.cyclic.util.FluidHelpers.FluidAttributes;
import com.lothrazar.library.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import com.lothrazar.cyclic.util.CapabilityUtil;

public class TileCableFluid extends TileCableBase implements MenuProvider {

  public static ModConfigSpec.IntValue BUFFERSIZE;
  public static ModConfigSpec.IntValue TRANSFER_RATE;

  public TileCableFluid(BlockPos pos, BlockState state) {
    super(TileRegistry.FLUID_PIPE.get(), pos, state);
    setIsFluid();
  }


  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableFluid e) {
    e.tickFluid();
  }

  public void tickFluid() {
    for (Direction extractSide : Direction.values()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(extractSide));
      if (connection.isExtraction()) {
        tryExtractFluidFrom(extractSide, fluidFilter);
      }
    }
    normalFlow();
  }

  private void tryExtractFluidFrom(Direction extractSide,ItemStackHandler filterIn) {
    if (extractSide == null) {
      return;
    }
    ItemStack filterSta = filterIn==null?ItemStack.EMPTY: filterIn.getStackInSlot(0);
    final BlockPos target = this.worldPosition.relative(extractSide); // .offset(
    final Direction incomingSide = extractSide.getOpposite();
    //when draining from a tank (instead of a source/waterlogged block) check the filter
    final IFluidHandler tankTarget = CapabilityUtil.fluid(level, target, incomingSide);
    if (tankTarget != null
        && tankTarget.getTanks() > 0
        && !FluidFilterCardItem.filterAllowsExtract(filterSta, tankTarget.getFluidInTank(0))) {
      return;
    }
    //first try standard fluid transfer
    if (FluidHelpers.tryFillPositionFromTank(level, worldPosition, extractSide, tankTarget, TRANSFER_RATE.get())) {
      return;
    }
    //handle special cases 
    //waterlogged
    //cauldron
    FluidTankBase sideHandler = mapFluidFlow.get(extractSide);
    if (sideHandler != null && sideHandler.getSpace() >= FluidType.BUCKET_VOLUME) {

      FluidHelpers.extractSourceWaterloggedCauldron(level, target, sideHandler, filterSta);
    }
  }

  private void normalFlow() {
    for (Direction incomingSide : Direction.values()) {
      final FluidTankBase sideHandler = mapFluidFlow.get(incomingSide);//.orElse(null);
      for (final Direction outgoingSide : DirectionUtil.getAllInDifferentOrder()) {
        if (outgoingSide == incomingSide) {
          continue;
        }
        EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
        if (connection.isExtraction() || connection.isBlocked()) {
          continue;
        }
        if (sideHandler.getFluidAmount() <= 0) {
          continue;
        }
        this.moveFluids(outgoingSide, worldPosition.relative(outgoingSide), TRANSFER_RATE.get(), sideHandler);
      }
    }
  }


  @Override
  public Component getDisplayName() {
    return BlockRegistry.FLUID_PIPE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCableFluid(i, level, worldPosition, playerInventory, playerEntity);
  }

}
