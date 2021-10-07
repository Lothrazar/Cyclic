package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockItemInfinite extends BlockBase {

  public BlockItemInfinite(Properties properties) {
    super(properties.strength(1.8F));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileItemInfinite(pos,state );
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!world.isClientSide) {
      BlockEntity tileEntity = world.getBlockEntity(pos);
      if (tileEntity instanceof TileItemInfinite) {
        TileItemInfinite tile = (TileItemInfinite) tileEntity;
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty()) {
          tile.inputSlots.setStackInSlot(0, stack);
          return InteractionResult.SUCCESS;
        }
      }
    }
    return super.use(state, world, pos, player, hand, hit);
  }
}
