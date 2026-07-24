package com.lothrazar.cyclic.block.dropper;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileDropper extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    TIMER, REDSTONE, DROPCOUNT, DELAY, RENDER;
  }

  static final int MAX = 64000;
  public static ModConfigSpec.IntValue POWERCONF;
  private EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
    ItemStackHandler inventory = new ItemStackHandler(1);
  ItemStackHandler gpsSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };
  private int dropCount = 1;
  private int delay = 10;

  public TileDropper(BlockPos pos, BlockState state) {
    super(TileRegistry.DROPPER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileDropper e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    timer--;
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost) {
      if (cost > 0) {
        setLitProperty(false);
        return; //out of energy so keep it rendered as off
      }
    }
    setLitProperty(true);
    if (timer > 0) {
      return;
    }
    timer = delay;
    ItemStack dropMe = inventory.getStackInSlot(0).copy();
    BlockPos target = getTargetPos();
    int amtDrop = Math.min(this.dropCount, dropMe.getCount());
    if (amtDrop > 0) {
      energy.extractEnergy(cost, false);
      dropMe.setCount(amtDrop);
      ItemStackUtil.dropItemStackMotionless(level, target, dropMe);
      inventory.getStackInSlot(0).shrink(amtDrop);
    }
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.DROPPER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerDropper(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    gpsSlots.deserializeNBT(registries,tag.getCompound(NBTINV + "gps"));
    this.delay = tag.getIntOr("delay", 0);
    this.dropCount = tag.getIntOr("dropCount", 0);
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inventory.serializeNBT(registries));
    tag.put(NBTINV + "gps", gpsSlots.serializeNBT(registries));
    tag.putInt("delay", delay);
    tag.putInt("dropCount", dropCount);
    super.saveAdditional(tag, registries);
  }

  private BlockPos getTargetPos() {
    BlockPosDim loc = LocationGpsCard.getPosition(this.gpsSlots.getStackInSlot(0));
    if (loc != null && LevelWorldUtil.dimensionIsEqual(loc, level)) {
      return loc.getPos();
    }
    return this.getCurrentFacingPos().relative(this.getCurrentFacing(), 1);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case DELAY:
        return this.delay;
      case DROPCOUNT:
        return this.dropCount;
      case RENDER:
        return render;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case DELAY:
        delay = Math.max(0, value);
      break;
      case DROPCOUNT:
        dropCount = Math.max(1, value);
      break;
      case RENDER:
        this.render = value % PreviewOutlineType.values().length;
      break;
    }
  }

  public List<BlockPos> getShapeHollow() {
    return getShape();
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<>();
    shape.add(getTargetPos());
    return shape;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
