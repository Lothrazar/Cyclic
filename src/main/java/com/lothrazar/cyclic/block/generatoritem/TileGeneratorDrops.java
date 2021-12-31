package com.lothrazar.cyclic.block.generatoritem;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.cyclic.capabilities.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGeneratorDrops extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return true; // stack.isFood();
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left
  private int burnPerTick;
  private RecipeGeneratorItem<?> currentRecipe;

  public TileGeneratorDrops(BlockPos pos, BlockState state) {
    super(TileRegistry.GENERATOR_ITEM.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileGeneratorDrops e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileGeneratorDrops e) {
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
    if (level.isClientSide) {
      return;
    }
    if (this.burnTime <= 0) {
      currentRecipe = null;
      this.burnTimeMax = 0;
      this.burnTime = 0;
    }
    tryConsumeFuel();
  }

  private void tryConsumeFuel() {
    //to consume fuel we first need recipe
    this.findMatchingRecipe();
    if (burnPerTick == 0 || this.burnTime == 0) {
      return;
    }
    setLitProperty(true); // has recipe so lit
    int onSim = energy.receiveEnergy(this.burnPerTick, true);
    if (onSim >= burnPerTick) {
      //gen up. we burned away a tick of this fuel 
      energy.receiveEnergy(this.burnPerTick, false);
      this.burnTime--;
    }
  }

  private void findMatchingRecipe() {
    if (currentRecipe != null && currentRecipe.matches(this, level)) {
      return;
    }
    currentRecipe = null;
    List<RecipeGeneratorItem<TileBlockEntityCyclic>> recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.GENERATOR_ITEM);
    for (RecipeGeneratorItem<?> rec : recipes) {
      if (rec.matches(this, level)) {
        this.burnTimeMax = rec.getTicks();
        this.burnTime = this.burnTimeMax;
        this.burnPerTick = rec.getRfpertick();
        this.inputSlots.extractItem(0, 1, false); // always slot 0 and amount 1
        this.currentRecipe = rec;
        return;
      }
    }
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGeneratorDrops(i, level, worldPosition, playerInventory, playerEntity);
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
  public void load(CompoundTag tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    burnTime = tag.getInt("burnTime");
    burnTimeMax = tag.getInt("burnTimeMax");
    burnPerTick = tag.getInt("burnPerTick");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("burnTime", this.burnTime);
    tag.putInt("burnTimeMax", this.burnTimeMax);
    tag.putInt("burnPerTick", this.burnPerTick);
    super.saveAdditional(tag);
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
}
