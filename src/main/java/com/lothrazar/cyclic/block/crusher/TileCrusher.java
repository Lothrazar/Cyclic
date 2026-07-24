package com.lothrazar.cyclic.block.crusher;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileCrusher extends TileBlockEntityCyclic implements MenuProvider, WorldlyContainer {

  static enum Fields {
    TIMER, REDSTONE, TIMERMAX;
  }

  static final int MAX = 64000;
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlots = new ItemStackHandler(2);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left
  private RecipeCrusher currentRecipe;

  public TileCrusher(BlockPos pos, BlockState state) {
    super(TileRegistry.CRUSHER.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCrusher e) {
    e.tick();
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.CRUSHER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCrusher(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(ValueInput input) {
          energy.deserialize(input.childOrEmpty(NBTENERGY));
    inventory.deserialize(input.childOrEmpty(NBTINV));
    this.burnTime = input.getIntOr("burnTime", 0);
    this.burnTimeMax = input.getIntOr("burnTimeMax", 0);
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    output.putInt("burnTime", burnTime);
    output.putInt("burnTimeMax", burnTimeMax);
    energy.serialize(output.child(NBTENERGY));
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (level.isClientSide()) {
      return;
    }
    if (currentRecipe == null) {
      this.findMatchingRecipe();
      return;
    }
    //Checks if there is space in the output slots before processing
    var res = currentRecipe.getResultItem(level.registryAccess()).copy();
    var max = res.getMaxStackSize();
    if (outputSlots.getStackInSlot(0).getCount() > max - res.getCount()) {
      return;
    }
    max = currentRecipe.randOutput.bonus.getMaxStackSize();
    if (outputSlots.getStackInSlot(1).getCount() > max - currentRecipe.randOutput.bonus.getCount()) {
      return;
    }
    if (this.burnTime <= 0 && this.currentRecipe != null) {
      this.burnTimeMax = 0;
      this.burnTime = 0;
      // FIRE AWAY
      if (!currentRecipe.getResultItem(level.registryAccess()).isEmpty()) {
        this.outputSlots.insertItem(0, currentRecipe.getResultItem(level.registryAccess()).copy(), false);
      }
      if (!currentRecipe.randOutput.bonus.isEmpty() && currentRecipe.randOutput.percent > 0) {
        // 1 is always, 0 is never so yeah
        //if you put 90, and i roll between 0 and 90 gj u win
        if (currentRecipe.randOutput.percent == 1 || level.getRandom().nextInt(100) < currentRecipe.randOutput.percent) {
          this.outputSlots.insertItem(1, this.currentRecipe.createBonus(level.getRandom()), false);
        }
      }
      level.levelEvent((Player) null, 1042, worldPosition, 0);
      currentRecipe = null;
    }
    // if MAX is zero, we are not currently burning 
    if (this.burnTime <= 0 && this.burnTimeMax == 0) {
      this.findMatchingRecipe();
    }
    if (this.burnTimeMax > 0) {
      setLitProperty(true); // has recipe so lit
      int onSim = energy.extractEnergy(currentRecipe.energy.getRfPertick(), true);
      if (onSim >= currentRecipe.energy.getRfPertick()) {
        //gen up. we burned away a tick of this fuel 
        energy.extractEnergy(currentRecipe.energy.getRfPertick(), false);
        this.burnTime--; // paying per tick for this recipe
      }
    }
  }

  private void findMatchingRecipe() {
    CrusherRecipeInput input = new CrusherRecipeInput(inputSlots.getStackInSlot(0));
    if (currentRecipe != null && currentRecipe.matches(input, level)) {
      return;
    }
    currentRecipe = null;
    var recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.CRUSHER.get());
    for (var holder : recipes) {
      RecipeCrusher rec = holder.value();
      if (rec.matches(input, level)) {
        this.currentRecipe = rec;
        this.burnTimeMax = this.currentRecipe.energy.getTicks();
        this.burnTime = this.burnTimeMax;
        this.inputSlots.extractItem(0, 1, false);
        return;
      }
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.burnTime;
      case TIMERMAX:
        return this.burnTimeMax;
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
      case TIMERMAX:
        this.burnTimeMax = value;
      break;
    }
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
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
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canPlaceItemThroughFace(i, itemStack, direction);
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return inventory.canTakeItemThroughFace(i, itemStack, direction);
  }
}
