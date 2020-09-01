package com.lothrazar.cyclic.block.sprinklers;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockTerraPreta extends BlockBase {

  public BlockTerraPreta(Properties properties) {
    //https://en.wikipedia.org/wiki/Terra_preta
    super(properties.hardnessAndResistance(1.8F));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    //    ScreenManager.registerFactory(BlockRegistry.ContainerScreens.anvil, ScreenAnvilMagma::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileTerraPreta();
  }
}
