package com.lothrazar.cyclic.block.beaconredstone;

import java.util.List;
import com.lothrazar.cyclic.block.beaconpotion.RenderBeaconPotion;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

public class RenderBeaconRedstone implements BlockEntityRenderer<TileBeaconRedstone> {

  public RenderBeaconRedstone(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileBeaconRedstone tile, float p_112141_, PoseStack p_112142_, MultiBufferSource p_112143_, int p_112144_, int p_112145_) {
    long i = tile.getLevel().getGameTime();
    if (!tile.isPowered()) {
      return; // do not render if redstone offed 
    }
    List<BeaconBlockEntity.BeaconBeamSection> list = tile.getBeamSections();
    int j = 0;
    for (int k = 0; k < list.size(); ++k) {
      BeaconBlockEntity.BeaconBeamSection beaconblockentity$beaconbeamsection = list.get(k);
      RenderBeaconPotion.renderBeaconBeam(p_112142_, p_112143_, p_112141_, i, j, k == list.size() - 1 ? 1024 : beaconblockentity$beaconbeamsection.getHeight(), beaconblockentity$beaconbeamsection.getColor());
      j += beaconblockentity$beaconbeamsection.getHeight();
    }
  }

  @Override
  public int getViewDistance() {
    return 256;
  }
}
