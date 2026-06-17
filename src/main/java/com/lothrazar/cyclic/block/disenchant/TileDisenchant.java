package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import com.lothrazar.library.util.FluidHelpersUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileDisenchant extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    REDSTONE, TIMER, TIMERMAX;
  }

  static final int MAX = 640000;
  private static final int SLOT_INPUT = 0;
  private static final int SLOT_BOOK = 1;
  ItemStackHandler inputSlots = new ItemStackHandler(2) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (slot == SLOT_BOOK) {
        return stack.getItem() == Items.BOOK;
      }
      else if (slot == SLOT_INPUT) {
        ItemEnchantments enchants = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        return enchants != null && !enchants.isEmpty();
      }
      return stack.getItem() == Items.ENCHANTED_BOOK;
    }
  };
  public static final int CAPACITY = 16 * FluidType.BUCKET_VOLUME;
  ItemStackHandler outputSlots = new ItemStackHandler(2);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
// //  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX / 4);
  public static ModConfigSpec.IntValue POWERCONF;
  public static ModConfigSpec.IntValue FLUIDCOST;
  public static ModConfigSpec.IntValue TIMERCONF;
// //  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> {
    return FluidHelpersUtil.matches(p.getFluid(), DataTags.EXPERIENCE);
  });
// //  LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  public TileDisenchant(BlockPos pos, BlockState state) {
    super(TileRegistry.DISENCHANTER.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileDisenchant e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileDisenchant e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (level.isClientSide) {
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      timer = 0;
      return;
    }
    ItemStack input = inputSlots.getStackInSlot(SLOT_INPUT);
    if (input.isEmpty() || input.is(DataTags.DISENCHANTER_IMMUNE)) {
      timer = 0;
      return;
    }
    Integer cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && (cost > 0)) {
      timer = 0;
      return;
    }
    if (FLUIDCOST.get() > 0 && tank.getFluidAmount() < FLUIDCOST.get()) {
      timer = 0;
      return;
    }
    ItemStack book = inputSlots.getStackInSlot(SLOT_BOOK);
    if (book.getItem() != Items.BOOK
        || outputSlots.getStackInSlot(0).isEmpty() == false
        || outputSlots.getStackInSlot(1).isEmpty() == false
        || input.getCount() != 1) {
      timer = 0;
      return;
    }
    //input is size 1, at least one book exists, and output IS empty
    ItemEnchantments inputEnchants = EnchantmentHelper.getEnchantmentsForCrafting(input);
    Holder<Enchantment> keyMoved = null;
    int levelMoved = 0;
    for (var entry : inputEnchants.entrySet()) {
      keyMoved = entry.getKey();
      levelMoved = entry.getIntValue();
      break;
    }
    if (keyMoved == null) {
      timer = 0;
      return;
    }
    //and input has at least one enchantment - count down the timer
    if (timer <= 0) {
      timer = TIMERCONF.get();
    }
    timer--;
    if (timer > 0) {
      return;
    }
    timer = 0;
    //success happening
    if (level.random.nextDouble() < 0.5) {
      SoundUtil.playSound(level, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE);
    } else {
      SoundUtil.playSound(level, worldPosition, SoundEvents.ANVIL_USE);
    }
    energy.extractEnergy(cost, false);
    if (FLUIDCOST.get() > 0) {
      tank.drain(FLUIDCOST.get(), IFluidHandler.FluidAction.EXECUTE);
    } else if (FLUIDCOST.get() < 0) {
      Fluid newFluid = FluidXpJuiceHolder.STILL.get();
      if (!this.getFluid().isEmpty()) {
        //if its holding a tag compatible but different fluid, just fill
        newFluid = this.getFluid().getFluid();
      }
      tank.fill(new FluidStack(newFluid, -1 * FLUIDCOST.get()), IFluidHandler.FluidAction.EXECUTE);
    }
    //build the enchanted book with just the single moved enchantment
    final Holder<Enchantment> movedKey = keyMoved;
    final int movedLvl = levelMoved;
    ItemStack eBook = new ItemStack(Items.ENCHANTED_BOOK);
    EnchantmentHelper.updateEnchantments(eBook, m -> m.set(movedKey, movedLvl));
    //replace book with enchanted
    inputSlots.extractItem(SLOT_BOOK, 1, false);
    outputSlots.insertItem(0, eBook, false);
    //strip the moved enchantment off a copy so we can see what (if anything) remains
    ItemStack stripped = input.copy();
    EnchantmentHelper.updateEnchantments(stripped, m -> m.removeIf(h -> h.equals(movedKey)));
    boolean remainingEmpty = EnchantmentHelper.getEnchantmentsForCrafting(stripped).isEmpty();
    //do i replace input with a book?
    if (input.getItem() == Items.ENCHANTED_BOOK && remainingEmpty) { // empty ench on enchbook override
      inputSlots.extractItem(SLOT_INPUT, 64, false); //delete input
      inputSlots.insertItem(SLOT_INPUT, new ItemStack(Items.BOOK), false);
    } else {
      //was a normal item, so ok to set its ench list to empty
      if (input.getItem() == Items.ENCHANTED_BOOK) { // normal enchanted book - swap in stripped copy
        inputSlots.extractItem(SLOT_INPUT, 64, false); //delete input
        inputSlots.insertItem(SLOT_INPUT, stripped, false);
      } else { // non-book set as removed from item
        EnchantmentHelper.updateEnchantments(input, m -> m.removeIf(h -> h.equals(movedKey)));
      }
    }
    //recalculate input item
    input = inputSlots.getStackInSlot(SLOT_INPUT);
    ItemEnchantments postInput = EnchantmentHelper.getEnchantmentsForCrafting(input);
    if (!input.isEmpty() && postInput.isEmpty()) {
      //hey we done, bump it over to the ALL NEW finished slot
      if (outputSlots.getStackInSlot(1).isEmpty()) {
        //only if there is space, then do it
        outputSlots.insertItem(1, input.copy(), false);
        inputSlots.extractItem(SLOT_INPUT, 64, false);
      }
      //delete input
    }
  }

  @Override
  public int getEnergy() {
    return CapabilityUtil.energyStored(level,this.getBlockPos());
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.DISENCHANTER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerDisenchant(i, level, worldPosition, playerInventory, playerEntity);
  }


  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tank.readFromNBT(registries,tag.getCompound(NBTFLUID));
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inventory.serializeNBT(registries));
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(registries,fluid);
    tag.put(NBTFLUID, fluid);
    super.saveAdditional(tag, registries);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TIMER:
        timer = value;
      break;
      case TIMERMAX:
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return needsRedstone;
      case TIMER:
        return timer;
      case TIMERMAX:
        return TIMERCONF.get();
    }
    return 0;
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
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    return inventory.getSlotsForFace(direction);
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack,  Direction direction) {
    return inventory.canPlaceItemThroughFace(i, itemStack, direction);
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canTakeItemThroughFace(i, itemStack, direction);
  }
}
