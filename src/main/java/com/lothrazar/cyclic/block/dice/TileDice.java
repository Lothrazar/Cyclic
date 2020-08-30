package com.lothrazar.cyclic.block.dice;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

public class TileDice extends TileEntityBase implements ITickableTileEntity {

  private static final int TICKS_MAX_SPINNING = 45;
  private static final int TICKS_PER_CHANGE = 4;
  private int spinningIfZero = 1;

  public static enum Fields {
    TIMER, SPINNING;
  }

  public TileDice() {
    super(BlockRegistry.TileRegistry.dice);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    tag.putInt("spinningIfZero", spinningIfZero);
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    spinningIfZero = tag.getInt("spinningIfZero");
    return super.write(tag);
  }

  public void startSpinning() {
    timer = TICKS_MAX_SPINNING;
    spinningIfZero = 0;
  }

  @Override
  public void tick() {
    if (this.timer == 0) {
      this.spinningIfZero = 1;
      world.updateComparatorOutputLevel(pos, this.getBlockState().getBlock());
    }
    else {
      this.timer--;
      //toggle block state
      if (this.timer % TICKS_PER_CHANGE == 0) {
        this.spinningIfZero = 0;
        Direction fac = BlockDice.getRandom(world.rand);
        BlockState stateold = world.getBlockState(pos);
        BlockState newstate = stateold.with(BlockStateProperties.FACING, fac);
        world.setBlockState(pos, newstate);
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
