package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.components.Tooltip;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ScreenDetectorItem extends ScreenBase<ContainerDetectorItem> {

  private ButtonMachine btnComp;
  private ButtonMachineField btnRender;

  public ScreenDetectorItem(ContainerDetectorItem screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.imageHeight = 214;
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
    int h = 20, w=50;
    btnComp = addRenderableWidget(new ButtonMachine(x, y, w, h, "", (p) -> {
      int f = TileDetectorItem.Fields.GREATERTHAN.ordinal();
      ClientPacketDistributor.sendToServer(new PacketTileData(f,
          menu.tile.getField(f) + 1, menu.tile.getBlockPos()));
    }));
    //x 
    //sliders
      w = 160;
      h = 18;
    x = leftPos + 8;
    y += h + 4;
    int f = TileDetectorItem.Fields.RANGEX.ordinal();
    GuiSliderInteger red = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    red.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.rangex")));
    //
    y += h + 1;
    f = TileDetectorItem.Fields.RANGEY.ordinal();
    GuiSliderInteger rangey = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangey.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.rangey")));
    y += h + 1;
    f = TileDetectorItem.Fields.RANGEZ.ordinal();
    GuiSliderInteger rangez = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangez.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.rangez")));
    //
    y += h + 1;
    f = TileDetectorItem.Fields.LIMIT.ordinal();
    GuiSliderInteger limitsl = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    limitsl.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.limit")));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRender.onValueUpdate(menu.tile);
    btnComp.setTooltip(ChatUtil.lang("cyclic.detector.compare.tooltip"));
    btnComp.setMessage(ChatUtil.ilang("cyclic.detector.compare" +
        menu.tile.getField(TileDetectorItem.Fields.GREATERTHAN.ordinal())));
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_MEDIUM);
    this.drawSlot(ms, 151, 6, TextureRegistry.SLOT_FILTER, 18);
  }
}
