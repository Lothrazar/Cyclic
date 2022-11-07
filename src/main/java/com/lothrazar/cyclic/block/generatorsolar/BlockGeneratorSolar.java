package com.lothrazar.cyclic.block.generatorsolar;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class BlockGeneratorSolar extends BlockCyclic {

  public static final VoxelShape AABB = Block.box(0, 0, 0, 16, 1, 16);
  public static IntValue ENERGY_GENERATE;
  public static IntValue TIMEOUT;

  public BlockGeneratorSolar(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
    registerDefaultState(defaultBlockState().setValue(LIT, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return Shapes.block();
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(LIT);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileGeneratorSolar(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.GENERATOR_SOLAR.get(), world.isClientSide ? TileGeneratorSolar::clientTick : TileGeneratorSolar::serverTick);
  }
}
