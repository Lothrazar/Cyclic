package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.util.UtilRenderText;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;

public class EnderShelfRenderer extends TileEntityRenderer<TileEnderShelf> {

  public EnderShelfRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
    super(rendererDispatcherIn);
  }

  @Override
  public void render(TileEnderShelf tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlayLight) {
    Direction side = tile.getCurrentFacing();
    UtilRenderText.alignRendering(ms, side);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        renderSlot(tile, i, h.getStackInSlot(i), ms, buffer, light);
      }
    });
    //    if (EnderShelfHelper.isController(tile.getBlockState())) { 
    //      EnderControllerItemHandler h = EnderShelfHelper.getControllerHandler(tile);
    //      if (h != null) {
    //        int count = tile.getShelves().size();
    //        String shelves = count == 1 ? " shelf" : " shelves"; // TODO lang entry
    //        ms.push();
    //        FontRenderer fontRenderer = this.renderDispatcher.getFontRenderer();
    //        double x = 1.5F / 16F;
    //        double y = 14 / 16F;
    //        float scale = 0.1F;
    //        ms.translate(x, y, 1 - OFFSET);
    //        ms.scale(1 / 16f * scale, -1 / 16f * scale, 0.00005f);
    //        fontRenderer.renderString(count + shelves, 0, 0, 421025, false, ms.getLast().getMatrix(), buffer, false, 0, light);
    //        ms.pop();
    //      }
    //    }
  }

  private void renderSlot(TileEnderShelf tile, int slot, ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffer, int light) {
    if (stack.isEmpty()) {
      return;
    }
    final float sh = 16F;
    final int color = 0;
    final double x = 1.5F / sh;
    final double y = (3 * slot + 2) / sh;
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
      // 0xF000F0
      Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, 0x500050, light, ms, buffer);
      ms.pop();
    }
    else if (tile.renderStyle == RenderTextType.TEXT) {
      if (tile.inventory.nameCache[slot] == null || tile.inventory.nameCache[slot].isEmpty()) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(stack));
        for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
          tile.inventory.nameCache[slot] = entry.getKey().getDisplayName(entry.getValue()).getString();
          break;
        }
      }
      String displayName = tile.inventory.nameCache[slot];
      if (displayName.isEmpty()) {
        displayName = stack.getDisplayName().getString();
      }
      final float scaleName = 0.02832999F + 0.1F * getScaleFactor(displayName);
      ms.push();
      ms.translate(x, y, 1.01F);
      ms.scale(1 / sh * scaleName, -1 / sh * scaleName, 0.00005F);
      fontRenderer.renderString(displayName, 0, 0, color, false, ms.getLast().getMatrix(), buffer, false, 0, light);
      ms.pop();
    }
    if (tile.renderStyle != RenderTextType.NONE) {
      //render stack count
      ms.push();
      ms.translate(x + 0.081F, y, 1.01F);
      ms.scale(1 / sh * scaleNum, -1 / sh * scaleNum, 0.00005F);
      String displayCount = "x" + stack.getCount();
      fontRenderer.renderString(displayCount, 110, 0, color, false, ms.getLast().getMatrix(), buffer, false, 0, light);
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
