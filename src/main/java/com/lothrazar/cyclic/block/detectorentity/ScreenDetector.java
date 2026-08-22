package com.lothrazar.cyclic.block.detectorentity;

import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.item.datacard.EntityDataCard;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ScreenDetector extends ScreenBase<ContainerDetector> {

  private ButtonMachine btnEntity;
  private ButtonMachine btnComp;
  private ButtonMachineField btnRender;

  public ScreenDetector(ContainerDetector screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn, 176, 214);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = leftPos + 8;
    y = topPos + 18;
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, TileDetector.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    x += 22;
    int w = 50, h = 20;
    btnEntity = addRenderableWidget(new ButtonMachine(x, y, w, h, "", (p) -> {
      int f = TileDetector.Fields.ENTITYTYPE.ordinal();
      ClientPacketDistributor.sendToServer(new PacketTileData(f,
          menu.tile.getField(f) + 1, menu.tile.getBlockPos()));
    }));
    x += 58;
    btnComp = addRenderableWidget(new ButtonMachine(x, y, w, h, "", (p) -> {
      int f = TileDetector.Fields.GREATERTHAN.ordinal();
      ClientPacketDistributor.sendToServer(new PacketTileData(f,
          menu.tile.getField(f) + 1, menu.tile.getBlockPos()));
    }));
    //sliders
    w = 160;
    h = 18;
    x = leftPos + 8;
    y += h + 4;
    int f = TileDetector.Fields.RANGEX.ordinal();
    GuiSliderInteger red = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    red.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.rangex")));
    y += h + 1;
    f = TileDetector.Fields.RANGEY.ordinal();
    GuiSliderInteger rangey = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangey.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.rangey")));
    y += h + 1;
    f = TileDetector.Fields.RANGEZ.ordinal();
    GuiSliderInteger rangez = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    rangez.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.rangez")));
    y += h + 1;
    f = TileDetector.Fields.LIMIT.ordinal();
    GuiSliderInteger limit = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        0, 64, menu.tile.getField(f)));
    limit.setTooltip(Tooltip.create(Component.translatable("cyclic.detector.limit")));
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
    //when a filled entity_data card is in the filter slot, the Type button is ignored - disable it
    boolean hasCard = EntityDataCard.hasEntity(menu.tile.filter.getStackInSlot(0));
    btnEntity.active = !hasCard;
    if (hasCard) {
      btnEntity.setTooltip(ChatUtil.lang("cyclic.detector.entitytype.disabled.tooltip"));
      btnEntity.setMessage(ChatUtil.ilang("cyclic.detector.entitytype.card"));
    }
    else {
      btnEntity.setTooltip(ChatUtil.lang("cyclic.detector.entitytype.tooltip"));
      btnEntity.setMessage(ChatUtil.ilang("cyclic.entitytype." + menu.tile.entityFilter.name().toLowerCase()));
    }
    btnComp.setTooltip(ChatUtil.lang("cyclic.detector.compare.tooltip"));
    btnComp.setMessage(ChatUtil.ilang("cyclic.detector.compare" +
        menu.tile.getField(TileDetector.Fields.GREATERTHAN.ordinal())));
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_MEDIUM);
    this.drawSlot(ms, 151, 6, TextureRegistry.SLOT_FILTER, 18);
  }
}
