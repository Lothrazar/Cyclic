package com.lothrazar.cyclic.block.fan;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.components.Tooltip;

public class ScreenFan extends ScreenBase<ContainerFan> {

  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;

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
    x += 20;
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, TileFan.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //
    int w = 140;
    int h = 20;
    int f = TileFan.Fields.SPEED.ordinal();
    x = leftPos + 8;
    y = topPos + 30;
    GuiSliderInteger speedsl = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, TileFan.MAX_SPEED, menu.tile.getField(f)));
    speedsl.setTooltip(Tooltip.create(Component.translatable("cyclic.fan.speed")));
    //    
    f = TileFan.Fields.RANGE.ordinal();
    x = leftPos + 8;
    y = topPos + 54;
    GuiSliderInteger rangesl = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, TileFan.MAX_RANGE, menu.tile.getField(f)));
    rangesl.setTooltip(Tooltip.create(Component.translatable("cyclic.fan.range")));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
  }

  @Override
  protected void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 148, 8, TextureRegistry.SLOT_FILTER, 18);
  }
}
