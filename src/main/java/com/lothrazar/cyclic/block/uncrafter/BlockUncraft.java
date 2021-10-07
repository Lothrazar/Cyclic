package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockUncraft extends BlockBase {

  public BlockUncraft(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state ) {
    return new TileUncraft(pos,state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public void registerClient() {
    //    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
    MenuScreens.register(ContainerScreenRegistry.UNCRAFT, ScreenUncraft::new);
  }
}
