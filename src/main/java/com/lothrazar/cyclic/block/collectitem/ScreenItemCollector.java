package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenItemCollector extends ScreenBase<ContainerItemCollector> {

  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private GuiSliderInteger sizeSlider;
  private ButtonMachineField btnDirection;
  private GuiSliderInteger heightslider;

  public ScreenItemCollector(ContainerItemCollector screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.imageHeight = 256;
  }

  @Override
  public void init() {
    super.init();
    int x = leftPos + 6;
    int y = topPos + 6;
    int f = TileItemCollector.Fields.REDSTONE.ordinal();
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, f, menu.tile.getBlockPos()));
    f = TileItemCollector.Fields.RENDER.ordinal();
    y += 20;
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"))
    //            .setSize(18)
    ;
    //then toggle
    f = TileItemCollector.Fields.DIRECTION.ordinal();
    y += 20;
    btnDirection = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.DIR_DOWN, TextureEnum.DIR_UPWARDS, "gui.cyclic.direction"))
    //.setSize(18)
    ;
    int w = 110;
    int h = 18;
    //now start sliders
    //
    y = topPos + 22;
    x = leftPos + 34;
    f = TileItemCollector.Fields.HEIGHT.ordinal();
    heightslider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, TileItemCollector.Fields.HEIGHT.ordinal(), menu.tile.getBlockPos(),
        0, TileItemCollector.MAX_HEIGHT, menu.tile.getField(f)));
    heightslider.setTooltip("buildertype.height.tooltip");
    //then size
    f = TileItemCollector.Fields.SIZE.ordinal();
    y += h + 1;
    sizeSlider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, TileItemCollector.Fields.SIZE.ordinal(),
        menu.tile.getBlockPos(), 0, TileItemCollector.MAX_SIZE, menu.tile.getField(f)));
    sizeSlider.setTooltip("buildertype.size.tooltip");
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    btnDirection.onValueUpdate(menu.tile);
    sizeSlider.setTooltip("cyclic.screen.size" + menu.tile.getField(sizeSlider.getField()));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    for (int i = 0; i < 9; i++) {
      int y = 81;
      this.drawSlot(ms, 7 + i * Const.SQ, y);
      this.drawSlot(ms, 7 + i * Const.SQ, y + Const.SQ);
    }
    this.drawSlot(ms, 151, 8, TextureRegistry.SLOT_FILTER, 18);
  }
}
