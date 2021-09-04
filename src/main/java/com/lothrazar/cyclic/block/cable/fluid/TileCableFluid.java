package com.lothrazar.cyclic.block.cable.fluid;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilFluid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
  public static final int TRANSFER_FLUID_PER_TICK = FluidAttributes.BUCKET_VOLUME / 2;
  private Map<Direction, LazyOptional<FluidTankBase>> flow = Maps.newHashMap();

  public TileCableFluid() {
    super(TileRegistry.fluid_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(() -> new FluidTankBase(this, CAPACITY, p -> true)));
    }
  }

  List<Integer> rawList = IntStream.rangeClosed(0, 5).boxed().collect(Collectors.toList());

  @Override
  public void tick() {
    for (Direction side : Direction.values()) {
      EnumConnectType connection = this.getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(side));
      if (connection.isExtraction()) {
        tryExtract(side);
      }
    }
    normalFlow();
  }

  private void tryExtract(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    BlockPos target = this.pos.offset(extractSide);
    Direction incomingSide = extractSide.getOpposite();
    IFluidHandler stuff = UtilFluid.getTank(world, target, incomingSide);
    if (stuff != null
        && stuff.getTanks() > 0
        && !FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), stuff.getFluidInTank(0))) {
      return;
    }
    boolean success = UtilFluid.tryFillPositionFromTank(world, pos, extractSide, stuff, EXTRACT_RATE);
    FluidTankBase sideHandler = flow.get(extractSide).orElse(null);
    if (!success && sideHandler != null
        && sideHandler.getSpace() >= FluidAttributes.BUCKET_VOLUME) {
      //test if its a source block, or a waterlogged block
      BlockState targetState = world.getBlockState(target);
      FluidState fluidState = world.getFluidState(target);
      if (fluidState != null && fluidState.isSource()) {
        //not just water. any fluid source block
        if (world.setBlockState(target, Blocks.AIR.getDefaultState())) {
          sideHandler.fill(new FluidStack(fluidState.getFluid(), FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
        }
      }
      else if (targetState.hasProperty(BlockStateProperties.WATERLOGGED) && targetState.get(BlockStateProperties.WATERLOGGED) == true) {
        //
        targetState = targetState.with(BlockStateProperties.WATERLOGGED, false);
        //for waterlogged it is hardcoded to water
        if (world.setBlockState(target, targetState)) {
          sideHandler.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
        }
      }
    }
  }

  private void normalFlow() {
    IFluidHandler sideHandler;
    Direction outgoingSide;
    for (Direction incomingSide : Direction.values()) {
      sideHandler = flow.get(incomingSide).orElse(null);
      //thise items came from that
      Collections.shuffle(rawList);
      for (Integer i : rawList) {
        outgoingSide = Direction.values()[i];
        if (outgoingSide == incomingSide) {
          continue;
        }
        EnumConnectType connection = this.getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
        if (connection.isExtraction() || connection.isBlocked()) {
          continue;
        }
        this.moveFluids(outgoingSide, FLOW_RATE, sideHandler);
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
