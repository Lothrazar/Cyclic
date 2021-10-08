package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockShapedata extends BlockBase {

  public BlockShapedata(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.computer_shape, ScreenShapedata::new);
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileShapedata(pos,state);
  }
}
