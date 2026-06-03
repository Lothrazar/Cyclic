package com.lothrazar.cyclic.render.beacon;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.block.entity.BeaconBlockEntity.BeaconBeamSection;

/**
 * Per-tile state for the incremental beam scan. One instance lives on each
 * tile that implements {@link BeamHolder}. {@code beamSections} is what the
 * renderer reads; {@code checkingBeamSections} is the in-progress list being
 * built up over multiple ticks before being swapped in.
 */
public class BeamStuff {

  public List<BeaconBeamSection> beamSections;
  public List<BeaconBeamSection> checkingBeamSections;
  public int lastCheckY;

  public BeamStuff() {
    this(new ArrayList<>(), new ArrayList<>(), 0);
  }

  private BeamStuff(ArrayList<BeaconBeamSection> beamSections, ArrayList<BeaconBeamSection> checkingBeamSections, int lastCheckY) {
    this.beamSections = beamSections;
    this.checkingBeamSections = checkingBeamSections;
    this.lastCheckY = lastCheckY;
  }
}
