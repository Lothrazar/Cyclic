package com.lothrazar.cyclic.base;

import java.util.function.Predicate;
import com.lothrazar.cyclic.net.PacketFluidSync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkDirection;

public class FluidTankBase extends FluidTank {

  private TileEntityBase tile;

  public FluidTankBase(TileEntityBase tile, int capacity, Predicate<FluidStack> validator) {
    super(capacity, validator);
    this.tile = tile;
  }

  @Override
  protected void onContentsChanged() {
    //    tile.markDirty();//doesnt work
    //send to client
    IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack f = handler.getFluidInTank(0);
    if (tile.getWorld().isRemote == false)//if serverside then 
      for (PlayerEntity player : tile.getWorld().getPlayers()) {
        ServerPlayerEntity sp = ((ServerPlayerEntity) player);
        PacketRegistry.INSTANCE.sendTo(new PacketFluidSync(tile.getPos(), f), sp.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
      }
  }
}
