package com.lothrazar.cyclic.block;

import com.lothrazar.library.util.BlockstatesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BlockWaxedRedstone extends BlockCyclic {

  public BlockWaxedRedstone(Properties properties) {
    super(properties.strength(4.0F, 5.0F).sound(SoundType.METAL).isRedstoneConductor(BlockCyclic::never));
  }

  @Override
  public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    Direction op = side.getOpposite();
    return blockState.getValue(BlockStateProperties.FACING) == op ? getPower(blockAccess, pos, op) : 0;
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    Direction op = side.getOpposite();
    return blockState.getValue(BlockStateProperties.FACING) == op ? getPower(blockAccess, pos, op) : 0;
  }

  private int getPower(BlockGetter world, BlockPos pos, Direction side) {
    return 15;
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlock(pos, state.setValue(BlockStateProperties.FACING, BlockstatesUtil.getFacingFromEntity(pos, entity)), 2);
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING);
  }
}
