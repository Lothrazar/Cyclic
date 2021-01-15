package com.lothrazar.cyclic.block.breaker;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenBreaker extends ScreenBase<ContainerBreaker> {

  private ButtonMachineRedstone btnRedstone;
  private EnergyBar energy;

  public ScreenBreaker(ContainerBreaker screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, container.tile.getEnergyMax());
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.visible = TileBreaker.POWERCONF.get() > 0;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileBreaker.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    int w = 160;
    int h = 20;
    int f = TileBreaker.Fields.TIMER.ordinal();
    x = guiLeft + 8;
    y = guiTop + 30;
    GuiSliderInteger speedsl = this.addButton(new GuiSliderInteger(x, y, w, h, f, container.tile.getPos(),
        1, TileBreaker.TIMER_FULL, container.tile.getField(f)));
    speedsl.setTooltip("cyclic.gui.timer");
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
  }
}
