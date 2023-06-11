package com.lothrazar.cyclic.block.clock;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenClock extends ScreenBase<ContainerClock> {

  private ButtonMachineField btnRedstone;

  public ScreenClock(ContainerClock screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.imageHeight = 256;
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileRedstoneClock.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    int w = 160;
    int h = 20;
    int f = TileRedstoneClock.Fields.DURATION.ordinal();
    x = leftPos + 8;
    y = topPos + 38;
    GuiSliderInteger dur = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 200, menu.tile.getField(f)));
    dur.setTooltip("cyclic.clock.duration");
    y += 26;
    f = TileRedstoneClock.Fields.DELAY.ordinal();
    GuiSliderInteger delay = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 200, menu.tile.getField(f)));
    delay.setTooltip("cyclic.clock.delay");
    y += 26;
    f = TileRedstoneClock.Fields.POWER.ordinal();
    GuiSliderInteger power = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 15, menu.tile.getField(f)));
    power.setTooltip("cyclic.clock.power");
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
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    //    this.txtDuration.render(ms, mouseX, mouseX, partialTicks);
    //    this.txtDelay.render(ms, mouseX, mouseX, partialTicks);
    //    this.txtPower.render(ms, mouseX, mouseX, partialTicks);
  }
}
