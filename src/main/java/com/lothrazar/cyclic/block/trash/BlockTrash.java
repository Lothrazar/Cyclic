package com.lothrazar.cyclic.block.trash;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockTrash extends BlockBase {

  public BlockTrash(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).sound(SoundType.METAL)
        .func_226896_b_());
    RenderTypeLookup.setRenderLayer(this, RenderType.func_228641_d_());
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileTrash();
  }
}
