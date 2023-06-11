package com.lothrazar.cyclic.block.fan;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFan extends ScreenBase<ContainerFan> {

  private ButtonMachineField btnRedstone;

  public ScreenFan(ContainerFan screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileFan.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    //
    int w = 160;
    int h = 20;
    int f = TileFan.Fields.SPEED.ordinal();
    x = leftPos + 8;
    y = topPos + 30;
    GuiSliderInteger speedsl = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, TileFan.MAX_SPEED, menu.tile.getField(f)));
    speedsl.setTooltip("cyclic.fan.speed");
    //    
    f = TileFan.Fields.RANGE.ordinal();
    x = leftPos + 8;
    y = topPos + 54;
    GuiSliderInteger rangesl = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, TileFan.MAX_RANGE, menu.tile.getField(f)));
    rangesl.setTooltip("cyclic.fan.range");
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 148, 8, TextureRegistry.SLOT_FILTER, 18);
  }
}
