package com.lothrazar.cyclic.block.uncrafter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
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
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileUncraft extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static final int MAX = 64000;
  public static IntValue POWERCONF;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  static enum Fields {
    REDSTONE, STATUS;
  }

  public TileUncraft() {
    super(TileRegistry.uncrafter);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    timer--;
    if (timer > 0) {
      return;
    }
    IEnergyStorage en = this.energy.orElse(null);
    IItemHandler inv = this.inventory.orElse(null);
    if (en == null || inv == null || world.getServer() == null) {
      this.status = UncraftStatusEnum.EMPTY;
      return;
    }
    final int cost = POWERCONF.get();
    if (en.getEnergyStored() < cost && cost > 0) {
      return;//broke
    }
    //    int RADIUS = 2;
    //
    ItemStack dropMe = inv.getStackInSlot(0).copy();
    if (dropMe.isEmpty()) {
      this.status = UncraftStatusEnum.EMPTY;
    }
    else {
      setLitProperty(true);
      IRecipe<?> match = this.findMatchingRecipe(world, dropMe);
      if (match != null) {
        this.status = UncraftStatusEnum.MATCH;
        uncraftRecipe(inv, match);
        en.extractEnergy(cost, false);
      }
      else {
        this.status = UncraftStatusEnum.CANT;
      }
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

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerUncraft(i, world, pos, playerInventory, playerEntity);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1 + 8 + 8);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    this.status = UncraftStatusEnum.values()[tag.getInt("ucstats")];
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("ucstats", status.ordinal());
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  private void uncraftRecipe(IItemHandler inv, IRecipe<?> match) {
    List<ItemStack> result = new ArrayList<>();
    for (Ingredient ing : match.getIngredients()) {
      if (ing.getMatchingStacks().length == 0) {
        continue;
      }
      //ok
      int index = MathHelper.nextInt(world.rand, 0, ing.getMatchingStacks().length - 1);
      index = 0;//non random
      result.add(ing.getMatchingStacks()[index]);
    }
    for (ItemStack r : result) {
      if (r.isEmpty()) {
        continue;
      }
      //pay cost 
      inv.extractItem(0, match.getRecipeOutput().getCount(), false);
      //give result items 
      for (int i = 1; i < inv.getSlots(); i++) {
        if (r.isEmpty()) {
          break;
        }
        r = inv.insertItem(i, r.copy(), false);
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
    if (!dropMe.isEmpty())
      ModCyclic.LOGGER.info("No recipe found " + dropMe);
    return null;
  }

  // matches count and has enough
  public static boolean recipeMatches(ItemStack stack, IRecipe<?> recipe) {
    // do items match
    if (stack.isEmpty() ||
        stack.getItem() != recipe.getRecipeOutput().getItem()) {
      return false;
    }
    //data tags match, and we have enough quantity 
    return ItemStack.areItemStackTagsEqual(stack, recipe.getRecipeOutput())
        && recipe.getRecipeOutput().getCount() <= stack.getCount();
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
