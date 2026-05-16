package com.lothrazar.cyclic.block.antipotion;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.beaconpotion.BeamStuff;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class TileAntiBeacon extends TileBlockEntityCyclic {

  public static ModConfigSpec.IntValue RADIUS;
  public static ModConfigSpec.IntValue TICKS;
  public static ModConfigSpec.ConfigValue<List<? extends String>> POTIONS;
  public static ModConfigSpec.BooleanValue HARMFUL_POTIONS;
  private BeamStuff beamStuff = new BeamStuff();

  public TileAntiBeacon(BlockPos pos, BlockState state) {
    super(TileRegistry.ANTI_BEACON.get(), pos, state);
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
    // beam tick disabled — TileBlockEntityCyclic.updateBeam is commented out in 1.21 port,
    // matching TilePotionBeacon which also does not render its beam yet.
  }

  public List<BeaconBlockEntity.BeaconBeamSection> getBeamSections() {
    return beamStuff.beamSections;
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
