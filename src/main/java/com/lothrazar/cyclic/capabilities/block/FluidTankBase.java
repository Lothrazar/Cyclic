package com.lothrazar.cyclic.capabilities.block;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.packet.PacketSyncFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class FluidTankBase extends FluidTank {

  private TileBlockEntityCyclic tile;

  public FluidTankBase(TileBlockEntityCyclic tile, int capacity, Predicate<FluidStack> validator) {
    super(capacity, validator);
    this.tile = tile;
  }

  @Override
  public void onContentsChanged() {
    if (tile.getLevel() == null || tile.getLevel().isClientSide()) {
      return;
    }
    IFluidHandler handler = CapabilityUtil.fluid(tile.getLevel(), tile.getBlockPos());
    if (handler == null) {
      return;
    }
    FluidStack f = handler.getFluidInTank(0);
    PacketRegistry.sendToAllClients(tile.getLevel(), new PacketSyncFluid(tile.getBlockPos(), f));
  }
}
