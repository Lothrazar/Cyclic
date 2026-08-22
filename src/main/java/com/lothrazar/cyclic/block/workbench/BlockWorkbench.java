package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockWorkbench extends BlockCyclic {

  public BlockWorkbench(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    return super.useWithoutItem(state, world, pos, player, hit);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileWorkbench(pos, state);
  }

  @Override // was onReplaced
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel worldIn, BlockPos pos, boolean movedByPiston) {
    if (true) {
      TileWorkbench tileentity = (TileWorkbench) worldIn.getBlockEntity(pos);
      if (tileentity != null) {
        for (int i = 0; i < tileentity.inventory.getSlots(); ++i) {
          // was  InventoryHelper.spawnItemStack
          Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.inventory.getStackInSlot(i));
        }
      }
      super.affectNeighborsAfterRemoval(state, worldIn, pos, movedByPiston);
    }
  }
}
