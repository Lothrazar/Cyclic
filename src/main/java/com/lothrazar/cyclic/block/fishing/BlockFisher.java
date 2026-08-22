package com.lothrazar.cyclic.block.fishing;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class BlockFisher extends BlockCyclic {

  public BlockFisher(Properties properties) {
    super(properties.strength(1.8F).noOcclusion());
    this.setHasGui();
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState bs) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState st, Level level, BlockPos pos, Direction direction) {
    return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndLightGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileFisher(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.FISHER.get(), world.isClientSide() ? null : TileFisher::serverTick);
  }
}
