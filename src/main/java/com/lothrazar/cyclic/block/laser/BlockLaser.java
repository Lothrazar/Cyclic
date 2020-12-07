package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockLaser extends BlockBase {

  public BlockLaser(Properties properties) {
    super(properties.hardnessAndResistance(1.3F));
    //    this.setHasGui();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ClientRegistry.bindTileEntityRenderer(TileRegistry.laser, RenderLaser::new);
    //    ScreenManager.registerFactory(ContainerScreenRegistry.harvester, ScreenHarvester::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileLaser();
  }
}
