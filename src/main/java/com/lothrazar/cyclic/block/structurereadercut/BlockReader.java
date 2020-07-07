package com.lothrazar.cyclic.block.structurereadercut;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockReader extends BlockBase {

  public BlockReader(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setHasGui();
    StructureBlock x;
    StructureBlockTileEntity y;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileReader();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    //    ClientRegistry.bindTileEntityRenderer(BlockRegistry.Tiles.harvesterTile, RenderHarvester::new);
    ScreenManager.registerFactory(BlockRegistry.ContainerScreens.structure_reader, ScreenReader::new);
  }
}
