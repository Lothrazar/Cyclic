package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockLaser extends BlockBase {

  public BlockLaser(Properties properties) {
    super(properties.strength(1.3F).noOcclusion());
    this.setHasGui();
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
    ClientRegistry.bindTileEntityRenderer(TileRegistry.laser, RenderLaser::new);
    MenuScreens.register(ContainerScreenRegistry.laser, ScreenLaser::new);
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileLaser();
  }
}
