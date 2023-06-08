package com.lothrazar.cyclic.block.shears;

import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffolding;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IForgeShearable;

public class BlockShearing extends BlockCyclic {

  public BlockShearing(Properties properties) {
    super(properties.strength(1.1F).randomTicks().noOcclusion().sound(SoundType.CHAIN));
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return BlockScaffolding.AABB;
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
    if (entityIn instanceof IForgeShearable) {
      //do it
      IForgeShearable sheep = (IForgeShearable) entityIn;
      if (sheep.isShearable(ItemStack.EMPTY, entityIn.level, pos)) {
        List<ItemStack> drops = sheep.onSheared(null, ItemStack.EMPTY, entityIn.level, pos, entityIn.level.random.nextInt(3));
        drops.forEach(d -> {
          RandomSource rand = entityIn.level.random;
          ItemEntity ent = entityIn.spawnAtLocation(d, 1.0F);
          ent.setDeltaMovement(ent.getDeltaMovement().add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F));
        });
      }
    }
  }
}
