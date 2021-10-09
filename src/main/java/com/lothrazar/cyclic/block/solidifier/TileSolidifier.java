package com.lothrazar.cyclic.block.solidifier;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.data.Const;
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
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("rawtypes")
public class TileSolidifier extends TileEntityBase implements MenuProvider {

  public static final int TIMER_FULL = Const.TICKS_PER_SEC * 5;
  public static final int MAX = 64000;
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidAttributes.BUCKET_VOLUME / 20;
  public static IntValue POWERCONF;
  private RecipeSolidifier currentRecipe;
  FluidTankBase tank;
  ItemStackHandler inputSlots = new ItemStackHandler(3);
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private final LazyOptional<FluidTankBase> tankWrapper = LazyOptional.of(() -> tank);
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);

  static enum Fields {
    REDSTONE, TIMER, RENDER;
  }

  public TileSolidifier(BlockPos pos, BlockState state) {
    super(TileRegistry.solidifier, pos, state);
    tank = new FluidTankBase(this, CAPACITY, p -> true);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileSolidifier e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileSolidifier e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    this.findMatchingRecipe();
    if (currentRecipe == null) {
      this.timer = TIMER_FULL;
      return;
    }
    this.timer--;
    if (timer < 0) {
      timer = 0;
    }
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      return;
    }
    if (timer == 0 && this.tryProcessRecipe()) {
      this.timer = TIMER_FULL;
      energy.extractEnergy(cost, false);
    }
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return this.render;
    }
    return 0;
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerSolidifier(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void load(CompoundTag tag) {
    tank.readFromNBT(tag.getCompound(NBTFLUID));
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inputSlots.deserializeNBT(tag.getCompound(NBTINV));
    outputSlots.deserializeNBT(tag.getCompound("invoutput"));
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    CompoundTag fluid = new CompoundTag();
    tank.writeToNBT(fluid);
    tag.put(NBTFLUID, fluid);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inputSlots.serializeNBT());
    tag.put("invoutput", outputSlots.serializeNBT());
    return super.save(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return tankWrapper.cast();
    }
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  public FluidStack getFluid() {
    return tank == null ? FluidStack.EMPTY : tank.getFluid();
  }

  public float getCapacity() {
    return CAPACITY;
  }

  @Override
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  private void findMatchingRecipe() {
    if (currentRecipe != null && currentRecipe.matches(this, level)) {
      return;
    }
    currentRecipe = null;
    List<RecipeSolidifier<TileEntityBase>> recipes = level.getRecipeManager().getAllRecipesFor(CyclicRecipeType.SOLID);
    for (RecipeSolidifier rec : recipes) {
      if (rec.matches(this, level)) {
        currentRecipe = rec;
        break;
      }
    }
  }

  private boolean tryProcessRecipe() {
    FluidStack test = tank.drain(this.currentRecipe.getRecipeFluid(), FluidAction.SIMULATE);
    if (test.getAmount() >= this.currentRecipe.getRecipeFluid().getAmount()) {
      //wait is output slot compatible
      if (!outputSlots.insertItem(0, currentRecipe.getResultItem(), true).isEmpty()) {
        ModCyclic.LOGGER.info(worldPosition + "recipe stop on output is full");
        return false;
        //there was non-empty left after this, so no room for all
      }
      //ok it has room for all the fluid none will be wasted 
      inputSlots.getStackInSlot(0).shrink(1);
      inputSlots.getStackInSlot(1).shrink(1);
      inputSlots.getStackInSlot(2).shrink(1);
      tank.drain(this.currentRecipe.getRecipeFluid(), FluidAction.EXECUTE);
      outputSlots.insertItem(0, currentRecipe.getResultItem(), false);
      return true;
    }
    ModCyclic.LOGGER.info(worldPosition + " recipe stop on fluid not enoughl");
    return false;
  }

  public ItemStack getStackInputSlot(int slot) {
    return inputSlots.getStackInSlot(slot);
  }
}
