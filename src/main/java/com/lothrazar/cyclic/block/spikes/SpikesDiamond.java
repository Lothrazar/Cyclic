package com.lothrazar.cyclic.block.spikes;

import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SpikesDiamond extends SpikesBlock {

  public SpikesDiamond(Properties properties) {
    super(properties, EnumSpikeType.NONE);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileDiamondSpikes(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.SPIKES_DIAMOND.get(), world.isClientSide ? TileDiamondSpikes::clientTick : TileDiamondSpikes::serverTick);
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity && state.getValue(ACTIVATED) && worldIn instanceof ServerLevel) {
      //attck from fake player
      TileDiamondSpikes tile = (TileDiamondSpikes) worldIn.getBlockEntity(pos);
      if (tile.fakePlayer == null || tile.fakePlayer.get() == null) {
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
