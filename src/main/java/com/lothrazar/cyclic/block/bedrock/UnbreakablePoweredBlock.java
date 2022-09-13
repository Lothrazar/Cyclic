package com.lothrazar.cyclic.block.bedrock;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class UnbreakablePoweredBlock extends BlockBase {

  public static final BooleanProperty BREAKABLE = UnbreakableBlock.BREAKABLE;

  public UnbreakablePoweredBlock(Properties properties) {
    super(properties.hardnessAndResistance(50.0F, 1200.0F));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new UnbreakablePoweredTile();
  }

  public static void setBreakable(World world, BlockPos pos, boolean isBreakable) {
    BlockState state = world.getBlockState(pos);
    if (state.hasProperty(BREAKABLE)) {
      boolean oldBreakable = state.get(BREAKABLE);
      if (oldBreakable != isBreakable) {
        world.setBlockState(pos, state.with(BREAKABLE, isBreakable));
        if (world.isRemote) {
          UtilParticle.spawnParticle(world, RedstoneParticleData.REDSTONE_DUST, pos, 5);
        }
      }
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
    return (state.hasProperty(BREAKABLE) && !state.get(BREAKABLE)) ? 0.0F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BREAKABLE);
  }
}
