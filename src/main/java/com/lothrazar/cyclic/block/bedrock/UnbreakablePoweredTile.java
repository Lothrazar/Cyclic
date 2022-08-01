package com.lothrazar.cyclic.block.bedrock;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UnbreakablePoweredTile extends TileBlockEntityCyclic {

  public UnbreakablePoweredTile(BlockPos pos, BlockState state) {
    super(TileRegistry.UNBREAKABLE_REACTIVE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, UnbreakablePoweredTile e) {
    boolean isBreakable = !e.isPowered();
    UnbreakablePoweredBlock.setBreakable(blockState, level, blockPos, isBreakable);
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, UnbreakablePoweredTile e) {
    if (blockState.hasProperty(UnbreakablePoweredBlock.BREAKABLE) && !blockState.getValue(UnbreakablePoweredBlock.BREAKABLE)) {
      if (level.random.nextDouble() < 0.3)
        ParticleUtil.spawnParticle(level, DustParticleOptions.REDSTONE, blockPos, 5);
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
