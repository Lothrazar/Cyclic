package com.lothrazar.cyclic.block.cable.fluid;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import com.lothrazar.cyclic.util.UtilFluid;
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
import net.minecraftforge.items.ItemStackHandler;

public class TileCableFluid extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  final ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.filter_data;
    }
  };
  public static final int CAPACITY = 16 * FluidAttributes.BUCKET_VOLUME;
  public static final int FLOW_RATE = CAPACITY; //normal non-extract flow
  public static final int EXTRACT_RATE = CAPACITY;
  private final Map<Direction, LazyOptional<FluidTankBase>> flow = Maps.newHashMap();

  public TileCableFluid() {
    super(TileRegistry.fluid_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(() -> new FluidTankBase(this, CAPACITY, p -> true)));
    }
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
    final FluidTankBase sideHandler = flow.get(extractSide).orElse(null);
    if (sideHandler == null) {
      return;
    }
    else if (sideHandler.getSpace() < FluidAttributes.BUCKET_VOLUME) {
      return;
    }
    //handle waterlogged blocks
    final BlockState targetState = world.getBlockState(target);
    if (targetState.hasProperty(BlockStateProperties.WATERLOGGED)
        && targetState.get(BlockStateProperties.WATERLOGGED)
        && world.setBlockState(target, targetState.with(BlockStateProperties.WATERLOGGED, false))) {
      //the waterlogged block contained a bucket amount of water
      final FluidStack bucketAmountOfWater = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);
      final int filledAmount = sideHandler.fill(bucketAmountOfWater, FluidAction.EXECUTE);
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
    final int filledAmount = sideHandler.fill(bucketAmountOfFluid, FluidAction.EXECUTE);
    //sanity check
    if (filledAmount != bucketAmountOfFluid.getAmount()) {
      ModCyclic.LOGGER.error("Incorrect amount of fluid extracted from fluid source, filled " + filledAmount + " expected " + bucketAmountOfFluid);
    }
  }

  private void normalFlow() {
    for (Direction incomingSide : Direction.values()) {
      final IFluidHandler sideHandler = flow.get(incomingSide).orElse(null);
      for (final Direction outgoingSide : UtilDirection.getDirectionsInDifferentOrder()) {
        if (outgoingSide == incomingSide) {
          continue;
        }
        final EnumProperty<EnumConnectType> outgoingFace = CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide);
        final EnumConnectType connection = this.getBlockState().get(outgoingFace);
        if (connection.isExtraction() || connection.isBlocked()) {
          continue;
        }
        this.moveFluids(outgoingSide, pos.offset(outgoingSide), FLOW_RATE, sideHandler);
      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (side != null && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      if (!CableBase.isCableBlocked(this.getBlockState(), side)) {
        return flow.get(side).cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    filter.deserializeNBT(tag.getCompound("filter"));
    FluidTankBase fluidh;
    for (Direction dir : Direction.values()) {
      fluidh = flow.get(dir).orElse(null);
      if (tag.contains("fluid" + dir.toString())) {
        fluidh.readFromNBT(tag.getCompound("fluid" + dir.toString()));
      }
    }
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put("filter", filter.serializeNBT());
    FluidTankBase fluidh;
    for (Direction dir : Direction.values()) {
      fluidh = flow.get(dir).orElse(null);
      CompoundNBT fluidtag = new CompoundNBT();
      if (fluidh != null) {
        fluidh.writeToNBT(fluidtag);
      }
      tag.put("fluid" + dir.toString(), fluidtag);
    }
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
