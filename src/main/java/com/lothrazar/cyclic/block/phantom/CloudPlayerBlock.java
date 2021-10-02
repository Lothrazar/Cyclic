package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CloudPlayerBlock extends BlockBase {

  public CloudPlayerBlock(Properties properties) {
    super(properties.hardnessAndResistance(1.2F, 1.0F).notSolid());
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
  }

  @Override
  @Deprecated
  public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1; // isPassable(worldIn, pos) ? 1 : 0;
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    //    return super.getCollisionShape(state, worldIn, pos, context);
    return (context.getEntity() instanceof PlayerEntity) ? VoxelShapes.empty() : VoxelShapes.fullCube();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.getBlock() == this;
  }
}
