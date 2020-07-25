package com.lothrazar.cyclic.block.structurewritercopy;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenReaderCopy extends ScreenBase<ContainerReaderCopy> {

  public ScreenReaderCopy(ContainerReaderCopy screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.ySize = 256 - 21;
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.func_230459_a_(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    this.drawSlot(ms, 27, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 43, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 67, 28, TextureRegistry.SLOT_LARGE, 26);
    for (int s = 0; s < 9; s++) {
      this.drawSlot(ms, 7 + 18 * s, 111, TextureRegistry.SLOT, 18);
    }
    for (int s = 0; s < 9; s++) {
      this.drawSlot(ms, 7 + 18 * s, 111 + 18, TextureRegistry.SLOT, 18);
    }
  }
}
