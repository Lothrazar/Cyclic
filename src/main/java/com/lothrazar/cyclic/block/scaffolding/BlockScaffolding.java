package com.lothrazar.cyclic.block.scaffolding;

import java.util.Random;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockScaffolding extends BlockBase {

  private static final double CLIMB_SPEED = 0.31D;//climbing glove is 0.288D
  private static final double OFFSET = 0.0125D;//shearing & cactus are  0.0625D;
  protected static final VoxelShape AABB = Block.makeCuboidShape(OFFSET * 16, 0, OFFSET * 16, (1 - OFFSET) * 16, 1, (1 - OFFSET) * 16);//required to make entity collied happen for ladder climbing
  private boolean doesAutobreak = true;

  public BlockScaffolding(Properties properties, boolean autobreak) {
    super(properties.hardnessAndResistance(0.1F).tickRandomly().harvestLevel(0).notSolid());
    this.doesAutobreak = autobreak;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return AABB;
  }

  // TICK
  @Override
  @Deprecated
  public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
    if (doesAutobreak && worldIn.rand.nextDouble() < 0.5) {
      worldIn.destroyBlock(pos, true);
    }
  }

  @Override
  public int tickRate(IWorldReader worldIn) {
    return 200;
  }

  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    if (!(entityIn instanceof LivingEntity)) {
      return;
    }
    LivingEntity entity = (LivingEntity) entityIn;
    if (!entityIn.collidedHorizontally) {
      return;
    }
    UtilEntity.tryMakeEntityClimb(worldIn, entity, CLIMB_SPEED);
  }
}
