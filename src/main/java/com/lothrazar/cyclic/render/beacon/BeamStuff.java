package com.lothrazar.cyclic.render.beacon;

import net.minecraft.world.level.block.entity.BeaconBeamOwner.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Per-tile state for the incremental beam scan. One instance lives on each
 * tile that implements {@link BeamHolder}. {@code beamSections} is what the
 * renderer reads; {@code checkingBeamSections} is the in-progress list being
 * built up over multiple ticks before being swapped in.
 */
public class BeamStuff {

  public List<Section> beamSections;
  public List<Section> checkingBeamSections;
  public int lastCheckY;

  public BeamStuff() {
    this(new ArrayList<>(), new ArrayList<>(), 0);
  }

  private BeamStuff(ArrayList<Section> beamSections, ArrayList<Section> checkingBeamSections, int lastCheckY) {
    this.beamSections = beamSections;
    this.checkingBeamSections = checkingBeamSections;
    this.lastCheckY = lastCheckY;
  }
}
