package com.lothrazar.cyclic.block.structurewriter;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.structurewriter.TileWriter.StructureStatus;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenWriter extends ScreenBase<ContainerWriter> {

  public ScreenWriter(ContainerWriter screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
    StructureStatus status = this.container.tile.structStatus;
    this.drawString(UtilChat.lang(status.name()), 20, 20);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    //    this.drawSlot(3, 21, TextureRegistry.SLOT, 18);
  }
}
