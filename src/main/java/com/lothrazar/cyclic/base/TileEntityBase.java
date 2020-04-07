package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.util.UtilFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityBase extends TileEntity {

  public static final int FUEL_WEAK = 256;
  public static final int MENERGY = 64 * 1000;
  private int needsRedstone = 1;
  private int renderParticles = 1;

  public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public CompoundNBT getUpdateTag() {
    //thanks http://www.minecraftforge.net/forum/index.php?topic=39162.0
    CompoundNBT syncData = new CompoundNBT();
    this.write(syncData);//this calls writeInternal
    return syncData;
  }

  @Override
  public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
    this.read(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.pos, 1, getUpdateTag());
  }

  public boolean isPowered() {
    return this.getWorld().isBlockPowered(this.getPos());
  }

  public boolean requiresRedstone() {
    return this.needsRedstone == 1;
  }

  public void moveFluids(Direction myFacingDir, int TRANSFER_FLUID_PER_TICK, IFluidHandler tank) {
    //    private void moveFluid(EnumFacing myFacingDir) {
    Direction themFacingMe = myFacingDir.getOpposite();
    if (tank == null || tank.getFluidInTank(0).getAmount() <= 0) {
      return;
    }
    int toFlow = TRANSFER_FLUID_PER_TICK;
    //    if (hasAnyIncomingFluidFaces() && toFlow >= tank.getFluidAmount()) {
    //      toFlow = tank.getFluidAmount();//NOPE// - 1;//keep at least 1 unit in the tank if flow is moving
    //    }
    BlockPos posTarget = pos.offset(myFacingDir);
    boolean outputSuccess = UtilFluid.tryFillPositionFromTank(world, posTarget, themFacingMe, tank, toFlow);
  }

  public void moveItems(Direction myFacingDir, int max, IItemHandler handlerHere) {
    if (this.world.isRemote()) {
      return;
    }
    if (handlerHere == null) {
      return;
    }
    Direction themFacingMe = myFacingDir.getOpposite();
    BlockPos posTarget = pos.offset(myFacingDir);
    TileEntity tileTarget = world.getTileEntity(posTarget);
    if (tileTarget == null) {
      return;
    }
    IItemHandler handlerOutput = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, themFacingMe).orElse(null);
    if (handlerOutput == null) {
      return;
    }
    if (handlerHere != null && handlerOutput != null) {
      int SLOT = 0;
      //first simulate 
      ItemStack drain = handlerHere.getStackInSlot(SLOT).copy();
      int sizeStarted = drain.getCount();
      if (!drain.isEmpty()) {
        //now push it into output, but find out what was ACTUALLY taken
        for (int i = 0; i < handlerOutput.getSlots(); i++) {
          drain = handlerOutput.insertItem(i, drain, false);
          if (drain.isEmpty()) {
            break;//done draining
          }
        }
      }
      int sizeAfter = sizeStarted - drain.getCount();
      if (sizeAfter > 0) {
        handlerHere.extractItem(SLOT, sizeAfter, false);
      }
    }
  }

  protected void moveEnergy(Direction myFacingDir, int quantity) {
    IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, myFacingDir).orElse(null);
    if (handlerHere == null || handlerHere.getEnergyStored() == 0) {
      return;
    }
    Direction themFacingMe = myFacingDir.getOpposite();
    BlockPos posTarget = pos.offset(myFacingDir);
    TileEntity tileTarget = world.getTileEntity(posTarget);
    if (tileTarget == null) {
      return;
    }
    IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, themFacingMe).orElse(null);
    if (handlerOutput == null) {
      return;
    }
    //    System.out.println("Bttery export " + myFacingDir);
    if (handlerHere != null && handlerOutput != null
        && handlerHere.canExtract() && handlerOutput.canReceive()) {
      //first simulate
      int drain = handlerHere.extractEnergy(quantity, true);
      if (drain > 0
      //          && handlerOutput.getEnergyStored() + drain <= handlerOutput.getMaxEnergyStored()
      ) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = handlerOutput.receiveEnergy(drain, false);
        //now actually drain that much from here
        handlerHere.extractEnergy(filled, false);
        if (filled > 0 && tileTarget instanceof TileCableEnergy) {
          //TODO: not so compatible with other fluid systems. itl do i guess
          TileCableEnergy cable = (TileCableEnergy) tileTarget;
          cable.updateIncomingEnergyFace(themFacingMe);
        }
      }
    }
  }

  @Override
  public void read(CompoundNBT tag) {
    needsRedstone = tag.getInt("needsRedstone");
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("needsRedstone", needsRedstone);
    return super.write(tag);
  }

  public abstract void setField(int field, int value);

  public int getField(int field) {
    return 0;
  }

  public int getNeedsRedstone() {
    return needsRedstone;
  }

  public void setNeedsRedstone(int needsRedstone) {
    this.needsRedstone = needsRedstone;
  }
}
