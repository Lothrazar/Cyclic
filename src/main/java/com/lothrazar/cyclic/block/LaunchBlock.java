package com.lothrazar.cyclic.block;

import com.lothrazar.library.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LaunchBlock extends BlockCyclic {

  private static final float ANGLE = 90;
  protected static final VoxelShape PRESSED_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D);
  boolean sneakPlayerAvoid = true;
  boolean doRedstone;

  public LaunchBlock(Properties properties, boolean doRedstone) {
    super(properties.noCollission().strength(0.5F));
    this.doRedstone = doRedstone;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return PRESSED_AABB;
  }

  @Override
  public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
    return false;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
    BlockPos blockpos = pos.below();
    return canSupportRigidBlock(worldIn, blockpos) || canSupportCenter(worldIn, blockpos, Direction.UP);
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
    if (sneakPlayerAvoid && entity instanceof Player && ((Player) entity).isCrouching()) {
      return;
    }
    if (worldIn.isClientSide) {
      EntityUtil.launch(entity, ANGLE, getPower(worldIn, pos));
    }
    else if (entity instanceof Player) {
      //          ((EntityPlayer) entity).addPotionEffect(new PotionEffect(PotionEffects.BOUNCE, 300, 0));
    }
  }

  private float getPower(Level world, BlockPos pos) {
    if (this.doRedstone == false) {
      return 1.6F;
    }
    int power = 0;
    for (Direction direction : Direction.values()) {
      if (direction == Direction.UP) {
        continue;
      }
      int localPow = world.getSignal(pos.relative(direction), direction);
      if (localPow > power) {
        power = localPow;
      }
    }
    //    int power = world.getRedstonePowerFromNeighbors(pos);// this.getStrongPower(state, worldIn, pos, Direction.UP);
    float ratio = ((power + 2) / 16F);
    return 2.4F * ratio;
  }
}
