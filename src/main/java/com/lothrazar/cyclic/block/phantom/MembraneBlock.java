package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MembraneBlock extends BlockCyclic {

  protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.5D, 16.0D);

  public MembraneBlock(Properties properties) {
    super(properties.strength(1.5F, 1.0F));
    this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
    return Shapes.block();
  }

  @Override
  public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
    return Shapes.block();
  }

  @Override
  public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    //if i am not wet
    //not lit so im dry
    boolean iamDry = true;
    for (Direction direction : Direction.values()) {
      BlockPos blockpos1 = pos.relative(direction);
      //      BlockState blockstate = worldIn.getBlockState(blockpos1);
      FluidState fluidstate = worldIn.getFluidState(blockpos1);
      if (fluidstate.is(FluidTags.WATER)) {
        //water! 
        iamDry = false;
        break;
      }
    }
    boolean iamWet = !iamDry;
    if (iamWet != state.getValue(LIT)) {
      state = state.setValue(LIT, iamWet);
      worldIn.setBlockAndUpdate(pos, state);
      worldIn.levelEvent(2001, pos, Block.getId(Blocks.WATER.defaultBlockState()));
    }
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
    if (worldIn.isClientSide || !(entityIn instanceof Player)) {
      //not a server player
      return;
    }
    BlockState myState = worldIn.getBlockState(pos);
    if (myState.getValue(LIT)) {
      // WETTTTTT
      LivingEntity player = (Player) entityIn;
      if (player.isSprinting()) {
        //zscaler
        MobEffectInstance eff = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 5);
        eff.showIcon = false;
        eff.visible = false;
        player.addEffect(eff);
        eff = new MobEffectInstance(MobEffects.JUMP, 30, 5);
        eff.showIcon = false;
        eff.visible = false;
        player.addEffect(eff);
      }
    }
  }

  @Override
  public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
    fallDistance = 0;
    super.fallOn(worldIn, state, pos, entityIn, fallDistance);
  }
}
