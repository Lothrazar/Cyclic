package com.lothrazar.cyclic.block.beaconpotion;

import com.lothrazar.cyclic.render.beacon.RenderBeaconBase;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

/**
 * Concrete renderer for the potion beacon. Activation is gated by
 * {@link TilePotionBeacon#isBeamActive()}; the render loop, bounding box,
 * view distance, and beam-draw helpers live in
 * {@link com.lothrazar.cyclic.render.beacon}.
 */
public class RenderBeaconPotion extends RenderBeaconBase<TilePotionBeacon> {

  public RenderBeaconPotion(BlockEntityRendererProvider.Context d) {
  }
}
