package com.lothrazar.cyclic.fluid.block;

import com.lothrazar.library.fluid.GenericFluidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SlimeFluidBlock extends GenericFluidBlock {

  private final VoxelShape[] shapes = new VoxelShape[16];

  public SlimeFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Properties props) {
    super(supplier, props);
    float offset = 0.875F;
    for (int i = 0; i <= 15; i++) {
      shapes[i] = Shapes.create(new AABB(0, 0, 0, 1, offset - i / 8F, 1));
    }
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    return shapes[state.getValue(LEVEL).intValue()];
  }

  @Override
  public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
    if (entityIn.isSuppressingBounce()) {
      super.fallOn(worldIn, state, pos, entityIn, fallDistance);
    }
  }

  @Override
  public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
    if (entityIn.isSuppressingBounce()) {
      super.updateEntityAfterFallOn(worldIn, entityIn);
    }
    else {
      this.collision(entityIn);
    }
  }

  /**
   * From SlimeBlock.java bounceUp
   */
  private void collision(Entity entity) {
    Vec3 vec3d = entity.getDeltaMovement();
    if (vec3d.y < 0.0D) {
      double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
      entity.setDeltaMovement(vec3d.x, -vec3d.y * d0, vec3d.z);
    }
  }
}
