package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockDarkGlass extends BlockBase {

  public BlockDarkGlass(Properties properties) {
    super(properties.hardnessAndResistance(0.5F, 3600000.0F).harvestTool(ToolType.PICKAXE).sound(SoundType.GLASS));
  }

  @Override
  @Deprecated
  public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 255;//zero is transparent fully
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  /**
   * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off transparency (glass, reeds),
   *
   * TRANSLUCENT for fully blended transparency (stained glass)
   */
  @Override
  public BlockRenderType getRenderType(BlockState bs) {
    return BlockRenderType.INVISIBLE;
  }
}
