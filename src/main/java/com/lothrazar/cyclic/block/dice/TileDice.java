package com.lothrazar.cyclic.block.dice;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.HolderLookup;

public class TileDice extends TileBlockEntityCyclic {

  private static final int TICKS_MAX_SPINNING = 45;
  private static final int TICKS_PER_CHANGE = 4;
  private int spinningIfZero = 1;

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
  public void loadAdditional(ValueInput input) {
    spinningIfZero = input.getIntOr("spinningIfZero", 0);
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    output.putInt("spinningIfZero", spinningIfZero);
    super.saveAdditional(output);
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
        Direction fac = LevelWorldUtil.getRandomDirection(level.getRandom());
        BlockState stateold = level.getBlockState(worldPosition);
        BlockState newstate = stateold.setValue(BlockStateProperties.FACING, fac);
        level.setBlockAndUpdate(worldPosition, newstate);
      }
    }
  }

  public int getComparatorSignal() {
    if (spinningIfZero == 0) {
      return 0;
    }
    Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
    int face = switch (facing) {
      case NORTH -> 1;
      case SOUTH -> 2;
      case UP -> 3;
      case DOWN -> 4;
      case WEST -> 5;
      case EAST -> 6;
    };
    return face * 2;
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
