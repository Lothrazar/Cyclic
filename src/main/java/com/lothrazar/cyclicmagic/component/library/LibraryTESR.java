package com.lothrazar.cyclicmagic.component.library;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

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
      renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.TL), QuadrantEnum.TL, face, x, y, z, destroyStage, leftColumn, topRow, horizDistFromCenter);
      renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.TR), QuadrantEnum.TR, face, x, y, z, destroyStage, rightColumn, topRow, horizDistFromCenter);
      renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.BL), QuadrantEnum.BL, face, x, y, z, destroyStage, leftColumn, bottomRow, horizDistFromCenter);
      renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.BR), QuadrantEnum.BR, face, x, y, z, destroyStage, rightColumn, bottomRow, horizDistFromCenter);
      if (te.getLastClicked() != null && te.getEnchantStack(te.lastClicked).isEmpty() == false) {
        //TODO: we could xyz offset in different ways too
        this.drawNameplate((T) te, te.getEnchantStack(te.lastClicked).toString(), x, y, z, 50);
      }
      //WIP
      //      renderStack(te, new ItemStack(Items.ENCHANTED_BOOK), face,QuadrantEnum.TL, x, y, z);
      //      renderStack(te, new ItemStack(Items.DIAMOND_SWORD), face,QuadrantEnum.TR, x, y, z);
      //      renderStack(te, new ItemStack(Items.APPLE), face,QuadrantEnum.BL, x, y, z);
      //      renderStack(te, new ItemStack(Items.ARROW), face,QuadrantEnum.BR, x, y, z);
    }
  }
  private void renderStack(TileEntityLibrary te, EnchantStack stack, EnumFacing face, QuadrantEnum quad, double x, double y, double z) {
    float scaleFactor = 0.045F;
    float borderWidth = 0.038F;
    int angle = angleOfFace(face);
    ItemStack s = stack.getRenderIcon();
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    float width = 0.25F;
    float bookX = borderWidth, bookY = 0.956F;
    switch (quad) {
      case TL:
        //TODO: REFACTOR INTO SHARED METHOD
        for (int i = 0; i < stack.getCount(); i++) {
          bookX += scaleFactor;
          if (i % 8 == 0) {
            bookX = 0.089F;
            bookY -= scaleFactor;
          }
          renderItem(te, s, bookX, bookY, 1F, angle, false, scaleFactor);
        }
      break;
      case TR:
       
        for (int i = 0; i < stack.getCount(); i++) {
          bookX += scaleFactor;
          if (i % 8 == 0) {
            bookX =  0.6F;
            bookY -= scaleFactor;
          }
          renderItem(te, s, bookX, bookY, 1F, angle, false, scaleFactor);
        }
      //  renderItem(te, s, 0.75F, 0.75F, 1F, 90, false, scaleFactor);
      break;
      case BL:
        bookY = 0.45F;
        for (int i = 0; i < stack.getCount(); i++) {
          bookX += scaleFactor;
          if (i % 8 == 0) {
            bookX =  0.6F;
            bookY -= scaleFactor;
          }
          renderItem(te, s, bookX, bookY, 1F, angle, false, scaleFactor);
        }
        //renderItem(te, s, 0.25F, 0.25F, 1F, 90, false, scaleFactor);
      break;
      case BR:
        bookY = 0.45F;
        for (int i = 0; i < stack.getCount(); i++) {
          bookX += scaleFactor;
          if (i % 8 == 0) {
            bookX =  0.089F;
            bookY -= scaleFactor;
          }
          renderItem(te, s, bookX, bookY, 1F, angle, false, scaleFactor);
        }
       // renderItem(te, s, 0.75F, 0.25F, 1F, 90, false, scaleFactor);
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
  private void renderEnchantStack(TileEntityLibrary te, EnchantStack stack, QuadrantEnum quad, EnumFacing face, double x, double y, double z, int destroyStage, float xt, float yt, float zt) {
    if (stack.isEmpty() == false) {
      renderStack(te, stack, face, quad, x, y, z);
    }
    //    int angle = angleOfFace(face);
    //    renderTextAt(stack.shortName(), x, y, z, destroyStage, xt, yt, zt, angle);
    //    if (stack.isEmpty() == false) {
    //      renderTextAt(stack.levelName(), x, y, z, destroyStage, xt, yt + vOffset, zt, angle);
    //      renderTextAt(stack.countName(), x, y, z, destroyStage, xt, yt + 2 * vOffset, zt, angle);
    //    }
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
