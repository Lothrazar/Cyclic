package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.block.battery.TileBattery.Fields;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenBattery extends ScreenBase<ContainerBattery> {

  private ButtonMachine btnToggle;
  private EnergyBar energy;
  private ButtonMachine btnU;
  private ButtonMachine btnD;
  private ButtonMachine btnN;
  private ButtonMachine btnS;
  private ButtonMachine btnE;
  private ButtonMachine btnW;

  public ScreenBattery(ContainerBattery screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TileBattery.MAX);
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x = leftPos + 132, y = topPos + 8;
    int size = 14;
    btnToggle = addRenderableWidget(new ButtonMachine(x, y, size, size, "", (p) -> {
      menu.tile.setFlowing((menu.tile.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(Fields.FLOWING.ordinal(), menu.tile.getFlowing(), menu.tile.getBlockPos()));
    }));
    x = leftPos + 18;
    y = topPos + 18;
    btnU = addRenderableWidget(new ButtonMachine(x, y, size, size, "", (p) -> {
      int f = Fields.U.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnU.setTooltip(ChatUtil.lang("gui.cyclic.flowing.up"));
    y = topPos + 60;
    btnD = addRenderableWidget(new ButtonMachine(x, y, size, size, "", (p) -> {
      int f = Fields.D.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnD.setTooltip(ChatUtil.lang("gui.cyclic.flowing.down"));
    int xCenter = leftPos + 80;
    int yCenter = topPos + 38;
    int space = 18;
    x = xCenter;
    y = yCenter - space;
    btnN = addRenderableWidget(new ButtonMachine(x, y, size, size, "", (p) -> {
      int f = Fields.N.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnN.setTooltip(ChatUtil.lang("gui.cyclic.flowing.north"));
    x = xCenter;
    y = yCenter + space;
    btnS = addRenderableWidget(new ButtonMachine(x, y, size, size, "", (p) -> {
      int f = Fields.S.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnS.setTooltip(ChatUtil.lang("gui.cyclic.flowing.south"));
    //now east west
    x = xCenter + space;
    y = yCenter;
    btnE = addRenderableWidget(new ButtonMachine(x, y, size, size, "", (p) -> {
      int f = Fields.E.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnE.setTooltip(ChatUtil.lang("gui.cyclic.flowing.east"));
    x = xCenter - space;
    y = yCenter;
    btnW = addRenderableWidget(new ButtonMachine(x, y, size, size, "", (p) -> {
      int f = Fields.W.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnW.setTooltip(ChatUtil.lang("gui.cyclic.flowing.west"));
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    btnToggle.setTooltip(ChatUtil.lang("gui.cyclic.flowing" + menu.tile.getFlowing()));
    btnToggle.setTextureId(menu.tile.getFlowing() == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
    btnU.setTextureId(getTextureId(Fields.U));
    btnD.setTextureId(getTextureId(Fields.D));
    btnN.setTextureId(getTextureId(Fields.N));
    btnS.setTextureId(getTextureId(Fields.S));
    btnE.setTextureId(getTextureId(Fields.E));
    btnW.setTextureId(getTextureId(Fields.W));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  private TextureEnum getTextureId(Enum<Fields> field) {
    return menu.tile.getField(field.ordinal()) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP;
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 133, 53, TextureRegistry.SLOT_CHARGE);
    energy.draw(ms, menu.getEnergy());
  }
}
