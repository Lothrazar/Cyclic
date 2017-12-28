package com.lothrazar.cyclicmagic.component.library;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

public class LibraryTESR<T extends TileEntityLibrary> extends BaseTESR<T> {
  public LibraryTESR(Block block) {
    super(block);
  }
  @Override
  public void render(TileEntityLibrary te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    float zTransl = 0.500005F;
    float leftColumn = 1.6F, rightColumn = 2.2F;
    float topRow = -0.9F, bottomRow = -1.6125F;
    for (EnumFacing face : EnumFacing.HORIZONTALS) {
      //TODO: loop on storage length. FOR NOW we have fixed size of 4 so do this
      renderTextAt(te.getEnchantStack(QuadrantEnum.TL).shortName(), x, y, z, destroyStage, leftColumn, topRow, zTransl, angleOfFace(face));
      renderTextAt(te.getEnchantStack(QuadrantEnum.TR).shortName(), x, y, z, destroyStage, rightColumn, topRow, zTransl, angleOfFace(face));
      renderTextAt(te.getEnchantStack(QuadrantEnum.BL).shortName(), x, y, z, destroyStage, leftColumn, bottomRow, zTransl, angleOfFace(face));
      renderTextAt(te.getEnchantStack(QuadrantEnum.BR).shortName(), x, y, z, destroyStage, rightColumn, bottomRow, zTransl, angleOfFace(face));
    }
  }
  private int angleOfFace(EnumFacing side) {
    switch (side) {
      case SOUTH:
        return 0;
      case EAST:
        return 90;
      case NORTH:
        return 180;
      case WEST:
        return 270;
      default:
        return -1;
    }
  }
  private void renderTextAt(String s, double x, double y, double z, int destroyStage, float xt, float yt, float zt, float angle) {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
    if (angle != 0) {
      GlStateManager.rotate(angle, 0, 1, 0);
    }
    //initial setup
    float scaleTo = 0.6666667F;
    GlStateManager.enableRescaleNormal();
    GlStateManager.pushMatrix();
    GlStateManager.scale(scaleTo, -1 * scaleTo, -1 * scaleTo);
    GlStateManager.popMatrix();
    FontRenderer fontrenderer = this.getFontRenderer();
    float f3 = 0.010416667F;
    GlStateManager.translate(-2.0F, 1.33333334F, 0.046666667F);
    //below sets position
    GlStateManager.translate(xt, yt, zt);
    //sake makes it the right size do not touch
    GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
    GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);//no idea what this does
    GlStateManager.depthMask(false);
    fontrenderer.drawString(s, 0, 0, 0xFF0000);
    GlStateManager.depthMask(true);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
    if (destroyStage >= 0) {
      GlStateManager.matrixMode(5890);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
    }
  }
}
