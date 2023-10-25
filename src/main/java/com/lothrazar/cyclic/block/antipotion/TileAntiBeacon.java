package com.lothrazar.cyclic.block.antipotion;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.beaconpotion.BeamStuff;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileAntiBeacon extends TileBlockEntityCyclic {

  public static IntValue RADIUS;
  public static IntValue TICKS;
  public static ConfigValue<List<? extends String>> POTIONS;
  public static BooleanValue HARMFUL_POTIONS;
  private BeamStuff beamStuff = new BeamStuff();

  public TileAntiBeacon(BlockPos pos, BlockState state) {
    super(TileRegistry.ANTI_BEACON.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAntiBeacon tile) {
    if (tile.isPowered()) {
      return; // redstone power = not running
    }
    //ok go
    tile.tick(level, blockPos);
    if (tile.timer <= 0) {
      BlockAntiBeacon.markNearbyEntitiesWithAntiBeaconPosition(level, blockPos);
      BlockAntiBeacon.absorbPotions(level, blockPos);
      tile.timer = TICKS.get();
    }
    else {
      tile.timer--;
    }
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileAntiBeacon e) {
    e.tick(level, blockPos);
  }

  private void tick(Level level, BlockPos blockPos) {
    updateBeam(level, blockPos, beamStuff);
  }

  @Override
  public void setLevel(Level p_155091_) {
    super.setLevel(p_155091_);
    beamStuff.lastCheckY = p_155091_.getMinBuildHeight() - 1;
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

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
  }
}
