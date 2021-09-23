package com.lothrazar.cyclic.block.shears;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffolding;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;

public class BlockShearing extends BlockBase {

  public BlockShearing(Properties properties) {
    super(properties.hardnessAndResistance(1.1F).tickRandomly().notSolid().sound(SoundType.CHAIN));
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return BlockScaffolding.AABB;
  }

  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    if (entityIn instanceof IForgeShearable) {
      //do it
      IForgeShearable sheep = (IForgeShearable) entityIn;
      if (sheep.isShearable(ItemStack.EMPTY, entityIn.world, pos)) {
        List<ItemStack> drops = sheep.onSheared(null, ItemStack.EMPTY, entityIn.world, pos, entityIn.world.rand.nextInt(3));
        drops.forEach(d -> {
          Random rand = entityIn.world.rand;
          ItemEntity ent = entityIn.entityDropItem(d, 1.0F);
          ent.setMotion(ent.getMotion().add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F));
        });
      }
    }
  }
}
