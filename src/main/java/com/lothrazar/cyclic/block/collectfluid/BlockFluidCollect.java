package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockFluidCollect extends BlockBase {

  public BlockFluidCollect(Properties properties) {
    super(properties);
    this.setHasGui();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ClientRegistry.bindTileEntityRenderer(BlockRegistry.TileRegistry.collector_fluid, RenderPlacerFluid::new);
    ScreenManager.registerFactory(BlockRegistry.ContainerScreenRegistry.collector_fluid, ScreenFluidCollect::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileFluidCollect();
  }
}
