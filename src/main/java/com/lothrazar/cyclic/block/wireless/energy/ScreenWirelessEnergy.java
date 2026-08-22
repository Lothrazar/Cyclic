package com.lothrazar.cyclic.block.wireless.energy;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.gui.EnergyBar;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenWirelessEnergy extends ScreenBase<ContainerWirelessEnergy> {

  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;
  private EnergyBar energy;

  public ScreenWirelessEnergy(ContainerWirelessEnergy screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TileWirelessEnergy.MAX);
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileWirelessEnergy.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    y += 20;
    btnRender = addRenderableWidget(new ButtonMachineField(x, y, TileWirelessEnergy.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    //    this.drawSlot(ms, 79, 35, TextureRegistry.SLOT_GPS);
    int y = 35 + 16;
    for (int i = 0; i < 8; i++) {
      this.drawSlot(ms, 7 + i * Const.SQ, y, TextureRegistry.SLOT_GPS, 18);
    }
    energy.draw(ms, menu.tile.getEnergy());
  }
}
