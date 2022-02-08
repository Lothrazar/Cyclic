package com.lothrazar.cyclic.block.anvil;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.cyclic.capabilities.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
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
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileAnvilAuto extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    TIMER, REDSTONE;
  }

  static final int MAX = 64000;
  public static IntValue POWERCONF;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    /* https://github.com/Lothrazar/Cyclic/pull/1990/files#diff-4eb95a3d9ac136172375b3e7be5bc26c576f9fa6efcabee458c4c80797205b73R40 */
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (!stack.isEmpty() && stack.isRepairable() && stack.getDamageValue() == 0) {
        return outputSlots.insertItem(slot, stack, simulate);
      }
      else {
        return super.insertItem(slot, stack, simulate);
      }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.isRepairable() && stack.getDamageValue() > 0;
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileAnvilAuto(BlockPos pos, BlockState state) {
    super(TileRegistry.ANVIL.get(), pos, state);
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileAnvilAuto tileAnvilAuto) {
    tileAnvilAuto.tick();
  }

  public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAnvilAuto e) {
    e.tick();
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerAnvil(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
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
    if (repair > 0 &&
        energy.getEnergyStored() >= repair &&
        stack.isRepairable() &&
        stack.getDamageValue() > 0) {
      //we can repair so steal some power 
      //ok drain power  
      energy.extractEnergy(repair, false);
      work = true;
    }
    //shift to other slot
    if (work) {
      UtilItemStack.repairItem(stack);
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
}
