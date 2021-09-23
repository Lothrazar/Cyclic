package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockUncraft extends BlockBase {

  public BlockUncraft(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setHasGui();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileUncraft();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public void registerClient() {
    //    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
    ScreenManager.registerFactory(ContainerScreenRegistry.uncraft, ScreenUncraft::new);
  }
}
