package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.battery.TileBattery.Fields;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

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
    this.energy = new EnergyBar(this, TileBattery.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x = leftPos + 132, y = topPos + 8;
    int size = 14;
    btnToggle = addButton(new ButtonMachine(x, y, size, size, "", (p) -> {
      menu.tile.setFlowing((menu.tile.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(Fields.FLOWING.ordinal(), menu.tile.getFlowing(), menu.tile.getBlockPos()));
    }));
    x = leftPos + 18;
    y = topPos + 18;
    btnU = addButton(new ButtonMachine(x, y, size, size, "U", (p) -> {
      int f = Fields.U.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnU.setTooltip(UtilChat.lang("gui.cyclic.flowing.up"));
    y = topPos + 60;
    btnD = addButton(new ButtonMachine(x, y, size, size, "D", (p) -> {
      int f = Fields.D.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnD.setTooltip(UtilChat.lang("gui.cyclic.flowing.down"));
    int xCenter = leftPos + 80;
    int yCenter = topPos + 38;
    int space = 18;
    x = xCenter;
    y = yCenter - space;
    btnN = addButton(new ButtonMachine(x, y, size, size, "N", (p) -> {
      int f = Fields.N.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnN.setTooltip(UtilChat.lang("gui.cyclic.flowing.north"));
    x = xCenter;
    y = yCenter + space;
    btnS = addButton(new ButtonMachine(x, y, size, size, "S", (p) -> {
      int f = Fields.S.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnS.setTooltip(UtilChat.lang("gui.cyclic.flowing.south"));
    //now east west
    x = xCenter + space;
    y = yCenter;
    btnE = addButton(new ButtonMachine(x, y, size, size, "E", (p) -> {
      int f = Fields.E.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnE.setTooltip(UtilChat.lang("gui.cyclic.flowing.east"));
    x = xCenter - space;
    y = yCenter;
    btnW = addButton(new ButtonMachine(x, y, size, size, "W", (p) -> {
      int f = Fields.W.ordinal();
      menu.tile.setField(f, menu.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, menu.tile.getField(f), menu.tile.getBlockPos()));
    }));
    btnW.setTooltip(UtilChat.lang("gui.cyclic.flowing.west"));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + menu.tile.getFlowing()));
    btnToggle.setTextureId(menu.tile.getFlowing() == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
    //    btnU.setTooltip("gui.cyclic.flowing" + container.tile.getField(Fields.U.ordinal()));
    //    btnD.setTooltip("gui.cyclic.flowing" + container.tile.getField(Fields.D.ordinal()));
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
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.drawSlot(60, 20);
    energy.draw(ms, menu.getEnergy());
  }
}
