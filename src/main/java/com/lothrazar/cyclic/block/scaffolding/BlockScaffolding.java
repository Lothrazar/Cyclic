package com.lothrazar.cyclic.block.scaffolding;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import java.util.Random;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

public class BlockScaffolding extends BlockBase {

  private static final double CLIMB_SPEED = 0.31D; //climbing glove is 0.288D
  private static final double OFFSET = 0.125D;
  //shearing & cactus are  0.0625D;
  public static final VoxelShape AABB = Block.box(OFFSET, OFFSET, OFFSET,
      16 - OFFSET, 16 - OFFSET, 16 - OFFSET); //required to make entity collied happen for ladder climbing
  private boolean doesAutobreak = true;

  public BlockScaffolding(Properties properties, boolean autobreak) {
    super(properties.strength(0.1F).randomTicks().noOcclusion()
        .sound(SoundRegistry.SCAFFOLD));
    System.out.println("TODO: +harvestLevel(0)");
    this.doesAutobreak = autobreak;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutout());
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  // TICK
  @Override
  @Deprecated
  public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
    if (doesAutobreak && worldIn.random.nextDouble() < 0.5) {
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
    UtilEntity.tryMakeEntityClimb(worldIn, entity, CLIMB_SPEED);
  }
}
