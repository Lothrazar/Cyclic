package com.lothrazar.cyclic.block.detectorentity;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenDetector extends ScreenBase<ContainerDetector> {

  private ButtonMachine btnEntity;
  private ButtonMachine btnComp;
  private ButtonMachineField btnRender;

  public ScreenDetector(ContainerDetector screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 18;
    btnRender = addButton(new ButtonMachineField(x, y, TileDetector.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x += 22;
    int w = 50, h = 20;
    btnEntity = addButton(new ButtonMachine(x, y, 50, 20, "", (p) -> {
      int f = TileDetector.Fields.ENTITYTYPE.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          container.tile.getField(f) + 1, container.tile.getPos()));
    }));
    x += 58;
    btnComp = addButton(new ButtonMachine(x, y, 50, 20, "", (p) -> {
      int f = TileDetector.Fields.GREATERTHAN.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          container.tile.getField(f) + 1, container.tile.getPos()));
    }));
    //sliders
    w = 160;
    h = 20;
    x = guiLeft + 8;
    y += h + 1;
    int f = TileDetector.Fields.RANGEX.ordinal();
    GuiSliderInteger red = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    red.setTooltip("cyclic.detector.rangex");
    y += h + 1;
    f = TileDetector.Fields.RANGEY.ordinal();
    GuiSliderInteger rangey = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    rangey.setTooltip("cyclic.detector.rangey");
    y += h + 1;
    f = TileDetector.Fields.RANGEZ.ordinal();
    GuiSliderInteger rangez = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    rangez.setTooltip("cyclic.detector.rangez");
    y += h + 1;
    f = TileDetector.Fields.LIMIT.ordinal();
    GuiSliderInteger limit = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        0, 64, container.tile.getField(f)));
    limit.setTooltip("cyclic.detector.limit");
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
    btnEntity.setTooltip(UtilChat.lang("cyclic.detector.entitytype.tooltip"));
    btnEntity.setMessage(UtilChat.ilang("cyclic.entitytype." + container.tile.entityFilter.name().toLowerCase()));
    btnComp.setTooltip(UtilChat.lang("cyclic.detector.compare.tooltip"));
    btnComp.setMessage(UtilChat.ilang("cyclic.detector.compare" +
        container.tile.getField(TileDetector.Fields.GREATERTHAN.ordinal())));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_PLAIN);
  }
}
