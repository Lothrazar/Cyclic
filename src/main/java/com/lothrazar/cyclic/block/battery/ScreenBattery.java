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
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenBattery extends ScreenBase<ContainerBattery> {

  private ButtonMachine btnToggle;
  private EnergyBar energy;
  private ButtonMachine btnU;
  private ButtonMachine btnD;
  private ButtonMachine btnN;
  private ButtonMachine btnS;
  private ButtonMachine btnE;
  private ButtonMachine btnW;

  public ScreenBattery(ContainerBattery screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileBattery.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    int x = guiLeft + 132, y = guiTop + 8;
    int size = 14;
    btnToggle = addButton(new ButtonMachine(x, y, size, size, "", (p) -> {
      container.tile.setFlowing((container.tile.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(Fields.FLOWING.ordinal(), container.tile.getFlowing(), container.tile.getPos()));
    }));
    x = guiLeft + 18;
    y = guiTop + 18;
    btnU = addButton(new ButtonMachine(x, y, size, size, "U", (p) -> {
      int f = Fields.U.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnU.setTooltip(UtilChat.lang("gui.cyclic.flowing.up"));
    y = guiTop + 60;
    btnD = addButton(new ButtonMachine(x, y, size, size, "D", (p) -> {
      int f = Fields.D.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnD.setTooltip(UtilChat.lang("gui.cyclic.flowing.down"));
    int xCenter = guiLeft + 80;
    int yCenter = guiTop + 38;
    int space = 18;
    x = xCenter;
    y = yCenter - space;
    btnN = addButton(new ButtonMachine(x, y, size, size, "N", (p) -> {
      int f = Fields.N.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnN.setTooltip(UtilChat.lang("gui.cyclic.flowing.north"));
    x = xCenter;
    y = yCenter + space;
    btnS = addButton(new ButtonMachine(x, y, size, size, "S", (p) -> {
      int f = Fields.S.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnS.setTooltip(UtilChat.lang("gui.cyclic.flowing.south"));
    //now east west
    x = xCenter + space;
    y = yCenter;
    btnE = addButton(new ButtonMachine(x, y, size, size, "E", (p) -> {
      int f = Fields.E.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnE.setTooltip(UtilChat.lang("gui.cyclic.flowing.east"));
    x = xCenter - space;
    y = yCenter;
    btnW = addButton(new ButtonMachine(x, y, size, size, "W", (p) -> {
      int f = Fields.W.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnW.setTooltip(UtilChat.lang("gui.cyclic.flowing.west"));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + container.tile.getFlowing()));
    btnToggle.setTextureId(container.tile.getFlowing() == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
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
    return container.tile.getField(field.ordinal()) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.drawSlot(60, 20);
    energy.draw(ms, container.getEnergy());
  }
}
