package com.lothrazar.cyclic.block.structurereadercreative;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenReader extends ScreenBase<ContainerReader> {

  public ScreenReader(ContainerReader screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
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
    this.drawBackground(TextureRegistry.INVENTORY);
    this.drawSlot(27, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(43, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(67, 28, TextureRegistry.SLOT_LARGE, 18);
  }
}
