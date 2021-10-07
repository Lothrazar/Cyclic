package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.gui.TimerBar;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ScreenGeneratorFuel extends ScreenBase<ContainerGeneratorFuel> {

  private ButtonMachineField btnRedstone;
  private ButtonMachine btnToggle;
  private EnergyBar energy;
  private TimerBar timer;

  public ScreenGeneratorFuel(ContainerGeneratorFuel screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileGeneratorFuel.MAX);
    this.timer = new TimerBar(this, 70, 60, 1);
  }

  @Override
  public void init() {
    super.init();
    energy.visible = true; //TileGeneratorFuel.POWERCONF.get() > 0;
    timer.guiLeft = energy.guiLeft = leftPos;
    timer.guiTop = energy.guiTop = topPos;
    int x, y;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileGeneratorFuel.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    x = leftPos + 132;
    y = topPos + 8;
    btnToggle = addButton(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      int f = TileGeneratorFuel.Fields.FLOWING.ordinal();
      int tog = (menu.tile.getField(f) + 1) % 2;
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, tog, menu.tile.getBlockPos()));
    }));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    timer.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getField(TileGeneratorFuel.Fields.TIMER.ordinal()));
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int fld = TileGeneratorFuel.Fields.FLOWING.ordinal();
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + menu.tile.getField(fld)));
    btnToggle.setTextureId(menu.tile.getField(fld) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.drawSlot(ms, 54, 34); 
    this.drawSlotLarge(ms, 70, 30);
    energy.draw(ms, menu.tile.getEnergy());
    timer.capacity = menu.tile.getField(TileGeneratorFuel.Fields.BURNMAX.ordinal());
    timer.visible = (timer.capacity > 0);
    timer.draw(ms, menu.tile.getField(TileGeneratorFuel.Fields.TIMER.ordinal()));
  }
}
