package com.lothrazar.cyclicmagic.component.library;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class LibraryTESR<T extends TileEntityLibrary> extends BaseTESR<T> {
  final float horizDistFromCenter = 0.46F;
  final float leftColumn = 1.6F, rightColumn = 2.08F;
  final float topRow = -0.9F, bottomRow = -1.4125F;
  final float vOffset = -0.11F;
  public LibraryTESR(Block block) {
    super(block);
  }
  @Override
  public void render(TileEntityLibrary te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    for (EnumFacing face : EnumFacing.HORIZONTALS) {
      //TODO: loop on storage length. FOR NOW we have fixed size of 4 so do this
      renderEnchantStack(te.getEnchantStack(QuadrantEnum.TL), x, y, z, destroyStage, leftColumn, topRow, horizDistFromCenter, angleOfFace(face));
      renderEnchantStack(te.getEnchantStack(QuadrantEnum.TR), x, y, z, destroyStage, rightColumn, topRow, horizDistFromCenter, angleOfFace(face));
      renderEnchantStack(te.getEnchantStack(QuadrantEnum.BL), x, y, z, destroyStage, leftColumn, bottomRow, horizDistFromCenter, angleOfFace(face));
      renderEnchantStack(te.getEnchantStack(QuadrantEnum.BR), x, y, z, destroyStage, rightColumn, bottomRow, horizDistFromCenter, angleOfFace(face));
      //WIP
      renderStack(te, new ItemStack(Items.ENCHANTED_BOOK), face,QuadrantEnum.TL, x, y, z);
    }
  }
  private void renderStack(TileEntityLibrary te, ItemStack s,EnumFacing face, QuadrantEnum quad, double x, double y, double z) {
  float scaleFactor = 0.2F;
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    switch (quad) {
      case TL:
        renderItem(te, s, 0.25F, 0.75F, 1F, 90, false, scaleFactor);
      break;
      case TR:
        renderItem(te, s, 0.25F, 0.75F, 1F, 90, false, scaleFactor);
      break;
      case BL:
        renderItem(te, s, 0.25F, 0.75F, 1F, 90, false, scaleFactor);
      break;
      case BR:
        renderItem(te, s, 0.25F, 0.75F, 1F, 90, false, scaleFactor);
      break;
    }
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
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
  private void renderEnchantStack(EnchantStack stack, double x, double y, double z, int destroyStage, float xt, float yt, float zt, float angle) {
    renderTextAt(stack.shortName(), x, y, z, destroyStage, xt, yt, zt, angle);
    if (stack.isEmpty() == false) {
      renderTextAt(stack.levelName(), x, y, z, destroyStage, xt, yt + vOffset, zt, angle);
      renderTextAt(stack.countName(), x, y, z, destroyStage, xt, yt + 2 * vOffset, zt, angle);
    }
  }
  private void renderTextAt(String s, double x, double y, double z, int destroyStage, float xt, float yt, float zt, float angle) {
    int textColor = 0xFF0055;//TODO: input or per type?
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
    GlStateManager.translate(-2.0F, 1.33333334F, 0.046666667F);
    //below sets position
    GlStateManager.translate(xt, yt, zt);
    //sake makes it the right size do not touch
    float f3 = 0.010416667F;
    GlStateManager.scale(0.010416667F, -0.010416667F, 0.010416667F);
    GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);//no idea what this does
    GlStateManager.depthMask(false);
    fontrenderer.drawString(s, 0, 0, textColor);
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
