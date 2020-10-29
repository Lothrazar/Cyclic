package com.lothrazar.cyclic.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BuildShape {

  //  @Nullable
  private String structure = null;
  //corner small always has the lowest Y point, 
  //if same y level, whoever has smallest sum of x+z, closest to negative infinity
  //  private BlockPos cornerSmall;
  //  private BlockPos cornerLarge; 
  //should not allow points outside corners
  private List<BlockPos> shape;
  private List<BlockState> blockstates;

  public BuildShape() {
    shape = new ArrayList<>();
    blockstates = new ArrayList<>();
  }

  public BuildShape(List<BlockPos> pos) {
    this();
    shape = pos;
  }

  public BuildShape(World world, List<BlockPos> options) {
    this();
    for (BlockPos pos : options) {
      if (!world.isAirBlock(pos)) {
        shape.add(pos);
        blockstates.add(world.getBlockState(pos));
      }
    }
  }

  public String getStructure() {
    return structure;
  }

  public void setStructure(String structure) {
    this.structure = structure;
  }

  public BlockState getBlock(int i) {
    if (i >= blockstates.size()) {
      return null;
    }
    return blockstates.get(i);
  }

  public List<BlockState> getBlocks() {
    return blockstates;
  }

  public List<BlockPos> getShape() {
    return shape;
  }
}
