package com.lothrazar.cyclic.block.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * as of minecraft 1.16 much of this file contains code from this mod which is MIT License, the same as this project
 * 
 * https://github.com/jaquadro/StorageDrawers/blob/1.16/LICENSE
 * 
 */
@OnlyIn(Dist.CLIENT)
public class RenderScreen extends TileEntityRenderer<TileScreentext> {

  public RenderScreen(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileScreentext tile, float v, MatrixStack matrix,
      IRenderTypeBuffer buffer, int light, int overlayLight) {
    //
    this.renderText(tile.getFieldString(0), tile, matrix, buffer, light, tile.getCurrentFacing());
  }

  private void renderText(String text, TileScreentext tile, MatrixStack matrix, IRenderTypeBuffer buffer, int light, Direction side) {
    if (text == null || text.isEmpty()) {
      return;
    }
    float alpha = 1;
    FontRenderer fontRenderer = this.renderDispatcher.getFontRenderer();
    //    BlockDrawers block = (BlockDrawers) state.getBlock();
    AxisAlignedBB labelGeometry = new AxisAlignedBB(.9, .9, 0, 9, 1, 1);
    int textWidth = fontRenderer.getStringWidth(text);
    float offsetX = (float) (labelGeometry.minX + labelGeometry.getXSize() / 2);
    float offsetY = 16f - (float) labelGeometry.minY - (float) labelGeometry.getYSize();
    float offsetZ = (float) labelGeometry.minZ * .0625f - .01f;
    matrix.push();
    alignRendering(matrix, side);
    //move it over
    // NOTE: RenderItem expects to be called in a context where Y increases toward the bottom of the screen
    // However, for in-world rendering the opposite is true. So we translate up by 1 along Y, and then flip
    // along Y. Since the item is drawn at the back of the drawer, we also translate by `1-offsetZ` to move
    // it to the front.
    // The 0.00001 for the Z-scale both flattens the item and negates the 32.0 Z-scale done by RenderItem.
    matrix.translate(0, 1, 1 - offsetZ);
    matrix.scale(1 / 16f, -1 / 16f, 0.00005f);
    matrix.translate(offsetX, offsetY, 0);
    //set the size
    float scaleX = .125F, scaleY = .125F;
    matrix.scale(scaleX * tile.fontSize, scaleY * tile.fontSize, 1);
    //then draw it
    //    moveRendering(matrix, .125f, .125f, offsetX, offsetY, offsetZ);
    int red = tile.red, green = tile.green, blue = tile.blue;
    int color = (int) (255 * alpha) << red | 255 << green | 255 << blue | 255;
    fontRenderer.renderString(text, -textWidth / 2f, 0.5f, color, false, matrix.getLast().getMatrix(), buffer, false, 0, light); // 15728880
    matrix.pop();
  }

  private void alignRendering(MatrixStack matrix, Direction side) {
    // Rotate to face the correct direction for the drawer's orientation.
    matrix.translate(.5f, .5f, .5f);
    matrix.rotate(new Quaternion(Vector3f.YP, getRotationYForSide2D(side), true));
    matrix.translate(-.5f, -.5f, -.5f);
  }

  float[] sideRotationY2D = { 0, 0, 2, 0, 3, 1 };

  private float getRotationYForSide2D(Direction side) {
    return sideRotationY2D[side.ordinal()] * 90;
  }
}
