package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenScreentext extends ScreenBase<ContainerScreentext> {

  private ButtonMachineField btnRedstone;
  private TextBoxAutosave txtString;

  public ScreenScreentext(ContainerScreentext screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x = leftPos + 6;
    int y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileScreentext.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    int w = 140;
    x = leftPos + 28;
    y = topPos + 8;
    txtString = new TextBoxAutosave(this.font, x, y, w, menu.tile.getBlockPos(), 0);
    txtString.setValue(menu.tile.getFieldString(0));
    this.addRenderableWidget(txtString);
    w = 160;
    x = leftPos + 8;
    y = topPos + 28;
    int h = 20;
    int f = TileScreentext.Fields.RED.ordinal();
    GuiSliderInteger red = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 255, menu.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.GREEN.ordinal();
    GuiSliderInteger green = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 255, menu.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.BLUE.ordinal();
    GuiSliderInteger blue = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 255, menu.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.PADDING.ordinal();
    GuiSliderInteger pad = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 20, menu.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.FONT.ordinal();
    GuiSliderInteger font = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 100, menu.tile.getField(f)));
    y += h + 1;
    f = TileScreentext.Fields.OFFSET.ordinal();
    GuiSliderInteger offset = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 10, menu.tile.getField(f)));
    red.setTooltip("cyclic.screen.red");
    green.setTooltip("cyclic.screen.green");
    blue.setTooltip("cyclic.screen.blue");
    font.setTooltip("cyclic.screen.font");
    offset.setTooltip("cyclic.screen.offset");
    pad.setTooltip("cyclic.screen.padding");
  }
  //  @Override
  //  public void tick() {
  //    this.txtString.tick();
  //  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_PLAIN);
    this.txtString.render(ms, mouseX, mouseY, partialTicks);
  }
}
