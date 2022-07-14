package com.lothrazar.cyclic.block.bedrock;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class UnbreakablePoweredBlock extends BlockCyclic {

  public static final BooleanProperty BREAKABLE = UnbreakableBlock.BREAKABLE;

  public UnbreakablePoweredBlock(Properties properties) {
    super(properties.strength(50.0F, 1200.0F));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new UnbreakablePoweredTile(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.UNBREAKABLE_REACTIVE.get(), world.isClientSide ? UnbreakablePoweredTile::clientTick : UnbreakablePoweredTile::serverTick);
  }

  public static void setBreakable(Level world, BlockPos pos, boolean isBreakable) {
    BlockState state = world.getBlockState(pos);
    boolean oldBreakable = state.getValue(BREAKABLE);
    if (oldBreakable != isBreakable) {
      world.setBlockAndUpdate(pos, state.setValue(BREAKABLE, isBreakable));
      if (world.isClientSide) {
        ParticleUtil.spawnParticle(world, DustParticleOptions.REDSTONE, pos, 5);
      }
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
    return (state.hasProperty(BREAKABLE) && !state.getValue(BREAKABLE)) ? 0.0F : super.getDestroyProgress(state, player, worldIn, pos);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BREAKABLE);
  }
}
