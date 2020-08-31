package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilRender;
import com.lothrazar.cyclic.util.UtilRender.LaserConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderHarvester extends TileEntityRenderer<TileHarvester> {

  public RenderHarvester(TileEntityRendererDispatcher d) {
    super(d);
  }

  static final float[] laserColor = new float[] { 0.04F, 0.99F, 0F };
  static final double rotationTime = 0;
  static final double beamWidth = 0.02;
  static final float alpha = 0.9F;
  public static boolean ENABLED = false;

  @Override
  public void render(TileHarvester te, float v, MatrixStack matrixStack,
      IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    // ok
    UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack);
    if (ENABLED) {
      if (te.laserTimer > 0) {
        UtilRender.renderLaser(new LaserConfig(te.laserTarget, te.getPos(),
            rotationTime, alpha, beamWidth, laserColor), matrixStack);
      }
    }
    else if (te.laserTimer > 0) {
      // fallback 
      UtilParticle.spawnParticle(te.getWorld(), ParticleTypes.PORTAL, te.laserTarget.down(), 4);
    }
  }
}
