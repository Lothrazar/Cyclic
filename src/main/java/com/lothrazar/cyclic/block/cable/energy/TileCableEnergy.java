package com.lothrazar.cyclic.block.cable.energy;

import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TileCableEnergy extends TileCableBase {

  public static ModConfigSpec.IntValue BUFFERSIZE;
  public static ModConfigSpec.IntValue TRANSFER_RATE;

  public TileCableEnergy(BlockPos pos, BlockState state) {
    super(TileRegistry.ENERGY_PIPE.get(), pos, state);
    setIsEnergy();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableEnergy e) {
    e.tickEnergy();
  }


}
