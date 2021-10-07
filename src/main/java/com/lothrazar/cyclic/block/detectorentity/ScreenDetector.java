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
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenDetector extends ScreenBase<ContainerDetector> {

  private ButtonMachine btnEntity;
  private ButtonMachine btnComp;
  private ButtonMachineField btnRender;

  public ScreenDetector(ContainerDetector screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = leftPos + 8;
    y = topPos + 18;
    btnRender = addWidget(new ButtonMachineField(x, y, TileDetector.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x += 22;
    int w = 50, h = 20;
    btnEntity = addWidget(new ButtonMachine(x, y, 50, 20, "", (p) -> {
      int f = TileDetector.Fields.ENTITYTYPE.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          menu.tile.getField(f) + 1, menu.tile.getBlockPos()));
    }));
    x += 58;
    btnComp = addWidget(new ButtonMachine(x, y, 50, 20, "", (p) -> {
      int f = TileDetector.Fields.GREATERTHAN.ordinal();
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f,
          menu.tile.getField(f) + 1, menu.tile.getBlockPos()));
    }));
    //sliders
    w = 160;
    h = 20;
    x = leftPos + 8;
    y += h + 1;
    int f = TileDetector.Fields.RANGEX.ordinal();
    GuiSliderInteger red = this.addWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    red.setTooltip("cyclic.detector.rangex");
    y += h + 1;
    f = TileDetector.Fields.RANGEY.ordinal();
    GuiSliderInteger rangey = this.addWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangey.setTooltip("cyclic.detector.rangey");
    y += h + 1;
    f = TileDetector.Fields.RANGEZ.ordinal();
    GuiSliderInteger rangez = this.addWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangez.setTooltip("cyclic.detector.rangez");
    y += h + 1;
    f = TileDetector.Fields.LIMIT.ordinal();
    GuiSliderInteger limit = this.addWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    limit.setTooltip("cyclic.detector.limit");
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
    btnRender.onValueUpdate(menu.tile);
    btnEntity.setTooltip(UtilChat.lang("cyclic.detector.entitytype.tooltip"));
    btnEntity.setMessage(UtilChat.ilang("cyclic.entitytype." + menu.tile.entityFilter.name().toLowerCase()));
    btnComp.setTooltip(UtilChat.lang("cyclic.detector.compare.tooltip"));
    btnComp.setMessage(UtilChat.ilang("cyclic.detector.compare" +
        menu.tile.getField(TileDetector.Fields.GREATERTHAN.ordinal())));
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_PLAIN);
  }
}
