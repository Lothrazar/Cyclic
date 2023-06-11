package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenDetectorItem extends ScreenBase<ContainerDetectorItem> {

  private ButtonMachine btnComp;
  private ButtonMachineField btnRender;

  public ScreenDetectorItem(ContainerDetectorItem screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = leftPos + 8;
    y = topPos + 18;
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, TileDetectorItem.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x += 22;
    btnComp = addRenderableWidget(new ButtonMachine(x, y, 50, 20, "", (p) -> {
      int f = TileDetectorItem.Fields.GREATERTHAN.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          menu.tile.getField(f) + 1, menu.tile.getBlockPos()));
    }));
    //x 
    //sliders
    int w = 160;
    int h = 20;
    x = leftPos + 8;
    y += h + 1;
    int f = TileDetectorItem.Fields.RANGEX.ordinal();
    GuiSliderInteger red = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    red.setTooltip("cyclic.detector.rangex");
    //
    y += h + 1;
    f = TileDetectorItem.Fields.RANGEY.ordinal();
    GuiSliderInteger rangey = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangey.setTooltip("cyclic.detector.rangey");
    y += h + 1;
    f = TileDetectorItem.Fields.RANGEZ.ordinal();
    GuiSliderInteger rangez = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangez.setTooltip("cyclic.detector.rangez");
    //
    y += h + 1;
    f = TileDetectorItem.Fields.LIMIT.ordinal();
    GuiSliderInteger limitsl = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    limitsl.setTooltip("cyclic.detector.limit");
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
    btnRender.onValueUpdate(menu.tile);
    btnComp.setTooltip(ChatUtil.lang("cyclic.detector.compare.tooltip"));
    btnComp.setMessage(ChatUtil.ilang("cyclic.detector.compare" +
        menu.tile.getField(TileDetectorItem.Fields.GREATERTHAN.ordinal())));
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_PLAIN);
  }
}
