package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CloudBlock extends BlockCyclic {

  public CloudBlock(Properties properties) {
    super(properties.strength(1.2F, 1.0F).noOcclusion());
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.translucent());
  }

  @Override
  @Deprecated
  public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1;
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return Shapes.empty();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.getBlock() == this;
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
    if (!worldIn.isClientSide && entityIn instanceof LivingEntity) {
      LivingEntity e = (LivingEntity) entityIn;
      MobEffectInstance eff = new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 5);
      eff.visible = false;
      eff.showIcon = false;
      e.addEffect(eff);
      eff = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 5);
      eff.visible = false;
      eff.showIcon = false;
      e.addEffect(eff);
    }
  }
}
