package com.lothrazar.cyclicmagic.item.cannon;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class RenderEmberPacket extends RenderEntity {

  public RenderEmberPacket(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }

  @Override
  public void doRender(Entity entity, double x, double y, double z, float yaw, float pTicks) {
    //
  }

  @Override
  public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float pTicks) {
    //
  }

  @Override
  public boolean canRenderName(Entity entity) {
    return false;
  }
}
