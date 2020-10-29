package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenShapedata extends ScreenBase<ContainerShapedata> {

  public ScreenShapedata(ContainerShapedata screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 8, 28 + 18, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 8 + 18, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 70, 38, TextureRegistry.SLOT_SHAPE, 18);
    //    this.drawSlot(ms, 60, 68, TextureRegistry.SLOT_SHAPE, 18);
  }
}
