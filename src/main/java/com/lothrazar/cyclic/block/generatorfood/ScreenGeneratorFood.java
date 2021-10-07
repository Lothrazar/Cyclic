package com.lothrazar.cyclic.block.generatorfood;

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

public class ScreenGeneratorFood extends ScreenBase<ContainerGeneratorFood> {

  private ButtonMachine btnToggle;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private TimerBar timer;

  public ScreenGeneratorFood(ContainerGeneratorFood screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileGeneratorFood.MAX);
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
    btnRedstone = addWidget(new ButtonMachineField(x, y, TileGeneratorFood.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    x = leftPos + 132;
    y = topPos + 8;
    btnToggle = addWidget(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      int f = TileGeneratorFood.Fields.FLOWING.ordinal();
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
    timer.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getField(TileGeneratorFood.Fields.TIMER.ordinal()));
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int fld = TileGeneratorFood.Fields.FLOWING.ordinal();
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + menu.tile.getField(fld)));
    btnToggle.setTextureId(menu.tile.getField(fld) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.drawSlot(ms, 54, 34); 
    this.drawSlotLarge(ms, 70, 30);
    energy.draw(ms, menu.tile.getEnergy());
    timer.capacity = menu.tile.getField(TileGeneratorFood.Fields.BURNMAX.ordinal());
    timer.visible = (timer.capacity > 0);
    timer.draw(ms, menu.tile.getField(TileGeneratorFood.Fields.TIMER.ordinal()));
  }
}
