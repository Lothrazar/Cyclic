package com.lothrazar.cyclic.block.crate;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockCrate extends BlockBase {

  public BlockCrate(Properties properties) {
    super(properties.hardnessAndResistance(1.1F, 3600000.0F).sound(SoundType.STONE));
    this.setHasGui();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCrate();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.crate, ScreenCrate::new);
  }
}
