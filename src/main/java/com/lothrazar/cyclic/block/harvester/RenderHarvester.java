package com.lothrazar.cyclic.block.harvester;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderHarvester extends TileEntityRenderer<TileHarvester> {

  public RenderHarvester(TileEntityRendererDispatcher p_i226006_1_) {
    super(p_i226006_1_);
  }

  static final float[] laserColor = new float[] { 0.04F, 0.99F, 0F };
  static final double rotationTime = 0;
  static final double beamWidth = 0.02;
  static final float alpha = 0.9F;
  //  @Override //renderTileEntityFast
  //  public void func_225616_a_(TileHarvester te, double x, double y, double z, float partialTicks, int destroyStage) {
  //    //test 
  //    if (te.laserTimer > 0) {
  //      RenderUtil.renderLaser(new LaserConfig(te.laserTarget, te.getPos(),
  //          rotationTime, alpha, beamWidth, laserColor));
  //    }
  //  }

  @Override
  public void func_225616_a_(TileHarvester p_225616_1_, float p_225616_2_, MatrixStack p_225616_3_, IRenderTypeBuffer p_225616_4_, int p_225616_5_, int p_225616_6_) {
    // TODO Auto-generated method stub
  }
}
