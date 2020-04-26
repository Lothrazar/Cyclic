package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.battery.TileBattery.Fields;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.datafix.fixes.ChunkPaletteFormat.Direction;
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
    this.energy = new EnergyBar(this);
    energy.max = TileBattery.MAX;
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    int x = guiLeft + 132, y = guiTop + 8; 
    int size = 14;
    btnToggle = addButton(new ButtonMachine(x, y, size, size, "", (p) -> {
      container.tile.setFlowing((container.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(0, container.tile.getFlowing(), container.tile.getPos()));
    }));   x = guiLeft + 18;
    y = guiTop + 18;
    btnU = addButton(new ButtonMachine(x, y, size, size, "U", (p) -> {
      int f = Fields.U.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnU.setTooltip(Direction.UP.name());
    y = guiTop + 60;
    btnD = addButton(new ButtonMachine(x, y, size, size, "D", (p) -> {
      int f = Fields.D.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnD.setTooltip(Direction.DOWN.name());
    int xCenter = guiLeft + 80;
    int yCenter = guiTop + 38;
    int space = 18;
    x = xCenter;//north
    y = yCenter - space;
    btnN = addButton(new ButtonMachine(x, y, size, size, "N", (p) -> {
      int f = Fields.N.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnN.setTooltip(Direction.NORTH.name());
    x = xCenter;
    y = yCenter + space;
    btnS = addButton(new ButtonMachine(x, y, size, size, "S", (p) -> {
      int f = Fields.S.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnS.setTooltip(Direction.SOUTH.name());
    //now east west
    x = xCenter + space;
    y = yCenter;
    btnE = addButton(new ButtonMachine(x, y, size, size, "E", (p) -> {
      int f = Fields.E.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnE.setTooltip(Direction.EAST.name());
    x = xCenter - space;
    y = yCenter;
    btnW = addButton(new ButtonMachine(x, y, size, size, "W", (p) -> {
      int f = Fields.W.ordinal();
      container.tile.setField(f, container.tile.getField(f) + 1);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
    btnW.setTooltip(Direction.WEST.toString() );
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    energy.renderHoveredToolTip(mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + container.getFlowing()));
    btnToggle.setTextureId(container.getFlowing() == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
    btnU.setTextureId(getTextureId(Fields.U));
    btnD.setTextureId(getTextureId(Fields.D));
    btnN.setTextureId(getTextureId(Fields.N));
    btnS.setTextureId(getTextureId(Fields.S));
    btnE.setTextureId(getTextureId(Fields.E));
    btnW.setTextureId(getTextureId(Fields.W));
//    
//    btnU.setTooltip("gui.cyclic.flowing.up" + container.tile.getField(Fields.D.ordinal()));
//    btnD.setTooltip("gui.cyclic.flowing.down" + container.tile.getField(Fields.D.ordinal()));
//    btnN.setTooltip("gui.cyclic.flowing.north" + container.tile.getField(Fields.N.ordinal()));
//    btnE.setTooltip("gui.cyclic.flowing.east" + container.tile.getField(Fields.E.ordinal()));
//    btnS.setTooltip("gui.cyclic.flowing.south" + container.tile.getField(Fields.S.ordinal()));
//    btnW.setTooltip("gui.cyclic.flowing.west" + container.tile.getField(Fields.W.ordinal()));
    this.drawButtonTooltips(mouseX, mouseY);
  }  private TextureEnum getTextureId(Enum<Fields> field) {
    return container.tile.getField(field.ordinal()) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP;
  }


  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(CyclicRegistry.Textures.GUI);
    energy.renderEnergy(container.getEnergy());
  }
}
