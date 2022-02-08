package com.lothrazar.cyclic.block.wireless.fluid;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.FluidTankBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWirelessFluid extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    RENDER, TRANSFER_RATE, REDSTONE;
  }

  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  static final int MAX = 64000;
  public static final int MAX_TRANSFER = MAX;
  private int transferRate = FluidAttributes.BUCKET_VOLUME;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, f -> true);
  LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);
  public ItemStackHandler gpsSlots = new ItemStackHandler(1) {

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }
  };

  public TileWirelessFluid(BlockPos pos, BlockState state) {
    super(TileRegistry.WIRELESS_FLUID.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessFluid e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileWirelessFluid e) {
    e.tick();
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerWirelessFluid(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return fluidCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    gpsSlots.deserializeNBT(tag.getCompound(NBTINV));
    this.transferRate = tag.getInt("transferRate");
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("transferRate", transferRate);
    tag.put(NBTINV, gpsSlots.serializeNBT());
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    super.saveAdditional(tag);
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  //  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (level.isClientSide) {
      return;
    }
    boolean moved = false;
    //run the transfer. one slot only
    BlockPosDim loc = getTargetInSlot(0);
    if (loc != null && UtilWorld.dimensionIsEqual(loc, level)) {
      this.moveFluids(loc.getSide(), loc.getPos(), this.transferRate, tank);
    }
    this.setLitProperty(moved);
  }

  BlockPosDim getTargetInSlot(int s) {
    return LocationGpsCard.getPosition(gpsSlots.getStackInSlot(s));
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
      case TRANSFER_RATE:
        transferRate = value;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case TRANSFER_RATE:
        return this.transferRate;
    }
    return 0;
  }

  public float getRed() {
    return 0.89F;
  }

  public float getBlue() {
    return 0;
  }

  public float getGreen() {
    return 0.12F;
  }

  public float getAlpha() {
    return 0.9F;
  }

  public float getThick() {
    return 0.065F;
  }
}
