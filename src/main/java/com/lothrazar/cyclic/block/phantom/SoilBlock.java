package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SoilBlock extends BlockBase {

  public SoilBlock(Properties properties) {
    super(properties.strength(1.0F, 1.0F));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new SoilTile();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.translucent());
  }
}
