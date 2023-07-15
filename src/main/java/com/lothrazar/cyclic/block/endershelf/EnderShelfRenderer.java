package com.lothrazar.cyclic.block.endershelf;

import java.util.Map;
import java.util.Map.Entry;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.library.util.RenderTextUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class EnderShelfRenderer implements BlockEntityRenderer<TileEnderShelf> {

  public EnderShelfRenderer(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileEnderShelf tile, float partialTicks, PoseStack ms, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
    Direction side = tile.getCurrentFacing();
    RenderTextUtil.alignRendering(ms, side);
    tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        renderSlot(tile, i, h.getStackInSlot(i), ms, buffer, combinedLightIn, combinedOverlayIn);
      }
    });
  }

  private void renderSlot(TileEnderShelf tile, int slot, ItemStack stack, PoseStack ms, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
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
      // 0xF000F0
      Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, combinedLightIn, combinedOverlayIn, ms, buffer, tile.getLevel(), combinedLightIn);
      ms.popPose();
    }
    else if (tile.renderStyle == RenderTextType.TEXT) {
      if (tile.inventory.nameCache[slot] == null || tile.inventory.nameCache[slot].isEmpty()) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(stack));
        for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
          tile.inventory.nameCache[slot] = entry.getKey().getFullname(entry.getValue()).getString();
          break;
        }
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
  }

  private float getScaleFactor(String displayName) {
    int lv = 17;
    if (displayName.length() > lv) {
      return 1.0F - 1 / 36F * (displayName.length() - lv);
    }
    return 1.0F;
  }
}
