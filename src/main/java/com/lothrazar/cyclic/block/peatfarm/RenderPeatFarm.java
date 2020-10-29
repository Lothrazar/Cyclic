package com.lothrazar.cyclic.block.peatfarm;

import java.awt.Color;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderPeatFarm extends TileEntityRenderer<TilePeatFarm> {

  public RenderPeatFarm(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TilePeatFarm te, float v, MatrixStack matrixStack,
      IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    // ok
    if (te.getField(TilePeatFarm.Fields.RENDER.ordinal()) == 1)
      UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack, 0.4F, Color.DARK_GRAY);
  }
}
