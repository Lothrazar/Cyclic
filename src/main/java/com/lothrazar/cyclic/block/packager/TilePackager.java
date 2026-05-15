package com.lothrazar.cyclic.block.packager;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;


public class TilePackager extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static ModConfigSpec.IntValue POWERCONF;
  public static final int TICKS = 10;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return isItemStackValid(stack);
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  final ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left

  public TilePackager(BlockPos pos, BlockState state) {
    super(TileRegistry.PACKAGER.get(), pos, state);
    this.needsRedstone = 0;
  }

  private boolean isItemStackValid(ItemStack stack) {
    if (level == null) {
      return false;
    }
    return UtilPackager.isItemStackValid(level.getRecipeManager(), level.registryAccess(), stack);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TilePackager e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TilePackager e) {
    e.tick();
  }

  public void tick() {
    //    if (world == null || world.isRemote) {
    //      return;
    //    }
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
    if (level == null) {
      return;
    }
    //shapeless recipes / shaped check either 
    final CraftingRecipe recipe = UtilPackager.getRecipeForItemStack(level.getRecipeManager(), level.registryAccess(), stack); // level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
    if (recipe == null) {
      return;
    }
    if (outputSlots.insertItem(0, recipe.getResultItem(level.registryAccess()).copy(), true).isEmpty()) {
      final int total = UtilPackager.getIngredientsInRecipe(recipe);
      final ItemStack output = recipe.getResultItem(level.registryAccess()).copy();
      ModCyclic.LOGGER.info("Packager recipe match of size " + total + " producing -> " + output);
      //consume items, produce output
      inputSlots.extractItem(0, total, false);
      outputSlots.insertItem(0, output, false);
      energy.extractEnergy(POWERCONF.get(), false);
      this.updateComparatorOutputLevel();
    }
  }

  public static boolean isRecipeValid(CraftingRecipe recipe, Level level) {
    int total = 0, matched = 0;
    Ingredient first = null;
    ItemStack[] firstItems = null;
    for (Ingredient ingr : recipe.getIngredients()) {
      ItemStack[] ingrItemList = ingr.getItems();
      if (ingr == Ingredient.EMPTY || ingrItemList.length == 0) {
        continue;
      }
      total++;
      if (first == null) {
        first = ingr;
        firstItems = ingrItemList;
        matched = 1;
        continue;
      }
      if (first.test(ingrItemList[0])) {
        matched++;
      }
    }
    if (first == null || firstItems == null || firstItems.length == 0) {
      return false; //nothing here
    }
    boolean outIsStorage = recipe.getResultItem(level.registryAccess()).is(Tags.Items.STORAGE_BLOCKS);
    boolean inIsIngot = firstItems[0].is(Tags.Items.INGOTS);
    if (!outIsStorage && inIsIngot) {
      //ingots can only go to storage blocks, nothing else
      //avoids armor/ iron trap doors. kinda hacky
      return false;
    }
    if (total > 0 && total == matched &&
        recipe.getResultItem(level.registryAccess()).getMaxStackSize() > 1 && //aka not tools/boots/etc
        //        stack.getCount() >= total &&
        (total == 4 || total == 9) &&
        (recipe.getResultItem(level.registryAccess()).getCount() == 1 || recipe.getResultItem(level.registryAccess()).getCount() == total)) {
      return true;
    }
    return false;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.PACKAGER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerPackager(i, level, worldPosition, playerInventory, playerEntity);
  }


  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    inventory.deserializeNBT(registries, tag.getCompound(NBTINV));
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag,registries);
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inputSlots;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
