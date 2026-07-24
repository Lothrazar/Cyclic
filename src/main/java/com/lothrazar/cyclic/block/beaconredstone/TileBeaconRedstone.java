package com.lothrazar.cyclic.block.beaconredstone;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.render.beacon.BeamHolder;
import com.lothrazar.cyclic.render.beacon.BeamStuff;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileBeaconRedstone extends TileBlockEntityCyclic implements BeamHolder {

  private final BeamStuff beamStuff = new BeamStuff();

  public TileBeaconRedstone(BlockPos pos, BlockState state) {
    super(TileRegistry.BEACON_REDSTONE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileBeaconRedstone tile) {
    tile.tickBeam(level, blockPos);
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileBeaconRedstone e) {
    e.tickBeam(level, blockPos);
  }

  @Override
  public BeamStuff getBeamStuff() {
    return beamStuff;
  }

  @Override
  public boolean isBeamActive() {
    return isPowered();
  }

  @Override
  public void setLevel(Level newLevel) {
    super.setLevel(newLevel);
    beamStuff.lastCheckY = newLevel.getMinBuildHeight() - 1;
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
  }
}
