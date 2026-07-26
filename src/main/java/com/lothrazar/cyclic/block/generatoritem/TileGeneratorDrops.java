package com.lothrazar.cyclic.block.generatoritem;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.cap.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileGeneratorDrops extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return true; // stack.isFood();
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left
  private int burnPerTick;
  private RecipeGeneratorItem currentRecipe;

  public TileGeneratorDrops(BlockPos pos, BlockState state) {
    super(TileRegistry.GENERATOR_ITEM.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileGeneratorDrops e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.flowing == 1) {
      this.exportEnergyAllSides();
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (this.burnTime == 0) {
      setLitProperty(false);
    }
    if (level.isClientSide()) {
      return;
    }
    if (this.burnTime <= 0) {
      currentRecipe = null;
      this.burnTimeMax = 0;
      this.burnTime = 0;
      //try to find a recipe now that its empty 
      this.findMatchingRecipe();
    }
    tryConsumeFuel();
  }

  private void tryConsumeFuel() {
    if (burnPerTick == 0 || this.burnTime == 0) {
      return;
    }
    //we are burning a valid recipe now
    setLitProperty(true);
    int onSim = energy.receiveEnergy(this.burnPerTick, true);
    if (onSim > 0) {
      //gen up. we burned away a tick of this fuel
      energy.receiveEnergy(this.burnPerTick, false);
      this.burnTime--;
    }
  }

  private void findMatchingRecipe() {
    RecipeInput input = new RecipeInput() {
      @Override public ItemStack getItem(int i) { return i == 0 ? inputSlots.getStackInSlot(0) : ItemStack.EMPTY; }
      @Override public int size() { return 1; }
    };
    if (currentRecipe != null && currentRecipe.matches(input, level)) {
      return;
    }
    currentRecipe = null;
    var recipes = level.getServer().getRecipeManager().recipeMap().byType(CyclicRecipeType.GENERATOR_ITEM.get());
    for (var holder : recipes) {
      RecipeGeneratorItem rec = holder.value();
      if (rec.matches(input, level)) {
        this.burnTimeMax = rec.getTicks();
        this.burnTime = this.burnTimeMax;
        this.burnPerTick = rec.getRfPertick();
        this.currentRecipe = rec;
        this.inputSlots.extractItem(0, 1, false);
        updateComparatorOutputLevel();
        return;
      }
    }
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.GENERATOR_ITEM.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGeneratorDrops(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(ValueInput input) {
          energy.deserialize(input.childOrEmpty(NBTENERGY));
    inventory.deserialize(input.childOrEmpty(NBTINV));
    burnTime = input.getIntOr("burnTime", 0);
    burnTimeMax = input.getIntOr("burnTimeMax", 0);
    burnPerTick = input.getIntOr("burnPerTick", 0);
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    energy.serialize(output.child(NBTENERGY));
    inventory.serialize(output.child(NBTINV));
    output.putInt("burnTime", this.burnTime);
    output.putInt("burnTimeMax", this.burnTimeMax);
    output.putInt("burnPerTick", this.burnPerTick);
    super.saveAdditional(output);
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
      case FLOWING:
        return this.flowing;
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
      case FLOWING:
        this.flowing = value;
      break;
    }
  }

  public int getEnergyMax() {
    return TileGeneratorDrops.MAX;
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
