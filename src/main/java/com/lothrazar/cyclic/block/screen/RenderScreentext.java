package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.util.UtilRenderText;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

/**
 * as of minecraft 1.16 parts of this file contains code from this mod which is MIT License, the same as this project
 * 
 * https://github.com/jaquadro/StorageDrawers/blob/1.16/LICENSE
 * 
 */
public class RenderScreentext implements BlockEntityRenderer<TileScreentext> {


  public RenderScreentext(BlockEntityRendererProvider.Context d) {   }

  @Override
  public void render(TileScreentext tile, float v, PoseStack matrix,
      MultiBufferSource buffer, int light, int overlayLight) {
    if (tile.requiresRedstone() && !tile.isPowered()) {
      return;
    }
    this.renderText(tile.getFieldString(0), tile, matrix, buffer, light, tile.getCurrentFacing());
  }

  private void renderText(String text, TileScreentext tile, PoseStack matrix, MultiBufferSource buffer, int light, Direction side) {
    if (text == null || text.isEmpty()) {
      return;
    }
    int fontSize = tile.fontSize;
    int padding = tile.padding;
    int offset = tile.offset;
    //    int textWidth = fontRenderer.getStringWidth(text);// for Centering feature in future?
    float offsetHoriz = padding * 5;
    float offsetVertical = 0;
    float blockOffset = -0.01F - offset; //negative val is so its not overlapping texture  
    matrix.pushPose();
    UtilRenderText.alignRendering(matrix, side);
    matrix.translate(0, 1, 1 - blockOffset);
    matrix.scale(1 / 16f, -1 / 16f, 0.00005f);
    matrix.translate(offsetHoriz, offsetVertical, 0);
    //set the size
    float scaleX = 0.05F, scaleY = 0.05F;
    matrix.scale(scaleX * fontSize, scaleY * fontSize, 1);
    //then draw it

    Font fontRenderer = Minecraft.getInstance().font; //this.renderer.getFont();
    fontRenderer.drawInBatch(text, 0, 0, tile.getColor(),
        tile.getDropShadow(), matrix.last().pose(), buffer, false, 0, light); // 15728880
    matrix.popPose();
  }
}
