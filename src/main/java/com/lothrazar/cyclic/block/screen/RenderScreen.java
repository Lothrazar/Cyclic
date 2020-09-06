package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.util.UtilRenderText;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * as of minecraft 1.16 parts of this file contains code from this mod which is MIT License, the same as this project
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
    if (tile.requiresRedstone() && !tile.isPowered()) {
      return;
    }
    this.renderText(tile.getFieldString(0), tile, matrix, buffer, light, tile.getCurrentFacing());
  }

  private void renderText(String text, TileScreentext tile, MatrixStack matrix, IRenderTypeBuffer buffer, int light, Direction side) {
    if (text == null || text.isEmpty()) {
      return;
    }
    int fontSize = tile.fontSize;
    int padding = tile.padding;
    int offset = tile.offset;
    //    int textWidth = fontRenderer.getStringWidth(text);// for Centering feature in future?
    float offsetHoriz = padding * 5;
    float offsetVertical = 0;
    float blockOffset = -0.01F - offset;//negative val is so its not overlapping texture  
    matrix.push();
    UtilRenderText.alignRendering(matrix, side);
    matrix.translate(0, 1, 1 - blockOffset);
    matrix.scale(1 / 16f, -1 / 16f, 0.00005f);
    matrix.translate(offsetHoriz, offsetVertical, 0);
    //set the size
    float scaleX = 0.05F, scaleY = 0.05F;
    matrix.scale(scaleX * fontSize, scaleY * fontSize, 1);
    //then draw it
    FontRenderer fontRenderer = this.renderDispatcher.getFontRenderer();
    fontRenderer.renderString(text, 0, 0, tile.getColor(),
        tile.getDropShadow(), matrix.getLast().getMatrix(), buffer, false, 0, light); // 15728880
    matrix.pop();
  }
}
