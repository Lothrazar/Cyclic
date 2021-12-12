package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGeneratorFuel extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static enum Fields {
    TIMER, REDSTONE, BURNMAX, FLOWING;
  }

  static final int MAX = TileBattery.MENERGY * 10;
  public static IntValue RF_PER_TICK;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX) {
    @Override
    public boolean canReceive() {
      return false;
    }
  };
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING) > 0; //stack.getBurnTime(IRecipeType.SMELTING) >= 0;
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private final ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  final int factor = 1;
  public final Map<Direction, Boolean> neighborHasEnergyStorage = new HashMap<>();
  private final Map<Direction, IEnergyStorage> energyCache = new HashMap<>();
  private final Map<Direction, TileEntityBase> adjacentTileEntityBases = new HashMap<>();
  private int burnTimeMax = 0; //only non zero if processing
  private int burnTime = 0; //how much of current fuel is left

  public TileGeneratorFuel() {
    super(TileRegistry.GENERATOR_FUEL.get());
    this.needsRedstone = 0;
  }

  @Override
  public IEnergyStorage getEnergyStorage() {
    return energy;
  }

  @Override
  protected IEnergyStorage getAdjacentEnergyStorage(final Direction side) {
    Boolean hasEnergyStorage = neighborHasEnergyStorage.get(side);
    if (hasEnergyStorage != null && !hasEnergyStorage) {
      return null;
    }
    final IEnergyStorage adjacentEnergyStorage = energyCache.computeIfAbsent(side, k -> {
      adjacentTileEntityBases.remove(k);
      if (world == null) {
        return null;
      }
      final TileEntity tileEntity = world.getTileEntity(pos.offset(k));
      if (tileEntity == null) {
        return null;
      }
      else if (tileEntity instanceof TileEntityBase) {
        adjacentTileEntityBases.put(k, (TileEntityBase) tileEntity);
      }
      final LazyOptional<IEnergyStorage> optCap = tileEntity.getCapability(CapabilityEnergy.ENERGY, k.getOpposite());
      final IEnergyStorage storage = optCap.resolve().orElse(null);
      if (storage != null) {
        optCap.addListener((o) -> {
          adjacentTileEntityBases.remove(k);
          energyCache.remove(k);
        });
      }
      return storage;
    });
    hasEnergyStorage = adjacentEnergyStorage != null && adjacentEnergyStorage.canReceive();
    neighborHasEnergyStorage.put(side, hasEnergyStorage);
    return adjacentEnergyStorage;
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }

    if (this.flowing == 1) {
      int remainingAmount = MENERGY / 2;
      for (final Direction side : UtilDirection.getAllInDifferentOrder()) {
        final int moved = moveEnergyToAdjacent(energy, side, remainingAmount);
        if (moved <= 0) {
          continue;
        }
        remainingAmount -= moved;
        final TileEntityBase adjacentTileEntityBase = adjacentTileEntityBases.get(side);
        if (adjacentTileEntityBase != null) {
          adjacentTileEntityBase.setReceivedFrom(side.getOpposite());
        }
      }
    }

    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    //are we EMPTY
    if (this.burnTime == 0) {
      this.burnTimeMax = 0;
      //pull in new fuel
      final ItemStack stack = inputSlots.getStackInSlot(0);
      if (stack.isEmpty()) {
        return;
      }
      final int factor = 1;
      int burnTimeTicks = factor * ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING);
      if (burnTimeTicks > 0) {
        // BURN IT
        this.burnTimeMax = burnTimeTicks;
        this.burnTime = this.burnTimeMax;
        stack.shrink(1);
      }
    }

    int currentEnergy = energy.getEnergyStored();

    if (this.burnTime <= 0) {
      setLitProperty(false);
    }
    else if (currentEnergy < energy.getMaxEnergyStored()) {
      setLitProperty(true);
      this.burnTime--;
      //we have room in the tank, burn one tck and fill up
      currentEnergy += RF_PER_TICK.get();
      energy.setEnergy(currentEnergy);
    }

    if (world.getGameTime() % 20 != 0) {
      return;
    }

    if (currentEnergy != energy.energyLastSynced) {
      final PacketEnergySync packetEnergySync = new PacketEnergySync(this.getPos(), currentEnergy);
      PacketRegistry.sendToAllClients(world, packetEnergySync);
      energy.energyLastSynced = currentEnergy;
    }
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerGeneratorFuel(i, world, pos, playerInventory, playerEntity);
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
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
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
      case FLOWING:
        this.flowing = value;
      break;
    }
  }

  public int getEnergyMax() {
    return TileGeneratorFuel.MAX;
  }
}
