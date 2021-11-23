package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GhostBlock extends BlockCyclic {

  private boolean isInvisible;

  public GhostBlock(Properties properties, boolean isInvisible) {
    super(properties.strength(2.0F, 1200.0F).noOcclusion());
    this.isInvisible = isInvisible;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.translucent());
  }

  @Override
  @Deprecated
  public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
    //    this.onFallenUpon(null, pos, null, blastResistance);
    return isPassable(worldIn, pos) ? 1 : 0;
  }

  @Override
  public VoxelShape getVisualShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return isPassable(worldIn, pos) ? Shapes.empty() : Shapes.block();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    if (isInvisible) {
      return true;
    }
    return adjacentBlockState.getBlock() == this;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return isPassable(worldIn, pos) ? Shapes.empty() : Shapes.block();
  }

  private boolean isPassable(BlockGetter worldIn, BlockPos pos) {
    boolean powered = false;
    if ((worldIn instanceof Level) == false) {
      return powered;
    }
    Level world = (Level) worldIn;
    powered = world.getBestNeighborSignal(pos) > 0;
    return powered;
  }
}
