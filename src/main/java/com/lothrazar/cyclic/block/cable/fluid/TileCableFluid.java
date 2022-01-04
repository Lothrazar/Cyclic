package com.lothrazar.cyclic.block.cable.fluid;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import com.lothrazar.cyclic.util.UtilFluid;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
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
  private final FluidTank fluidTank = new FluidTankBase(this, CAPACITY, fluidStack ->
    FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), fluidStack));
  private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> fluidTank);
  private final Map<Direction, LazyOptional<IFluidHandler>> fluidCapSides = new HashMap<>();

  public TileCableFluid() {
    super(TileRegistry.fluid_pipeTile);
  }

  @Override
  public void updateConnection(final Direction side, final EnumConnectType connectType) {
    final EnumConnectType oldConnectType = connectTypeMap.computeIfAbsent(side, k -> getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(k)));
    if (connectType == EnumConnectType.BLOCKED && oldConnectType != EnumConnectType.BLOCKED) {
      fluidCapSides.computeIfPresent(side, (k, v) -> {
        v.invalidate();
        return null;
      });
    }
    else if (oldConnectType == EnumConnectType.BLOCKED && connectType != EnumConnectType.BLOCKED) {
      fluidCapSides.put(side, LazyOptional.of(() -> fluidTank));
    }
    super.updateConnection(side, connectType);
  }

  @Override
  public void tick() {
    for (final Direction extractSide : Direction.values()) {
      final EnumProperty<EnumConnectType> extractFace = CableBase.FACING_TO_PROPERTY_MAP.get(extractSide);
      final EnumConnectType connection = this.getBlockState().get(extractFace);
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
    final IFluidHandler tank = UtilFluid.getTank(world, target, incomingSide);
    if (tank != null
        && tank.getTanks() > 0
        && !FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), tank.getFluidInTank(0))) {
      return;
    }
    //first try standard fluid transfer
    if (UtilFluid.tryFillPositionFromTank(world, pos, extractSide, tank, EXTRACT_RATE)) {
      return;
    }
    //handle special cases
    if (fluidTank.getSpace() < FluidAttributes.BUCKET_VOLUME) {
      return;
    }
    //handle waterlogged blocks
    final BlockState targetState = world.getBlockState(target);
    if (targetState.hasProperty(BlockStateProperties.WATERLOGGED)
        && targetState.get(BlockStateProperties.WATERLOGGED)
        && world.setBlockState(target, targetState.with(BlockStateProperties.WATERLOGGED, false))) {
      //the waterlogged block contained a bucket amount of water
      final FluidStack bucketAmountOfWater = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);
      final int filledAmount = fluidTank.fill(bucketAmountOfWater, FluidAction.EXECUTE);
      //sanity check
      if (filledAmount != bucketAmountOfWater.getAmount()) {
        ModCyclic.LOGGER.error("Imbalance filling water extracted from waterlogged block, filled " + filledAmount + " expected " + bucketAmountOfWater);
      }
      return;
    }
    //handle source blocks
    final FluidState fluidState = world.getFluidState(target);
    final Fluid fluid = fluidState.getFluid();
    if (!fluid.isSource(fluidState)) {
      return;
    }
    //remove the fluid source block
    if (!world.setBlockState(target, Blocks.AIR.getDefaultState())) {
      return;
    }
    //the fluid source block contained a bucket amount of that fluid
    final FluidStack bucketAmountOfFluid = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);
    final int filledAmount = fluidTank.fill(bucketAmountOfFluid, FluidAction.EXECUTE);
    //sanity check
    if (filledAmount != bucketAmountOfFluid.getAmount()) {
      ModCyclic.LOGGER.error("Incorrect amount of fluid extracted from fluid source, filled " + filledAmount + " expected " + bucketAmountOfFluid);
    }
  }

  private void normalFlow() {
    for (final Direction outgoingSide : UtilDirection.getAllInDifferentOrder()) {
      final EnumProperty<EnumConnectType> outgoingFace = CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide);
      final EnumConnectType connection = this.getBlockState().get(outgoingFace);
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
      final LazyOptional<IFluidHandler> fluidCapSide = fluidCapSides.computeIfAbsent(side, k -> {
        if (getConnectionType(k) != EnumConnectType.BLOCKED) {
          final LazyOptional<IFluidHandler> v = LazyOptional.of(() -> fluidTank);
          fluidCapSides.put(k, v);
          return v;
        }
        return null;
      });
      if (fluidCapSide != null) {
        return fluidCapSide.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  public void invalidateCaps() {
    fluidCap.invalidate();
    for (final LazyOptional<IFluidHandler> sidedCap : fluidCapSides.values()) {
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
