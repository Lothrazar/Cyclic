package com.lothrazar.cyclicmagic.block.pump;

import com.lothrazar.cyclicmagic.core.block.BlockBaseFacingOmni;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockPump extends BlockBaseFacingOmni {

  private static final double SIZE = 0.875D;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(1 - SIZE, 1 - SIZE, 1 - SIZE, SIZE, SIZE, SIZE);

  public BlockPump() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(3F);
    this.setHarvestLevel("pickaxe", 1);
    this.setTranslucent();
    this.placeType = PlacementType.SIDE_BLOCK;
  }

  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if (side == EnumFacing.UP || side == EnumFacing.DOWN)
      return false;//allows, for example, you to open chest that is directly below
    return true;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
}
