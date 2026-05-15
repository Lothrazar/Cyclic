package com.lothrazar.cyclic.block.disenchant;

import java.util.Map;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.fixers.CapabilityFixer;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import com.lothrazar.library.util.FluidHelpersUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
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
    REDSTONE, TIMER;
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
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  public static ModConfigSpec.IntValue POWERCONF;
  public static ModConfigSpec.IntValue FLUIDCOST;
// //  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, p -> {
    return FluidHelpersUtil.matches(p.getFluid(), DataTags.EXPERIENCE);
  });
// //  LazyOptional<FluidTankBase> fluidCap = LazyOptional.of(() -> tank);

  public TileDisenchant(BlockPos pos, BlockState state) {
    super(TileRegistry.DISENCHANTER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileDisenchant e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileDisenchant e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    ItemStack input = inputSlots.getStackInSlot(SLOT_INPUT);
    if (input.isEmpty() || input.is(DataTags.DISENCHANTER_IMMUNE)) {
      return;
    }
    Integer cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && (cost > 0)) {
      return;
    }
    if (FLUIDCOST.get() > 0 && tank.getFluidAmount() < FLUIDCOST.get()) {
      return;
    }
    ItemStack book = inputSlots.getStackInSlot(SLOT_BOOK);
    if (book.getItem() != Items.BOOK
        || outputSlots.getStackInSlot(0).isEmpty() == false
        || outputSlots.getStackInSlot(1).isEmpty() == false
        || input.getCount() != 1) {
      return;
    }
    //input is size 1, at least one book exists, and output IS empty
    /*
    Map<Enchantment, Integer> inputEnchants = EnchantmentHelper.getEnchantments(input);
    Enchantment keyMoved = null;
    for (Map.Entry<Enchantment, Integer> entry : inputEnchants.entrySet()) {
      keyMoved = entry.getKey();
      outEnchants.put(keyMoved, entry.getValue());
      break;
    }
    if (outEnchants.size() == 0 || keyMoved == null) {
      return;
    }
    //and input has at least one enchantment 
    //success happening
    if (level.random.nextDouble() < 0.5) {
      SoundUtil.playSound(level, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE);
    }
    else {
      SoundUtil.playSound(level, worldPosition, SoundEvents.ANVIL_USE);
    }
    energy.extractEnergy(cost, false);
    if (FLUIDCOST.get() > 0) {
      tank.drain(FLUIDCOST.get(), IFluidHandler.FluidAction.EXECUTE);
    }
    else if (FLUIDCOST.get() < 0) {
      Fluid newFluid = FluidXpJuiceHolder.STILL.get();
      if (!this.getFluid().isEmpty()) {
        //if its holding a tag compatible but different fluid, just fill 
        newFluid = this.getFluid().getFluid();
      }
      tank.fill(new FluidStack(newFluid, -1 * FLUIDCOST.get()), IFluidHandler.FluidAction.EXECUTE);
    }
    inputEnchants.remove(keyMoved);
    ItemStack eBook = new ItemStack(Items.ENCHANTED_BOOK);
    EnchantmentHelper.setEnchantments(outEnchants, eBook); //add to book
    //replace book with enchanted
    inputSlots.extractItem(SLOT_BOOK, 1, false);
    outputSlots.insertItem(0, eBook, false);
    //do i replace input with a book?
    if (input.getItem() == Items.ENCHANTED_BOOK && inputEnchants.size() == 0) { // empty ench on enchbook override
      inputSlots.extractItem(SLOT_INPUT, 64, false); //delete input
      inputSlots.insertItem(SLOT_INPUT, new ItemStack(Items.BOOK), false);
    }
    else {
      //was a normal item, so ok to set its ench list to empty
      if (input.getItem() == Items.ENCHANTED_BOOK) { // normal enchanted book
        ItemStack inputCopy = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(inputEnchants, inputCopy); //set as remove
        inputSlots.extractItem(SLOT_INPUT, 64, false); //delete input
        inputSlots.insertItem(SLOT_INPUT, inputCopy, false);
      }
      else { // non-book set as removed from item
        EnchantmentHelper.setEnchantments(inputEnchants, input); //set as removed
      }
    }
    //recalculate input item
    input = inputSlots.getStackInSlot(SLOT_INPUT);
    inputEnchants = EnchantmentHelper.getEnchantments(input);
    if (!input.isEmpty() && inputEnchants.size() == 0) {
      //hey we done, bump it over to the ALL NEW finished slot
      }*/
  }

  @Override
  public int getEnergy() {
    return CapabilityFixer.energyStored(level,this.getBlockPos());
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
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return needsRedstone;
      case TIMER:
        return timer;
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
    return inputSlots;
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
