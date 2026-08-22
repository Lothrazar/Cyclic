package com.lothrazar.cyclic.block.screen;

import com.lothrazar.library.util.RenderTextUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * as of minecraft 1.16 parts of this file contains code from this mod which is MIT License, the same as this project
 * <p>
 * https://github.com/jaquadro/StorageDrawers/blob/1.16/LICENSE
 */
public class RenderScreentext implements BlockEntityRenderer<TileScreentext, RenderScreentext.State> {

  public static class State extends BlockEntityRenderState {
    TileScreentext blockEntity;
  }

  public RenderScreentext(BlockEntityRendererProvider.Context d) {
  }

  @Override
  public boolean shouldRender(TileScreentext blockEntity, Vec3 cameraPosition) {
    return true;
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileScreentext blockEntity, State state, float partialTicks, Vec3 cameraPosition,
                                 ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
  }

  @Override
  public void submit(State state, PoseStack matrix, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileScreentext tile = state.blockEntity;
    if (tile.requiresRedstone() && !tile.isPowered()) {
      return;
    }
    MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
    this.renderText(tile.getFieldString(0), tile, matrix, buffer, state.lightCoords, tile.getCurrentFacing());
    buffer.endBatch();
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
    RenderTextUtil.alignRendering(matrix, side);
    matrix.translate(0, 1, 1 - blockOffset);
    matrix.scale(1 / 16f, -1 / 16f, 0.00005f);
    matrix.translate(offsetHoriz, offsetVertical, 0);
    //set the size
    float scaleX = 0.05F, scaleY = 0.05F;
    matrix.scale(scaleX * fontSize, scaleY * fontSize, 1);
    //then draw it
    Font fontRenderer = Minecraft.getInstance().font; //this.renderer.getFont();
    // the Font.DisplayMode.NORMAL was 'false'
    fontRenderer.drawInBatch(text, 0, 0, tile.getColor(),
        tile.getDropShadow(), matrix.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, light); // 15728880
    matrix.popPose();
  }
}
