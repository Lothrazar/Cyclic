package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class BlockDarkGlass extends BlockBase {

  public BlockDarkGlass(Properties properties) {
    super(properties.hardnessAndResistance(0.5F, 3600000.0F).harvestTool(ToolType.PICKAXE).sound(SoundType.GLASS)
        .func_226896_b_());
    RenderTypeLookup.setRenderLayer(this, RenderType.func_228641_d_());
  }

  //
  @Override
  @Deprecated
  public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 255;//zero is transparent fully
  }

  //  //
  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return false;
  }

  //
  @Override //BlockGlass
  @OnlyIn(Dist.CLIENT)
  public float func_220080_a(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1.0F;
  }

  //
  //  @Override //BlockGlass
  //  public boolean func_229869_c_(BlockState p_229869_1_, IBlockReader p_229869_2_, BlockPos p_229869_3_) {
  //    return false;
  //  }
  //
  @Override //BlockGlass
  public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return false;
  }
}
