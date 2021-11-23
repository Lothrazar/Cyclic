package com.lothrazar.cyclic.block.dice;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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
    super(TileRegistry.dice, pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    tag.putInt("spinningIfZero", spinningIfZero);
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    spinningIfZero = tag.getInt("spinningIfZero");
    return super.save(tag);
  }

  public void startSpinning() {
    timer = TICKS_MAX_SPINNING;
    spinningIfZero = 0;
  }

  //  @Override
  public void tick() {
    if (this.timer == 0) {
      this.spinningIfZero = 1;
      level.updateNeighbourForOutputSignal(worldPosition, this.getBlockState().getBlock());
    }
    else {
      this.timer--;
      //toggle block state
      if (this.timer % TICKS_PER_CHANGE == 0) {
        this.spinningIfZero = 0;
        Direction fac = BlockDice.getRandom(level.random);
        BlockState stateold = level.getBlockState(worldPosition);
        BlockState newstate = stateold.setValue(BlockStateProperties.FACING, fac);
        level.setBlockAndUpdate(worldPosition, newstate);
        //        world.notifyBlockUpdate(pos, stateold, newstate, 3);
      }
    }
    //
    //
    //    @Override
    //    public void update() {
    //      if (this.timer == 0) {
    //        this.spinningIfZero = 1;
    //        world.updateComparatorOutputLevel(pos, this.blockType);
    //      }
    //      else {
    //        this.timer--;
    //        //toggle block state
    //        if (this.timer % TICKS_PER_CHANGE == 0) {
    //          this.spinningIfZero = 0;
    //          EnumFacing fac = BlockDice.getRandom(world.rand);
    //          IBlockState stateold = world.getBlockState(pos);
    //          IBlockState newstate = stateold.withProperty(BlockDice.PROPERTYFACING, fac);
    //          world.setBlockState(pos, newstate);
    //          //        world.notifyBlockUpdate(pos, stateold, newstate, 3);
    //        }
    //      }
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
