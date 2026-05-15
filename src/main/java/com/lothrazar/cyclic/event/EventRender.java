package com.lothrazar.cyclic.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@OnlyIn(Dist.CLIENT)
public class EventRender {
  @SubscribeEvent
  public void onRenderOverlay(RenderGuiEvent.Post event) {}
  @SubscribeEvent
  public void onRenderWorldLast(RenderLevelStageEvent event) {}
}
