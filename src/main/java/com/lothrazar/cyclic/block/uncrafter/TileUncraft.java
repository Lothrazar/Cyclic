package com.lothrazar.cyclic.block.uncrafter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileUncraft extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, STATUS, TIMER;
  }

  static final int MAX = 64000;
  public static ModConfigSpec.IntValue POWERCONF;
  public static ModConfigSpec.BooleanValue NBT_IGNORED;
  public static ModConfigSpec.ConfigValue<Integer> TIMER;
  public static  ModConfigSpec.ConfigValue<List<? extends String>> IGNORE_LIST;
  public static  ModConfigSpec.ConfigValue<List<? extends String>> IGNORE_RECIPES;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    protected void onContentsChanged(int slot) {
      TileUncraft.this.status = UncraftStatusEnum.EMPTY;
    };
  };
  ItemStackHandler outputSlots = new ItemStackHandler(8 * 2) {

    @Override
    protected void onContentsChanged(int slot) {
      if (TileUncraft.this.status == UncraftStatusEnum.NOROOM) {
        TileUncraft.this.status = UncraftStatusEnum.EMPTY;
      }
    };
  };
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);

  public TileUncraft(BlockPos pos, BlockState state) {
    super(TileRegistry.UNCRAFTER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileUncraft e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileUncraft e) {
    e.tick();
  }

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
    if (this.status != UncraftStatusEnum.EMPTY && this.status != UncraftStatusEnum.MATCH) {
      this.timer = TIMER.get();
      return;
    }
    if (--timer > 0) {
      return;
    }
    timer = TIMER.get();
    if (level.isClientSide || level.getServer() == null) {
      return;
    }
    RecipeHolder<?> match = this.findMatchingRecipe(level, dropMe);
    if (match != null) {
      var status = uncraftRecipe(match.value());
      this.status = status;
      if (status == UncraftStatusEnum.MATCH) {
        //pay cost
        // ModCyclic.LOGGER.info("before extract cost" + inputSlots.getStackInSlot(0));
        inputSlots.extractItem(0, match.value().getResultItem(level.registryAccess()).getCount(), false);
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
  public Component getDisplayName() {
    return BlockRegistry.UNCRAFTER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerUncraft(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    energy.deserializeNBT(registries,tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    this.status = UncraftStatusEnum.values()[tag.getInt("ucstats")];
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
    tag.putInt("ucstats", status.ordinal());
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag, registries);
  }

  private UncraftStatusEnum uncraftRecipe(Recipe<?> match) {
    List<ItemStack> result = match.getIngredients().stream().flatMap(ingredient -> Arrays.stream(ingredient.getItems())
        .filter(stack -> !stack.hasCraftingRemainingItem())
        .findAny()
        .map(Stream::of)
        .orElseGet(Stream::empty))
        .collect(Collectors.toList());
    if (result.isEmpty()) {
      return UncraftStatusEnum.NORECIPE;
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
        return UncraftStatusEnum.NOROOM;
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
    return UncraftStatusEnum.MATCH;
  }

  public RecipeHolder<?> findMatchingRecipe(Level world, ItemStack dropMe) {
    Collection<RecipeHolder<?>> list = world.getServer().getRecipeManager().getRecipes();
    for (RecipeHolder<?> recipe : list) {

      if (recipe.value().getType() == RecipeType.CRAFTING) {
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
  private boolean recipeMatches(ItemStack stack, RecipeHolder<?> recipe) {
    if (recipe == null) {
      return false;
    }
    var recipeResultItem = recipe.value().getResultItem(level.registryAccess());
    if (recipeResultItem.isEmpty() ||
        recipeResultItem.getItem() != stack.getItem() ||
        recipeResultItem.getCount() > stack.getCount()) {
      return false;
    }
    //check config
    List<String> recipes = (List<String>) TileUncraft.IGNORE_RECIPES.get();
    if (StringParseUtil.isInList(recipes, recipe.id())) {
      //check the RECIPE id list
      return false;
    }
    ResourceLocation stackKey = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem());
    if (StringParseUtil.isInList((List<String>) TileUncraft.IGNORE_LIST.get(), stackKey)) {
      //checked the ITEM id list
      return false;
    }
    //both itemstacks are non-empty, and we have enough quantity
    if (TileUncraft.NBT_IGNORED.get()) {
      return true;
    }
    else {
      return ItemStack.isSameItemSameComponents(stack, recipeResultItem);
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
