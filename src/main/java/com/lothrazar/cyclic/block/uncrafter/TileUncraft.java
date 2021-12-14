package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemHandler;
import com.lothrazar.cyclic.util.UtilString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import net.minecraft.world.server.ServerWorld;
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

  enum Fields {
    REDSTONE, STATUS, TIMER;
  }

  static final int MAX = 64000;
  public static IntValue POWERCONF;
  public static BooleanValue IGNORE_NBT;
  public static ConfigValue<Integer> TIMER;
  public static ConfigValue<List<? extends String>> IGNORELIST;
  public static ConfigValue<List<? extends String>> IGNORELIST_RECIPES;
  public final CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  public final ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      if (world == null || world.isRemote) {
        return;
      }
      final ItemStack itemStack = stacks.get(slot);
      if (itemStack.isEmpty()) {
        currentRecipe = null;
        status = UncraftStatusEnum.EMPTY;
        timer = TIMER.get();
      }
      else {
        currentRecipe = getNonIgnoredRecipe(itemStack);
        if (currentRecipe == null) {
          if (getValidRecipe(itemStack) == null) {
            status = UncraftStatusEnum.NORECIPE;
          }
          else {
            status = UncraftStatusEnum.CONFIG;
          }
        }
        else {
          status = UncraftStatusEnum.MATCH;
        }
      }
    }
  };
  public final ItemStackHandler outputSlots = new ItemStackHandler(8 * 2);
  private final ItemStackHandler outputBuffer = new ItemStackHandler(9);
  private final ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private ICraftingRecipe currentRecipe = null;
  private static final Map<Item, List<ICraftingRecipe>> recipeCache = new HashMap<>();
  private Boolean isLit = null;

  public TileUncraft() {
    super(TileRegistry.uncrafter);
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }

    if (world.getGameTime() % 20 == 0) {
      final int currentEnergy = energy.getEnergyStored();
      if (currentEnergy != energy.energyLastSynced) {
        final PacketEnergySync packetEnergySync = new PacketEnergySync(this.getPos(), currentEnergy);
        PacketRegistry.sendToAllClients(world, packetEnergySync);
        energy.energyLastSynced = currentEnergy;
      }
    }

    final boolean lit = !(this.requiresRedstone() && !this.isPowered());
    if (isLit == null || isLit != lit) {
      setLitProperty(lit);
      isLit = lit;
    }

    if (!lit) {
      return;
    }

    //empty output buffer
    for (int outputBufferSlot = 0; outputBufferSlot < outputBuffer.getSlots(); outputBufferSlot++) {
      UtilItemHandler.moveItems(outputBuffer, outputBufferSlot, outputSlots, Integer.MAX_VALUE);
    }
    final boolean outputBufferEmpty = isOutputBufferEmpty();
    if (!outputBufferEmpty) {
      status = UncraftStatusEnum.NOROOM;
      return;
    }

    final ItemStack itemStack = inputSlots.getStackInSlot(0);
    if (itemStack.isEmpty()) {
      status = UncraftStatusEnum.EMPTY;
      currentRecipe = null;
      return;
    }
    else if (status != UncraftStatusEnum.NORECIPE && currentRecipe == null) {
      currentRecipe = getNonIgnoredRecipe(itemStack);
    }

    if (currentRecipe == null) {
      if (getValidRecipe(itemStack) == null) {
        status = UncraftStatusEnum.NORECIPE;
      }
      else {
        status = UncraftStatusEnum.CONFIG;
      }
      return;
    }
    else {
      status = UncraftStatusEnum.MATCH;
    }

    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost) {
      return;
    }

    if (timer > 0) {
      timer--;
    }
    else if (isOutputBufferEmpty()) {
      uncraft();
      inputSlots.extractItem(0, currentRecipe.getRecipeOutput().getCount(), false);
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
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    status = UncraftStatusEnum.values()[tag.getInt("ucstats")];
    outputBuffer.deserializeNBT(tag.getCompound("outputbuffer"));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("ucstats", status.ordinal());
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    tag.put("outputbuffer", outputBuffer.serializeNBT());
    return super.write(tag);
  }

  private List<ICraftingRecipe> getAllRecipes(final RecipeManager recipeManager, final ItemStack itemStack) {
    return recipeCache.computeIfAbsent(itemStack.getItem(), item -> {
      final List<ICraftingRecipe> result = new ArrayList<>();
      for (final ICraftingRecipe recipe : recipeManager.getRecipesForType(IRecipeType.CRAFTING)) {
        final ItemStack recipeOutput = recipe.getRecipeOutput();
        if (!itemStack.isItemEqual(recipeOutput)) {
          continue;
        }
        result.add(recipe);
      }
      return result;
    });
  }

  private List<ICraftingRecipe> getValidRecipes(final RecipeManager recipeManager, final ItemStack itemStack) {
    final List<ICraftingRecipe> result = new ArrayList<>();
    for (final ICraftingRecipe recipe : getAllRecipes(recipeManager, itemStack)) {
      //check for adequate quantity
      final ItemStack recipeOutput = recipe.getRecipeOutput();
      if (recipeOutput.getCount() > itemStack.getCount()) {
        continue;
      }

      result.add(recipe);
    }
    return result;
  }

  private ICraftingRecipe getValidRecipe(final ItemStack itemStack) {
    if (!(world instanceof ServerWorld)) {
      return null;
    }
    final ServerWorld serverWorld = (ServerWorld) world;
    final List<ICraftingRecipe> recipes = getValidRecipes(serverWorld.getRecipeManager(), itemStack);
    if (recipes.isEmpty()) {
      return null;
    }
    return recipes.get(serverWorld.getRandom().nextInt(recipes.size()));
  }

  private List<ICraftingRecipe> getNonIgnoredRecipes(final RecipeManager recipeManager, final ItemStack itemStack) {
    final List<? extends String> recipesToIgnore = TileUncraft.IGNORELIST_RECIPES.get();
    final List<? extends String> recipeOutputsToIgnore = TileUncraft.IGNORELIST.get();
    final List<ICraftingRecipe> result = new ArrayList<>();
    for (final ICraftingRecipe recipe : getValidRecipes(recipeManager, itemStack)) {
      //honour recipe ignore list
      if (UtilString.isInList(recipesToIgnore, recipe.getId())) {
        continue;
      }

      //honour recipe output ignore list
      final ItemStack recipeOutput = recipe.getRecipeOutput();
      if (UtilString.isInList(recipeOutputsToIgnore, recipeOutput.getItem().getRegistryName())) {
        continue;
      }

      //check NBT equality unless ignoring
      if (TileUncraft.IGNORE_NBT.get() && !ItemStack.areItemStackTagsEqual(itemStack, recipeOutput)) {
        continue;
      }

      result.add(recipe);
    }
    return result;
  }

  private ICraftingRecipe getNonIgnoredRecipe(final ItemStack itemStack) {
    if (!(world instanceof ServerWorld)) {
      return null;
    }
    final ServerWorld serverWorld = (ServerWorld) world;
    final List<ICraftingRecipe> recipes = getNonIgnoredRecipes(serverWorld.getRecipeManager(), itemStack);
    if (recipes.isEmpty()) {
      return null;
    }
    return recipes.get(serverWorld.getRandom().nextInt(recipes.size()));
  }

  private boolean isOutputBufferEmpty() {
    for (int outputBufferSlot = 0; outputBufferSlot < outputBuffer.getSlots(); outputBufferSlot++) {
      if (!outputBuffer.getStackInSlot(outputBufferSlot).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private void uncraft() {
    final List<Ingredient> ingredients = currentRecipe.getIngredients();
    for (int i = 0; i < ingredients.size(); i++) {
      final Ingredient ingredient = ingredients.get(i);
      for (final ItemStack itemStack : ingredient.getMatchingStacks()) {
        if (itemStack.hasContainerItem()) {
          continue;
        }
        outputBuffer.insertItem(i, itemStack, false);
        break;
      }
    }
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
