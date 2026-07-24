package com.lothrazar.cyclic.block.packager;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.TexturedProgress;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPackager extends ScreenBase<ContainerPackager> {

  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private TexturedProgress progress;

  public ScreenPackager(ContainerPackager screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TilePackager.MAX);
    energy.visible = TilePackager.POWERCONF.get() > 0;
    this.progress = new TexturedProgress(this.font, 73, 40, 24, 17, TextureRegistry.ARROW);
    this.progress.setTopDown(false);
    progress.guiLeft = energy.guiLeft = leftPos;
    progress.guiTop = energy.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TilePackager.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
    btnRedstone.onValueUpdate(menu.tile);
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 50, 40);
    // int timerMax = menu.tile.getField(TilePackager.Fields.BURNMAX.ordinal());
//    int timer = menu.tile.getField(TilePackager.Fields.TIMER.ordinal());
    progress.max = menu.tile.getField(TilePackager.Fields.BURNMAX.ordinal());
    progress.draw(ms, menu.tile.getField(TilePackager.Fields.TIMER.ordinal()));
    this.drawSlotLarge(ms, 107, 36);
    energy.draw(ms, menu.tile.getEnergy());

  }
}
