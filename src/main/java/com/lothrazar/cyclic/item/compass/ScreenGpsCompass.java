package com.lothrazar.cyclic.item.compass;

import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGpsCompass extends ScreenBase<ContainerGpsCompass> {

  public ScreenGpsCompass(ContainerGpsCompass container, Inventory inv, Component title) {
    super(container, inv, title);
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int x, int y, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 79, 35, TextureRegistry.SLOT_GPS);
  }
}
