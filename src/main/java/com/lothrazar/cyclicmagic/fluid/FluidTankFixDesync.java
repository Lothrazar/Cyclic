package com.lothrazar.cyclicmagic.fluid;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * All credit to Tinkers Construct where i learned this strategy source:
 * https://github.com/SlimeKnights/TinkersConstruct/blob/82d6fa81af5599dae14e2fd06a7eb3b9d6404c8e/src/main/java/slimeknights/tconstruct/smeltery/tileentity/TileTank.java#L66
 * 
 *
 */
public class FluidTankFixDesync extends FluidTankBase {
  private final TileEntityBaseMachineInvo parent;
  public FluidTankFixDesync(int capacity, TileEntityBaseMachineInvo parent) {
    super(capacity);
    this.parent = parent;
  }
  @Override
  public int fill(FluidStack resource, boolean doFill) {
    int amount = super.fill(resource, doFill);
    if (amount > 0 && doFill) {
      //    sendClientUpdate();
    }
    return amount;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    FluidStack fluid = super.drain(resource, doDrain);
    if (fluid != null && doDrain) {
      //   sendClientUpdate();
    }
    return fluid;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack fluid = super.drain(maxDrain, doDrain);
    if (fluid != null && doDrain) {
      // sendClientUpdate();
    }
    return fluid;
  }
  private void sendClientUpdate() {
    //   ModCyclic.logger.info(" sendClientUpdate() ");
    if (parent.getWorld().isRemote == false) {
      PacketFluidSync packet = new PacketFluidSync(parent.getPos(), this.getFluid());
      // Chunk chunk = world.getChunkFromBlockCoords(pos);
      for (EntityPlayer player : parent.getWorld().playerEntities) {
        // only send to relevant players
        if (player instanceof EntityPlayerMP) {
          EntityPlayerMP playerMP = (EntityPlayerMP) player;
          //ModCyclic.logger.info(" sendToPlayer() ");
          // if(world.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, chunk.x, chunk.z)) {
          ModCyclic.network.sendTo(packet, playerMP);
          // }
        }
      }
    }
    // ModCyclic.network.sendTo(message, player);
  }
  @Override
  protected void onContentsChanged() {
    // ModCyclic.logger.info(" onContentsChanged() ");
    sendClientUpdate();
    //    // updates the tile entity for the sake of things that detect when contents change (such as comparators)
    //    if(parent instanceof IFluidTankUpdater) {
    //      ((IFluidTankUpdater) parent).onTankContentsChanged();
    //    }
  }
}
