package com.lothrazar.cyclic.block.disenchant;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.registry.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileDisenchant extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

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
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        return enchants != null && enchants.size() > 0;
      }
      return stack.getItem() == Items.ENCHANTED_BOOK;
    }
  };
  public static final int CAPACITY = 16 * FluidAttributes.BUCKET_VOLUME;
  ItemStackHandler outputSlots = new ItemStackHandler(2);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  public static IntValue POWERCONF;
  public static IntValue FLUIDCOST;
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  public FluidTankBase tank;

  public TileDisenchant() {
    super(TileRegistry.disenchanter);
    tank = new FluidTankBase(this, CAPACITY, p -> {
      return p.getFluid().isIn(DataTags.XP);
    });
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    Integer cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && (cost > 0)) {
      return;
    }
    if (FLUIDCOST.get() > 0 && tank.getFluidAmount() < FLUIDCOST.get()) {
      return;
    }
    ItemStack input = inputSlots.getStackInSlot(SLOT_INPUT);
    if (input.isEmpty() || input.getItem().isIn(DataTags.DISENCHIMMUNE)) {
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
    Map<Enchantment, Integer> outEnchants = Maps.<Enchantment, Integer> newLinkedHashMap();
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
    UtilSound.playSound(world, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE);
    energy.extractEnergy(cost, false);
    System.out.println("f cost eh" + FLUIDCOST.get());
    if (FLUIDCOST.get() > 0) {
      tank.drain(FLUIDCOST.get(), FluidAction.EXECUTE);
    }
    else if (FLUIDCOST.get() < 0) {
      Fluid newFluid = FluidXpJuiceHolder.STILL.get();
      if (!this.getFluid().isEmpty()) {
        //if its holding a tag compatible but different fluid, just fill 
        newFluid = this.getFluid().getFluid();
      }
      tank.fill(new FluidStack(newFluid, -1 * FLUIDCOST.get()), FluidAction.EXECUTE);
    }
    inputEnchants.remove(keyMoved);
    ItemStack eBook = new ItemStack(Items.ENCHANTED_BOOK);
    EnchantmentHelper.setEnchantments(outEnchants, eBook); //add to book
    //replace book with enchanted
    inputSlots.extractItem(SLOT_BOOK, 1, false);
    outputSlots.insertItem(0, eBook, false);
    //do i replace input with a book?
    if (input.getItem() == Items.ENCHANTED_BOOK && inputEnchants.size() == 0) {
      ModCyclic.LOGGER.info("book size zero");
      inputSlots.extractItem(SLOT_INPUT, 64, false); //delete input
      inputSlots.insertItem(SLOT_INPUT, new ItemStack(Items.BOOK), false);
    }
    else {
      //was a normal item, so ok to set its ench list to empty
      if (input.getItem() == Items.ENCHANTED_BOOK) { //hotfix workaround for book: so it dont try to merge eh
        ModCyclic.LOGGER.info("book normal flow");
        ItemStack inputCopy = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(inputEnchants, inputCopy); //set as remove
        inputSlots.extractItem(SLOT_INPUT, 64, false); //delete input
        inputSlots.insertItem(SLOT_INPUT, inputCopy, false);
      }
      else {
        ModCyclic.LOGGER.info("non-book set as removed from item");
        EnchantmentHelper.setEnchantments(inputEnchants, input); //set as removed
      }
    }
    //recalculate input item
    input = inputSlots.getStackInSlot(SLOT_INPUT);
    inputEnchants = EnchantmentHelper.getEnchantments(input);
    if (!input.isEmpty() && inputEnchants.size() == 0) {
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
    return this.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerDisenchant(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    CompoundNBT fluid = new CompoundNBT();
    tank.writeToNBT(fluid);
    tag.put("fluid", fluid);
    return super.write(tag);
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

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }
}
