package com.lothrazar.cyclic.block.fan;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenFan extends ScreenBase<ContainerFan> {

  private ButtonMachineRedstone btnRedstone;

  public ScreenFan(ContainerFan screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileFan.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    //
    int w = 160;
    int h = 20;
    int f = TileFan.Fields.SPEED.ordinal();
    x = guiLeft + 8;
    y = guiTop + 30;
    GuiSliderInteger speedsl = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, TileFan.MAX_SPEED, container.tile.getField(f)));
    speedsl.setTooltip("cyclic.fan.speed");
    //    
    f = TileFan.Fields.RANGE.ordinal();
    x = guiLeft + 8;
    y = guiTop + 54;
    GuiSliderInteger rangesl = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, TileFan.MAX_RANGE, container.tile.getField(f)));
    rangesl.setTooltip("cyclic.fan.range");
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
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
  }
}
