package com.lothrazar.cyclic.block.generatorpeat;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockGeneratorPeat extends BlockBase {

  public BlockGeneratorPeat(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileGeneratorPeat();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.generatorCont, ScreenGeneratorPeat::new);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }
}
