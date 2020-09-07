package com.lothrazar.cyclic.block.detectorentity;

import java.awt.Color;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderDetector extends TileEntityRenderer<TileDetector> {

  public RenderDetector(TileEntityRendererDispatcher d) {
    super(d);
  }

  static final float[] laserColor = new float[] { 0.04F, 0.99F, 0F };
  static final double rotationTime = 0;
  static final double beamWidth = 0.02;
  static final float alpha = 0.9F;
  public static boolean ENABLED = false;

  @Override
  public void render(TileDetector te, float v, MatrixStack matrixStack,
      IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TileDetector.Fields.RENDER.ordinal()) == 1)
      UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack, 0.6F, Color.GREEN);
  }
}
