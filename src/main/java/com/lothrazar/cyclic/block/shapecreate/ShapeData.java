package com.lothrazar.cyclic.block.shapecreate;

import com.lothrazar.cyclic.block.shapebuilder.BuildStructureType;
import net.minecraft.nbt.CompoundNBT;

public class ShapeData {

  private BuildStructureType buildType = BuildStructureType.CUP;
  private int buildSize = 3;
  private int height = 2;

  public ShapeData(BuildStructureType buildType, int buildSize, int height) {
    super();
    this.buildType = buildType;
    this.buildSize = buildSize;
    this.height = height;
  }

  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("shape_buildType", buildType.ordinal());
    tag.putInt("shape_buildSize", buildSize);
    tag.putInt("shape_height", height);
    return tag;
  }

  public void read(CompoundNBT tag) {
    int t = tag.getInt("shape_buildType");
    buildType = BuildStructureType.values()[t];
    buildSize = tag.getInt("shape_buildSize");
    height = tag.getInt("shape_height");
  }
}
