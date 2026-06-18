package com.lothrazar.cyclic.block.cable;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.block.cable.fluid.TileCableFluid;
import com.lothrazar.cyclic.block.cable.item.TileCableItem;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.core.ITileFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TileCableBase extends TileBlockEntityCyclic implements ITileFacade {

  protected final Map<Direction, Integer> mapIncomingEnergy = Maps.newHashMap();
  protected final Map<Direction, FluidTankBase> mapFluidFlow = new ConcurrentHashMap<>();
  protected final Map<Direction, IItemHandler> mapItemFlow = new ConcurrentHashMap<>();

  protected boolean isEnergyCable = false;
  protected boolean isItemCable = false;
  protected boolean isFluidCable = false;
  protected static final int FLOW_QTY = 64; // fixed, for non-extract motion

  protected EnergyStorageWrapper energy;

  public ItemStackHandler itemFilter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.FILTER_DATA.get();
    }
  };
  public final ItemStackHandler fluidFilter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.FILTER_FLUID.get();
    }
  };

  public TileCableBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
    super(tileEntityTypeIn, pos, state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    this.loadFacade(tag);
    if (this.isEnergyCable) {
      for (Direction f : Direction.values()) {
        mapIncomingEnergy.put(f, tag.getInt(f.getSerializedName() + "_incenergy"));
      }
      if (tag.contains(NBTENERGY)) {
        energy.deserializeNBT(registries, tag.get(NBTENERGY));
      }
    }
    if(this.isItemCable) {
      IItemHandler item;
      for (Direction f : Direction.values()) {
        item = mapItemFlow.get(f);
        if(item !=null){ //item.ifPresent(h -> {
          CompoundTag itemTag = tag.getCompound("item" + f.toString());
          ((ItemStackHandler)item).deserializeNBT(registries, itemTag);
        }
      }
      itemFilter.deserializeNBT(registries,tag.getCompound("itemFilter"));
    }
    if(this.isFluidCable) {

      fluidFilter.deserializeNBT(registries,tag.getCompound("filter"));
      FluidTankBase fluidh;
      for (Direction dir : Direction.values()) {
        fluidh = mapFluidFlow.get(dir);
        if (tag.contains("fluid" + dir.toString())) {
          fluidh.readFromNBT(registries,tag.getCompound("fluid" + dir.toString()));
        }
      }
    }
    super.loadAdditional(tag, registries);
  }
  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    this.saveFacade(tag);
    if (this.isEnergyCable) {
      for (Direction f : Direction.values()) {
        tag.putInt(f.getSerializedName() + "_incenergy", mapIncomingEnergy.get(f));
      }
      tag.put(NBTENERGY, energy.serializeNBT(registries));
    }
    if(this.isItemCable) {

      tag.put("itemFilter", itemFilter.serializeNBT(registries));
      IItemHandler item;
      for (Direction f : Direction.values()) {
        item = mapItemFlow.get(f);
        if (item != null) {
          CompoundTag compound = ((ItemStackHandler) item).serializeNBT(registries);
          tag.put("item" + f.toString(), compound);
        }
      }
    }
    if(this.isFluidCable) {

      tag.put("filter", fluidFilter.serializeNBT(registries));
      FluidTankBase fluidh;
      for (Direction dir : Direction.values()) {
        fluidh = mapFluidFlow.get(dir);
        CompoundTag fluidtag = new CompoundTag();
        if (fluidh != null) {
          fluidh.writeToNBT(registries, fluidtag);
        }
        tag.put("fluid" + dir.toString(), fluidtag);
      }
    }
    super.saveAdditional(tag, registries);
  }

  protected void setIsEnergy() {
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, 0);
    }
    energy = new EnergyStorageWrapper(TileCableEnergy.BUFFERSIZE.get(),
        TileCableEnergy.TRANSFER_RATE.get());
    isEnergyCable = true;
  }

  protected void setIsItem() {
    for (Direction f : Direction.values()) {
      mapItemFlow.put(f, new ItemStackHandler(1));
    }
    this.isItemCable = true;
  }
  protected void setIsFluid() {
    for (Direction f : Direction.values()) {
      mapFluidFlow.put(f, new FluidTankBase(this, TileCableFluid.BUFFERSIZE.get() * FluidHelpers.FluidAttributes.BUCKET_VOLUME, p -> true));
    }
    this.isFluidCable = true;
  }
  private CompoundTag facadeState = null;

  @Override
  public CompoundTag getFacade() {
    return facadeState;
  }

  @Override
  public void setFacade(CompoundTag facadeState) {
    this.facadeState = facadeState;
  }

  protected static final int TIMER_SIDE_INPUT = 15;

  protected boolean isEnergyIncomingFromFace(Direction face) {
    return mapIncomingEnergy.get(face) > 0;
  }

  public void updateIncomingEnergyFace(Direction inputFrom) {
    mapIncomingEnergy.put(inputFrom, TIMER_SIDE_INPUT);
  }


  protected void tryExtractEnergyFrom(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    final BlockPos posTarget = this.worldPosition.relative(extractSide);

    final IEnergyStorage itemHandlerFrom = CapabilityUtil.energy(level, posTarget, extractSide.getOpposite());

    if (itemHandlerFrom == null) {
      return;
    }
    final int capacity = energy.getMaxEnergyStored() - energy.getEnergyStored();
    if (capacity <= 0) {
      return;
    }
    //first we simulate
    final int energyToExtract = itemHandlerFrom.extractEnergy(TileCableEnergy.TRANSFER_RATE.get(), true);
    if (energyToExtract <= 0) {
      return;
    }
    final int energyReceived = energy.receiveEnergy(energyToExtract, false);
    if (energyReceived <= 0) {
      return;
    }
    final int energyExtracted = itemHandlerFrom.extractEnergy(energyReceived, false);
    //sanity check
    if (energyExtracted != energyReceived) {
      ModCyclic.LOGGER.error("Imbalance extracting energy, extracted " + energyExtracted + " received " + energyReceived);
    }
  }


  @Override
  public IItemHandler getItemHandler(Direction side) {
    if (this.isItemCable &&
        side != null &&
        !CableBase.isCableBlocked(this.getBlockState(), side)) {
      return mapItemFlow.get(side);
    }
    return null;
  }

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    if (this.isFluidCable &&
        side != null &&
        !CableBase.isCableBlocked(this.getBlockState(), side)) {
      return mapFluidFlow.get(side);
    }
    return null;
  }

  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    if (this.isEnergyCable &&
        side != null &&
        !CableBase.isCableBlocked(this.getBlockState(), side)) {
      return energy;
    }
    return null;
  }

}
