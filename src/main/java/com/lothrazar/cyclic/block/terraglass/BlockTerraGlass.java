package com.lothrazar.cyclic.block.terraglass;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockTerraGlass extends BlockBase {

  public BlockTerraGlass(Properties properties) {
    //https://en.wikipedia.org/wiki/Terra_preta
    super(properties.hardnessAndResistance(1.8F).notSolid().setLightLevel(state -> {
      return state.get(LIT) ? 6 : 0;
    }));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1.0F;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  @Deprecated
  public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 0;
  }

  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(LIT) ? 15 : 0;
  }

  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(LIT) ? 15 : 0;
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileTerraGlass();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }
}
