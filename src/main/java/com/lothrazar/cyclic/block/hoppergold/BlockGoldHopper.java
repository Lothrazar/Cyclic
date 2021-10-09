package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.block.hopper.BlockSimpleHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockGoldHopper extends BlockSimpleHopper {

  public BlockGoldHopper(Properties properties) {
    super(properties.strength(1.3F));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state ) {
    return new TileGoldHopper(pos,state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.HOPPERGOLD.get(), world.isClientSide ? TileGoldHopper::clientTick : TileGoldHopper::serverTick);
  }
}
