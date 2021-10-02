package com.lothrazar.cyclic.block.generatorfluid;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockGeneratorFluid extends BlockBase {

  public BlockGeneratorFluid(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    setDefaultState(getDefaultState().with(LIT, false));
    this.setHasGui();
    this.setHasFluidInteract();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(LIT);
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    ScreenManager.registerFactory(ContainerScreenRegistry.GENERATOR_FLUID, ScreenGeneratorFluid::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileGeneratorFluid();
  }
}
