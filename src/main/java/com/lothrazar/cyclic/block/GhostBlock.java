package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GhostBlock extends BlockBase {

  public GhostBlock(Properties properties) {
    super(properties.hardnessAndResistance(10.0F, 1200.0F).notSolid());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());//getTranslucent());
  }

  @Override
  @Deprecated
  public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1;//same as GlassBlock
  }

  @Override
  public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.empty();//same as Glass Block
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }
  //  @Override
  //  @Deprecated
  //  @OnlyIn(Dist.CLIENT)
  //  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
  //    return 1.0F;
  //  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    if ((worldIn instanceof World) == false) {
      //on world exit/save it can do this
      //      ModCyclic.LOGGER.error("no world " + worldIn);
      return VoxelShapes.fullCube();
    }
    World world = (World) worldIn;//
    boolean powered = world.getRedstonePowerFromNeighbors(pos) > 0;
    return powered ? VoxelShapes.empty() : VoxelShapes.fullCube();
  }
}
