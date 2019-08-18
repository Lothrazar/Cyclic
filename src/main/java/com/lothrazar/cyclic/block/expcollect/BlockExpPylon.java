package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.UtilStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockExpPylon extends Block {

  public BlockExpPylon(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).sound(SoundType.GLASS));
  }

  @Override
  public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (player.isSneaking() && !worldIn.isRemote) {
      TileExpPylon tile = (TileExpPylon) worldIn.getTileEntity(pos);
      UtilStuff.messageStatus(player, "" + tile.getStoredXp());
      return true;
    }
    return false;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileExpPylon();
  }
}
