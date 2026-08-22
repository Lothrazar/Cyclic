package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public class BlockMelter extends BlockCyclic {

  public BlockMelter(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
    this.setHasGui();
    this.setHasFluidInteract();
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
  @Deprecated
  public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1.0f;
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return false;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileMelter(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.MELTER.get(), world.isClientSide() ? null : TileMelter::serverTick);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndLightGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }
}
