package com.lothrazar.cyclic.block.structurewritercopy;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenReaderCopy extends ScreenBase<ContainerReaderCopy> {

  public ScreenReaderCopy(ContainerReaderCopy screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.ySize = 256 - 21;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawButtonTooltips(mouseX, mouseY);
    this.drawName(this.title.getFormattedText());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY_LARGE_PLAIN);
    this.drawSlot(27, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(43, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(67, 28, TextureRegistry.SLOT_LARGE, 26);
    for (int s = 0; s < 9; s++) {
      this.drawSlot(7 + 18 * s, 111, TextureRegistry.SLOT, 18);
    }
    for (int s = 0; s < 9; s++) {
      this.drawSlot(7 + 18 * s, 111 + 18, TextureRegistry.SLOT, 18);
    }
  }
}
