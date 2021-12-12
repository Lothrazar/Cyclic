package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GhostBlock extends BlockBase {

  private boolean isInvisible;

  public GhostBlock(Properties properties, boolean isInvisible) {
    super(properties.hardnessAndResistance(2.0F, 1200.0F).notSolid());
    this.isInvisible = isInvisible;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
  }

  @Override
  @Deprecated
  public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
    //    this.onFallenUpon(null, pos, null, blastResistance);
    return isPassable(worldIn, pos) ? 1 : 0;
  }

  @Override
  public VoxelShape getRayTraceShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return isPassable(worldIn, pos) ? VoxelShapes.empty() : VoxelShapes.fullCube();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
    if (isInvisible) {
      return true;
    }
    return adjacentBlockState.getBlock() == this;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return isPassable(worldIn, pos) ? VoxelShapes.empty() : VoxelShapes.fullCube();
  }

  private boolean isPassable(IBlockReader worldIn, BlockPos pos) {
    boolean powered = false;
    if (!(worldIn instanceof World)) {
      return powered;
    }
    World world = (World) worldIn;
    powered = world.getRedstonePowerFromNeighbors(pos) > 0;
    return powered;
  }
}
