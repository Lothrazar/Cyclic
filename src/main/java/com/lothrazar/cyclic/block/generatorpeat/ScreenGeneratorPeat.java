package com.lothrazar.cyclic.block.generatorpeat;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.melter.TileMelter;
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

public class ScreenGeneratorPeat extends ScreenBase<ContainerGeneratorPeat> {

  private ButtonMachine btnToggle;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private TimerBar timer;

  public ScreenGeneratorPeat(ContainerGeneratorPeat screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileGeneratorPeat.MENERGY);
    timer = new TimerBar(this, 78, 55, TileMelter.TIMER_FULL);
  }

  @Override
  public void init() {
    super.init();
    timer.guiLeft = energy.guiLeft = leftPos;
    timer.guiTop = energy.guiTop = topPos;
    energy.guiTop = topPos;
    int x = leftPos + 132, y = topPos + 8;
    btnToggle = addButton(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      menu.tile.setFlowing((menu.tile.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileGeneratorPeat.Fields.FLOWING.ordinal(), menu.tile.getFlowing(), menu.tile.getBlockPos()));
    }));
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileGeneratorPeat.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + menu.tile.getFlowing()));
    btnToggle.setTextureId(menu.tile.getFlowing() == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
    btnRedstone.onValueUpdate(menu.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, imageWidth / 2 - 9, 28);
    energy.draw(ms, menu.tile.getEnergy());
    timer.draw(ms, menu.tile.getField(TileGeneratorPeat.Fields.BURNTIME.ordinal()));
  }
}
