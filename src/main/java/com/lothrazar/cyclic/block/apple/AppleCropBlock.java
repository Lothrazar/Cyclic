package com.lothrazar.cyclic.block.apple;

import java.util.Random;
import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public class AppleCropBlock extends BlockCyclic implements BonemealableBlock {

  private static final int MAX_AGE = 7;
  private static final IntegerProperty AGE = BlockStateProperties.AGE_7;
  private static final VoxelShape[] SHAPES = new VoxelShape[] {
      //////////////         x1    y1    z1    x2     y2     z2
      Block.box(4.0D, 12.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 0
      Block.box(4.0D, 11.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 1
      Block.box(4.0D, 10.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 2
      Block.box(4.0D, 9.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 3
      Block.box(4.0D, 8.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 4
      Block.box(4.0D, 6.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 5
      Block.box(4.0D, 4.0D, 2.0D, 14.0D, 16.0D, 12.0D), // 6
      Block.box(4.0D, 4.0D, 2.0D, 14.0D, 16.0D, 12.0D) };
  boolean canBonemeal = true;

  public AppleCropBlock(Block.Properties builder, boolean canBonemeal) {
    super(builder.noCollission().randomTicks().instabreak().sound(SoundType.CROP));
    this.canBonemeal = canBonemeal;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
    return canBonemeal; //false if its a super-apple
  }

  @Override
  public boolean isRandomlyTicking(BlockState state) {
    return state.getValue(AGE) < MAX_AGE;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.above()).is(BlockTags.LEAVES);
  }

  @Override
  public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
    int age = state.getValue(AGE);
    if (age < MAX_AGE && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, worldIn.random.nextInt(5) == 0)) {
      worldIn.setBlock(pos, state.setValue(AGE, Integer.valueOf(age + 1)), 2);
      // this.grow(worldIn, random, pos, state);
      ForgeHooks.onCropsGrowPost(worldIn, pos, state);
    }
  }

  @Override
  public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
    return state.getValue(AGE) < MAX_AGE;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return SHAPES[state.getValue(AGE)];
  }

  @Override
  public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
    worldIn.setBlock(pos, state.setValue(AGE, Integer.valueOf(state.getValue(AGE) + 1)), 2);
  }
}
