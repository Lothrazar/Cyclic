package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockLaunch extends BlockBase {

  private final static float ANGLE = 90;
  protected static final VoxelShape PRESSED_AABB = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D);

  public BlockLaunch(Properties properties) {
    super(properties.doesNotBlockMovement().hardnessAndResistance(0.5F));
    PressurePlateBlock x;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return PRESSED_AABB;
  }

  @Override
  public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
    BlockPos blockpos = pos.down();
    return hasSolidSideOnTop(worldIn, blockpos) || hasEnoughSolidSide(worldIn, blockpos, Direction.UP);
  }

  boolean sneakPlayerAvoid = true;

  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
    if (sneakPlayerAvoid && entity instanceof PlayerEntity && ((PlayerEntity) entity).isCrouching()) {
      return;
    }
    if (!worldIn.isRemote) {
      UtilEntity.launch(entity, ANGLE, getPower());
      if (entity instanceof PlayerEntity) {
        //        ((EntityPlayer) entity).addPotionEffect(new PotionEffect(PotionEffectRegistry.BOUNCE, 300, 0));
      }
      //      int i = this.getRedstoneStrength(state);
      //      if (i == 0) {
      //        this.updateState(worldIn, pos, state, i);
      //      }
    }
  }

  private float getPower() {
    return 1.8F;
  }
}
