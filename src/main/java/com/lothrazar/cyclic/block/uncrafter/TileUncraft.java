package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
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

  static final int MAX = 64000;
  public static IntValue POWERCONF;
  public static BooleanValue IGNORE_NBT;
  public static ConfigValue<List<String>> IGNORELIST;
  public static ConfigValue<Integer> TIMER;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(1 + 8 + 8);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  static enum Fields {
    REDSTONE, STATUS;
  }

  public TileUncraft() {
    super(TileRegistry.uncrafter);
  }

  @Override
  public void tick() {
    this.syncEnergy();
    ItemStack dropMe = inventory.getStackInSlot(0).copy();
    if (dropMe.isEmpty()) {
      this.status = UncraftStatusEnum.EMPTY;
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
    timer--;
    if (timer > 0) {
      return;
    }
    if (world.isRemote || world.getServer() == null) {
      return;
    }
    IRecipe<?> match = this.findMatchingRecipe(world, dropMe);
    if (match != null) {
      this.status = UncraftStatusEnum.MATCH;
      uncraftRecipe(match);
      energy.extractEnergy(cost, false);
      timer = TIMER.get();
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

  private void uncraftRecipe(IRecipe<?> match) {
    List<ItemStack> result = new ArrayList<>();
    for (Ingredient ing : match.getIngredients()) {
      if (ing.getMatchingStacks().length == 0) {
        continue;
      }
      //ok
      int index = MathHelper.nextInt(world.rand, 0, ing.getMatchingStacks().length - 1);
      index = 0;
      //non random
      result.add(ing.getMatchingStacks()[index]);
    }
    for (ItemStack r : result) {
      if (r.isEmpty()) {
        continue;
      }
      //pay cost 
      inventory.extractItem(0, match.getRecipeOutput().getCount(), false);
      //give result items 
      for (int i = 1; i < inventory.getSlots(); i++) {
        if (r.isEmpty()) {
          break;
        }
        r = inventory.insertItem(i, r.copy(), false);
      }
    }
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
  private boolean recipeMatches(ItemStack stack, IRecipe<?> recipe) {
    //    if (stack.getTag() != null && stack.getTag().keySet().size() == 1 && stack.getTag().keySet().contains(Const.NBT_REPAIR_COST)) {
    //      //what is it
    //      stack.setTag(null);
    //    }
    // do items match
    if (stack.isEmpty() ||
        recipe.getRecipeOutput().isEmpty() ||
        recipe.getRecipeOutput().getCount() > stack.getCount()) {
      this.status = UncraftStatusEnum.NORECIPE;
      return false;
    }
    //check config
    if (UtilString.isInList(TileUncraft.IGNORELIST.get(), stack.getItem().getRegistryName())) {
      ModCyclic.LOGGER.info("Uncrafter: blocked by config list " + stack);
      this.status = UncraftStatusEnum.CONFIG;
      return false;
    }
    //both itemstacks are non-empty, and we have enough quantity
    boolean matches = false;
    if (TileUncraft.IGNORE_NBT.get()) {
      ModCyclic.LOGGER.info("Uncrafter NBT ignored " + stack.getTag());
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
    }
  }
}
