package com.lothrazar.cyclic.capabilities.block;

import java.util.function.Predicate;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.net.PacketFluidSync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidTankBase extends FluidTank {

  private TileBlockEntityCyclic tile;

  public FluidTankBase(TileBlockEntityCyclic tile, int capacity, Predicate<FluidStack> validator) {
    super(capacity, validator);
    this.tile = tile;
  }

  @Override
  public void onContentsChanged() {
    //send to client
    IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack f = handler.getFluidInTank(0);
    if (tile.getLevel().isClientSide == false) { //if serverside then 
      PacketRegistry.sendToAllClients(tile.getLevel(), new PacketFluidSync(tile.getBlockPos(), f));
    }
  }
}
