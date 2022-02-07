package com.lothrazar.cyclic.block.packager;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePackager extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static IntValue POWERCONF;
  public static final int TICKS = 10;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left

  public TilePackager() {
    super(TileRegistry.PACKAGER.get());
    this.needsRedstone = 0;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    this.burnTime--;
    if (burnTime <= 0) {
      burnTime = TICKS;
      tryDoPackage();
    }
  }

  private void tryDoPackage() {
    if (POWERCONF.get() > 0 && energy.getEnergyStored() < POWERCONF.get()) {
      return; //not enough pow
    }
    setLitProperty(true);
    //pull in new fuel
    ItemStack stack = inputSlots.getStackInSlot(0);
    //shapeless recipes / shaped check either
    List<ICraftingRecipe> recipes = world.getRecipeManager().getRecipesForType(IRecipeType.CRAFTING);
    for (ICraftingRecipe rec : recipes) {
      if (!isRecipeValid(rec)) {
        continue;
      }
      //test matching recipe and its size
      int total = getCostIfMatched(stack, rec);
      if (total > 0 &&
          outputSlots.insertItem(0, rec.getRecipeOutput().copy(), true).isEmpty()) {
        ModCyclic.LOGGER.info("Packager recipe match of size " + total + " producing -> " + rec.getRecipeOutput().copy());
        //consume items, produce output
        inputSlots.extractItem(0, total, false);
        outputSlots.insertItem(0, rec.getRecipeOutput().copy(), false);
        energy.extractEnergy(POWERCONF.get(), false);
      }
    }
  }

  public static boolean isRecipeValid(ICraftingRecipe recipe) {
    int total = 0, matched = 0;
    Ingredient first = null;
    for (Ingredient ingr : recipe.getIngredients()) {
      if (ingr == Ingredient.EMPTY || ingr.getMatchingStacks().length == 0) {
        continue;
      }
      total++;
      if (first == null) {
        first = ingr;
        matched = 1;
        continue;
      }
      if (first.test(ingr.getMatchingStacks()[0])) {
        matched++;
      }
    }
    if (first == null || first.getMatchingStacks() == null || first.getMatchingStacks().length == 0) {
      return false; //nothing here
    }
    boolean outIsStorage = recipe.getRecipeOutput().getItem().isIn(Tags.Items.STORAGE_BLOCKS);
    boolean inIsIngot = first.getMatchingStacks()[0].getItem().isIn(Tags.Items.INGOTS);
    if (!outIsStorage && inIsIngot) {
      //ingots can only go to storage blocks, nothing else
      //avoids armor/ iron trap doors. kinda hacky
      return false;
    }
    if (total > 0 && total == matched &&
        recipe.getRecipeOutput().getMaxStackSize() > 1 && //aka not tools/boots/etc
        //        stack.getCount() >= total &&
        (total == 4 || total == 9) &&
        (recipe.getRecipeOutput().getCount() == 1 || recipe.getRecipeOutput().getCount() == total)) {
      return true;
    }
    return false;
  }

  private int getCostIfMatched(ItemStack stack, ICraftingRecipe recipe) {
    int total = 0, matched = 0;
    for (Ingredient ingr : recipe.getIngredients()) {
      if (ingr == Ingredient.EMPTY) {
        continue;
      }
      total++;
      if (ingr.test(stack)) {
        matched++;
      }
    }
    if (total == matched &&
        stack.getCount() >= total &&
        (total == 4 || total == 9) &&
        (recipe.getRecipeOutput().getCount() == 1 || recipe.getRecipeOutput().getCount() == total)) {
      return total;
    }
    return -1;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerPackager(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.burnTime;
      case BURNMAX:
        return this.burnTimeMax;
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
        this.burnTime = value;
      break;
      case BURNMAX:
        this.burnTimeMax = value;
      break;
    }
  }

  public int getEnergyMax() {
    return TilePackager.MAX;
  }
}
