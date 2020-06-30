package com.lothrazar.cyclic.block.generator;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockPeatGenerator extends BlockBase {

  public BlockPeatGenerator(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setHasGui();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TilePeatGenerator();
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(BlockRegistry.ContainerScreens.generatorCont, ScreenGenerator::new);
  }
}
