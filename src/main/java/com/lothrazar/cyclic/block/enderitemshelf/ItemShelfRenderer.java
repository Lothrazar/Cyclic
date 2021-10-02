package com.lothrazar.cyclic.block.enderitemshelf;

import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.util.UtilRenderText;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class ItemShelfRenderer extends TileEntityRenderer<TileItemShelf> {

  public ItemShelfRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
    super(rendererDispatcherIn);
  }

  @Override
  public void render(TileItemShelf tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
    Direction side = tile.getCurrentFacing();
    UtilRenderText.alignRendering(ms, side);
    for (int i = 0; i < tile.inventory.getSlots(); i++) {
      renderSlot(tile, i, tile.inventory.getStackInSlot(i), ms, buffer, combinedLightIn, combinedOverlayIn);
    }
  }

  private void renderSlot(TileItemShelf tile, int slot, ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
    if (stack.isEmpty()) {
      return;
    }
    final float sh = 16F;
    final int color = 0;
    final double x = 1.5F / sh;
    final double y = (3 * slot + 2) / sh;
    final double z = 1.01;
    final float scaleNum = 0.094F;
    FontRenderer fontRenderer = this.renderDispatcher.getFontRenderer();
    if (tile.renderStyle == RenderTextType.STACK) {
      final float sp = 0.19F;
      final float xf = 0.16F + slot * sp / 1.5F;
      final float yf = 1 - (0.88F - slot * sp);
      float size = 0.12F;
      //similar to but different, i didnt use rotation
      //https://github.com/InnovativeOnlineIndustries/Industrial-Foregoing/blob/1.16/src/main/java/com/buuz135/industrial/proxy/client/render/BlackHoleUnitTESR.java#L76
      ms.push();
      ms.translate(0, 0, 1);
      ms.translate(xf, yf, 0);
      ms.scale(size, size, size);
      // TODO: use light offset
      //      float lf = tile.getWorld().getLight(tile.getPos().offset(tile.getCurrentFacing()));
      Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, ms, buffer);
      ms.pop();
    }
    else if (tile.renderStyle == RenderTextType.TEXT) {
      //      if (tile.inventory.nameCache[slot] == null || tile.inventory.nameCache[slot].isEmpty()) {
      //        Map<Enchantment, Integer> enchantments = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(stack));
      //        for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
      //          tile.inventory.nameCache[slot] = entry.getKey().getDisplayName(entry.getValue()).getString();
      //          break;
      //        }
      //      }
      String displayName = stack.getDisplayName().getString();
      //      if (displayName.isEmpty()) {
      //        displayName = stack.getDisplayName().getString();
      //      }
      final float scaleName = 0.02832999F + 0.1F * getScaleFactor(displayName);
      ms.push();
      ms.translate(x - 0.02, y + 0.06, z);
      ms.scale(1 / sh * scaleName, -1 / sh * scaleName, 0.00005F);
      fontRenderer.renderString(displayName, 0, 0, color, false, ms.getLast().getMatrix(), buffer, false, 0, combinedLightIn);
      ms.pop();
    }
    if (tile.renderStyle != RenderTextType.NONE) {
      //render stack count
      ms.push();
      ms.translate(x + 0.081F, y, z);
      ms.scale(1 / sh * scaleNum, -1 / sh * scaleNum, 0.00005F);
      String displayCount = "x" + stack.getCount();
      fontRenderer.renderString(displayCount, 110, 0, color, false, ms.getLast().getMatrix(), buffer, false, 0, combinedLightIn);
      ms.pop();
    }
  }

  private float getScaleFactor(String displayName) {
    int lv = 17;
    if (displayName.length() > lv) {
      return 1.0F - 1 / 36F * (displayName.length() - lv);
    }
    return 1.0F;
  }
}
