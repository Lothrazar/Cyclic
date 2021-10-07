package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockWirelessTransmit extends BlockBase {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public BlockWirelessTransmit(Properties properties) {
    super(properties.strength(1.8F));
    this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
    this.setHasGui();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.WIRELESS_TRANSMITTER, ScreenTransmit::new);
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileWirelessTransmit(pos,state );
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }
}
