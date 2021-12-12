package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class MembraneLamp extends BlockBase {

  public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;

  public MembraneLamp(Properties properties) {
    super(properties.hardnessAndResistance(1.0F, 1.0F).setLightLevel(state -> state.get(POWER)).notSolid());
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWER);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new MembraneLampTile();
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
  }

  @Override
  public VoxelShape getRayTraceShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.fullCube();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }
}
