package com.lothrazar.cyclic.block.eye;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileEye extends TileEntityBase {

  public static IntValue RANGE;
  public static IntValue FREQUENCY;

  public TileEye(BlockPos pos, BlockState state) {
    super(TileRegistry.eye_redstone, pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileEye e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileEye e) {
    e.tick();
  }

  public void tick() {
    if (level.isClientSide) {
      return;
    }
    timer--;
    if (timer > 0) {
      return;
    }
    timer = FREQUENCY.get();
    //
    boolean playerFound = getLookingPlayer(RANGE.get(), false) != null;
    this.setLitProperty(playerFound);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
