package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.item.datacard.fluid.FluidFilterCardItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.cyclic.util.FluidHelpers.FluidAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileFluidHopper extends TileBlockEntityCyclic implements MenuProvider {

  private static final int FLOW = FluidType.BUCKET_VOLUME;
  public static final int CAPACITY = FluidType.BUCKET_VOLUME;
  private static final String NBT_FILTER = "filter";
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> true);

  final ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (stack.isEmpty()) {
        return true;
      }
      return stack.getItem() == ItemRegistry.FILTER_FLUID.get();
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  public TileFluidHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.HOPPER_FLUID.get(), pos, state);
  }

  public ItemStackHandler getFilterSlot() {
    return filter;
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileFluidHopper e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileFluidHopper e) {
    e.tick();
  }

  public void tick() {
    if (this.isPowered()) {
      return;
    }
    if (level.isClientSide) {
      return;
    }
    //first pull down from above
    tryExtract();
    //then pull from hopper facey side
    Direction exportToSide = this.getBlockState().getValue(BlockFluidHopper.FACING);
    if (exportToSide != null && exportToSide != Direction.UP) {
      //if the target is a tank
      moveFluids(exportToSide, worldPosition.relative(exportToSide), FLOW, tank);
      //if the target is a cauldron
      FluidHelpers.insertSourceCauldron(level, worldPosition.relative(exportToSide), tank);
      this.updateComparatorOutputLevel();
      this.updateComparatorOutputLevelAt(worldPosition.relative(exportToSide));
    }
  }

  private void tryExtract() {
    if (tank == null) {
      return;
    }
    ItemStack filterSta = filter.getStackInSlot(0);
    BlockPos target = this.worldPosition.relative(Direction.UP);
    IFluidHandler tankAbove = CapabilityUtil.fluid(level, target, Direction.DOWN);
    //gate tank-to-tank extract through the filter too
    if (tankAbove != null
        && tankAbove.getTanks() > 0
        && !filterSta.isEmpty()
        && !FluidFilterCardItem.filterAllowsExtract(filterSta, tankAbove.getFluidInTank(0))) {
      //filter rejects this fluid; don't pull from the tank but still try world below
    }
    else {
      boolean success = FluidHelpers.tryFillPositionFromTank(level, worldPosition, Direction.UP, tankAbove, FLOW);
      if (success) {
        this.updateComparatorOutputLevelAt(target);
        this.updateComparatorOutputLevel();
        return;
      }
    }
    //try from the world (extractSourceWaterloggedCauldron handles the filter internally)
    if (tank.getSpace() >= FluidAttributes.BUCKET_VOLUME) {
      FluidHelpers.extractSourceWaterloggedCauldron(level, target, tank, filterSta);
      this.updateComparatorOutputLevel();
    }
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.HOPPER_FLUID.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerFluidHopper(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tank.readFromNBT(registries, tag.getCompound(NBTFLUID));
    if (tag.contains(NBT_FILTER)) {
      filter.deserializeNBT(registries, tag.getCompound(NBT_FILTER));
    }
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries, fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBT_FILTER, filter.serializeNBT(registries));
    super.saveAdditional(tag, registries);
  }

  public int getFill() {
    if (tank == null || tank.getFluidInTank(0).isEmpty()) {
      return 0;
    }
    return tank.getFluidInTank(0).getAmount();
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }
}
