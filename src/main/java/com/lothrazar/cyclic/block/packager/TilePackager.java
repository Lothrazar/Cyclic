package com.lothrazar.cyclic.block.packager;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
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

  enum Fields {
    TIMER, REDSTONE, BURNMAX;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static IntValue POWERCONF;
  public static final int TICKS = 10;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private final ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left
  private static final Map<Item, ICraftingRecipe> fourItemRecipeCache = new HashMap<>();
  private static final Map<Item, ICraftingRecipe> nineItemRecipeCache = new HashMap<>();
  private static final Map<ICraftingRecipe, Integer> ingredientsInRecipeCache = new HashMap<>();

  public TilePackager() {
    super(TileRegistry.PACKAGER.get());
    this.needsRedstone = 0;
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
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
    if (world == null) {
      return;
    }
    final ICraftingRecipe recipe = getRecipeForItemStack(world.getRecipeManager(), stack);
    if (recipe == null) {
      return;
    }
    if (outputSlots.insertItem(0, recipe.getRecipeOutput().copy(), true).isEmpty()) {
      final int total = ingredientsInRecipeCache.get(recipe);
      final ItemStack output = recipe.getRecipeOutput().copy();
      ModCyclic.LOGGER.info("Packager recipe match of size " + total + " producing -> " + output);
      //consume items, produce output
      inputSlots.extractItem(0, total, false);
      outputSlots.insertItem(0, output, false);
      energy.extractEnergy(POWERCONF.get(), false);
    }
  }

  public static ICraftingRecipe getRecipeForItemStack(final RecipeManager recipeManager, final ItemStack itemStack) {
    if (itemStack.getCount() >= 9) {
      if (nineItemRecipeCache.isEmpty()) {
        buildRecipeCaches(recipeManager);
      }
      final ICraftingRecipe recipe = nineItemRecipeCache.get(itemStack.getItem());
      if (recipe != null) {
        return recipe;
      }
    }
    if (itemStack.getCount() >= 4) {
      if (fourItemRecipeCache.isEmpty()) {
        buildRecipeCaches(recipeManager);
      }
      return fourItemRecipeCache.get(itemStack.getItem());
    }
    return null;
  }

  public static boolean isRecipeValid(final ICraftingRecipe recipe) {
    final ItemStack recipeOutput = recipe.getRecipeOutput();
    if (recipeOutput.getMaxStackSize() == 1 || recipeOutput.getCount() != 1) {
      return false;
    }
    Ingredient mainIngredient = null;
    ItemStack mainIngredientStack = null;
    int count = 0;
    for (final Ingredient ingredient : recipe.getIngredients()) {
      if (ingredient == Ingredient.EMPTY) {
        continue;
      }
      final ItemStack[] matchingStacks = ingredient.getMatchingStacks();
      if (matchingStacks.length == 0) {
        continue;
      }
      else if (matchingStacks.length > 1) {
        return false;
      }
      final ItemStack matchingStack = matchingStacks[0];
      if (mainIngredient != null && !mainIngredient.test(matchingStack)) {
        return false;
      }
      mainIngredient = ingredient;
      mainIngredientStack = matchingStack;
      count++;
    }
    if (mainIngredient == null) {
      return false;
    }
    boolean outIsStorage = recipeOutput.getItem().isIn(Tags.Items.STORAGE_BLOCKS);
    final Item mainIngredientItem = mainIngredientStack.getItem();
    boolean inIsIngot = mainIngredientItem.isIn(Tags.Items.INGOTS);
    if (!outIsStorage && inIsIngot) {
      return false;
    }
    return true;
  }

  public static void buildRecipeCaches(final RecipeManager recipeManager) {
    recipeLoop: for (final ICraftingRecipe recipe : recipeManager.getRecipesForType(IRecipeType.CRAFTING)) {
      final ItemStack recipeOutput = recipe.getRecipeOutput();
      if (recipeOutput.getMaxStackSize() == 1 || recipeOutput.getCount() != 1) {
        continue;
      }
      Ingredient mainIngredient = null;
      ItemStack mainIngredientStack = null;
      int count = 0;
      for (final Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == Ingredient.EMPTY) {
          continue;
        }
        final ItemStack[] matchingStacks = ingredient.getMatchingStacks();
        if (matchingStacks.length != 1) {
          continue recipeLoop;
        }
        final ItemStack matchingStack = matchingStacks[0];
        if (mainIngredient != null && !mainIngredient.test(matchingStack)) {
          continue recipeLoop;
        }
        mainIngredient = ingredient;
        mainIngredientStack = matchingStack;
        count++;
      }
      if (mainIngredient == null) {
        continue;
      }
      boolean outIsStorage = recipeOutput.getItem().isIn(Tags.Items.STORAGE_BLOCKS);
      final Item mainIngredientItem = mainIngredientStack.getItem();
      boolean inIsIngot = mainIngredientItem.isIn(Tags.Items.INGOTS);
      if (!outIsStorage && inIsIngot) {
        continue;
      }
      if (count == 4) {
        fourItemRecipeCache.put(mainIngredientItem, recipe);
        ingredientsInRecipeCache.put(recipe, 4);
      }
      else if (count == 9) {
        nineItemRecipeCache.put(mainIngredientItem, recipe);
        ingredientsInRecipeCache.put(recipe, 9);
      }
    }
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
