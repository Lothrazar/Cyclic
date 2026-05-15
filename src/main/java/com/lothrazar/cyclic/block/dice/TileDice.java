package com.lothrazar.cyclic.block.dice;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.HolderLookup;

public class TileDice extends TileBlockEntityCyclic {

  private static final int TICKS_MAX_SPINNING = 45;
  private static final int TICKS_PER_CHANGE = 4;
  private int spinningIfZero = 1;

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileDice tile) {
    tile.tick();
  }

  public static <E extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileDice e) {
    e.tick();
  }

  static enum Fields {
    TIMER, SPINNING;
  }

  public TileDice(BlockPos pos, BlockState state) {
    super(TileRegistry.DICE.get(), pos, state);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    spinningIfZero = tag.getInt("spinningIfZero");
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("spinningIfZero", spinningIfZero);
    super.saveAdditional(tag, registries);
  }

  public void startSpinning() {
    timer = TICKS_MAX_SPINNING;
    spinningIfZero = 0;
  }

  //  @Override
  public void tick() {
    if (this.timer == 0) {
      this.spinningIfZero = 1;
      updateComparatorOutputLevel();
    }
    else {
      this.timer--;
      //toggle block state
      if (this.timer % TICKS_PER_CHANGE == 0) {
        this.spinningIfZero = 0;
        Direction fac = LevelWorldUtil.getRandomDirection(level.random);
        BlockState stateold = level.getBlockState(worldPosition);
        BlockState newstate = stateold.setValue(BlockStateProperties.FACING, fac);
        level.setBlockAndUpdate(worldPosition, newstate);
        //        world.notifyBlockUpdate(pos, stateold, newstate, 3);
      }
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case SPINNING:
        return this.spinningIfZero;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case SPINNING:
        spinningIfZero = value;
      break;
    }
  }
}
