package com.lothrazar.cyclic.block.dice;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.tileentity.ITickableTileEntity;

public class TileDice extends TileEntityBase implements ITickableTileEntity {

  private static final int TICKS_MAX_SPINNING = 45;
  private static final int TICKS_PER_CHANGE = 4;
  private static final String NBT_PART = "is_spinning";
  private int spinningIfZero = 1;

  public static enum Fields {
    TIMER, SPINNING;
  }

  public TileDice() {
    super(BlockRegistry.TileRegistry.dice);
  }

  @Override
  public void tick() {
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
