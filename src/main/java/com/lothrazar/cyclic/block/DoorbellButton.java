package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.registry.SoundRegistry;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class DoorbellButton extends AbstractButtonBlock {

  private static final SoundEvent SOUND = SoundRegistry.DOORBELL_MIKEKOENIG;
  public static final int LIGHTLVL = 4;
  public static final int POWERLVL = 1;

  public DoorbellButton(Properties properties) {
    super(false, properties.hardnessAndResistance(0.5F).setLightLevel(s -> s.get(POWERED) ? LIGHTLVL : 0));
  }

  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(POWERED) ? POWERLVL : 0;
  }

  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(POWERED) && getFacing(blockState) == side ? POWERLVL : 0;
  }

  @Override
  public boolean canProvidePower(BlockState state) {
    return true;
  }

  @Override
  protected SoundEvent getSoundEvent(boolean isOn) {
    return isOn ? SOUND : null;
  }
}
