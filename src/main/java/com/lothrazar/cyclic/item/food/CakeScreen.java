package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CakeScreen extends ScreenBase<CakeContainer> {

  public CakeScreen(CakeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int row = 0; row < 3; row++)
      for (int i = 0; i < 9; i++) {
        int y = 16 + row * Const.SQ;
        this.drawSlot(ms, 7 + i * Const.SQ, y);
      }
  }
}
