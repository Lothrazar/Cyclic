package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CloudPlayerBlock extends BlockCyclic {

  public CloudPlayerBlock(Properties properties) {
    super(properties.strength(1.2F, 1.0F).noOcclusion());
  }

  @Override
  @Deprecated
  public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1;
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    if (worldIn instanceof EmptyBlockGetter) {
      return Shapes.empty();
    }
    if (context instanceof EntityCollisionContext) {
      EntityCollisionContext real = (EntityCollisionContext) context;
      if (real.getEntity() instanceof Player) {
        return Shapes.empty();
      }
    }
    return Shapes.block();
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
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {}
}
