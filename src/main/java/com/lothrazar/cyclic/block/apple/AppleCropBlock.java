package com.lothrazar.cyclic.block.apple;

import com.lothrazar.cyclic.base.BlockBase;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

public class AppleCropBlock extends BlockBase implements IGrowable {

  private static final int MAX_AGE = 7;
  private static final IntegerProperty AGE = BlockStateProperties.AGE_0_7;
  private static final VoxelShape[] SHAPES = new VoxelShape[] {
      //////////////         x1    y1    z1    x2     y2     z2
      Block.makeCuboidShape(4.0D, 12.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 0
      Block.makeCuboidShape(4.0D, 11.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 1
      Block.makeCuboidShape(4.0D, 10.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 2
      Block.makeCuboidShape(4.0D, 9.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 3
      Block.makeCuboidShape(4.0D, 8.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 4
      Block.makeCuboidShape(4.0D, 6.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 5
      Block.makeCuboidShape(4.0D, 4.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 6
      Block.makeCuboidShape(4.0D, 4.0D, 2.0D, 14.0D, 16.0D, 12.0D) };
  boolean canBonemeal = true;

  public AppleCropBlock(Block.Properties builder, boolean canBonemeal) {
    super(builder.doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.CROP));
    this.canBonemeal = canBonemeal;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
    return canBonemeal; //false if its a super-apple
  }

  @Override
  public boolean ticksRandomly(BlockState state) {
    return state.get(AGE) < MAX_AGE;
  }

  @Override
  public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.up()).isIn(BlockTags.LEAVES);
  }

  @Override
  public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
    int age = state.get(AGE);
    if (age < MAX_AGE && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, worldIn.rand.nextInt(5) == 0)) {
      this.grow(worldIn, random, pos, state);
      ForgeHooks.onCropsGrowPost(worldIn, pos, state);
    }
  }

  @Override
  public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
    return state.get(AGE) < MAX_AGE;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPES[state.get(AGE)];
  }

  @Override
  public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
    worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(state.get(AGE) + 1)), 2);
  }
}
