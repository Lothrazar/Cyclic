package com.lothrazar.cyclic.block.antipotion;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.render.beacon.BeamHolder;
import com.lothrazar.cyclic.render.beacon.BeamStuff;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TileAntiBeacon extends TileBlockEntityCyclic implements BeamHolder {

  public static ModConfigSpec.IntValue RADIUS;
  public static ModConfigSpec.IntValue TICKS;
  public static ModConfigSpec.ConfigValue<List<? extends String>> POTIONS;
  private final BeamStuff beamStuff = new BeamStuff();

  public TileAntiBeacon(BlockPos pos, BlockState state) {
    super(TileRegistry.BEACON_SPONGE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAntiBeacon tile) {
    if (tile.isPowered()) {
      return; // redstone power = not running
    }
    if (tile.timer <= 0) {
      BlockAntiBeacon.absorbPotions(level, blockPos);
      BlockAntiBeacon.markNearbyEntitiesWithAntiBeaconPosition(level, blockPos);
      tile.timer = TICKS.get();
    }
    else {
      tile.timer--;
    }
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileAntiBeacon e) {
    e.tickBeam(level, blockPos);
  }

  @Override
  public BeamStuff getBeamStuff() {
    return beamStuff;
  }

  @Override
  public boolean isBeamActive() {
    // anti-beacon runs while NOT redstone-powered
    return !isPowered();
  }

  @Override
  public void setLevel(Level newLevel) {
    super.setLevel(newLevel);
    beamStuff.lastCheckY = newLevel.getMinY() - 1;
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
