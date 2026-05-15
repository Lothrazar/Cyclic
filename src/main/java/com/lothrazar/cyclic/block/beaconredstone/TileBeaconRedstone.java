package com.lothrazar.cyclic.block.beaconredstone;

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
import net.minecraft.core.HolderLookup;

public class TileBeaconRedstone extends TileBlockEntityCyclic {

  private BeamStuff beamStuff = new BeamStuff();

  public TileBeaconRedstone(BlockPos pos, BlockState state) {
    super(TileRegistry.BEACON_REDSTONE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileBeaconRedstone tile) {
    tile.tick(level, blockPos);
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileBeaconRedstone e) {
    e.tick(level, blockPos);
  }

  private void tick(Level level, BlockPos blockPos) {
    // updateBeam(level, blockPos, beamStuff);
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
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
  }
}
