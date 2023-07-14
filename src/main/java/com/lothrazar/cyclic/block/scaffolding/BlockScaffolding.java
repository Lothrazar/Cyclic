package com.lothrazar.cyclic.block.scaffolding;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockScaffolding extends BlockCyclic {

  private static final double CHANCE_CRUMBLE = 0.5;
  private static final double CLIMB_SPEED = 0.31D; // compare to climbing glove
  private static final double OFFSET = 0.125D;
  public static final VoxelShape AABB = Block.box(OFFSET, OFFSET, OFFSET,
      16 - OFFSET, 16 - OFFSET, 16 - OFFSET); //required to make entity collied happen for ladder climbing
  private boolean doesAutobreak = true;

  public BlockScaffolding(Properties properties, boolean autobreak) {
    super(properties.strength(0.1F).randomTicks().noOcclusion().sound(SoundRegistry.SCAFFOLD));
    this.doesAutobreak = autobreak;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
    if (doesAutobreak && random.nextDouble() < CHANCE_CRUMBLE) {
      worldIn.destroyBlock(pos, true);
    }
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
    if (!(entityIn instanceof LivingEntity)) {
      return;
    }
    LivingEntity entity = (LivingEntity) entityIn;
    if (!entityIn.horizontalCollision) {
      return;
    }
    EntityUtil.tryMakeEntityClimb(worldIn, entity, CLIMB_SPEED);
  }
}
