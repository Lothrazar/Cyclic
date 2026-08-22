package com.lothrazar.cyclic.block.anvil;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileAnvilAuto extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    TIMER, REDSTONE;
  }

  static final int MAX = 64000;
  public static ModConfigSpec.IntValue POWERCONF;
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      // 26.1: ItemStack#isRepairable() removed with no replacement - getDamageValue() > 0 already implies
      // the stack is damageable, which covers the meaningful part of the old check.
      boolean v = stack.getDamageValue() > 0;
      return v;
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);

  public TileAnvilAuto(BlockPos pos, BlockState state) {
    super(TileRegistry.ANVIL.get(), pos, state);
    this.needsRedstone = 0;
  }

//  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileAnvilAuto tileAnvilAuto) {
//    tileAnvilAuto.tick();
//  }

  public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAnvilAuto e) {
    e.tick();
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.ANVIL.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerAnvil(i, level, worldPosition, playerInventory, playerEntity);
  }


  @Override
  public void loadAdditional(ValueInput input) {
    energy.deserialize(input.childOrEmpty(NBTENERGY));
    inventory.deserialize(input.childOrEmpty(NBTINV));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    energy.serialize(output.child(NBTENERGY));
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    //
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.isEmpty() || stack.is(DataTags.ANVIL_IMMUNE)) {
      return;
    }
    final int repair = POWERCONF.get();
    boolean work = false;
    // 26.1: ItemStack#isRepairable() removed with no replacement - getDamageValue() > 0 already implies
    // the stack is damageable, which covers the meaningful part of the old check.
    if (repair > 0 &&
        energy.getEnergyStored() >= repair &&
        stack.getDamageValue() > 0) {
      //we can repair so steal some power 
      //ok drain power  
      energy.extractEnergy(repair, false);
      work = true;
    }
    //shift to other slot
    if (work) {
      ItemStackUtil.repairItem(stack);
      boolean done = stack.getDamageValue() == 0;
      if (done && outputSlots.getStackInSlot(0).isEmpty()) {
        outputSlots.insertItem(0, stack.copy(), false);
        inputSlots.extractItem(0, stack.getCount(), false);
      }
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      default:
        break;
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

  public int getEnergyMax() {
    return TileAnvilAuto.MAX;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
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
