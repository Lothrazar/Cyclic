package com.lothrazar.cyclicmagic.item.cannon;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleEventManager {

  public static long ticks = 0;

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTextureStitch(TextureStitchEvent event) {
    //    ResourceLocation particleGlow = new ResourceLocation(Const.MODID, "entity/particle_glow");
    //    event.getMap().registerSprite(particleGlow);
    //    ResourceLocation particleSmoke = new ResourceLocation(Const.MODID, "entity/particle_smoke");
    //    event.getMap().registerSprite(particleSmoke);
    //    ResourceLocation particleStar = new ResourceLocation(Const.MODID, "entity/particle_star");
    //    event.getMap().registerSprite(particleStar);
    event.getMap().registerSprite(ParticleGolemLaser.texture);
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
      ClientProxy.particleRenderer.updateParticles();
      ticks++;
    }
  }

  @SideOnly(Side.CLIENT)
  public static void renderEntityStatic(Entity entityIn, float partialTicks, boolean b, Render render) {
    if (entityIn.ticksExisted == 0) {
      entityIn.lastTickPosX = entityIn.posX;
      entityIn.lastTickPosY = entityIn.posY;
      entityIn.lastTickPosZ = entityIn.posZ;
    }
    //        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
    //        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
    //        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
    float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
    int i = entityIn.getBrightnessForRender();
    if (entityIn.isBurning()) {
      i = 15728880;
    }
    int j = i % 65536;
    int k = i / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    //  ((IRenderEntityLater) render).renderLater(entityIn, -TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ, f, partialTicks);
  }

  static float tickCounter = 0;
  static EntityPlayer clientPlayer = null;

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onRenderAfterWorld(RenderWorldLastEvent event) {
    tickCounter++;
    //    if (Roots.proxy instanceof ClientProxy) {
    //      GlStateManager.pushMatrix();
    //      ClientProxy.particleRendererGolem.renderParticles(clientPlayer, event.getPartialTicks());
    //      GlStateManager.popMatrix();
    //    }
    //OpenGlHelper.glUseProgram(ShaderUtil.lightProgram);
    //    GlStateManager.pushMatrix();
    //    for (Entity e : Minecraft.getMinecraft().world.getLoadedEntityList()) {
    //      Render render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(e);
    //            if (render instanceof IRenderEntityLater) {
    //              renderEntityStatic(e, Minecraft.getMinecraft().getRenderPartialTicks(), true, render);
    //            }
    //    }
    //    GlStateManager.popMatrix();
    if (ModCyclic.proxy instanceof ClientProxy && Minecraft.getMinecraft().player != null) {
      ClientProxy.particleRenderer.renderParticles(Minecraft.getMinecraft().player, event.getPartialTicks());
    }
    //OpenGlHelper.glUseProgram(0);
  }
}
