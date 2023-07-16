package com.lothrazar.cyclic.block.packager;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPackager extends ScreenBase<ContainerPackager> {

  private ButtonMachineField btnRedstone;
  private EnergyBar energy;

  public ScreenPackager(ContainerPackager screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TilePackager.MAX);
    energy.visible = TilePackager.POWERCONF.get() > 0;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TilePackager.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    //    timer.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getField(TilePackager.Fields.TIMER.ordinal()));
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 50, 40);
    this.drawSlotLarge(ms, 90, 36);
    energy.draw(ms, menu.tile.getEnergy());
    //    timer.capacity = menu.tile.getField(TilePackager.Fields.BURNMAX.ordinal());
    //    timer.visible = (timer.capacity > 0);
    //    timer.draw(ms, menu.tile.getField(TilePackager.Fields.TIMER.ordinal()));
    // 
  }
}
