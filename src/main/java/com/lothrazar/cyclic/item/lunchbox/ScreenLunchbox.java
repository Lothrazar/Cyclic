package com.lothrazar.cyclic.item.lunchbox;

import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ScreenLunchbox extends ScreenBase<ContainerLunchbox> {

  public ScreenLunchbox(ContainerLunchbox screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int x, int y, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int colPos = 0; colPos < ItemLunchbox.SLOTS; colPos++) {
      this.drawSlot(ms, 25 + colPos * Const.SQ, 35);
    }
  }

  public static int getColour(ItemStack stack) {
    if (shouldHideLayer(stack)) {
      return 0x00000000;
    }
    return 0xFFFFFFFF;
  }

  private static boolean shouldHideLayer(ItemStack stack) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.screen instanceof ScreenLunchbox lunchScreen) {
      if (lunchScreen.getMenu().bag == stack) {
        return true;
      }
    }
    if (mc.player != null && mc.screen instanceof AbstractContainerScreen<?> && !(mc.screen instanceof CreativeModeInventoryScreen)) {
      ItemStack carried = mc.player.containerMenu.getCarried();
      if (!carried.isEmpty() && carried.getFoodProperties(mc.player) != null) {
        return true;
      }
    }
    return false;
  }
}
