package com.lothrazar.cyclic.render.beacon;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBeamOwner.Section;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Tile-side contract for the Cyclic beacon family
 * (potion beacon, anti beacon, redstone beacon).
 *
 * Implementers own a {@link BeamStuff} and decide when the beam is "active"
 * (lit by power, redstone, GUI state, etc.). Renderers should target
 * {@code RenderBeaconBase<T extends BlockEntity &amp; BeamHolder>} instead of
 * reimplementing the beam loop per tile.
 */
public interface BeamHolder {

  BeamStuff getBeamStuff();

  /**
   * @return true if the beam should currently render/tick. Tile-specific:
   *         redstone power, LIT block-state property, configured timer, etc.
   */
  boolean isBeamActive();

  default List<Section> getBeamSections() {
    return getBeamStuff().beamSections;
  }

  /**
   * Convenience wrapper for the per-tick beam scan. Tiles call this from
   * their server/client tick instead of poking {@link #updateBeam} directly.
   */
  default void tickBeam(Level level, BlockPos pos) {
    updateBeam(level, pos, getBeamStuff());
  }

  /**
   * Walks the column above the beacon a few blocks per tick and builds beam
   * sections, then swaps them into the public list each time the scan reaches
   * the world surface. Ported from {@code BeaconBlockEntity.tick} in 1.21:
   *
   *  - color blocks now implement {@link BeaconBeamBlock} (vanilla's
   *    {@code BlockState#getBeaconColorMultiplier} was removed),
   *  - {@link Section} stores ARGB ints instead
   *    of {@code float[3]}; mixing uses {@link FastColor.ARGB32#average}.
   *
   * For the beam to form without requiring stained glass on top, the beacon
   * block itself implements {@link BeaconBeamBlock} (returning white).
   */
  static void updateBeam(Level level, BlockPos pos, BeamStuff beamStuff) {
    BlockPos blockpos;
    if (beamStuff.lastCheckY < pos.getY()) {
      blockpos = pos;
      beamStuff.checkingBeamSections = new ArrayList<>();
      beamStuff.lastCheckY = pos.getY() - 1;
    }
    else {
      blockpos = new BlockPos(pos.getX(), beamStuff.lastCheckY + 1, pos.getZ());
    }
    Section currentSection = beamStuff.checkingBeamSections.isEmpty()
        ? null
        : beamStuff.checkingBeamSections.get(beamStuff.checkingBeamSections.size() - 1);
    int surfaceHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
    for (int yLoop = 0; yLoop < 10 && blockpos.getY() <= surfaceHeight; ++yLoop) {
      BlockState blockstate = level.getBlockState(blockpos);
      if (blockstate.getBlock() instanceof BeaconBeamBlock beamBlock) {
        int beamColor = beamBlock.getColor().getTextureDiffuseColor();
        if (beamStuff.checkingBeamSections.size() <= 1) {
          currentSection = new Section(beamColor);
          beamStuff.checkingBeamSections.add(currentSection);
        }
        else if (currentSection != null) {
          if (beamColor == currentSection.getColor()) {
            currentSection.increaseHeight();
          }
          else {
            int blended = FastColor.ARGB32.average(currentSection.getColor(), beamColor);
            currentSection = new Section(blended);
            beamStuff.checkingBeamSections.add(currentSection);
          }
        }
      }
      else {
        if (currentSection == null
            || (blockstate.getLightBlock(level, blockpos) >= 15 && !blockstate.is(Blocks.BEDROCK))) {
          beamStuff.checkingBeamSections.clear();
          beamStuff.lastCheckY = surfaceHeight;
          break;
        }
        currentSection.increaseHeight();
      }
      blockpos = blockpos.above();
      ++beamStuff.lastCheckY;
    }
    if (level.getGameTime() % 80L == 0L) {
      if (!beamStuff.beamSections.isEmpty()) {
        SoundUtil.playSound(level, pos, SoundEvents.BEACON_AMBIENT);
      }
    }
    if (beamStuff.lastCheckY >= surfaceHeight) {
      beamStuff.lastCheckY = level.getMinBuildHeight() - 1;
      beamStuff.beamSections = beamStuff.checkingBeamSections;
    }
  }
}
