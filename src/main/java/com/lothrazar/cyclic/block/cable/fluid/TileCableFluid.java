package com.lothrazar.cyclic.block.cable.fluid;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.cyclic.util.FluidHelpers.FluidAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableFluid extends TileBlockEntityCyclic implements MenuProvider {

  public static IntValue BUFFERSIZE;
  public static IntValue TRANSFER_RATE;
  final ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.FILTER_DATA.get();
    }
  };
  private Map<Direction, LazyOptional<FluidTankBase>> flow = Maps.newHashMap();

  public TileCableFluid(BlockPos pos, BlockState state) {
    super(TileRegistry.FLUID_PIPE.get(), pos, state);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(() -> new FluidTankBase(this, BUFFERSIZE.get() * FluidAttributes.BUCKET_VOLUME, p -> true)));
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableFluid e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileCableFluid e) {
    e.tick();
  }

  List<Integer> rawList = IntStream.rangeClosed(0, 5).boxed().collect(Collectors.toList());

  //  @Override
  public void tick() {
    for (Direction side : Direction.values()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(side));
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
    BlockPos target = this.worldPosition.relative(extractSide);
    Direction incomingSide = extractSide.getOpposite();
    IFluidHandler stuff = FluidHelpers.getTank(level, target, incomingSide);
    //check filter
    if (stuff != null
        && stuff.getTanks() > 0
        && !FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), stuff.getFluidInTank(0))) {
      return;
    }
    //is it a tank? try pulling from a tank
    boolean success = FluidHelpers.tryFillPositionFromTank(level, worldPosition, extractSide, stuff, TRANSFER_RATE.get());
    if (success) {
      return;
    }
    //not a tank. try the world
    FluidTankBase tank = flow.get(extractSide).orElse(null);
    if (tank != null && tank.getSpace() >= FluidType.BUCKET_VOLUME) {
      FluidHelpers.extractSourceWaterloggedCauldron(level, target, tank);
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
        EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
        if (connection.isExtraction() || connection.isBlocked()) {
          continue;
        }
        this.moveFluids(outgoingSide, worldPosition.relative(outgoingSide), TRANSFER_RATE.get(), sideHandler);
      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (side != null && cap == ForgeCapabilities.FLUID_HANDLER) {
      if (!CableBase.isCableBlocked(this.getBlockState(), side)) {
        return flow.get(side).cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    filter.deserializeNBT(tag.getCompound("filter"));
    FluidTankBase fluidh;
    for (Direction dir : Direction.values()) {
      fluidh = flow.get(dir).orElse(null);
      if (tag.contains("fluid" + dir.toString())) {
        fluidh.readFromNBT(tag.getCompound("fluid" + dir.toString()));
      }
    }
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put("filter", filter.serializeNBT());
    FluidTankBase fluidh;
    for (Direction dir : Direction.values()) {
      fluidh = flow.get(dir).orElse(null);
      CompoundTag fluidtag = new CompoundTag();
      if (fluidh != null) {
        fluidh.writeToNBT(fluidtag);
      }
      tag.put("fluid" + dir.toString(), fluidtag);
    }
    super.saveAdditional(tag);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.FLUID_PIPE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCableFluid(i, level, worldPosition, playerInventory, playerEntity);
  }
}
