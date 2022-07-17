package com.lothrazar.cyclic.block.beaconpotion;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.entity.BeaconBlockEntity.BeaconBeamSection;

public class BeamStuff {

  public List<BeaconBeamSection> beamSections;
  public List<BeaconBeamSection> checkingBeamSections;
  public int lastCheckY;

  public BeamStuff() {
    this(Lists.newArrayList(), Lists.newArrayList(), 0);
  }

  private BeamStuff(ArrayList<BeaconBeamSection> newArrayList, ArrayList<BeaconBeamSection> newArrayList2, int i) {
    this.beamSections = newArrayList;
    this.checkingBeamSections = newArrayList2;
    this.lastCheckY = i;
  }
}
