package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.util.UtilFluid;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityBase extends TileEntity implements IInventory {

  public static final int MENERGY = 64 * 1000;
  protected int needsRedstone = 0;//default to always on
  protected int renderParticles = 1;
  protected int timer;

  public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public void setAnimation(boolean lit) {
    BlockState st = this.getBlockState();
    if (!st.hasProperty(BlockBase.LIT)) {
      return;
    }
    boolean previous = st.get(BlockBreaker.LIT);
    if (previous != lit) {
      this.world.setBlockState(pos, st.with(BlockBreaker.LIT, lit));
    }
  }

  public Direction getCurrentFacing() {
    if (this.getBlockState().hasProperty(BlockStateProperties.FACING))
      return this.getBlockState().get(BlockStateProperties.FACING);
    if (this.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING))
      return this.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
    return null;
  }

  @Override
  public CompoundNBT getUpdateTag() {
    //thanks http://www.minecraftforge.net/forum/index.php?topic=39162.0
    CompoundNBT syncData = new CompoundNBT();
    this.write(syncData);//this calls writeInternal
    return syncData;
  }

  protected BlockPos getCurrentFacingPos() {
    Direction f = this.getCurrentFacing();
    if (f != null)
      return this.pos.offset(f);
    return this.pos;
  }

  @Override
  public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
    this.read(this.getBlockState(), pkt.getNbtCompound());
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
    UtilFluid.tryFillPositionFromTank(world, posTarget, themFacingMe, tank, toFlow);
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
    IInventory inv = null;
    if (tileTarget instanceof IInventory) {
      //is there a validation way
      inv = (IInventory) tileTarget;
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
        for (int slot = 0; slot < handlerOutput.getSlots(); slot++) {
          if (inv != null
              && inv.isItemValidForSlot(slot, drain) == false) {
            //            System.out.println("not valid");
          }
          else {
            drain = handlerOutput.insertItem(slot, drain, false);
            if (drain.isEmpty()) {
              break;//done draining
            }
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
          // not so compatible with other fluid systems. itl do i guess
          TileCableEnergy cable = (TileCableEnergy) tileTarget;
          cable.updateIncomingEnergyFace(themFacingMe);
        }
      }
    }
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    needsRedstone = tag.getInt("needsRedstone");
    renderParticles = tag.getInt("renderParticles");
    timer = tag.getInt("timer");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("needsRedstone", needsRedstone);
    tag.putInt("renderParticles", renderParticles);
    tag.putInt("timer", timer);
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

  public void setFluid(FluidStack fluid) {}

  /************************** IInventory needed for IRecipe **********************************/
  @Deprecated
  @Override
  public int getSizeInventory() {
    return 0;
  }

  @Deprecated
  @Override
  public boolean isEmpty() {
    return true;
  }

  @Deprecated
  @Override
  public ItemStack getStackInSlot(int index) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public ItemStack decrStackSize(int index, int count) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public ItemStack removeStackFromSlot(int index) {
    return ItemStack.EMPTY;
  }

  @Deprecated
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {}

  @Deprecated
  @Override
  public boolean isUsableByPlayer(PlayerEntity player) {
    return false;
  }

  @Deprecated
  @Override
  public void clear() {}

  public void setFieldString(int field, String value) {
    //for string field system
  }

  public String getFieldString(int field) {
    //for string field system
    return null;
  }
}
