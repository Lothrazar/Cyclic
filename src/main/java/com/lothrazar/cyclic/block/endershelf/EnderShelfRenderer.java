package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.util.UtilRenderText;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class EnderShelfRenderer extends TileEntityRenderer<TileEnderShelf> {

  private static final float OFFSET = -0.01F;

  public EnderShelfRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
    super(rendererDispatcherIn);
  }

  @Override
  public void render(TileEnderShelf tile, float partialTicks, MatrixStack ms,
                     IRenderTypeBuffer buffer, int light, int overlayLight) {
    Direction side = tile.getCurrentFacing();


    UtilRenderText.alignRendering(ms, side);
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        renderSlot(i, h.getStackInSlot(i), ms, buffer, light);
      }
    });

  }

  private void renderSlot(int slot, ItemStack itemStack, MatrixStack ms, IRenderTypeBuffer buffer, int light) {
    if (itemStack.isEmpty())
      return;
    ms.push();
    FontRenderer fontRenderer = this.renderDispatcher.getFontRenderer();
    AtomicReference<String> displayName = new AtomicReference<>("Nothing");
    String displayCount = "x" + itemStack.getCount();

    displayName.set(itemStack.getDisplayName().getString());
    if (!itemStack.isEmpty()) {
      Map<Enchantment, Integer> enchantments = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(itemStack));
      enchantments.forEach((enchantment, level) -> {
        displayName.set(enchantment.getDisplayName(level).getString());
      });
    }

    double x = 1.5F/16F;
    double y = (3 * slot+2) / 16F;
    float scale = 0.1F * getScaleFactor(displayName.get());
    ms.translate(x, y, 1 - OFFSET);
    ms.scale(1 / 16f * scale, -1 / 16f * scale, 0.00005f);
    fontRenderer.renderString(displayName.get(), 0, 0, 421025,
            false, ms.getLast().getMatrix(), buffer, false, 0, light);
    ms.pop();
    ms.push();
    scale = 0.1F;
    ms.translate(x, y, 1 - OFFSET);
    ms.scale(1 / 16f * scale, -1 / 16f * scale, 0.00005f);
    fontRenderer.renderString(displayCount, 110, 0, 421025,
            false, ms.getLast().getMatrix(), buffer, false, 0, light);
    ms.pop();
  }

  private float getScaleFactor(String displayName) {
    if (displayName.length() > 18)
      return 1.0F - 1/36F * (displayName.length() - 18);
    return 1.0F;
  }
}
