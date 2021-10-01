package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class MembraneBlock extends BlockBase {

  protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.5D, 16.0D);

  public MembraneBlock(Properties properties) {
    super(properties.hardnessAndResistance(4.0F, 1.0F));
    this.setDefaultState(this.getDefaultState().with(LIT, false));
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
    return VoxelShapes.fullCube();
  }

  @Override
  public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.fullCube();
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    //if i am not wet
    //not lit so im dry
    boolean iamDry = true;
    for (Direction direction : Direction.values()) {
      BlockPos blockpos1 = pos.offset(direction);
      //      BlockState blockstate = worldIn.getBlockState(blockpos1);
      FluidState fluidstate = worldIn.getFluidState(blockpos1);
      if (fluidstate.isTagged(FluidTags.WATER)) {
        //water! 
        iamDry = false;
        break;
      }
    }
    boolean iamWet = !iamDry;
    if (iamWet != state.get(LIT)) {
      state = state.with(LIT, iamWet);
      worldIn.setBlockState(pos, state);
      worldIn.playEvent(2001, pos, Block.getStateId(Blocks.WATER.getDefaultState()));
    }
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
    if (worldIn.isRemote || !(entityIn instanceof PlayerEntity)) {
      //not a server player
      return;
    }
    BlockState myState = worldIn.getBlockState(pos);
    if (myState.get(LIT)) {
      // WETTTTTT
      LivingEntity player = (PlayerEntity) entityIn;
      if (player.isSprinting()) {
        //zscaler
        EffectInstance eff = new EffectInstance(Effects.SPEED, 30, 5);
        eff.showIcon = false;
        eff.showParticles = false;
        player.addPotionEffect(eff);
        eff = new EffectInstance(Effects.JUMP_BOOST, 30, 5);
        eff.showIcon = false;
        eff.showParticles = false;
        player.addPotionEffect(eff);
      }
    }
  }

  @Override
  public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
    fallDistance = 0;
    super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
  }
}
