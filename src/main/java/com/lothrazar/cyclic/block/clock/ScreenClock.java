package com.lothrazar.cyclic.block.clock;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenClock extends ScreenBase<ContainerClock> {

  public ScreenClock(ContainerClock screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    int w = 160;
    int h = 20;
    int f = TileRedstoneClock.Fields.DURATION.ordinal();
    x = leftPos + 8;
    y = topPos + 18;
    GuiSliderInteger dur = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 200, menu.tile.getField(f)));
    dur.setTooltip("cyclic.clock.duration");
    y += 21;
    f = TileRedstoneClock.Fields.DELAY.ordinal();
    GuiSliderInteger delay = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 200, menu.tile.getField(f)));
    delay.setTooltip("cyclic.clock.delay");
    y += 21;
    f = TileRedstoneClock.Fields.POWER.ordinal();
    GuiSliderInteger power = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 15, menu.tile.getField(f)));
    power.setTooltip("cyclic.clock.power");
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.txtDuration.render(ms, mouseX, mouseX, partialTicks);
    //    this.txtDelay.render(ms, mouseX, mouseX, partialTicks);
    //    this.txtPower.render(ms, mouseX, mouseX, partialTicks);
  }
}
