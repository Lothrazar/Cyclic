package com.lothrazar.cyclic.fluid.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SlimeFluidBlock extends SolidFluidBlock {

  public SlimeFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
  }

  @Override
  public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
    if (entityIn.isSuppressingBounce()) {
      super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }
    else {
      entityIn.onLivingFall(fallDistance, 0.0F);
    }
    //    super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
  }

  @Override
  public void onLanded(IBlockReader worldIn, Entity entityIn) {
    if (entityIn.isSuppressingBounce()) {
      super.onLanded(worldIn, entityIn);
    }
    else {
      this.func_226946_a_(entityIn);
    }
  }

  /**
   * From SlimeBlock.java
   * 
   * @param entity
   */
  private void func_226946_a_(Entity entity) {
    Vec3d vec3d = entity.getMotion();
    if (vec3d.y < 0.0D) {
      double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
      entity.setMotion(vec3d.x, -vec3d.y * d0, vec3d.z);
    }
  }
}
