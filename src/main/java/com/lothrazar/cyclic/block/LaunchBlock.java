package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilEntity;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class LaunchBlock extends BlockBase {

  private static final float ANGLE = 90;
  protected static final VoxelShape PRESSED_AABB = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D);
  boolean sneakPlayerAvoid = true;
  boolean doRedstone;

  public LaunchBlock(Properties properties, boolean doRedstone) {
    super(properties.doesNotBlockMovement().hardnessAndResistance(0.5F));
    this.doRedstone = doRedstone;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return PRESSED_AABB;
  }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
    return false;
  }

  @Override
  public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
    BlockPos blockpos = pos.down();
    return hasSolidSideOnTop(worldIn, blockpos) || hasEnoughSolidSide(worldIn, blockpos, Direction.UP);
  }

  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
    if (sneakPlayerAvoid && entity instanceof PlayerEntity && ((PlayerEntity) entity).isCrouching()) {
      return;
    }
    if (worldIn.isRemote) {
      UtilEntity.launch(entity, ANGLE, getPower(worldIn, pos));
    }
    else if (entity instanceof PlayerEntity) {
      //          ((EntityPlayer) entity).addPotionEffect(new PotionEffect(PotionEffects.BOUNCE, 300, 0));
    }
  }

  private float getPower(World world, BlockPos pos) {
    if (this.doRedstone == false) {
      return 1.6F;
    }
    int power = 0;
    for (Direction direction : Direction.values()) {
      if (direction == Direction.UP) {
        continue;
      }
      int localPow = world.getRedstonePower(pos.offset(direction), direction);
      if (localPow > power) {
        power = localPow;
      }
    }
    //    int power = world.getRedstonePowerFromNeighbors(pos);// this.getStrongPower(state, worldIn, pos, Direction.UP);
    float ratio = ((power + 2) / 16F);
    return 2.4F * ratio;
  }
}
