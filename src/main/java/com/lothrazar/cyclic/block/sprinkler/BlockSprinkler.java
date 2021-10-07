package com.lothrazar.cyclic.block.sprinkler;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockSprinkler extends BlockBase {

  private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D,
      12.0D, 5.0D, 12.0D);

  public BlockSprinkler(Properties properties) {
    super(properties.strength(0.8F).noOcclusion());
    this.setHasFluidInteract();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }
  //  @Override
  //  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
  //    return blockState.get(LIT) ? 15 : 0;
  //  }
  //
  //  @Override
  //  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
  //    return blockState.get(LIT) ? 15 : 0;
  //  }

  @Override
  public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1.0f;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.translucent());
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return false;
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state, BlockGetter world) {
    return new TileSprinkler(pos,state);
  }
}
