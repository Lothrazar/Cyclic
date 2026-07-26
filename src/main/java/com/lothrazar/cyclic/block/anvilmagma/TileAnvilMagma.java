package com.lothrazar.cyclic.block.anvilmagma;

import java.util.function.Predicate;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.items.IItemHandler;

public class TileAnvilMagma extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    TIMER, REDSTONE;
  }

  public static final int CAPACITY = 64 * FluidType.BUCKET_VOLUME;
  public static ModConfigSpec.IntValue FLUIDCOST;
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      // 26.1: ItemStack#isRepairable() removed with no replacement - getDamageValue() > 0 already implies
      // the stack is damageable, which covers the meaningful part of the old check.
      return stack.getDamageValue() > 0;
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  public TileAnvilMagma(BlockPos pos, BlockState state) {
    super(TileRegistry.ANVIL_MAGMA.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAnvilMagma e) {
    e.tick();
  }

  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    ItemStack stack = inputSlots.getStackInSlot(0);
    if (stack.isEmpty() || stack.is(DataTags.ANVIL_IMMUNE)) {
      //move it over and then done
      if (outputSlots.getStackInSlot(0).isEmpty()) {
        outputSlots.insertItem(0, stack.copy(), false);
        inputSlots.extractItem(0, stack.getCount(), false);
      }
      return;
    }
    boolean done = (stack.getDamageValue() == 0);
    if (done && outputSlots.getStackInSlot(0).isEmpty()) {
      // 
      outputSlots.insertItem(0, stack.copy(), false);
      inputSlots.extractItem(0, stack.getCount(), false);
    }
    final int repair = FLUIDCOST.get(); // fluid
    boolean work = false;
    // 26.1: ItemStack#isRepairable() removed with no replacement - getDamageValue() > 0 already implies
    // the stack is damageable, which covers the meaningful part of the old check.
    if (tank != null &&
        tank.getFluidAmount() >= repair &&
        stack.getDamageValue() > 0) {
      //we can repair so steal some power 
      //ok drain power  
      work = true;
      tank.drain(repair, IFluidHandler.FluidAction.EXECUTE);
    }
    //shift to other slot
    if (work) {
      ItemStackUtil.repairItem(stack);
    }
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> {
      Fluid fluid = p.getFluid();
      return fluid == Fluids.LAVA;
    };
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.ANVIL_MAGMA.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerAnvilMagma(i, level, worldPosition, playerInventory, playerEntity);
  }


  @Override
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    tank.deserialize(input.childOrEmpty(NBTFLUID));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    tank.serialize(output.child(NBTFLUID));
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
    }
    return 0;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TIMER:
        this.timer = value;
      break;
    }
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  @Override
  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    return inventory.getSlotsForFace(direction);
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canPlaceItemThroughFace(i, itemStack, direction);
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canTakeItemThroughFace(i, itemStack, direction);
  }
}
