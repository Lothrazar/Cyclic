package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockItemInfinite extends BlockCyclic {

  public BlockItemInfinite(Properties properties) {
    super(properties.strength(1.8F));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileItemInfinite(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.ITEM_INFINITE.get(), world.isClientSide ? TileItemInfinite::clientTick : TileItemInfinite::serverTick);
  }

  @Override
  public net.minecraft.world.InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    if (!world.isClientSide) {
      BlockEntity tileEntity = world.getBlockEntity(pos);
      if (tileEntity instanceof TileItemInfinite) {
        TileItemInfinite tile = (TileItemInfinite) tileEntity;
        ItemStack stack = player.getMainHandItem();
        if (!stack.isEmpty()) {
          tile.inputSlots.setStackInSlot(0, stack);
          return InteractionResult.SUCCESS;
        }
      }
    }
    return super.useWithoutItem(state, world, pos, player, hit);
  }
}
