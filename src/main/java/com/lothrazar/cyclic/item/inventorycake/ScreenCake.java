package com.lothrazar.cyclic.item.inventorycake;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCake extends ScreenBase<ContainerCake> {

  public ScreenCake(ContainerCake screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    super.renderLabels(ms, mouseX, mouseY);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int row = 0; row < 3; row++) {
      for (int i = 0; i < 9; i++) {
        int y = 16 + row * Const.SQ;
        this.drawSlot(ms, 7 + i * Const.SQ, y);
      }
    }
  }
}
