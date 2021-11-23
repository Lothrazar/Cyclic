package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoilBlock extends BlockCyclic {

  public SoilBlock(Properties properties) {
    super(properties.strength(1.0F, 1.0F));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SoilTile(pos, state);
  }

  //  @Override
  //  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
  //    return createTickerHelper(type, TileRegistry.SOIL, world.isClientSide ? SoilTile::clientTick : SoilTile::serverTick);
  //  }
  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.translucent());
  }
}
