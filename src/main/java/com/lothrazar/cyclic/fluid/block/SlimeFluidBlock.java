package com.lothrazar.cyclic.fluid.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class SlimeFluidBlock extends LiquidBlock {

  public static class Flowing extends ForgeFlowingFluid.Flowing {

    public Flowing(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(LevelReader worldIn) {
      return 1;
    }

    @Override
    public int getDropOff(LevelReader worldIn) {
      return 2;
    }
  }

  public static class Source extends ForgeFlowingFluid.Source {

    public Source(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(LevelReader worldIn) {
      return 1;
    }

    @Override
    public int getDropOff(LevelReader worldIn) {
      return 6;
    }
  }

  VoxelShape shapes[] = new VoxelShape[16];

  public SlimeFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
    int max = 15; //max of the property LEVEL.getAllowedValues()
    float offset = 0.875F;
    for (int i = 0; i <= max; i++) { //x and z go from [0,1] 
      shapes[i] = Shapes.create(new AABB(0, 0, 0, 1, offset - i / 8F, 1));
    }
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return shapes[state.getValue(LEVEL).intValue()];
  }

  @Override
  @Deprecated
  public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return shapes[state.getValue(LEVEL).intValue()];
  }

  @Override
  public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
    if (entityIn.isSuppressingBounce()) {
      super.fallOn(worldIn, state, pos, entityIn, fallDistance);
    }
    else {
      //      entityIn.causeFallDamage(fallDistance, 0.0F,DamageSou );
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
   *
   * @param entity
   */
  private void collision(Entity entity) {
    Vec3 vec3d = entity.getDeltaMovement();
    if (vec3d.y < 0.0D) {
      double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
      entity.setDeltaMovement(vec3d.x, -vec3d.y * d0, vec3d.z);
    }
  }
}
