package com.lothrazar.cyclic.capabilities.block;

import java.util.function.Predicate;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.fixers.CapabilityFixer;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.library.packet.PacketSyncFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class FluidTankBase extends FluidTank {

  private TileBlockEntityCyclic tile;

  public FluidTankBase(TileBlockEntityCyclic tile, int capacity, Predicate<FluidStack> validator) {
    super(capacity, validator);
    this.tile = tile;
  }

  @Override
  public void onContentsChanged() {
    //send to client
    IFluidHandler handler = CapabilityFixer.fluid(tile.getLevel(), tile.getBlockPos()); // tile.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack f = handler.getFluidInTank(0);
    if (tile.getLevel().isClientSide == false) { //if serverside then 
      PacketRegistry.sendToAllClients(tile.getLevel(), new PacketSyncFluid(tile.getBlockPos(), f));
    }
  }
}
