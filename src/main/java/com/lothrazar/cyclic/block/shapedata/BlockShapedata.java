package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockShapedata extends BlockCyclic {

  public BlockShapedata(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.COMPUTER_SHAPE.get(), ScreenShapedata::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileShapedata(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.COMPUTER_SHAPE.get(), world.isClientSide ? TileShapedata::clientTick : TileShapedata::serverTick);
  }
}
