package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
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
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EnderShelfRenderer implements BlockEntityRenderer<TileEnderShelf, EnderShelfRenderer.State> {

  public static class State extends BlockEntityRenderState {
    TileEnderShelf blockEntity;
    ItemStackRenderState[] items;
  }

  private final ItemModelResolver itemModelResolver;

  public EnderShelfRenderer(BlockEntityRendererProvider.Context d) {
    this.itemModelResolver = d.itemModelResolver();
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileEnderShelf blockEntity, State state, float partialTicks, Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
    var h = blockEntity.inventory;
    state.items = new ItemStackRenderState[h.getSlots()];
    for (int i = 0; i < h.getSlots(); i++) {
      ItemStack stack = h.getStackInSlot(i);
      if (!stack.isEmpty()) {
        ItemStackRenderState itemState = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.NONE, blockEntity.getLevel(), null, i);
        state.items[i] = itemState;
      }
    }
  }

  @Override
  public void submit(State state, PoseStack ms, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileEnderShelf tile = state.blockEntity;
    Direction side = tile.getCurrentFacing();
    RenderTextUtil.alignRendering(ms, side);
    var h = tile.inventory;
    for (int i = 0; i < h.getSlots(); i++) {
      renderSlot(tile, i, h.getStackInSlot(i), state.items[i], ms, submitNodeCollector, state.lightCoords);
    }
  }

  private void renderSlot(TileEnderShelf tile, int slot, ItemStack stack, ItemStackRenderState itemState, PoseStack ms,
      SubmitNodeCollector submitNodeCollector, int combinedLightIn) {
    if (stack.isEmpty()) {
      return;
    }
    final float sh = 16F;
    final int color = 0;
    final double x = 1.5F / sh;
    final double y = (3 * slot + 2) / sh;
    final double z = 1.01;
    final float scaleNum = 0.094F;
    Font fontRenderer = Minecraft.getInstance().font; //this.renderer.getFont();
    MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
    if (tile.renderStyle == RenderTextType.STACK) {
      final float sp = 0.19F;
      final float xf = 0.16F + slot * sp / 1.5F;
      final float yf = 1 - (0.88F - slot * sp);
      float size = 0.12F;
      //similar to but different, i didnt use rotation
      //https://github.com/InnovativeOnlineIndustries/Industrial-Foregoing/blob/1.16/src/main/java/com/buuz135/industrial/proxy/client/render/BlackHoleUnitTESR.java#L76
      ms.pushPose();
      ms.translate(0, 0, 1);
      ms.translate(xf, yf, 0);
      ms.scale(size, size, size);
      if (itemState != null) {
        itemState.submit(ms, submitNodeCollector, combinedLightIn, OverlayTexture.NO_OVERLAY, 0);
      }
      ms.popPose();
    }
    else if (tile.renderStyle == RenderTextType.TEXT) {
      if (tile.inventory.nameCache[slot] == null || tile.inventory.nameCache[slot].isEmpty()) {
        // enchantment name lookup removed for 1.21.1 compat
      }
      String displayName = tile.inventory.nameCache[slot];
      if (displayName == null || displayName.isEmpty()) {
        displayName = stack.getHoverName().getString();
      }
      final float scaleName = 0.02832999F + 0.1F * getScaleFactor(displayName);
      ms.pushPose();
      ms.translate(x - 0.02, y + 0.06, z);
      ms.scale(1 / sh * scaleName, -1 / sh * scaleName, 0.00005F);
      fontRenderer.drawInBatch(displayName, 0, 0, color, false, ms.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, combinedLightIn);
      ms.popPose();
    }
    if (tile.renderStyle != RenderTextType.NONE) {
      //render stack count
      ms.pushPose();
      ms.translate(x + 0.081F, y, z);
      ms.scale(1 / sh * scaleNum, -1 / sh * scaleNum, 0.00005F);
      String displayCount = "x" + stack.getCount();
      fontRenderer.drawInBatch(displayCount, 110, 0, color, false, ms.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, combinedLightIn);
      ms.popPose();
    }
    buffer.endBatch();
  }

  private float getScaleFactor(String displayName) {
    int lv = 17;
    if (displayName.length() > lv) {
      return 1.0F - 1 / 36F * (displayName.length() - lv);
    }
    return 1.0F;
  }
}
