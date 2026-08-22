package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenItemCollector extends ScreenBase<ContainerItemCollector> {

  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private ButtonMachineField btnDirection;
  private GuiSliderInteger sizeSlider;
  private GuiSliderInteger heightslider;

  public ScreenItemCollector(ContainerItemCollector screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn, 176, 214);
  }

  @Override
  public void init() {
    super.init();
    int x = leftPos + 6;
    int y = topPos + 6;
    int f = TileItemCollector.Fields.REDSTONE.ordinal();
    int h = 20;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, f, menu.tile.getBlockPos()));
    f = TileItemCollector.Fields.RENDER.ordinal();
    y += h;
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //then toggle
    f = TileItemCollector.Fields.DIRECTION.ordinal();
    y += h;
    btnDirection = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"));
    int w = 140;
    //now start sliders
    //
    x = leftPos + 30;
    y = topPos + 32;
    f = TileItemCollector.Fields.HEIGHT.ordinal();
    heightslider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, TileItemCollector.Fields.HEIGHT.ordinal(), menu.tile.getBlockPos(),
        0, TileItemCollector.MAX_HEIGHT, menu.tile.getField(f)));
    heightslider.setTooltip(Tooltip.create(Component.translatable("buildertype.height.tooltip")));
    //then size
    f = TileItemCollector.Fields.SIZE.ordinal();
    y += h + 4;
    sizeSlider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, TileItemCollector.Fields.SIZE.ordinal(),
        menu.tile.getBlockPos(), 0, TileItemCollector.MAX_SIZE, menu.tile.getField(f)));
    sizeSlider.setTooltip(Tooltip.create(Component.translatable("buildertype.size.tooltip")));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    btnDirection.onValueUpdate(menu.tile);
    sizeSlider.setTooltip("cyclic.screen.size" + menu.tile.getField(sizeSlider.getField()));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnDirection.visible = !menu.tile.getBlockStateVertical();
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_MEDIUM);
    for (int i = 0; i < 9; i++) {
      int y = 81;
      this.drawSlot(ms, 7 + i * Const.SQ, y);
      this.drawSlot(ms, 7 + i * Const.SQ, y + Const.SQ);
    }
    this.drawSlot(ms, 151, 8, TextureRegistry.SLOT_FILTER, 18);
  }
}
