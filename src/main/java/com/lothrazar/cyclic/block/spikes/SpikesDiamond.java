package com.lothrazar.cyclic.block.spikes;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SpikesDiamond extends SpikesBlock {

  public SpikesDiamond(Properties properties) {
    super(properties, EnumSpikeType.NONE);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileDiamondSpikes();
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity && state.getValue(ACTIVATED) && worldIn instanceof ServerLevel) {
      //attck from fake player
      TileDiamondSpikes tile = (TileDiamondSpikes) worldIn.getBlockEntity(pos);
      if (tile.fakePlayer == null || tile.fakePlayer.get() == null) {
        ModCyclic.LOGGER.error("null player sup +" + tile, tile.fakePlayer);
        return;
      }
      if (tile.getTimer() == 1) {
        //public net.minecraft.entity.LivingEntity attackStrengthTicker # ticksSinceLastSwing
        tile.fakePlayer.get().attackStrengthTicker = (int) tile.fakePlayer.get().getCurrentItemAttackStrengthDelay();
        tile.fakePlayer.get().attack(entity);
        tile.fakePlayer.get().resetAttackStrengthTicker();
      }
    }
  }
}
