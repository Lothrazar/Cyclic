package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockWirelessTransmit extends BlockBase {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public BlockWirelessTransmit(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, Boolean.FALSE));
    this.setHasGui();
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.WIRELESS_TRANSMITTER, ScreenTransmit::new);
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    ClientRegistry.bindTileEntityRenderer(TileRegistry.wireless_transmitter, RenderTransmit::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileWirelessTransmit();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }
}
