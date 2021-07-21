package com.lothrazar.cyclic.block.spikes;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SpikesDiamond extends SpikesBlock {

  public SpikesDiamond(Properties properties) {
    super(properties, EnumSpikeType.NONE);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileDiamondSpikes();
  }

  @Override
  @Deprecated
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity && state.get(ACTIVATED) && worldIn instanceof ServerWorld) {
      //attck from fake player
      TileDiamondSpikes tile = (TileDiamondSpikes) worldIn.getTileEntity(pos);
      if (tile.fakePlayer == null || tile.fakePlayer.get() == null) {
        ModCyclic.LOGGER.error("null player sup +" + tile, tile.fakePlayer);
        return;
      }
      if (tile.getTimer() == 1) {
        ModCyclic.LOGGER.error(tile.fakePlayer.get().getHeldItemMainhand() + "spikes attack" + entity);
        tile.fakePlayer.get().attackTargetEntityWithCurrentItem(entity);
      }
    }
  }
}
