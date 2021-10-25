package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilString;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileUncraft extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    REDSTONE, STATUS, TIMER;
  }

  static final int MAX = 64000;
  public static IntValue POWERCONF;
  public static BooleanValue IGNORE_NBT;
  public static ConfigValue<Integer> TIMER;
  public static ConfigValue<List<? extends String>> IGNORELIST;
  public static ConfigValue<List<? extends String>> IGNORELIST_RECIPES;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1){
	  protected void onContentsChanged(int slot) {
			TileUncraft.this.status = UncraftStatusEnum.EMPTY;  
	  };
  };
  ItemStackHandler outputSlots = new ItemStackHandler(8 * 2) {
	  protected void onContentsChanged(int slot) {
			if(TileUncraft.this.status == UncraftStatusEnum.NOROOM) TileUncraft.this.status = UncraftStatusEnum.EMPTY;  
	  };
  };
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileUncraft() {
    super(TileRegistry.uncrafter);
  }

  @Override
  public void tick() {
    this.syncEnergy();
    ItemStack dropMe = inputSlots.getStackInSlot(0).copy();
    if (dropMe.isEmpty()) {
      this.status = UncraftStatusEnum.EMPTY;
      timer = TIMER.get();
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      return;
    }
    setLitProperty(true);
    //only tick down if we have enough energy and have a valid item
    
    if(this.status != UncraftStatusEnum.EMPTY && this.status != UncraftStatusEnum.MATCH) {
    	this.timer = TIMER.get();
    	return;
    }
    
    if (--timer > 0) {
      return;
    }
    timer = TIMER.get();
    if (world.isRemote || world.getServer() == null) {
      return;
    }
    IRecipe<?> match = this.findMatchingRecipe(world, dropMe);
    if (match != null) {
      if (uncraftRecipe(match)) {
        this.status = UncraftStatusEnum.MATCH;
        //pay cost
        // ModCyclic.LOGGER.info("before extract cost" + inputSlots.getStackInSlot(0));
        inputSlots.extractItem(0, match.getRecipeOutput().getCount(), false);
        // ModCyclic.LOGGER.info("AFTER  extract cost" + inputSlots.getStackInSlot(0));
        energy.extractEnergy(cost, false);
      }
    }
    else {
      this.status = UncraftStatusEnum.NORECIPE;
    }
  }

  private UncraftStatusEnum status = UncraftStatusEnum.EMPTY;

  public UncraftStatusEnum getStatus() {
    return status;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerUncraft(i, world, pos, playerInventory, playerEntity);
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
  public void read(BlockState bs, CompoundNBT tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    this.status = UncraftStatusEnum.values()[tag.getInt("ucstats")];
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("ucstats", status.ordinal());
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  private boolean uncraftRecipe(IRecipe<?> match) {
    List<ItemStack> result = match.getIngredients().stream().flatMap(ingredient -> Arrays.stream(ingredient.getMatchingStacks())
        .filter(stack -> !stack.hasContainerItem())
        .findAny()
        .map(Stream::of)
        .orElseGet(Stream::empty))
        .collect(Collectors.toList());
    if (result.isEmpty()) {
      this.status = UncraftStatusEnum.NORECIPE;
      return false;
    }
    //do we have space for out?
    boolean simulate = true;
    for (ItemStack r : result) {
      ItemStack rOut = r;
      for (int i = 0; i < outputSlots.getSlots(); i++) {
        if (!rOut.isEmpty()) {
          rOut = outputSlots.insertItem(i, rOut, simulate);
        }
      }
      if (!rOut.isEmpty()) { //This doesn't actually work - it will succeed if there is so much as 1 open slot. But idk how to fix it without being lag-inducing.
        this.status = UncraftStatusEnum.NOROOM;
        return false;
      }
    }
    //we have room for sure
    simulate = false;
    for (ItemStack r : result) {
      ItemStack forTesting = r.copy();
      //give result items
      for (int i = 0; i < outputSlots.getSlots(); i++) {
        if (forTesting.isEmpty()) {
          break;
        }
        forTesting = outputSlots.insertItem(i, forTesting, simulate);
      }
    }
    return true;
  }

  public IRecipe<?> findMatchingRecipe(World world, ItemStack dropMe) {
    Collection<IRecipe<?>> list = world.getServer().getRecipeManager().getRecipes();
    for (IRecipe<?> recipe : list) {
      if (recipe.getType() == IRecipeType.CRAFTING) {
        //actual uncraft, ie not furnace recipe or anything
        if (recipeMatches(dropMe, recipe)) {
          return recipe;
        }
      }
    }
    return null;
  }

  // matches count and has enough
  @SuppressWarnings("unchecked")
  private boolean recipeMatches(ItemStack stack, IRecipe<?> recipe) {
    if (stack.isEmpty() ||
        recipe == null ||
        recipe.getRecipeOutput().isEmpty() ||
        recipe.getRecipeOutput().getCount() > stack.getCount()) {
      this.status = UncraftStatusEnum.NORECIPE;
      return false;
    }
    //check config
    List<String> recipes = (List<String>) TileUncraft.IGNORELIST_RECIPES.get();
    if (UtilString.isInList(recipes, recipe.getId())) {
      //check the RECIPE id list
      this.status = UncraftStatusEnum.CONFIG;
      return false;
    }
    if (UtilString.isInList((List<String>) TileUncraft.IGNORELIST.get(), stack.getItem().getRegistryName())) {
      //checked the ITEM id list
      this.status = UncraftStatusEnum.CONFIG;
      return false;
    }
    //both itemstacks are non-empty, and we have enough quantity
    boolean matches = false;
    if (TileUncraft.IGNORE_NBT.get()) {
      matches = stack.getItem() == recipe.getRecipeOutput().getItem();
    }
    else {
      matches = stack.getItem() == recipe.getRecipeOutput().getItem() &&
          ItemStack.areItemStackTagsEqual(stack, recipe.getRecipeOutput());
    }
    if (!matches) {
      this.status = UncraftStatusEnum.NORECIPE;
    }
    return matches;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case STATUS:
        return this.status.ordinal();
      case TIMER:
        return timer;
    }
    return 0;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case STATUS:
        this.status = UncraftStatusEnum.values()[value];
      break;
      case TIMER:
        timer = value;
      break;
    }
  }
}
