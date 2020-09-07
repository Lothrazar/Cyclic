package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenDetectorItem extends ScreenBase<ContainerDetectorItem> {

  private ButtonMachine btnComp;
  private ButtonMachineRedstone btnRender;

  //TODO: unique items vs total items?
  public ScreenDetectorItem(ContainerDetectorItem screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 18;
    btnRender = addButton(new ButtonMachineRedstone(x, y, TileDetectorItem.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x += 22;
    btnComp = addButton(new ButtonMachine(x, y, 50, 20, "", (p) -> {
      int f = TileDetectorItem.Fields.GREATERTHAN.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          container.tile.getField(f) + 1, container.tile.getPos()));
    }));
    //x 
    //sliders
    int w = 160;
    int h = 20;
    x = guiLeft + 8;
    y += h + 1;
    int f = TileDetectorItem.Fields.RANGEX.ordinal();
    GuiSliderInteger red = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    red.setTooltip("cyclic.detector.rangex");
    //
    y += h + 1;
    f = TileDetectorItem.Fields.RANGEY.ordinal();
    GuiSliderInteger RANGEY = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    RANGEY.setTooltip("cyclic.detector.rangey");
    y += h + 1;
    f = TileDetectorItem.Fields.RANGEZ.ordinal();
    GuiSliderInteger RANGEZ = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    RANGEZ.setTooltip("cyclic.detector.rangez");
    //
    y += h + 1;
    f = TileDetectorItem.Fields.LIMIT.ordinal();
    GuiSliderInteger LIMIT = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    LIMIT.setTooltip("cyclic.detector.limit");
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
    btnRender.onValueUpdate(container.tile);
    btnComp.setTooltip(UtilChat.lang("cyclic.detector.compare.tooltip"));
    btnComp.setMessage(UtilChat.ilang("cyclic.detector.compare" +
        container.tile.getField(TileDetectorItem.Fields.GREATERTHAN.ordinal())));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_PLAIN);
  }
}
