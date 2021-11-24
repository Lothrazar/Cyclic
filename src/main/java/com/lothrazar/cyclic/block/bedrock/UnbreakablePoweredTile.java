package com.lothrazar.cyclic.block.bedrock;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UnbreakablePoweredTile extends TileBlockEntityCyclic {

  public UnbreakablePoweredTile(BlockPos pos, BlockState state) {
    super(TileRegistry.UNBREAKABLE_REACTIVE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, UnbreakablePoweredTile e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, UnbreakablePoweredTile e) {
    e.tick();
  }

  //  @Override
  public void tick() {
    boolean isBreakable = !this.isPowered();
    UnbreakablePoweredBlock.setBreakable(level, worldPosition, isBreakable);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
