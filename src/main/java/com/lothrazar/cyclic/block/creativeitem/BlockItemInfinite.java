package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockItemInfinite extends BlockBase {

  public BlockItemInfinite(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileItemInfinite();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);
      if (tileEntity instanceof TileItemInfinite) {
        TileItemInfinite tile = (TileItemInfinite) tileEntity;
        tile.inputSlots.setStackInSlot(0, player.getHeldItem(hand));
        return ActionResultType.CONSUME;
      }
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }
}
