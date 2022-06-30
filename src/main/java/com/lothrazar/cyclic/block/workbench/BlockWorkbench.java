package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWorkbench extends BlockBase {

  public BlockWorkbench(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setHasGui();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.WORKBENCH, ScreenWorkbench::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileWorkbench();
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileWorkbench tileentity = (TileWorkbench) worldIn.getTileEntity(pos);
      if (tileentity != null) {
        for (int i = 0; i < tileentity.inventory.getSlots(); ++i) {
          InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.inventory.getStackInSlot(i));
        }
      }
      super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
  }
}
