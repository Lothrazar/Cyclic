package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
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
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    return super.use(state, world, pos, player, hand, hit);
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.WORKBENCH, ScreenWorkbench::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileWorkbench(pos, state);
  }
  //  @Override
  //  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
  //    return createTickerHelper(type, TileRegistry.workbench, world.isClientSide ? TileWorkbench::clientTick : TileWorkbench::serverTick);
  //  }
}
