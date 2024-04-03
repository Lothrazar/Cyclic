package com.lothrazar.cyclic.block.cable.fluid;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import com.lothrazar.cyclic.util.UtilFluid;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableFluid extends TileCableBase implements ITickableTileEntity, INamedContainerProvider {

  final ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.filter_data;
    }
  };
  public static final int CAPACITY = 16 * FluidAttributes.BUCKET_VOLUME;
  public static final int FLOW_RATE = CAPACITY; //normal non-extract flow
  public static final int EXTRACT_RATE = CAPACITY;
  private final FluidTank fluidTank = new FluidTankBase(this, CAPACITY, fluidStack -> FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), fluidStack));
  private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> fluidTank);
  private final ConcurrentHashMap<Direction, LazyOptional<IFluidHandler>> flow = new ConcurrentHashMap<>();

  public TileCableFluid() {
    super(TileRegistry.fluid_pipeTile);
  }

  @Override
  public void updateConnection(final Direction side, final EnumConnectType connectType) {
    EnumProperty<EnumConnectType> property = CableBase.FACING_TO_PROPERTY_MAP.get(side);
    final EnumConnectType oldConnectType = getBlockState().get(property);
    if (connectType == EnumConnectType.BLOCKED && oldConnectType != EnumConnectType.BLOCKED) {
      flow.computeIfPresent(side, (k, v) -> {
        v.invalidate();
        return null;
      });
    }
    else if (oldConnectType == EnumConnectType.BLOCKED && connectType != EnumConnectType.BLOCKED) {
      flow.put(side, LazyOptional.of(() -> fluidTank));
    }
    super.updateConnection(side, connectType);
  }

  @Override
  public void tick() {
    for (final Direction extractSide : Direction.values()) {
      final EnumConnectType connection = getConnectionType(extractSide);
      if (connection.isExtraction()) {
        tryExtract(extractSide);
      }
    }
    normalFlow();
  }

  private void tryExtract(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    final BlockPos target = this.pos.offset(extractSide);
    final Direction incomingSide = extractSide.getOpposite();
    //when draining from a tank (instead of a source/waterlogged block) check the filter
    final IFluidHandler tankTarget = UtilFluid.getTank(world, target, incomingSide);
    if (tankTarget != null
        && tankTarget.getTanks() > 0
        && !FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), tankTarget.getFluidInTank(0))) {
      return;
    }
    //first try standard fluid transfer
    if (UtilFluid.tryFillPositionFromTank(world, pos, extractSide, tankTarget, EXTRACT_RATE)) {
      return;
    }
    //handle special cases
    if (!flow.containsKey(extractSide)) {
      final LazyOptional<IFluidHandler> hax = LazyOptional.of(() -> fluidTank);
      flow.put(extractSide, hax);
    }
    IFluidHandler tank = flow.get(extractSide).orElse(null);
    if (fluidTank.getSpace() > -FluidAttributes.BUCKET_VOLUME) {
      UtilFluid.extractSourceWaterloggedCauldron(world, target, tank);
    }
  }

  private void normalFlow() {
    for (final Direction outgoingSide : UtilDirection.getAllInDifferentOrder()) {
      final EnumConnectType connection = getConnectionType(outgoingSide);
      if (connection.isExtraction() || connection.isBlocked()) {
        continue;
      }
      this.moveFluids(outgoingSide, pos.offset(outgoingSide), FLOW_RATE, fluidTank);
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      if (side == null) {
        return fluidCap.cast();
      }
      LazyOptional<IFluidHandler> sidedCap = flow.get(side);
      if (sidedCap == null) {
        if (getConnectionType(side) != EnumConnectType.BLOCKED) {
          sidedCap = LazyOptional.of(() -> fluidTank);
          flow.put(side, sidedCap);
          return sidedCap.cast();
        }
      }
      else {
        return sidedCap.cast();
      }
      //works but
      //      if (fluidCapSides.containsKey(side)) {
      //        LazyOptional<IFluidHandler> sidedCap = fluidCapSides.get(side);
      //        return sidedCap.cast();
      //      }
      //      final LazyOptional<IFluidHandler> fluidCapSide = fluidCapSides.computeIfAbsent(side, k -> {
      //        if (getConnectionType(k) != EnumConnectType.BLOCKED) {
      //          //          final LazyOptional<IFluidHandler> v = LazyOptional.of(() -> fluidTank);
      //          return LazyOptional.of(() -> fluidTank);
      //        }
      //        return LazyOptional.empty();
      //      });
      //      if (fluidCapSide != null) {
      //        return fluidCapSide.cast();
      //      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    fluidCap.invalidate();
    for (final LazyOptional<IFluidHandler> sidedCap : flow.values()) {
      sidedCap.invalidate();
    }
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    filter.deserializeNBT(tag.getCompound("filter"));
    fluidTank.readFromNBT(tag.getCompound(NBTFLUID));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put("filter", filter.serializeNBT());
    tag.put(NBTFLUID, fluidTank.writeToNBT(new CompoundNBT()));
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerCableFluid(i, world, pos, playerInventory, playerEntity);
  }
}
