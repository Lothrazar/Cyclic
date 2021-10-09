package com.lothrazar.cyclic.block.clock;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileRedstoneClock extends TileEntityBase implements MenuProvider {

  static enum Fields {
    TIMER, DELAY, DURATION, POWER, REDSTONE, N, E, S, W, U, D;
  }

  private int delay; //dont let these times be zero !!!
  private int duration;
  private int power;
  private Map<Direction, Boolean> poweredSides = new HashMap<Direction, Boolean>();

  public TileRedstoneClock(BlockPos pos, BlockState state) {
    super(TileRegistry.clock, pos, state);
    timer = 0;
    delay = 60;
    duration = 60;
    power = 15;
    needsRedstone = 0;
    this.facingResetAllOn();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileRedstoneClock e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileRedstoneClock e) {
    //    e.tick();
  }

  public void tick() {
    try {
      updateMyState();
    }
    catch (Throwable e) {
      ModCyclic.LOGGER.error("Clock blockstate update error", e);
    }
  }

  private void facingResetAllOn() {
    for (Direction f : Direction.values()) {
      poweredSides.put(f, true);
    }
  }

  public int getPower() {
    return this.power;
  }

  public int getPowerForSide(Direction side) {
    if (this.getSideHasPower(side)) {
      return this.power;
    }
    else {
      return 0;
    }
  }

  public boolean getSideHasPower(Direction side) {
    return this.poweredSides.get(side);
  }

  public int getSideField(Direction side) {
    return this.getSideHasPower(side) ? 1 : 0;
  }

  public void setSideField(Direction side, int pow) {
    this.poweredSides.put(side, (pow == 1));
  }

  private boolean detectAllOff() {
    boolean areAnyOn = false;
    for (Direction f : Direction.values()) {
      areAnyOn = areAnyOn || poweredSides.get(f);
    }
    return !areAnyOn;
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerClock(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void load(CompoundTag tag) {
    delay = tag.getInt("redstone_delay");
    duration = tag.getInt("redstone_duration");
    power = tag.getInt("redstone_power");
    for (Direction f : Direction.values()) {
      poweredSides.put(f, tag.getBoolean(f.getName()));
    }
    if (this.detectAllOff()) {
      this.facingResetAllOn(); //fix legacy data for one
    }
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.putInt("redstone_delay", delay);
    tag.putInt("redstone_duration", duration);
    tag.putInt("redstone_power", power);
    for (Direction f : Direction.values()) {
      tag.putBoolean(f.getName(), poweredSides.get(f));
    }
    return super.save(tag);
  }

  private void updateMyState() throws IllegalArgumentException {
    BlockState blockState = level.getBlockState(worldPosition);
    if (blockState.hasProperty(BlockRedstoneClock.LIT) == false) {
      return;
    }
    if (this.power == 0) {
      level.setBlockAndUpdate(worldPosition, blockState.setValue(BlockRedstoneClock.LIT, false));
      return;
    }
    this.timer++;
    boolean powered;
    boolean prevPowered = blockState.getValue(BlockRedstoneClock.LIT);
    if (timer < delay) {
      powered = false;
    }
    else if (timer < delay + duration) {
      //we are in the ON section
      powered = true;
    }
    else {
      timer = 0;
      powered = false;
    }
    if (prevPowered != powered) {
      level.setBlockAndUpdate(worldPosition, blockState.setValue(BlockRedstoneClock.LIT, powered));
      //super weird hotfix for down state not updating
      //all other directions read update, but not down apparently!
      //      world.notifyNeighborsOfStateChange(pos.down(), world.getBlockState(pos.down()).getBlock(), true);
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case POWER:
        return power;
      case TIMER:
        return timer;
      case DELAY:
        return delay;
      case DURATION:
        return duration;
      case REDSTONE:
        return this.needsRedstone;
      case D:
        return this.getSideField(Direction.DOWN);
      case E:
        return this.getSideField(Direction.EAST);
      case N:
        return this.getSideField(Direction.NORTH);
      case S:
        return this.getSideField(Direction.SOUTH);
      case U:
        return this.getSideField(Direction.UP);
      case W:
        return this.getSideField(Direction.WEST);
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case POWER:
        if (value < 0) {
          value = 0;
        }
        if (value > 15) {
          value = 15;
        }
        power = value;
        break;
      case TIMER:
        timer = value;
        break;
      case DELAY:
        delay = Math.max(value, 1);
        break;
      case DURATION:
        duration = Math.max(value, 1);
        break;
      case REDSTONE:
        this.needsRedstone = value % 2;
        break;
      case D:
        this.setSideField(Direction.DOWN, value % 2);
        break;
      case E:
        this.setSideField(Direction.EAST, value % 2);
        break;
      case N:
        this.setSideField(Direction.NORTH, value % 2);
        break;
      case S:
        this.setSideField(Direction.SOUTH, value % 2);
        break;
      case U:
        this.setSideField(Direction.UP, value % 2);
        break;
      case W:
        this.setSideField(Direction.WEST, value % 2);
        break;
    }
  }
}
