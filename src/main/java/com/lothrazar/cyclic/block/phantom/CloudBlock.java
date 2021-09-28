package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CloudBlock extends BlockBase {

  public CloudBlock(Properties properties) {
    super(properties.hardnessAndResistance(4.0F, 1.0F).notSolid());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
  }

  @Override
  @Deprecated
  public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1; // isPassable(worldIn, pos) ? 1 : 0;
  }

  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    // 
    if (!worldIn.isRemote && entityIn instanceof LivingEntity) {
      LivingEntity e = (LivingEntity) entityIn;
      //zscaler
      EffectInstance eff = new EffectInstance(Effects.SLOW_FALLING, 20, 5);
      eff.showParticles = false;
      eff.showIcon = false;
      e.addPotionEffect(eff);
      eff = new EffectInstance(Effects.SLOWNESS, 20, 5);
      eff.showParticles = false;
      eff.showIcon = false;
      e.addPotionEffect(eff);
    }
  }
  //  @Override
  //  public VoxelShape getRayTraceShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
  //    return VoxelShapes.fullCube();
  //  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.getBlock() == this;
  }
  //  @Override
  //  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
  //    return super.getShape(state, worldIn, pos, context);
  //    //    return context.getEntity() instanceof PlayerEntity ? VoxelShapes.fullCube() : VoxelShapes.empty();
  //  }
}
